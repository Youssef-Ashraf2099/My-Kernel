import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class MasterCore {
    private PriorityQueue<PCB> readyQueue = new PriorityQueue<>();// make it a priority queue
    private SlaveCore[] slaveCores;
    private SharedMemory memory;
    private int activeProcesses = 0;
    private Queue<PCB> ACTIVE = new LinkedList<>();// make it a priority queue


    public MasterCore(int numCores) {
        memory = new SharedMemory();
        slaveCores = new SlaveCore[numCores];
        for (int i = 0; i < numCores; i++) {
            slaveCores[i] = new SlaveCore(memory, this,i);
            slaveCores[i].setReadyQueue(readyQueue);
            slaveCores[i].setACTIVEQueue(ACTIVE);// Set shared ready queue
        }
    }
    public synchronized void startSlaves() {
        for (int i = 0; i < 2; i++) {
            slaveCores[i].start();
        }
    }

    public synchronized void addProcess(PCB process) {
        readyQueue.add(process);
    }
    public synchronized void startACTIVE() {
        while(!readyQueue.isEmpty()){
            ACTIVE.add(readyQueue.poll());
        }
    }

    public synchronized void incrementActiveProcesses() {
        activeProcesses++;
    }

    public synchronized void decrementActiveProcesses() {
        activeProcesses--;
        if (activeProcesses == 0 && readyQueue.isEmpty()&&ACTIVE.isEmpty()) {
            notifyAll();
        }
    }

   public void waitForCompletion() {
        synchronized (this) {
            while (!ACTIVE.isEmpty() || !readyQueue.isEmpty()) { //--------> check this
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

