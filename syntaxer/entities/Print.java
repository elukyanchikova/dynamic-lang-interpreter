package entities;

import java.util.ArrayList;
import java.util.List;

public class Print extends Statement {
    private List<Expression> mExpressions;

    public Print(Expression expression) {
        this.mExpressions = new ArrayList<>();
        addExpression(expression);
    }

    public void addExpression(Expression expression) {
        this.mExpressions.add(expression);
    }

    /**
     * @return the expressions
     */
    public List<Expression> getExpressions() {
        return mExpressions;
    }
}
