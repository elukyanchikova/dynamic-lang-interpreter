package entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionalLiteral extends Primary {
    private List<Identifier> arguments;
    private FunctionBody functionBody;

    public FunctionalLiteral(Identifier argument, FunctionBody functionBody) {
        arguments = new ArrayList<>();
        this.functionBody = functionBody;
        addArgument(argument);
    }

    public FunctionalLiteral(Identifier argument) {
        arguments = new ArrayList<>();
        this.functionBody = null;
        addArgument(argument);
    }

    public void addArgument(Identifier argument) {
        arguments.add(argument);
    }

    public void setFunctionBody(FunctionBody body) {
        this.functionBody = body;
    }

    public void setArguments(List<Identifier> arguments) {
        this.arguments = arguments;
    }

    public FunctionBody getFunctionBody() {
        return functionBody;
    }

    public List<Identifier> getArguments() {
        return arguments;
    }
}
