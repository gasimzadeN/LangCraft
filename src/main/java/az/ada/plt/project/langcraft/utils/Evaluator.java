public class Evaluator {
    private ASTNode root;
    private State state;

    public Evaluator(ASTNode root) {
        this.root = root;
        this.state = new State();
    }

    public void evaluate() throws EvaluationException {
        evaluateNode(root);
    }

    private void evaluateNode(ASTNode node) throws EvaluationException {
        switch (node.getType()) {
            case PROGRAM:
                evaluateProgram(node);
                break;
            case FUNCTION:
                evaluateFunction(node);
                break;
            case COMPOUND_STATEMENT:
                evaluateCompoundStatement(node);
                break;
            case DECLARATION:
                evaluateDeclaration(node);
                break;
            case ASSIGNMENT:
                evaluateAssignment(node);
                break;
            case IF_STATEMENT:
                evaluateIfStatement(node);
                break;
            case RETURN_STATEMENT:
                evaluateReturnStatement(node);
                break;
            case EXPRESSION:
                evaluateExpression(node);
                break;
            case ADDITIVE_EXPRESSION:
                evaluateAdditiveExpression(node);
                break;
            case MULTIPLICATIVE_EXPRESSION:
                evaluateMultiplicativeExpression(node);
                break;
            case IDENTIFIER:
            case NUMBER:
                // Do nothing for identifiers and numbers in this example
                break;
            default:
                throw new EvaluationException("Unknown node type: " + node.getType());
        }
    }

    private void evaluateProgram(ASTNode programNode) throws EvaluationException {
        for (ASTNode functionNode : programNode.getChildren()) {
            evaluateNode(functionNode);
        }
    }

    private void evaluateFunction(ASTNode functionNode) throws EvaluationException {
        // Perform any necessary function evaluation logic
        evaluateNode(functionNode.getChildren().get(1)); // Evaluate compound statement
    }

    private void evaluateCompoundStatement(ASTNode compoundStatementNode) throws EvaluationException {
        for (ASTNode statementNode : compoundStatementNode.getChildren()) {
            evaluateNode(statementNode);
        }
    }

    private void evaluateDeclaration(ASTNode declarationNode) throws EvaluationException {
        String identifier = (String) declarationNode.getChildren().get(1).getValue();
        ASTNode expressionNode = declarationNode.getChildren().get(2);
        Object value = evaluateExpression(expressionNode);
        state.setValue(identifier, value);
    }

    private void evaluateAssignment(ASTNode assignmentNode) throws EvaluationException {
        String identifier = (String) assignmentNode.getChildren().get(0).getValue();
        ASTNode expressionNode = assignmentNode.getChildren().get(1);
        Object value = evaluateExpression(expressionNode);
        state.setValue(identifier, value);
    }

    private void evaluateIfStatement(ASTNode ifStatementNode) throws EvaluationException {
        ASTNode conditionNode = ifStatementNode.getChildren().get(0);
        ASTNode ifBodyNode = ifStatementNode.getChildren().get(1);
        ASTNode elseBodyNode = ifStatementNode.getChildren().get(2);

        boolean conditionResult = evaluateCondition(conditionNode);

        if (conditionResult) {
            evaluateNode(ifBodyNode);
        } else if (elseBodyNode != null) {
            evaluateNode(elseBodyNode);
        }
    }

    private void evaluateReturnStatement(ASTNode returnStatementNode) throws EvaluationException {
        ASTNode expressionNode = returnStatementNode.getChildren().get(0);
        Object value = evaluateExpression(expressionNode);
        throw new EvaluationException("Return statement encountered with value: " + value);
    }

    private Object evaluateExpression(ASTNode expressionNode) throws EvaluationException {
        return evaluateNode(expressionNode.getChildren().get(0));
    }

    private Object evaluateAdditiveExpression(ASTNode additiveExpressionNode) throws EvaluationException {
        Object leftOperand = evaluateNode(additiveExpressionNode.getChildren().get(0));
        Object rightOperand = evaluateNode(additiveExpressionNode.getChildren().get(2));
        String operator = (String) additiveExpressionNode.getChildren().get(1).getValue();

        if (leftOperand instanceof Integer && rightOperand instanceof Integer) {
            int result = evaluateIntegerOperation((int) leftOperand, (int) rightOperand, operator);
            return result;
        } else if (leftOperand instanceof Float && rightOperand instanceof Float) {
            float result = evaluateFloatOperation((float) leftOperand, (float) rightOperand, operator);
            return result;
        }

        throw new EvaluationException("Invalid operands for additive expression");
    }

    private Object evaluateMultiplicativeExpression(ASTNode multiplicativeExpressionNode) throws EvaluationException {
        Object leftOperand = evaluateNode(multiplicativeExpressionNode.getChildren().get(0));
        Object rightOperand = evaluateNode(multiplicativeExpressionNode.getChildren().get(2));
        String operator = (String) multiplicativeExpressionNode.getChildren().get(1).getValue();

        if (leftOperand instanceof Integer && rightOperand instanceof Integer) {
            int result = evaluateIntegerOperation((int) leftOperand, (int) rightOperand, operator);
            return result;
        } else if (leftOperand instanceof Float && rightOperand instanceof Float) {
            float result = evaluateFloatOperation((float) leftOperand, (float) rightOperand, operator);
            return result;
        }

        throw new EvaluationException("Invalid operands for multiplicative expression");
    }

    private int evaluateIntegerOperation(int leftOperand, int rightOperand, String operator) throws EvaluationException {
        switch (operator) {
            case "+":
                return leftOperand + rightOperand;
            case "-":
                return leftOperand - rightOperand;
            case "*":
                return leftOperand * rightOperand;
            case "/":
                if (rightOperand != 0) {
                    return leftOperand / rightOperand;
                } else {
                    throw new EvaluationException("Division by zero");
                }
            default:
                throw new EvaluationException("Unknown operator: " + operator);
        }
    }

    private float evaluateFloatOperation(float leftOperand, float rightOperand, String operator) throws EvaluationException {
        switch (operator) {
            case "+":
                return leftOperand + rightOperand;
            case "-":
                return leftOperand - rightOperand;
            case "*":
                return leftOperand * rightOperand;
            case "/":
                if (rightOperand != 0.0f) {
                    return leftOperand / rightOperand;
                } else {
                    throw new EvaluationException("Division by zero");
                }
            default:
                throw new EvaluationException("Unknown operator: " + operator);
        }
    }

    private boolean evaluateCondition(ASTNode conditionNode) throws EvaluationException {
        ASTNode leftOperandNode = conditionNode.getChildren().get(0);
        ASTNode rightOperandNode = conditionNode.getChildren().get(1);
        String operator = (String) conditionNode.getValue();

        Object leftOperand = evaluateNode(leftOperandNode);
        Object rightOperand = evaluateNode(rightOperandNode);

        if (leftOperand instanceof Integer && rightOperand instanceof Integer) {
            int leftValue = (int) leftOperand;
            int rightValue = (int) rightOperand;

            switch (operator) {
                case "==":
                    return leftValue == rightValue;
                case "!=":
                    return leftValue != rightValue;
                case ">":
                    return leftValue > rightValue;
                case ">=":
                    return leftValue >= rightValue;
                case "<":
                    return leftValue < rightValue;
                case "<=":
                    return leftValue <= rightValue;
                default:
                    throw new EvaluationException("Unknown operator: " + operator);
            }
        } else {
            throw new EvaluationException("Invalid operands for condition");
        }
    }

    public State getState() {
        return state;
    }
}

