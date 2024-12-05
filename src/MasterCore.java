import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

class MasterCore {
    private PriorityQueue<PCB> readyQueue = new PriorityQueue<>();
    private SlaveCore[] slaveCores;
    private SharedMemory memory;
    private int activeProcesses = 0;
    private Queue<PCB> activeQueue = new LinkedList<>();

    public MasterCore(int numCores, int memorySize, List<PCB> pcbs) {
        memory = new SharedMemory(memorySize);
        memory.setPCBs(pcbs);
        slaveCores = new SlaveCore[numCores];
        for (int i = 0; i < numCores; i++) {
            slaveCores[i] = new SlaveCore(memory, this, i);
            slaveCores[i].setReadyQueue(readyQueue);
            slaveCores[i].setActiveQueue(activeQueue);
        }
    }

    public synchronized void startSlaves() {
        for (SlaveCore core : slaveCores) {
            core.start();
        }
    }

    public synchronized void addProcess(PCB process) {
        readyQueue.add(process);
    }

    public synchronized void startActiveQueue() {
        while (!readyQueue.isEmpty()) {
            activeQueue.add(readyQueue.poll());
        }
    }

    public synchronized void incrementActiveProcesses() {
        activeProcesses++;
    }

    public synchronized void decrementActiveProcesses() {
        activeProcesses--;
        if (activeProcesses == 0 && readyQueue.isEmpty() && activeQueue.isEmpty()) {
            notifyAll();
        }
    }

    public void waitForCompletion() {
        synchronized (this) {
            while (!activeQueue.isEmpty() || !readyQueue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void shutdown() {
        for (SlaveCore core : slaveCores) {
            core.shutdown();
        }
        for (SlaveCore core : slaveCores) {
            try {
                core.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("MasterCore shutting down.");
    }
}