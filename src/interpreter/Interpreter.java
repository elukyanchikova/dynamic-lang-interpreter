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

    public ScopeTable getScope() {
        return scope;
    }

    public ScopeTable.ValueTypeWrapper execute() {
        List<Statement> statements = program.getStatements();
        for (Statement statement : statements) {
            if (statement instanceof Declaration) executeVariableDeclaration(statement);
            else if (statement instanceof Assignment) executeAssignment(statement);
            else if (statement instanceof Loop) executeVariableDeclaration(statement);
            else if (statement instanceof If) executeVariableDeclaration(statement);
            else if (statement instanceof Print) executeVariableDeclaration(statement);
            else if (statement instanceof Return) return executeReturn(statement);
            else System.out.println("Sheeesh, null Statement");
        }

        return null;

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

    private void executeLoop(Statement statement) {
        Loop loop = (Loop) statement;
    }

    private void executeIf(Statement statement) {
        If ifStatement = (If) statement;
    }

    private void executePrint(Statement statement) {
        Print printStatement = (Print) statement;
    }

    private ScopeTable.ValueTypeWrapper executeReturn(Statement statement) {
        Return returnStatement = (Return) statement;
        return evaluateExpression(returnStatement.getExpression());
    }


    private ScopeTable.ValueTypeWrapper exectuteFunction(FunctionalLiteral functionalLiteral){
        List<Identifier> identifiers = functionalLiteral.getArguments();

        List<Statement> statements = functionalLiteral.getFunctionBody().getStatements();
        Program program = new Program();
        program.setStatements(statements);

        Interpreter interpreter = new Interpreter(program, this.scope);

        for (Identifier identifier : identifiers) {
            interpreter.getScope().put(identifier.getName(), this.evaluateExpression(identifier));
        }
        return interpreter.execute();

//
//        for (Identifier identifier : identifiers) {
//        System.out.println(this.scope.get(identifier.getName()));}
    }

    private ScopeTable.ValueTypeWrapper evaluateExpression(Expression expr) {
        return null;
    }
}