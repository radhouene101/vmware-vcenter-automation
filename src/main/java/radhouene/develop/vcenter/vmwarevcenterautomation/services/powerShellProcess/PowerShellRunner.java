package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import radhouene.develop.vcenter.vmwarevcenterautomation.globalVars.GlobalVars;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class PowerShellRunner {
    public String runCommand(String command) {
    StringBuilder output = new StringBuilder();

    try {
        // Prepare PowerShell command
        String[] cmd = {"powershell.exe", "/c", command};

        // Use ProcessBuilder to run the command
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read the output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Wait for the process to finish
        process.waitFor();
    } catch (Exception e) {
        e.printStackTrace();
    }
        System.out.println("Raw Output: " + output.toString()); // Print raw output for debugging
        return output.toString();
}
    public JSONArray getTags(String commandToRun) throws JSONException {
        PowerShellRunner powerShellRunner = new PowerShellRunner();
        String connectCommand = "Connect-VIServer -Server "+ GlobalVars.serverIP +" -User "+GlobalVars.username
                +" -Password "+GlobalVars.password
                +" | Out-Null ; "
                +commandToRun+" -WarningAction SilentlyContinue -ErrorAction SilentlyContinue";

        String getTagsCommand = powerShellRunner.runCommand(connectCommand);
        //String getTagsCommand = "Get-Tag | ConvertTo-Json";
        String rawOutput = powerShellRunner.runCommand(getTagsCommand);
        //System.out.println(rawOutput);
        // Remove any unwanted content if necessary
        //rawOutput = rawOutput.replaceAll("(?s)^(?=.*?^Warning:.*$).*$", "").trim();

        // Debug the cleaned output
        rawOutput = rawOutput.substring(rawOutput.indexOf('['), rawOutput.indexOf(']'));
        rawOutput = rawOutput.replaceAll("(?s)^(?=.*?^Warning:.*$).*$", "").trim();
        System.out.println("Cleaned Output: " + rawOutput);
        // Convert to JSONObject if output is valid JSON
        return new JSONArray(rawOutput);
    }
    @Scheduled(fixedRate = 50000)
    public void tryingPowerShellExec() throws JSONException {
         String printTags = getTags("Get-Tag | ConvertTo-Json").toString();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+printTags);
    }
}