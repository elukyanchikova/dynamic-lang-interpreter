package entities;

import java.util.ArrayList;
import java.util.List;

public class TupleLiteral extends Literal {
    List<TupleElement> tupleElementList;

    public TupleLiteral(TupleElement tupleElement) {
        tupleElementList = new ArrayList<>();
        addElement(tupleElement);
    }

    public void addElement(TupleElement tupleElement) {
        tupleElementList.add(tupleElement);
    }
}
