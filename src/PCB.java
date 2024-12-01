public class PCB {
    int processId;
    int programCounter; // Tracks current instruction
    String[] instructions;

    public PCB(int processId, String[] instructions) {
        this.processId = processId;
        this.instructions = instructions;
        this.programCounter = 0;
    }

    public boolean hasNextInstruction() {
        return programCounter < instructions.length;
    }

    public String getNextInstruction() {
        return hasNextInstruction() ? instructions[programCounter++] : null;
    }
}

