import java.io.*;
import java.util.*;

public class MultiCoreSystem {
    public static void main(String[] args) {
        MasterCore master = new MasterCore(2);

        String[] programFiles = {
                "Program_1.txt",
                "Program_3.txt",
                "Program_4.txt",
                "Program_2.txt",
                "Program_3.txt",
                "Program_4.txt"
        };

        for (int i = 0; i < programFiles.length; i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(programFiles[i]))) {
                List<String> instructions = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    instructions.add(line);
                }

                PCB pcb = new PCB(i + 1, instructions.toArray(new String[0]));
                master.addProcess(pcb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        master.startSlaves();
        master.waitForCompletion();
        master.shutdown();
    }
}