package interpreter;

import syntaxer.entities.Literal;
import syntaxer.entities.TypeIndicator;

import java.util.HashMap;


public class ScopeTable extends HashMap<String, ScopeTable.ValueTypeWrapper> {

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

    @Override
    public ValueTypeWrapper get(Object key) {
        return super.get(key);
    }
}
