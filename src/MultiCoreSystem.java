import java.io.*;
import java.util.*;

public class MultiCoreSystem {
    public static void main(String[] args) {
        MasterCore master = new MasterCore(2); // 2 slave cores

        // List of program files to read
        String[] programFiles = {
                "src/Program_3.txt",
                "src/Program_4.txt"

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
        //master.startACTIVE();
        master.startSlaves();

        master.waitForCompletion(); // Wait for all processes to complete
        master.shutdown(); // Shut down threads after processing
    }
}