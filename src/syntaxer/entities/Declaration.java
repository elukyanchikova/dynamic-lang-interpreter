package syntaxer.entities;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends Statement {
    private List<VariableDefinition> mVariableDefinitions;

    public Declaration(VariableDefinition definition) {
        this.mVariableDefinitions = new ArrayList<>();
        addVariableDefinition(definition);
    }

    public Declaration() {
        this.mVariableDefinitions = new ArrayList<>();
    }

    public void addVariableDefinition(VariableDefinition variableDefinition) {
        this.mVariableDefinitions.add(variableDefinition);
    }

    public List<VariableDefinition> getVariableDefinition(){
        return this.mVariableDefinitions;
    }

    /**
     * @return the variable definitions
     */
    public List<VariableDefinition> getVariableDefinitions() {
        return mVariableDefinitions;
    }
}
