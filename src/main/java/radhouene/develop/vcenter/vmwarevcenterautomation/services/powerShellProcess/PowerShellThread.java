package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;


import lombok.Data;

@Data
public class PowerShellThread extends Thread{
    private String command;
    private String result;
    private int exitCode;
    private boolean isFinished;

    public PowerShellThread(String command) {
        this.command = command;
        this.isFinished = false;
    }


    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("powershell.exe -Command " + command);
            process.waitFor();
            this.exitCode = process.exitValue();
            this.result = process.getOutputStream().toString();
            this.isFinished = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}