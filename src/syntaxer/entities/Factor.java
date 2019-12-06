package syntaxer.entities;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < terms.size(); i++) {
            switch (arithmeticOperatorList.get(i)) {
                case ADD: sb.append(" + "); break;
                case SUB: sb.append(" - "); break;
                case NONE: break;
            }
            sb.append(terms.get(i).toString());
        }
        return sb.toString();
    }
}
