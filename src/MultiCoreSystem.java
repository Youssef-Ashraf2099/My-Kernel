import java.io.*;
import java.util.*;
public class MultiCoreSystem {
    public static void main(String[] args) {
        MasterCore master = new MasterCore(2); // 2 slave cores

        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/ahmed/OneDrive/Desktop/GIU_2531_64_19383_2024-12-01T15_08_01/Project Modified/Program_2.txt"))) {
            List<String> instructions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                instructions.add(line);
            }
            PCB pcb = new PCB(1, instructions.toArray(new String[0]));
            master.addProcess(pcb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        master.waitForCompletion(); // Wait for all processes to complete
        master.shutdown(); // Shut down threads after processing
    }
}
