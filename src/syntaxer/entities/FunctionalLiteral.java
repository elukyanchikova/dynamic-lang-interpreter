package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionalLiteral extends Literal {
    List<Identifier> arguments;
    Body functionBody;

    public FunctionalLiteral(Identifier argument, Body functionBody) {
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

    public void setFunctionBody(Body body) {
        this.functionBody = body;
    }

    public Body getFunctionBody() {
        return functionBody;
    }
}
