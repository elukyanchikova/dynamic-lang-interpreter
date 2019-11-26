package entities;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends Statement {
    private List<VariableDefinition> mVariableDefinitions;

    public Declaration(VariableDefinition definition) {
        this.mVariableDefinitions = new ArrayList<>();
        addVariableDefinition(definition);
    }

    public void addVariableDefinition(VariableDefinition variableDefinition) {
        this.mVariableDefinitions.add(variableDefinition);
    }

    /**
     * @return the variable definitions
     */
    public List<VariableDefinition> getVariableDefinitions() {
        return mVariableDefinitions;
    }
}
