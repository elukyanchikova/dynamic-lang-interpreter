package interpreter;

import jdk.internal.org.objectweb.asm.Type;
import syntaxer.entities.*;

import java.sql.Wrapper;
import java.util.List;

public class Interpreter {
  private ScopeTable scope;
  private Program program;

  public Interpreter(Program program) {
    this.program = program;
    this.scope = new ScopeTable();
  }

  public void execute() throws Exception {
    List<Statement> statements = program.getStatements();
    for (Statement statement : statements) {
      if (statement instanceof Declaration) executeVariableDeclaration(statement);
    }
  }

  private void executeVariableDeclaration(Statement statement) throws Exception {
    Declaration declaration = (Declaration) statement;
    List<VariableDefinition> variableDefinitions = declaration.getVariableDefinitions();

    for (VariableDefinition variableDefinition : variableDefinitions) {
      executeVariableDefinition(variableDefinition);
    }

  }

  private void executeVariableDefinition(VariableDefinition variableDefinition) throws Exception {
    Identifier identifier = variableDefinition.getIdentifier();

    /* Check for existing identifier */
    if (this.scope.get(identifier.getName()) != null) {
      throw new RuntimeException("Variable already defined!");
    }

    this.scope.put(identifier.getName(), evaluateExpression(variableDefinition.getExpression()));
    System.out.printf("Added identifier %s, value %s\n", identifier.getName(), "null");
  }

