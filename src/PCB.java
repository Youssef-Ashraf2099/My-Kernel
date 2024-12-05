public class PCB implements Comparable<PCB> {
    int processId;
    int programCounter;
    String[] instructions;
    int base;
    int limit;
    public static int PCBBase = 0;

    public PCB(int processId, String[] instructions) {
        this.processId = processId;
        this.instructions = instructions;
        this.programCounter = 0;
        this.base = PCBBase;
        this.limit = instructions.length;
        PCBBase += instructions.length;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PCB{");
        sb.append("processId=").append(processId);
        sb.append(", programCounter=").append(programCounter);
        sb.append(", base=").append(base);
        sb.append(", limit=").append(limit);
        sb.append("]}");
        return sb.toString();
    }
}