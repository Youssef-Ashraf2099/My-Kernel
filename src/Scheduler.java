import java.util.ArrayList;

public class Scheduler {
    private PriorityQueue processQueue;

    public Scheduler(int maxSize) {
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
}