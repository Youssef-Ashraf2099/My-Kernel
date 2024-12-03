import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Logger;

class SlaveCore extends Thread {
    int number=0;
    private static final Logger logger = Logger.getLogger(SlaveCore.class.getName());
    private SharedMemory memory;
    private MasterCore master;
    private PCB process;
    private boolean isRunning = true; // Flag to control the thread lifecycle
    private PriorityQueue<PCB> readyQueue;    // Shared ready queue reference
    private Queue<PCB> ACTIVE;   // Shared ready queue reference

    public SlaveCore(SharedMemory memory, MasterCore master,int x) {
        this.memory = memory;
        this.master = master;
        number=x;
    }

    public void setReadyQueue(PriorityQueue<PCB> readyQueue) {
        this.readyQueue = readyQueue; // Assign shared ready queue
    }
    public void setACTIVEQueue(Queue<PCB> A) {
        this.ACTIVE = A; // Assign shared ready queue
    }

    public void shutdown() {
        isRunning = false; // Signal the thread to stop
    }

    @Override
    public void run() {
        while ((!readyQueue.isEmpty()||!ACTIVE.isEmpty())&&this.isRunning) {
            synchronized (ACTIVE) {
                if (!readyQueue.isEmpty()&&ACTIVE.isEmpty()){master.startACTIVE();}
                System.out.println("SlaveCore " + this.number + "HERE");
                if (!ACTIVE.isEmpty()) {
                    process = ACTIVE.poll();
                    master.incrementActiveProcesses(); // Track active processes
                    logger.info("SlaveCore " + this.number + " picked up a process.");
                } else {
                    System.out.println("SlaveCore " + this.number + " found the ACTIVE empty.");
                }
            }


            if (process != null) {
                System.out.println("SlaveCore " + this.number + " processing PCB with ID: " + process.processId);
                int c=0;
                while (process.hasNextInstruction()&&c<2) {
                    String instruction = process.getNextInstruction();
                    logger.info("SlaveCore " + this.number + " executing instruction: " + instruction);
                    executeInstruction(instruction);
                    memory.printMemoryState();
                    c++;// Print memory state after each instruction
                }
                if(process.programCounter>=process.instructions.length){
                process = null;// Mark process as completed
                master.decrementActiveProcesses();
                }
                else{readyQueue.add(process);
                    master.decrementActiveProcesses();
                }
            }
                try {
                    Thread.sleep(50); // Idle wait
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                }
        }
        System.out.println("SlaveCore " + this.number + " shutting down.");
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        switch (parts[0]) {
            case "assign":
                if (parts[2].equals("input")) {
                    memory.setVariable(parts[1], (int) (Math.random() * 100)+1);
                }  if (parts.length>3&&isOperation(parts[3])) {
                    int operand1 = resolveValue(parts[4]);
                    int operand2 = resolveValue(parts[5]);
                    int result = performOperation(parts[3], operand1, operand2);
                    memory.setVariable(parts[1], result);
                }/* else {
                    memory.setVariable(parts[1], Integer.parseInt(parts[2]));
                }*/
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

