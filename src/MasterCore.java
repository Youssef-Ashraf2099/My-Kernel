import java.util.ArrayList;

public class MasterCore {
    private PriorityQueue processQueue;

    public MasterCore(int maxSize) {
        processQueue = new PriorityQueue(maxSize);
    }

    public void addProcess(int processId, int executionTime, int arrivalTime) {
        Process newProcess = new Process(processId, executionTime, arrivalTime);
        processQueue.insert(newProcess);
    }

    public void run() {
        while (!processQueue.isEmpty()) {
            Process currentProcess = (Process) processQueue.remove();
            System.out.println("Executing Process ID: " + currentProcess.getProcessId() +
                    " with execution time: " + currentProcess.getExecutionTime());

            // Simulate process execution (e.g., sleeping for the execution time)
            try {
                Thread.sleep(currentProcess.getExecutionTime() * 100); // Adjust scale as needed
            } catch (InterruptedException e) {
                System.out.println("Process execution interrupted.");
            }

            System.out.println("Completed Process ID: " + currentProcess.getProcessId());
        }
    }

    public static void main(String[] args) {
        MasterCore masterCore = new MasterCore(10); // Adjust max size as needed

        // Adding sample processes
        masterCore.addProcess(1, 5, 0); // Process ID 1, 5 units of execution time, arrival at 0
        masterCore.addProcess(2, 3, 1);
        masterCore.addProcess(3, 1, 2);
        masterCore.addProcess(4, 4, 3);

        // Run the master core
        masterCore.run();
    }
}