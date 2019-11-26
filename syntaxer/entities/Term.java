package entities;

import java.util.ArrayList;
import java.util.List;

public class Term {
    List<Unary> unaryList;
    List<MultiplicationOperator> multiplicationOperatorList;

    public Term(Unary unary) {
        unaryList = new ArrayList<>();
        multiplicationOperatorList = new ArrayList<>();
        addUnary(unary, MultiplicationOperator.NONE);
    }

    public void addUnary(Unary unary, MultiplicationOperator operator) {
        unaryList.add(unary);
        multiplicationOperatorList.add(operator);
    }
}
