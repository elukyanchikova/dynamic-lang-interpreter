package interpreter;

import syntaxer.entities.Literal;
import syntaxer.entities.TypeIndicator;

import java.util.HashMap;


public class ScopeTable<String, ValueTypeWrapper> extends HashMap<String, ValueTypeWrapper> {

    class ValueTypeWrapper{
        TypeIndicator type;
        Literal value;

        public ValueTypeWrapper(TypeIndicator type, Literal value) {
            this.type = type;
            this.value = value;
        }
    }

    public ScopeTable() {
        super();
    }

    @Override
    public ValueTypeWrapper put(String key, ValueTypeWrapper value) {
        return super.put(key, value);
    }

}
