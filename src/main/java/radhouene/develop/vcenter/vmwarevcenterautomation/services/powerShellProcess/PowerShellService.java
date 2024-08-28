//package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//
//@Component
//public class PowerShellService {
//
//    private Process powerShellProcess;
//    private PrintWriter writer;
//    private BufferedReader reader;
//
//    public void startPowerShellService() {
//        try {
//            // Start the PowerShell script
//            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-File", "src/main/java/radhouene/develop/vcenter/vmwarevcenterautomation/services/powerShellProcess/powerShellProcess.ps1");
//            processBuilder.redirectErrorStream(true);
//            powerShellProcess = processBuilder.start();
//
//            // Setup input/output streams
//            writer = new PrintWriter(new OutputStreamWriter(powerShellProcess.getOutputStream()), true);
//            reader = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
//
//            // Read initialization messages
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("PowerShell: " + line); // Debugging output
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
////    public void startPowerShellService() {
////        try {
////            // Start the PowerShell script
////            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-File", "src/main/java/radhouene/develop/vcenter/vmwarevcenterautomation/services/powerShellProcess/powerShellProcess.ps1");
////            processBuilder.redirectErrorStream(true);
////            powerShellProcess = processBuilder.start();
////
////            // Setup input/output streams
////            writer = new PrintWriter(new OutputStreamWriter(powerShellProcess.getOutputStream()), true);
////            reader = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    public String runCommand(String command) {
//        if (writer == null || reader == null) {
//            startPowerShellService();
//        }
//
//        try {
//            // Send command to PowerShell script
//            writer.println(command);
//            writer.flush();
//
//            // Read the response
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//                if (line.contains("Enter Command")) { // Check for prompt
//                    break;
//                }
//            }
//
//            return output.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error executing command";
//        }
//    }
//}