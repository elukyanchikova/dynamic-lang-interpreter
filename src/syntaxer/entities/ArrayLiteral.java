package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral extends Literal {
    private List<Expression> expressionList;

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

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Expression expression: expressionList) {
            sb.append(expression.toString()).append(", ");
        }
        return sb.toString() + ']';
    }
}
