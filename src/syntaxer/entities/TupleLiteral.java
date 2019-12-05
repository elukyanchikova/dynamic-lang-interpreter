package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class TupleLiteral extends Literal {
    private List<TupleElement> tupleElementList;

    public TupleLiteral(TupleElement tupleElement) {
        tupleElementList = new ArrayList<>();
        addElement(tupleElement);
    }

    public void addElement(TupleElement tupleElement) {
        tupleElementList.add(tupleElement);
    }

    public List<TupleElement> getTupleElementList() {
        return tupleElementList;
    }

    public TupleElement getElement(Identifier identifier) {
        for(TupleElement te: tupleElementList) {
            if (identifier.getName().equals(te.getIdentifier().getName())) {
                return te;
            }
        }
        return null;
    }

    public TupleElement getElement(int index) {
        return tupleElementList.get(index);
    }
}
