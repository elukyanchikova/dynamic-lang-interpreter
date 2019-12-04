package syntaxer.entities;

import java.util.List;
import java.util.ArrayList;

public class Reference extends Primary {
    Identifier identifier;
    List<Tail> tailList;

    public Reference(Identifier identifier) {
        tailList = new ArrayList<>();
        this.identifier = identifier;
    }

    public void addTail(Tail tail) {
        tailList.add(tail);
    }
}
