package interpreter;

import syntaxer.entities.Literal;
import syntaxer.entities.TypeIndicator;

import java.util.HashMap;


public class ScopeTable extends HashMap<String, ScopeTable.ValueTypeWrapper> {

    static class ValueTypeWrapper{
        private TypeIndicator type;
        private Literal value;

        public ValueTypeWrapper(TypeIndicator type, Literal value) {
            this.type = type;
            this.value = value;
        }

        public Literal getValue() {
            return value;
        }

        public TypeIndicator getType() {
            return type;
        }

        public void setType(TypeIndicator type) {
            this.type = type;
        }

        public void setValue(Literal value) {
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
