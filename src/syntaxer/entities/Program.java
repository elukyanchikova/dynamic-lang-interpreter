package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private List<Statement> statements;

    public Program() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public List<Statement> getStatements() {
        return this.statements;
    }
}
