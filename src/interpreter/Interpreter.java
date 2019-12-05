package interpreter;

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

  public void execute() {
    List<Statement> statements = program.getStatements();
    for (Statement statement : statements) {
      if (statement instanceof Declaration) executeVariableDeclaration(statement);
      else if (statement instanceof Assignment) executeAssignment(statement);
      else if (statement instanceof Loop) executeVariableDeclaration(statement);
      else if (statement instanceof If) executeVariableDeclaration(statement);
      else if (statement instanceof Print) executeVariableDeclaration(statement);
      else if (statement instanceof Return) executeVariableDeclaration(statement);
      else System.out.println("Sheeesh, null Statement");
    }
  }

    private void executeVariableDeclaration(Statement statement) {
        Declaration declaration = (Declaration) statement;
        List<VariableDefinition> variableDefinitions = declaration.getVariableDefinitions();

        for (VariableDefinition variableDefinition : variableDefinitions) {
            executeVariableDefinition(variableDefinition);
        }

    }

    private void executeVariableDefinition(VariableDefinition variableDefinition) {
        Identifier identifier = variableDefinition.getIdentifier();

        /* Check for existing identifier in the scope */
        if (this.scope.get(identifier.getName()) != null) {
            throw new RuntimeException("Variable already defined!");
        }

        this.scope.put(identifier.getName(), evaluateExpression(variableDefinition.getExpression()));
        System.out.printf("Added identifier %s, value %s\n", identifier.getName(), "null");
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
  private void executeLoop(Statement statement) {
    Loop loop = (Loop) statement;

    if (loop instanceof WhileLoop) executeWhileLoop(loop);
    else if (loop instanceof ForLoop) executeForLoop(loop);
  }

  /**
   * Executes `while` loops.
   * Syntax: `while Expression loop Body end`
   */
  private void executeWhileLoop(Loop loop) {
    WhileLoop wl = (WhileLoop) loop;

    Expression condition = wl.getCondition();

    while (evaluateExpressionToBoolean(condition)) {
      evaluateBody(wl.getBody());
    }
  }

  private void executeForLoop(Loop loop) {
    // todo
  }

  /**
   * Executes `if` statements.
   * Syntax: `if Expression then Body [ else Body ] end`
   */
  private void executeIf(Statement statement) {
    If ifStatement = (If) statement;
    Expression condition = ifStatement.getCondition();

    boolean isConditionTrue = evaluateExpressionToBoolean(condition);

    /* Execute body if condition is true */
    if (condition) {
      executeBody(ifStatement.getThenBody());
    } else if (ifStatement.getElseBody() != null) {
      executeBody(ifStatement.getElseBody());
    }
  }

  /**
   * Evaluates expressions to boolean values.
   * @throws RuntimeException In case that expression evaluated is not boolean.
   */
  private boolean evaluateExpressionToBoolean(Expression expr) {
    ScopeTable.ValueTypeWrapper result = evaluateExpression(condition);

    /* Condition should evaluate to Boolean value */
    if (result.type != TypeIndicator.BOOL) {
      throw new RuntimeException("Invalid condition"); // todo exc
    }

    return ((BooleanLiteral) result.value).value;
  }

  private void executePrint(Statement statement) {
    Print printStatement = (Print) statement;
    StringBuilder sb = new StringBuilder();

    /* Run through all the expressions and evaluate them */
    for (Expression expression : printStatement.getExpressions()) {
      ScopeTable.ValueTypeWrapper result = evaluateExpression(expression);

      sb.append(" ").append(result.value.toString());
    }

    /* Remove extra space at the beginning got by appending (if there is) */
    if (sb.length() > 0) sb.deleteCharAt(0);

    /* Print */
    System.out.printf("%s\n", sb.toString());
  }

    private void executeReturn(Statement statement) {
        Return returnStatement = (Return) statement;
    }

  private void executeBody(Body body) {
    // todo
  }

  private ScopeTable.ValueTypeWrapper evaluateExpression(Expression expr) {
    return null;
  }

    private void exectuteFunction(FunctionalLiteral functionalLiteral){
        List<Identifier> identifiers = functionalLiteral.getArguments();

        for (Identifier identifier : identifiers) {
            this.scope.put(identifier.getName(), this.evaluateExpression(identifier));
        }

        List<Statement> statements = functionalLiteral.getFunctionBody().getStatements();
        Program program = new Program();
        program.setStatements(statements);

        Interpreter interpreter = new Interpreter(program, this.scope);
        interpreter.execute();
//
//        for (Identifier identifier : identifiers) {
//        System.out.println(this.scope.get(identifier.getName()));}
    }
}