package entities;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral {
    List<Expression> expressionList;

    ArrayLiteral(Expression expression) {
        expressionList = new ArrayList<>();
        addExpression(expression);
    }

    public void addExpression(Expression expression) {
        expressionList.add(expression);
    }
}
