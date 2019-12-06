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
        if (tupleElement.getIdentifier() != null) {
            for(int i = 0; i < tupleElementList.size(); i++) {
                if (tupleElementList.get(i).getIdentifier() != null) {
                    if (tupleElementList.get(i).getIdentifier().getName().equals(tupleElement.getIdentifier().getName())) {
                        tupleElementList.remove(i);
                        break;
                    }
                }
            }
        }
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
        return tupleElementList.get(index - 1);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for(TupleElement tupleElement: tupleElementList) {
            if(tupleElement.getIdentifier() != null) {
                sb.append(tupleElement.getIdentifier().getName()).append(" := ");
            }
            sb.append(tupleElement.getExpression().toString()).append(", ");
        }
        return sb.toString().substring(0, sb.length() - 2) + '}';
    }
}
