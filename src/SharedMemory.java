import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedMemory {
    private final String[] variableNames;
    private final Integer[] values;
    private List<PCB> pcbs;
    private final Lock printLock = new ReentrantLock();

    public SharedMemory(int size) {
        this.variableNames = new String[size];
        this.values = new Integer[size];
    }

    public void setPCBs(List<PCB> pcbs) {
        this.pcbs = pcbs;
    }

    public synchronized void setVariable(int base, int limit, String key, int value) {
        for (int i = base; i < base + limit; i++) {
            if (variableNames[i] == null) {
                variableNames[i] = key;
                values[i] = value;
                return;
            }
        }
    }

    public synchronized int getVariable(int base, int limit, String key) {
        for (int i = base; i < base + limit; i++) {
            if (key.equals(variableNames[i])) {
                return values[i];
            }
        }
        return 0;
    }

    public void printMemoryState() {
        printLock.lock();
        try {
            StringBuilder memoryState = new StringBuilder("Memory State:\t");
            for (PCB pcb : pcbs) {
                memoryState.append("Process ").append(pcb.processId).append(" [");
                boolean first = true;
                for (int i = pcb.base; i < pcb.base + pcb.limit; i++) {
                    if (variableNames[i] != null) {
                        if (!first) {
                            memoryState.append(", ");
                        }
                        memoryState.append(variableNames[i]).append(" : ").append(values[i]);
                        first = false;
                    }
                }
                memoryState.append("] ");
            }
            System.out.println(memoryState);
        } finally {
            printLock.unlock();
        }
    }
}