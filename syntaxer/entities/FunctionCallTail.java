package entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallTail extends Tail {
    List<Expression> arguments;

    FunctionCallTail(Expression argument) {
        this();
        addArgument(argument);
    }

    FunctionCallTail() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }
}
