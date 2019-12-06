package syntaxer.entities;

import java.util.List;
import java.util.ArrayList;

public class Reference extends Primary {
    private Identifier identifier;
    private List<Tail> tailList;

    public Reference(Identifier identifier) {
        tailList = new ArrayList<>();
        this.identifier = identifier;
    }

    public void addTail(Tail tail) {
        tailList.add(tail);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public List<Tail> getTailList() {
        return tailList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(identifier.getName());
        for(Tail tail: tailList) {
            sb.append(tail.toString());
        }
        return sb.toString();
    }
}
