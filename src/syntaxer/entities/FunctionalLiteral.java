package syntaxer.entities;

import interpreter.ScopeTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionalLiteral extends Literal {
    List<Identifier> arguments;
    ScopeTable scope;
    Body functionBody;

    public FunctionalLiteral(Identifier argument, Body functionBody) {
        arguments = new ArrayList<>();
        this.functionBody = functionBody;
        scope = new ScopeTable();
        addArgument(argument);
    }

    public ScopeTable getScope() {
        return scope;
    }


    public FunctionalLiteral(Identifier argument) {
        arguments = new ArrayList<>();
        this.functionBody = null;
        addArgument(argument);
    }

    public void addArgument(Identifier argument) {
        arguments.add(argument);
    }

    public void setFunctionBody(Body body) {
        this.functionBody = body;
    }

    public Body getFunctionBody() {
        return functionBody;
    }

    public List<Identifier> getArguments() {
        return arguments;
    }
}
