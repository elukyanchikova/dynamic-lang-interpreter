package syntaxer.entities;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < relationList.size(); i++) {
            switch (logicalOperator1List.get(i)) {
                case OR: sb.append(" or "); break;
                case AND: sb.append(" and "); break;
                case XOR: sb.append(" xor "); break;
                case NONE: break;
            }
            sb.append(relationList.get(i).toString());
        }
        return sb.toString();
    }
}
