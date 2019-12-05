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

  private ScopeTable.ValueTypeWrapper evaluateExpression(Expression expr) {
    if (expr instanceof ExpressionComplex) {

    } else if (expr instanceof Relation) {

    } else if (expr instanceof Factor) {

    } else if (expr instanceof Term) {

    } else if (expr instanceof Unary) {
      if (expr instanceof Is) {
        ScopeTable.ValueTypeWrapper asd = evaluateExpression(((Is) expr).getPrimary());
        if (((Is) expr).getTypeIndicator() == asd.type) {
          return new ScopeTable.ValueTypeWrapper(TypeIndicator.BOOL, new BooleanLiteral(true));
        }

      }
    } else {

    }
  }


}