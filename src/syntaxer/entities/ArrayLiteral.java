package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral extends Literal {
    List<Expression> expressionList;

    ArrayLiteral(Expression expression) {
        expressionList = new ArrayList<>();
        addExpression(expression);
    }

    public ArrayLiteral() {
        expressionList = new ArrayList<>();
    }

    public void addExpression(Expression expression) {
        expressionList.add(expression);
    }

    @Override
    public String toString() {
        return "<Array>"; // todo
    }
}
