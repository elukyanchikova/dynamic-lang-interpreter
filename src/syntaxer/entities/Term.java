package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class Term extends Expression {
    private List<Expression> unaryList;
    private List<MultiplicationOperator> multiplicationOperatorList;

    public Term(Expression unary) {
        unaryList = new ArrayList<>();
        multiplicationOperatorList = new ArrayList<>();
        addUnary(unary, MultiplicationOperator.NONE);
    }

    public void addUnary(Expression unary, MultiplicationOperator operator) {
        unaryList.add(unary);
        multiplicationOperatorList.add(operator);
    }

    public List<Expression> getUnaryList() {
        return unaryList;
    }

    public List<MultiplicationOperator> getMultiplicationOperatorList() {
        return multiplicationOperatorList;
    }
}
