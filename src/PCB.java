public class PCB implements Comparable<PCB> {
    int processId;
    int programCounter;
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

    @Override
    public int compareTo(PCB o) {
        int burst1 = this.instructions.length - this.programCounter;
        int burst2 = o.instructions.length - o.programCounter;
        return burst1 - burst2;
    }
}