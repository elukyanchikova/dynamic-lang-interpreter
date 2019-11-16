package entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionalLiteral extends Primary {
    List<Identifier> arguments;
    FunctionBody functionBody;

    FunctionalLiteral(Identifier argument, FunctionBody functionBody) {
        arguments = new ArrayList<>();
        this.functionBody = functionBody;
        addArgument(argument);
    }

    public void addArgument(Identifier argument) {
        arguments.add(argument);
    }
}
