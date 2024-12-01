import java.util.concurrent.ConcurrentHashMap;

public class SharedMemory {
    private ConcurrentHashMap<String, Integer> memory = new ConcurrentHashMap<>();

    public void setVariable(String key, int value) {
        memory.put(key, value);
    }

    public int getVariable(String key) {
        return memory.getOrDefault(key, 0);
    }

    public void printMemoryState() {
        System.out.println("Memory State: " + memory);
    }
}
