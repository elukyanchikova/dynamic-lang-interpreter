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

    } else if (expr instanceof Relation) {

    } else if (expr instanceof Factor) {

    } else if (expr instanceof Term) {
      ScopeTable.ValueTypeWrapper result = new ScopeTable.ValueTypeWrapper(TypeIndicator.INT, new IntegerLiteral(0));
      List<Expression> unary = ((Term) expr).getUnaryList();о
      List<MultiplicationOperator> operators = ((Term) expr).getMultiplicationOperatorList();
      for(int i = 0; i < ((Term) expr).getUnaryList().size(); i++) {
        ScopeTable.ValueTypeWrapper unaryWrapper = evaluateExpression(unary.get(i));
        if (operators.get(i) == MultiplicationOperator.MUL) {
          if(result.getType() == TypeIndicator.INT) {
            if (unaryWrapper.getType() == TypeIndicator.INT) {
              int resValue = ((IntegerLiteral) result.getValue()).getValue();
              int unaryValue = ((IntegerLiteral) unaryWrapper.getValue()).getValue();
              ((IntegerLiteral) result.getValue()).setValue(resValue * unaryValue);
            } else if (unaryWrapper.getType() == TypeIndicator.REAL) {
              int resValue = ((IntegerLiteral) result.getValue()).getValue();
              double unaryValue = ((RealLiteral) unaryWrapper.getValue()).getValue();
              result = new ScopeTable.ValueTypeWrapper(TypeIndicator.REAL, new RealLiteral(unaryValue * resValue));
            } else {
              throw new Exception("Runtime Error: Trying to apply multplication to non real or integer value");
            }
          } else if (result.getType() == TypeIndicator.REAL) {
            if (unaryWrapper.getType() == TypeIndicator.INT) {
              double resValue = ((RealLiteral) result.getValue()).getValue();
              int unaryValue = ((IntegerLiteral) unaryWrapper.getValue()).getValue();
              ((RealLiteral) result.getValue()).setValue(resValue * unaryValue);
            } else if (unaryWrapper.getType() == TypeIndicator.REAL) {
              double resValue = ((RealLiteral) result.getValue()).getValue();
              double unaryValue = ((RealLiteral) unaryWrapper.getValue()).getValue();
              ((RealLiteral) result.getValue()).setValue(resValue * unaryValue);
            } else {
              throw new Exception("Runtime Error: Trying to apply multplication to non real or integer value");
            }
          } else {
            throw new Exception("Runtime Error: Trying to apply multplication to non real or integer value");
          }
        }
      }
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
        // TODO
        return null;
      }
    } else {
      // TODO
      return null;
    }


  }

}