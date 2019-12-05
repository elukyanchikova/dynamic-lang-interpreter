package interpreter;

import syntaxer.entities.*;

import java.util.List;

public class Interpreter {
  private ScopeTable scope;
  private Program program;

  public Interpreter(Program program) {
    this.program = program;
    this.scope = new ScopeTable();
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

  private void executeVariableDeclaration(Statement statement){
    Declaration declaration = (Declaration) statement;
    List<VariableDefinition> variableDefinitions = declaration.getVariableDefinitions();

    for (VariableDefinition variableDefinition : variableDefinitions) {
      executeVariableDefinition(variableDefinition);
    }

  }

  private void executeVariableDefinition(VariableDefinition variableDefinition) {
    Identifier identifier = variableDefinition.getIdentifier();

    /* Check for existing identifier */
    if (this.scope.get(identifier.getName()) != null) {
      throw new RuntimeException("Variable already defined!");
    }

    this.scope.put(identifier.getName(), evaluateExpression(variableDefinition.getExpression()));
    System.out.printf("Added identifier %s, value %s\n", identifier.getName(), "null");
  }

  private void executeAssignment(Statement statement) {
    Assignment assignment = (Assignment) statement;
  }

  private void executeLoop(Statement statement) {
    Loop loop = (Loop) statement;
  }

  private void executeIf(Statement statement) {
    If ifStatement = (If) statement;
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

  private ScopeTable.ValueTypeWrapper evaluateExpression(Expression expr) {
    return null;
  }
}