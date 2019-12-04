package entities;

import java.util.ArrayList;
import java.util.List;

public class Factor extends Expression {
    private List<Expression> terms;
    private List<ArithmeticOperator> arithmeticOperatorList;

    public Factor(Expression term) {
        terms = new ArrayList<>();
        arithmeticOperatorList = new ArrayList<>();
        addTerm(term, ArithmeticOperator.NONE);
    }

    public void addTerm(Expression term, ArithmeticOperator operator) {
        terms.add(term);
        arithmeticOperatorList.add(operator);
    }

    public List<Expression> getTerms() {
        return terms;
    }

    public List<ArithmeticOperator> getArithmeticOperatorList() {
        return arithmeticOperatorList;
    }
}
