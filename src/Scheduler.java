import java.util.ArrayList;

public class Scheduler {
    private PriorityQueue processQueue;
   private static final int QUANTUM = 2;
    
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
            }
            catch (InterruptedException e) {
                System.out.println("Process execution interrupted.");
            }
            if ( currentProcess.getExecutionTime()<=2 && currentProcess.getExecutionTime() > 0) {
                System.out.println("Completed Process ID: " + currentProcess.getProcessId());
                /* WE MIGHT WANT TO ADD A PRINT HERE*/
            }
            else if(currentProcess.getExecutionTime() <= 0){System.out.println("DEBUG PROBLEM WITH SCHEDULELR CLASS");
                System.out.println("THE getExecutionTime is:"+currentProcess.getExecutionTime());}
            else {
                currentProcess.setExecutionTime((currentProcess.getExecutionTime()-2));
                processQueue.insert(currentProcess);
                //System.out.println("Completed Process ID: " + currentProcess.getProcessId());
            }
        }
    }
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler(10); // Adjust max size as needed

        // Adding sample processes
        scheduler.addProcess(1, 5, 0); // Process ID 1, 5 units of execution time, arrival at 0
        scheduler.addProcess(2, 3, 1);
        scheduler.addProcess(3, 1, 2);
        scheduler.addProcess(4, 4, 3);

        // Run the scheduler
        scheduler.run();
    }
}
