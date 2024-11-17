class Process implements Comparable<Process> {
    private int processId;
    private int executionTime;
    private int arrivalTime;

    public Process(int processId, int executionTime, int arrivalTime) {
        this.processId = processId;
        this.executionTime = executionTime;
        this.arrivalTime = arrivalTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.executionTime, other.executionTime);
    }
}