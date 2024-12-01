import java.util.Queue;

class SlaveCore extends Thread {
    private SharedMemory memory;
    private MasterCore master;
    private PCB process;
    private boolean isRunning = true; // Flag to control the thread lifecycle
    private Queue<PCB> readyQueue;    // Shared ready queue reference

    public SlaveCore(SharedMemory memory, MasterCore master) {
        this.memory = memory;
        this.master = master;
    }

    public void setReadyQueue(Queue<PCB> readyQueue) {
        this.readyQueue = readyQueue; // Assign shared ready queue
    }

    public void shutdown() {
        isRunning = false; // Signal the thread to stop
    }

    @Override
    public void run() {
        while (isRunning) {
            synchronized (readyQueue) {
                if (!readyQueue.isEmpty()) {
                    process = readyQueue.poll();
                    master.incrementActiveProcesses(); // Track active processes
                }
            }

            if (process != null) {
                System.out.println("Processing PCB with ID: " + process.processId);
                while (process.hasNextInstruction()) {
                    String instruction = process.getNextInstruction();
                    System.out.println("Executing instruction: " + instruction);
                    executeInstruction(instruction);
                    memory.printMemoryState(); // Print memory state after each instruction
                }
                process = null;
                master.decrementActiveProcesses(); // Mark process as completed
            } else {
                try {
                    Thread.sleep(50); // Idle wait
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                }
            }
        }
        System.out.println("SlaveCore shutting down.");
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        switch (parts[0]) {
            case "assign":
                if (parts[2].equals("input")) {
                    memory.setVariable(parts[1], (int) (Math.random() * 100));
                } else if (isOperation(parts[2])) {
                    int operand1 = resolveValue(parts[3]);
                    int operand2 = resolveValue(parts[4]);
                    int result = performOperation(parts[2], operand1, operand2);
                    memory.setVariable(parts[1], result);
                } else {
                    memory.setVariable(parts[1], Integer.parseInt(parts[2]));
                }
                break;
            case "print":
                System.out.println(parts[1] + ": " + memory.getVariable(parts[1]));
                break;
            default:
                System.out.println("Unknown Instruction: " + instruction);
        }
    }

    private int resolveValue(String operand) {
        if (isNumeric(operand)) {
            return Integer.parseInt(operand);
        } else {
            return memory.getVariable(operand);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperation(String operation) {
        return operation.equals("add") || operation.equals("subtract") ||
                operation.equals("multiply") || operation.equals("divide");
    }

    private int performOperation(String operation, int operand1, int operand2) {
        switch (operation) {
            case "add":
                return operand1 + operand2;
            case "subtract":
                return operand1 - operand2;
            case "multiply":
                return operand1 * operand2;
            case "divide":
                if (operand2 == 0) throw new ArithmeticException("Division by zero");
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }
}

