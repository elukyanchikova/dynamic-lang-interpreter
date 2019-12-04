package entities;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<Statement> statements;

    Body() {
        statements = new ArrayList<>();
    }

    public Body(Statement statement) {
        statements = new ArrayList<>();
        addStatement(statement);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
