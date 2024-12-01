import java.io.*;
import java.util.*;
public class MultiCoreSystem {
    public static void main(String[] args) {
        MasterCore master = new MasterCore(2); // 2 slave cores

        try (BufferedReader reader = new BufferedReader(new FileReader("Program_1.txt"))) {
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
