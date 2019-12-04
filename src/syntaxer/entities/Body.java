package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class Body {
    List<Statement> statementList;

    Body() {
        statementList = new ArrayList<>();
    }

    public Body(Statement statement) {
        statementList = new ArrayList<>();
        addStatement(statement);
    }

    public void addStatement(Statement statement) {
        statementList.add(statement);
    }
}
