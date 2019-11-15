package entities;

import java.util.ArrayList;
import java.util.List;

public class Factor {
    List<Term> termList;
    List<ArithmeticOperator> arithmeticOperatorList;

    Factor(Term term) {
        termList = new ArrayList<>();
        arithmeticOperatorList = new ArrayList<>();
        addTerm(term, ArithmeticOperator.NONE);
    }

    public void addTerm(Term term, ArithmeticOperator operator) {
        termList.add(term);
        arithmeticOperatorList.add(operator);
    }
}
