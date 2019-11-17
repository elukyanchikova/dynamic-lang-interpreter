package entities;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private List<Statement> mStatements;

    public Program() {
        this.mStatements = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        this.mStatements.add(statement);
    }

    public List<Statement> getDeclarations() {
        return this.mStatements;
    }
}
