import java.util.LinkedList;
import java.util.Queue;

class MasterCore {
    private Queue<PCB> readyQueue = new LinkedList<>();
    private SlaveCore[] slaveCores;
    private SharedMemory memory;
    private int activeProcesses = 0;

    public MasterCore(int numCores) {
        memory = new SharedMemory();
        slaveCores = new SlaveCore[numCores];
        for (int i = 0; i < numCores; i++) {
            slaveCores[i] = new SlaveCore(memory, this);
            slaveCores[i].setReadyQueue(readyQueue); // Set shared ready queue
            slaveCores[i].start();
        }
    }

    public synchronized void addProcess(PCB process) {
        readyQueue.add(process);
    }

    public synchronized void incrementActiveProcesses() {
        activeProcesses++;
    }

    public synchronized void decrementActiveProcesses() {
        activeProcesses--;
        if (activeProcesses == 0 && readyQueue.isEmpty()) {
            notifyAll();
        }
    }

    public void waitForCompletion() {
        synchronized (this) {
            while (activeProcesses > 0 || !readyQueue.isEmpty()) {
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

