import java.util.PriorityQueue;
import java.util.Queue;

class SlaveCore extends Thread {
    int id;
    private SharedMemory memory;
    private MasterCore master;
    private PCB process;
    private boolean isRunning = true;
    private PriorityQueue<PCB> readyQueue;
    private Queue<PCB> activeQueue;

    public SlaveCore(SharedMemory memory, MasterCore master, int id) {
        this.memory = memory;
        this.master = master;
        this.id = id;
    }

    public void setReadyQueue(PriorityQueue<PCB> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public void setActiveQueue(Queue<PCB> activeQueue) {
        this.activeQueue = activeQueue;
    }

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void run() {
        while ((!readyQueue.isEmpty() || !activeQueue.isEmpty()) && isRunning) {
            synchronized (activeQueue) {
                if (!readyQueue.isEmpty() && activeQueue.isEmpty()) {
                    master.startActiveQueue();
                }
                System.out.println("SlaveCore " + id + " HERE");
                if (!activeQueue.isEmpty()) {
                    process = activeQueue.poll();
                    if (process != null) {
                        master.incrementActiveProcesses();
                    }
                    System.out.println("SlaveCore " + id + " picked up a process.");
                } else {//Unreachable code
                    System.out.println("SlaveCore " + id + " found the activeQueue empty.");
                }//
            }

            if (process != null) {
                System.out.println("SlaveCore " + id + " processing PCB with ID: " + process.processId);
                int quantum = 0;
                while (process.hasNextInstruction() && quantum < 2) {
                    String instruction = process.getNextInstruction();
                    System.out.println("SlaveCore " + id + " executing instruction: " + instruction);
                    executeInstruction(instruction);
                    memory.printMemoryState();
                    quantum++;
                }
                if (process.hasNextInstruction()) {
                    synchronized (readyQueue) {
                        readyQueue.add(process);
                        System.out.println("SlaveCore " + id + " moved process back to readyQueue.");
                    }
                }
                master.decrementActiveProcesses();
                process = null;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("SlaveCore " + id + " shutting down.");
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        switch (parts[0]) {
            case "assign":
                if (parts[2].equals("input")) {
                    memory.setVariable(parts[1], (int) (Math.random() * 100) + 1);
                }
                if (parts.length > 3 && isOperation(parts[3])) {
                    int operand1 = resolveValue(parts[4]);
                    int operand2 = resolveValue(parts[5]);
                    int result = performOperation(parts[3], operand1, operand2);
                    memory.setVariable(parts[1], result);
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