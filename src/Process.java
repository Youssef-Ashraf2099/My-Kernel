public class Process {
    private int processId;     //PCB
    private int executionTime;  // Time required for running application
    private int arrivalTime;    // Arrival time in the system

    public Process(int processId, int executionTime, int arrivalTime) {
        this.processId = processId;
        this.executionTime = executionTime;
        this.arrivalTime = arrivalTime;
    }

    public int getProcessId() { return processId; }
    public int getExecutionTime() { return executionTime; }
    public int getArrivalTime() { return arrivalTime; }
}
