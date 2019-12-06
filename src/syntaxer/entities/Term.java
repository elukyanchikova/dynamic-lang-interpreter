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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < unaryList.size(); i++) {
            switch (multiplicationOperatorList.get(i)) {
                case MUL: sb.append(" * "); break;
                case DIV: sb.append(" / "); break;
                case NONE: break;
            }
            sb.append(unaryList.get(i).toString());
        }
        return sb.toString();
    }
}