  private ScopeTable.ValueTypeWrapper evaluateExpression(Expression expr) throws Exception {
    if (expr instanceof ExpressionComplex) {
      ScopeTable.ValueTypeWrapper firstRelation = evaluateExpression(((ExpressionComplex) expr).getRelationList().get(0));
      ScopeTable.ValueTypeWrapper secondRelation = evaluateExpression(((ExpressionComplex) expr).getRelationList().get(1));
      LogicalOperator op = ((ExpressionComplex) expr).getLogicalOperator1List().get(1);
      boolean firstValue = ((BooleanLiteral) firstRelation.getValue()).getValue();
      boolean secondValue = ((BooleanLiteral) secondRelation.getValue()).getValue();
      boolean res;
      if (firstRelation.getType() == TypeIndicator.BOOL && secondRelation.getType() == TypeIndicator.BOOL) {
        if (op == LogicalOperator.XOR) {
          res = firstValue ^ secondValue;
        } else if (op == LogicalOperator.OR) {
          res = firstValue || secondValue;
        } else if (op == LogicalOperator.AND) {
          res = firstValue && secondValue;
        } else {
          throw new Exception("Interpreter Error: Unexpected logical operator");
        }
        return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, new BooleanLiteral(res));
      } else {
        throw new Exception("Runtime Error: Logical operetion between non boolean values");
      }
    } else if (expr instanceof Relation) {
      ScopeTable.ValueTypeWrapper firstFactor = evaluateExpression(((Relation) expr).getFirstFactor());
      ScopeTable.ValueTypeWrapper secondFactor = evaluateExpression(((Relation) expr).getSecondFactor());
      RelationOperator op = ((Relation) expr).getOperator();
      if ((firstFactor.getType() == TypeIndicator.INT || firstFactor.getType() == TypeIndicator.REAL)
          && (secondFactor.getType() == TypeIndicator.INT || secondFactor.getType() == TypeIndicator.REAL)) {
        double firstValue = getNumericValue(firstFactor);
        double secondValue = getNumericValue(secondFactor);
        boolean res;
        if (op == RelationOperator.LESS) {
          res = firstValue < secondValue;
        } else if (op == RelationOperator.LESS_EQ) {
          res = firstValue <= secondValue;
        } else if (op == RelationOperator.GREATER) {
          res = firstValue > secondValue;
        } else if (op == RelationOperator.GREATER_EQ) {
          res = firstValue >= secondValue;
        } else if (op == RelationOperator.EQUAL) {
          res = firstValue == secondValue;
        } else if (op == RelationOperator.NOT_EQUAL) {
          res = firstValue != secondValue;
        } else {
          throw new Exception("Interpreter Error: unexpected operator in comparison");
        }
        return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, new BooleanLiteral(res));
      } else {
        throw new Exception("Runtime Error: comparison between non-numeric values");
      }
    } else if (expr instanceof Factor) {
      List<Expression> terms = ((Factor) expr).getTerms();
      List<ArithmeticOperator> operators = ((Factor) expr).getArithmeticOperatorList();
      ScopeTable.ValueTypeWrapper result = evaluateExpression(terms.get(0));
      for (int i = 1; i < terms.size(); i++) {
        ScopeTable.ValueTypeWrapper termWrapper = evaluateExpression(terms.get(i));
        if (operators.get(i) == ArithmeticOperator.ADD) {
          // Concatenating strings
          if (result.getType() == TypeIndicator.STRING) {
            if (termWrapper.getType() == TypeIndicator.STRING) {
              String resultValue = ((StringLiteral) result.getValue()).getValue();
              String termValue = ((StringLiteral) termWrapper.getValue()).getValue();
              String resultingString = resultValue + termValue;
              ((StringLiteral) result.getValue()).setValue(resultingString);
            } else {
              throw new Exception("Runtime Error: Concatenating string value with non string value");
            }
            // Concatenating arrays
          } else if (result.getType() == TypeIndicator.VECTOR) {
            if (termWrapper.getType() == TypeIndicator.VECTOR) {
              List<Expression> secondArray = ((ArrayLiteral) termWrapper.getValue()).getExpressionList();
              ((ArrayLiteral) result.getValue()).getExpressionList().addAll(secondArray);
            } else {
              throw new Exception("Runtime Error: Concatenating array value with non array value");
            }
            // Concatenating tuples
          } else if (result.getType() == TypeIndicator.TUPLE) {
            if (termWrapper.getType() == TypeIndicator.TUPLE) {
              List<TupleElement> secondTuple = ((TupleLiteral) termWrapper.getValue()).getTupleElementList();
              for (TupleElement element : secondTuple) {
                ((TupleLiteral) result.getValue()).addElement(element);
              }
            } else {
              throw new Exception("Runtime Error: Concatenating tuple value with non tuple value");
            }
          }
        }
        double resValue = getNumericValue(result);
        double termValue = getNumericValue(termWrapper);
        double opResult;
        if (operators.get(i) == ArithmeticOperator.ADD) {
          opResult = resValue + termValue;
        } else if (operators.get(i) == ArithmeticOperator.SUB) {
          opResult = resValue - termValue;
        } else {
          throw new Exception("Interpreter error: NONE operation after first term");
        }
        result = getValueTypeWrapper(result, termWrapper, opResult);
      }
      return result;
    } else if (expr instanceof Term) {
      List<Expression> unary = ((Term) expr).getUnaryList();
      List<MultiplicationOperator> operators = ((Term) expr).getMultiplicationOperatorList();
      ScopeTable.ValueTypeWrapper result = evaluateExpression(unary.get(0));
      for (int i = 1; i < unary.size(); i++) {
        ScopeTable.ValueTypeWrapper unaryWrapper = evaluateExpression(unary.get(i));
        double resValue = getNumericValue(result);
        double unaryValue = getNumericValue(unaryWrapper);
        double opResult;
        if (operators.get(i) == MultiplicationOperator.MUL) {
          opResult = resValue * unaryValue;
        } else if (operators.get(i) == MultiplicationOperator.DIV) {
          opResult = resValue / unaryValue;
        } else {
          throw new Exception("Interpreter error: NONE operation after first unary");
        }
        result = getValueTypeWrapper(result, unaryWrapper, opResult);
      }
      return result;
    } else if (expr instanceof Unary) {
      if (expr instanceof Is) {
        // Reference is TypeIndicator -> Boolean
        ScopeTable.ValueTypeWrapper primary = evaluateExpression(((Is) expr).getPrimary());
        if (((Is) expr).getTypeIndicator() == primary.getType()) {
          return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, new BooleanLiteral(true));
        } else {
          return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, new BooleanLiteral(false));
        }
      } else if (expr instanceof IsolatedExpression) {
        // '(' Expression ')' -> Expression Type
        return evaluateExpression(((IsolatedExpression) expr).getExpression());
      } else if (expr instanceof SignedPrimary) {
        // ( + | - | not ) Primary -> Primary Type
        UnarySign op = ((SignedPrimary) expr).getSign();
        ScopeTable.ValueTypeWrapper primary = evaluateExpression(((SignedPrimary) expr).getPrimary());
        // not Boolean -> Boolean
        if (op == UnarySign.NOT) {
          if (primary.getValue() instanceof BooleanLiteral) {
            BooleanLiteral value = (BooleanLiteral) primary.getValue();
            value.setValue(!value.getValue());
            return primary;
          } else {
            throw new Exception("Runtime Error: Applying 'not' operator to non Boolean value");
          }
        }
        // ( + | - ) Integer -> Integer
        if (primary.getType() == TypeIndicator.INT) {
          Literal intLiteral = primary.getValue();
          if (intLiteral instanceof IntegerLiteral) {
            if (op == UnarySign.SUB) ((IntegerLiteral) intLiteral).setValue(-((IntegerLiteral) intLiteral).getValue());
            return primary;
          }
          throw new Exception("Interpreter Error: type INT assigned to non Integer literal");
        }
        // ( + | - ) Real -> Real
        if (primary.getType() == TypeIndicator.REAL) {
          Literal intLiteral = primary.getValue();
          if (intLiteral instanceof RealLiteral) {
            if (op == UnarySign.SUB) ((RealLiteral) intLiteral).setValue(-((RealLiteral) intLiteral).getValue());
            return primary;
          }
          throw new Exception("Interpreter Error: type REAL assigned to non real literal");
        }
        // Primary -> Interpreter Error
        if (op == UnarySign.NONE) {
          throw new Exception("Interpeter Error: Signed Primary has NONE operator type");
        }
        // ( + | - ) Primary (Primary is not real or int) -> Runtime Error
        throw new Exception("Rutime Error: Applying unary operation to neither real nor integer value");
      } else if (expr instanceof Primary) {
        // Standalone identifier
        if (expr instanceof Identifier) {
          return findVariableInScope(((Identifier) expr).getName());
        } else if (expr instanceof Reference) {
          Identifier id = ((Reference) expr).getIdentifier();
          ScopeTable.ValueTypeWrapper wrapper = findVariableInScope(id.getName());
          List<Tail> tails = ((Reference) expr).getTailList();
          for (Tail tail : tails) {
            // Function call: funcName()
            if (tail instanceof FunctionCallTail) {
              if (wrapper.getType() == TypeIndicator.FUNC) {
                wrapper = executeFunction(wrapper.getValue());
              } else {
                throw new Exception("Runtime Error: Applying call method to non function value");
              }
              // Take element from array by index: array[23]
            } else if (tail instanceof ArrayElementTail) {
              if (wrapper.getType() == TypeIndicator.VECTOR) {
                ScopeTable.ValueTypeWrapper index_wrapper = evaluateExpression(((ArrayElementTail) tail).getExpression());
                if (index_wrapper.getType() == TypeIndicator.INT) {
                  int index = ((IntegerLiteral) index_wrapper.getValue()).getValue();
                  wrapper = evaluateExpression(((ArrayLiteral) wrapper.getValue()).getExpressionList().get(index));
                } else {
                  throw new Exception("Runtime Error: Array index should be integer");
                }
              } else {
                throw new Exception("Runtime Error: Trying to lookup on non array value");
              }
              // Named element of tuple: tuple.elementName
            } else if (tail instanceof NamedElementTail) {
              if (wrapper.getType() == TypeIndicator.TUPLE) {
                Identifier identifier = ((NamedElementTail) tail).getIdentifier();
                wrapper = evaluateExpression(((TupleLiteral) wrapper.getValue()).getElement(identifier).getExpression());
              } else {
                throw new Exception("RuntimeError: Trying to access named element from non tuple value");
              }
              // Unnamed element of tuple: tuple.12
            } else if (tail instanceof UnnamedElementTail) {
              if (wrapper.getType() == TypeIndicator.TUPLE) {
                int index = ((UnnamedElementTail) tail).getInteger().getValue();
                wrapper = evaluateExpression(((TupleLiteral) wrapper.getValue()).getElement(index).getExpression());
              } else {
                throw new Exception("RuntimeError: Trying to access unnamed element from non tuple value");
              }
            } else {
              throw new Exception("Interpreter Error: Tail list element is not child of Tail class");
            }
          }
        } else if (expr instanceof Literal) {
          if (expr instanceof IntegerLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.INT, (Literal) expr);
          } else if (expr instanceof RealLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.REAL, (Literal) expr);
          } else if (expr instanceof BooleanLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, (Literal) expr);
          } else if (expr instanceof StringLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.STRING, (Literal) expr);
          } else if (expr instanceof EmptyLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.EMPTY, (Literal) expr);
          } else if (expr instanceof ArrayLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.VECTOR, (Literal) expr);
          } else if (expr instanceof TupleLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.TUPLE, (Literal) expr);
          } else if (expr instanceof FunctionalLiteral) {
            return new ScopeTable.ValueTypeWrapper(TypeIndicator.FUNC, (Literal) expr);
          } else {
            throw new Exception("Interpreter Error: Literal object is not a child of any literals");
          }
        } else {
          throw new Exception("Interpreter Error: Primary object is not an instance of any primary");
        }
      } else {
        throw new Exception("Interpreter Error: Unary object is not an instance of any Unary child");
      }
    } else {
      throw new Exception("Interpreter Error: Unary object is not an instance of any Unary child");
    }
  }

  private ScopeTable.ValueTypeWrapper getValueTypeWrapper(ScopeTable.ValueTypeWrapper result, ScopeTable.ValueTypeWrapper wrapper, double opResult) throws Exception {
    if (result.getType() == TypeIndicator.INT) {
      if (wrapper.getType() == TypeIndicator.INT) {
        ((IntegerLiteral) result.getValue()).setValue((int) opResult);
      } else if (wrapper.getType() == TypeIndicator.REAL) {
        return new ScopeTable.ValueTypeWrapper(TypeIndicator.REAL, new RealLiteral(opResult));
      } else {
        throw new Exception("Runtime Error: Trying to apply '*' or '/' to non real or integer value");
      }
    } else if (result.getType() == TypeIndicator.REAL) {
      if (wrapper.getType() == TypeIndicator.INT || wrapper.getType() == TypeIndicator.REAL) {
        ((RealLiteral) result.getValue()).setValue(opResult);
      } else {
        throw new Exception("Runtime Error: Trying to apply '*' or '/' to non real or integer value");
      }
    } else {
      throw new Exception("Runtime Error: Trying to apply '*' or '/' to non real or integer value");
    }
    return result;
  }

  private double getNumericValue(ScopeTable.ValueTypeWrapper wrapper) throws Exception {
    double result;
    if (wrapper.getType() == TypeIndicator.REAL) {
      result = ((RealLiteral) wrapper.getValue()).getValue();
    } else if (wrapper.getType() == TypeIndicator.INT) {
      result = ((IntegerLiteral) wrapper.getValue()).getValue();
    } else {
      // TODO: Divide Exception in two variants with either * or /
      throw new Exception("Runtime Error: Trying to apply '*' or '/' to non real or non integer value");
    }
    return result;
  }

}