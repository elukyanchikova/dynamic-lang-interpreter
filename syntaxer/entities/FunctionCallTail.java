package entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallTail extends Tail {
    List<Expression> arguments;

    public FunctionCallTail(Expression argument) {
        this.arguments = new ArrayList<>();
        addArgument(argument);
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }
}
