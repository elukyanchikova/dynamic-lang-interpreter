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
}
