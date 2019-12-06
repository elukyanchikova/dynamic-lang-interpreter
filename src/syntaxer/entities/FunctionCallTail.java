package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallTail extends Tail {
    private List<Expression> arguments;

    public FunctionCallTail(Expression argument) {
        this.arguments = new ArrayList<>();
        addArgument(argument);
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for(Expression arg: arguments) {
            sb.append(arg.toString()).append(',');
        }
        sb.setCharAt(sb.length() - 1, ')');
        return sb.toString();
    }
}
