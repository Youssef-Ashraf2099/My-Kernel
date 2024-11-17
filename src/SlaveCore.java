public class SlaveCore extends Thread {
    private String name;
    private Process assignedProcess;

    public SlaveCore(String name) {
        this.name = name;
    }

    public void assignProcess(Process process) {
        this.assignedProcess = process;
    }

    @Override
    public void run() {
        if (assignedProcess != null) {
            System.out.println(name + " is executing Process ID: " + assignedProcess.getProcessId() +
                    " for " + assignedProcess.getExecutionTime() + " units."); //shows status

            try {
                Thread.sleep(assignedProcess.getExecutionTime() * 100);  //can be changed ...//
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted.");
            }
            System.out.println(name + " completed Process ID: " + assignedProcess.getProcessId());
        }
    }
}