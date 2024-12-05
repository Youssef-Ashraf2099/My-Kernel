import java.io.*;
import java.util.*;

public class MultiCoreSystem {
    public static void main(String[] args) {
        String[] programFiles = {
                "Program_1.txt",
                "Program_2.txt",
                "Program_3.txt",
                "Program_4.txt"
        };

        int totalMemorySize = 0;
        List<PCB> pcbs = new ArrayList<>();

        for (int i = 0; i < programFiles.length; i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(programFiles[i]))) {
                List<String> instructions = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    instructions.add(line);
                }

                PCB pcb = new PCB(i + 1, instructions.toArray(new String[0]));
                pcbs.add(pcb);
                totalMemorySize += instructions.size();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MasterCore master = new MasterCore(2, totalMemorySize, pcbs);

        for (PCB pcb : pcbs) {
            master.addProcess(pcb);
        }

        master.startSlaves();
        master.waitForCompletion();
        master.shutdown();
    }
}