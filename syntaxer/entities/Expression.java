package entities;

import java.util.ArrayList;
import java.util.List;

public class Expression {
    List<Relation> relationList;
    List<LogicalOperator> logicalOperator1List;

    Expression(Relation relation) {
        relationList = new ArrayList<>();
        logicalOperator1List = new ArrayList<>();
        this.addRelation(relation, LogicalOperator.NONE);
    }

    public void addRelation(Relation relation, LogicalOperator operator) {
        relationList.add(relation);
        logicalOperator1List.add(operator);
    }
}
