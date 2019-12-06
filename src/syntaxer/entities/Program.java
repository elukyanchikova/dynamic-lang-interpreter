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

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Statement s: statements) {
            sb.append(s.toString());
        }
        return sb.toString();
    }
}
