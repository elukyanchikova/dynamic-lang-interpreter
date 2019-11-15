package entities;

import java.util.ArrayList;
import java.util.List;

public class Declarations extends Statement {
    List<VariableDefinition> variableDefinitionList;

    Declarations() {
        this.variableDefinitionList = new ArrayList<>();
    }

    public void addDeclaration(VariableDefinition variableDefinition) {
        this.variableDefinitionList.add(variableDefinition);
    }
}
