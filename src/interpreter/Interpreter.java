package interpreter;

import jdk.jshell.UnresolvedReferenceException;
import syntaxer.entities.*;

import java.util.List;

public class Interpreter {
    private ScopeTable scope;
    private ScopeTable parentScope;
    private Program program;

    public Interpreter(Program program, ScopeTable parentScope) {
        this.program = program;
        this.scope = new ScopeTable();
        this.parentScope = parentScope;
    }

    public Interpreter(Program program) {
        this(program, null);
    }

    private ScopeTable getScope() {
        return scope;
    }

    public ScopeTable.ValueTypeWrapper execute() throws Exception {
        List<Statement> statements = program.getStatements();
        for (Statement statement : statements) {
            if (statement instanceof Declaration) executeVariableDeclaration(statement);
            else if (statement instanceof Assignment) executeAssignment(statement);
            else if (statement instanceof Loop) executeLoop(statement);
            else if (statement instanceof If) executeIf(statement);
            else if (statement instanceof Print) executePrint(statement);
            else if (statement instanceof Return) return executeReturn(statement);
            else System.out.println("Sheeesh, null Statement");
        }
        return null;
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

        /* Check for existing identifier in the scope */
        if (this.scope.get(identifier.getName()) != null) {
            throw new RuntimeException("Variable already defined!");
        }

        this.scope.put(identifier.getName(), evaluateExpression(variableDefinition.getExpression()));
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
            result = new ScopeTable.ValueTypeWrapper(result.getType(), result.getValue().clone());
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
            result = new ScopeTable.ValueTypeWrapper(result.getType(), result.getValue().clone());
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
                        if (op == UnarySign.SUB)
                            ((IntegerLiteral) intLiteral).setValue(-((IntegerLiteral) intLiteral).getValue());
                        return primary;
                    }
                    throw new Exception("Interpreter Error: type INT assigned to non Integer literal");
                }
                // ( + | - ) Real -> Real
                if (primary.getType() == TypeIndicator.REAL) {
                    Literal intLiteral = primary.getValue();
                    if (intLiteral instanceof RealLiteral) {
                        if (op == UnarySign.SUB)
                            ((RealLiteral) intLiteral).setValue(-((RealLiteral) intLiteral).getValue());
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
                    if (wrapper == null) {
                        throw new Exception(String.format("Runtime Error: Undefined variable %s", id.getName()));
                    }
                    List<Tail> tails = ((Reference) expr).getTailList();
                    for (Tail tail : tails) {
                        // Function call: funcName()
                        if (tail instanceof FunctionCallTail) {
                            if (wrapper.getType() == TypeIndicator.FUNC) {
                                wrapper = executeFunction((FunctionalLiteral) wrapper.getValue(), ((FunctionCallTail) tail).getArguments());
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
                    return wrapper;
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

    /*
     * look for the variable definition in scope (from current to parent scopes recursively)*/
    private ScopeTable.ValueTypeWrapper findVariableInScope(String key) {

        ScopeTable currentScope = this.scope;
        while (currentScope != null) {
            if (currentScope.get(key) != null) {
                return currentScope.get(key);
            } else currentScope = this.parentScope;
        }
        return null;
    }

    private void executeAssignment(Statement statement) {
        Assignment assignment = (Assignment) statement;
    }

    /**
     * Executes loops, which can be either `while` or `for` loops.
     */
    private void executeLoop(Statement statement) throws Exception {
        Loop loop = (Loop) statement;

        if (loop instanceof WhileLoop) executeWhileLoop(loop);
        else if (loop instanceof ForLoop) executeForLoop(loop);
    }

    /**
     * Executes `while` loops.
     * Syntax: `while Expression loop Body end`
     */
    private void executeWhileLoop(Loop loop) throws Exception {
        WhileLoop wl = (WhileLoop) loop;

        Expression condition = wl.getCondition();
        if (condition == null) {
            throw new RuntimeException("Syntax error: loop condition cannot be empty");
        }

        while (evaluateExpressionToBoolean(condition)) {
            executeBody(wl.getBody());
        }
    }

    /**
     * Executes `for` loops.
     * Syntax: `for [ IDENT in ] [ Expression '..' Expression ] loop Body end`
     */
    private void executeForLoop(Loop loop) throws Exception {
        ForLoop fl = (ForLoop) loop;
        Identifier loopCounter = fl.getCounter();
        Range loopRange = fl.getRange();
        Body loopBody = fl.getBody();

        /* Check for counter (and load if present) */
        ScopeTable.ValueTypeWrapper counter = null;
        boolean isCounterVariableCreated = false;
        if (loopCounter != null) {
            counter = findVariableInScope(loopCounter.getName());

            /* Create counter value if it doesn't exist */
            if (counter == null) {
                counter = new ScopeTable.ValueTypeWrapper(TypeIndicator.INT, new IntegerLiteral(0));
                this.scope.put(loopCounter.getName(), counter);
                isCounterVariableCreated = true;
            }
        }

        /* Check for range (and evaluate if present) */
        ScopeTable.ValueTypeWrapper rangeStart = null;
        ScopeTable.ValueTypeWrapper rangeEnd = null;
        if (loopRange != null) {
            if (loopRange.getStart() == null) {
                throw new RuntimeException("Syntax error: loop range start cannot be empty");
            }
            if (loopRange.getEnd() == null) {
                throw new RuntimeException("Syntax error: loop range end cannot be empty");
            }

            rangeStart = evaluateExpression(loopRange.getStart());
            rangeEnd = evaluateExpression(loopRange.getEnd());
        }

        /* No range present: launch an infinite loop (say goodbye) */
        if (rangeStart == null || rangeEnd == null) {
            for (; ; ) {
                /* Update counter if needed */
                if (counter != null) {
                    IntegerLiteral oldValue = (IntegerLiteral) counter.getValue();
                    counter.setValue(new IntegerLiteral(oldValue.getValue() + 1));
                }

                /* Run body */
                // TODO: return if loop contains return statement
                executeBody(loopBody);
            }
        }

        /*
         * Range is present
         */

        /* Range can be only of two integers */
        if (rangeStart.getType() != TypeIndicator.INT) {
            throw new RuntimeException("Range start must be an integer");
        }
        if (rangeEnd.getType() != TypeIndicator.INT) {
            throw new RuntimeException("Range end must be an integer");
        }

        /* Define loop start and end */
        final int rangeStartInt = ((IntegerLiteral) rangeStart.getValue()).getValue();
        final int rangeEndInt = ((IntegerLiteral) rangeEnd.getValue()).getValue();

        /* Run loop */
        for (int i = rangeStartInt; i < rangeEndInt; i++) {
            /* Update counter if needed */
            if (counter != null) {
                counter.setValue(new IntegerLiteral(i));
            }

            /* Run body */
            executeBody(loopBody);
        }

        /* Clean up the scope */
        if (isCounterVariableCreated) {
            this.scope.remove(loopCounter.getName());
        }
    }

    /**
     * Executes `if` statements.
     * Syntax: `if Expression then Body [ else Body ] end`
     */
    private void executeIf(Statement statement) throws Exception {
        If ifStatement = (If) statement;
        Expression ifCondition = ifStatement.getCondition();

        boolean condition = evaluateExpressionToBoolean(ifCondition);

        /* Execute body if condition is true */
        if (condition) {
            executeBody(ifStatement.getThenBody());
        } else if (ifStatement.getElseBody() != null) {
            executeBody(ifStatement.getElseBody());
        }
    }

    /**
     * Evaluates expressions to boolean values.
     *
     * @throws RuntimeException In case that expression evaluated is not boolean.
     */
    private boolean evaluateExpressionToBoolean(Expression expr) throws Exception {
        ScopeTable.ValueTypeWrapper result = evaluateExpression(expr);

        /* Condition should evaluate to Boolean value */
        if (result.getType() != TypeIndicator.BOOL) {
            throw new RuntimeException("Invalid condition"); // todo exc
        }

        return ((BooleanLiteral) result.getValue()).getValue();
    }

    private void executePrint(Statement statement) throws Exception {
        Print printStatement = (Print) statement;
        StringBuilder sb = new StringBuilder();

        /* Run through all the expressions and evaluate them */
        for (Expression expression : printStatement.getExpressions()) {
            ScopeTable.ValueTypeWrapper result = evaluateExpression(expression);

            if (result == null) {
                sb.append(" <empty>");
            } else {
                sb.append(" ").append(result.getValue().toString());
            }
        }

        /* Remove extra space at the beginning got by appending (if there is) */
        if (sb.length() > 0) sb.deleteCharAt(0);

        /* Print */
        System.out.printf("%s\n", sb.toString());
    }

    private ScopeTable.ValueTypeWrapper executeBody(Body body) throws Exception {
        Program program = new Program();
        program.setStatements(body.getStatements());

        Interpreter interpreter = new Interpreter(program, this.scope);
        return interpreter.execute();
    }

    private ScopeTable.ValueTypeWrapper executeReturn(Statement statement) throws Exception {
        Return returnStatement = (Return) statement;
        return evaluateExpression(returnStatement.getExpression());
    }

    private ScopeTable.ValueTypeWrapper executeFunction(FunctionalLiteral functionalLiteral, List<Expression> arguments) throws Exception {
        List<Identifier> identifiers = functionalLiteral.getArguments();

        if (arguments.size() != identifiers.size()) {
            throw new Exception("Runtime Error: Arguments mismatch");
        }

        List<Statement> statements = functionalLiteral.getFunctionBody().getStatements();
        Program program = new Program();
        program.setStatements(statements);

        Interpreter interpreter = new Interpreter(program, this.scope);

        for (int i = 0; i < identifiers.size(); i++) {
            interpreter.getScope().put(identifiers.get(i).getName(), this.evaluateExpression(arguments.get(i)));
        }
        return interpreter.execute();

//
//        for (Identifier identifier : identifiers) {
//        System.out.println(this.scope.get(identifier.getName()));}
    }

}