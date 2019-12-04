package entities;

import java.util.ArrayList;
import java.util.List;

public class ExpressionComplex extends Expression {
    private List<Expression> relationList;
    private List<LogicalOperator> logicalOperator1List;

    public ExpressionComplex(Expression relation) {
        relationList = new ArrayList<>();
        logicalOperator1List = new ArrayList<>();
        this.addRelation(relation, LogicalOperator.NONE);
    }

    public void addRelation(Expression relation, LogicalOperator operator) {
        relationList.add(relation);
        logicalOperator1List.add(operator);
    }

    public List<Expression> getRelationList() {
        return relationList;
    }

    public List<LogicalOperator> getLogicalOperator1List() {
        return logicalOperator1List;
    }
}
