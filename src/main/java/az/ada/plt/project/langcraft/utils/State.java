import java.util.HashMap;
import java.util.Map;

public class State {
    private Map<String, Object> variables;

    public State() {
        variables = new HashMap<>();
    }

    public void setValue(String name, Object value) {
        variables.put(name, value);
    }

    public Object getValue(String name) {
        return variables.get(name);
    }

    public void printState() {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
