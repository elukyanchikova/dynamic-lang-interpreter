package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class Print extends Statement {
    private List<Expression> expressions;

    public Print(Expression expression) {
        this.expressions = new ArrayList<>();
        addExpression(expression);
    }

    public void addExpression(Expression expression) {
        this.expressions.add(expression);
    }

    /**
     * @return the expressions
     */
    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Expression expression: expressions) {
            sb.append(expression.toString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1);
        return "print(" + sb.toString() + ")\n";
    }
}
