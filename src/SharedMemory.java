import java.util.HashMap;
import java.util.Map;

public class SharedMemory {
    private Map<String, Integer> memory = new HashMap<>();

    public synchronized void setVariable(String key, int value) {
        memory.put(key, value);
        System.out.println("Set " + key + " to " + value);
    }

    public synchronized int getVariable(String key) {
        return memory.getOrDefault(key, 0);
    }
}
