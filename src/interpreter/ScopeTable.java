package interpreter;

import syntaxer.entities.TypeIndicator;

import java.util.HashMap;


public class ScopeTable<String, Integer> extends HashMap<String, Integer> {

    class ValueTypeWrapper{
        TypeIndicator type;

    }

    public ScopeTable() {
        super();
    }
}
