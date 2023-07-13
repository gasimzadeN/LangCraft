import java.util.List;

public class ASTNode {
    private NodeType type;
    private Object value;
    private List<ASTNode> children;

    public ASTNode(NodeType type, Object value, List<ASTNode> children) {
        this.type = type;
        this.value = value;
        this.children = children;
    }

    public NodeType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }
}
