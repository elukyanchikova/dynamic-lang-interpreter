package entities;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private List<Statement> statementList;

    Program() {
        this.statementList = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        this.statementList.add(statement);
    }
}
