package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import radhouene.develop.vcenter.vmwarevcenterautomation.globalVars.GlobalVars;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class PowerShellRunner {
    private static final String connect="Connect-VIServer -Server "+ GlobalVars.serverIP +" -User "+GlobalVars.username
            +" -Password "+GlobalVars.password
            +" 2>$null | Out-Null ; Set-PowerCLIConfiguration -Scope User -ParticipateInCEIP $false ; ";
    private int lineCount=0;
    public String runCommandAndReturnJsonArrayFormat(String command) {
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

        boolean isFROMjson = false;
        while ((line = reader.readLine()) != null) {

            if(line.contains("[")){
                isFROMjson= true;

            }else if(line.contains("]")){
                output.append(line).append("\n");
                break;

            }
            if(isFROMjson){
                output.append(line).append("\n");
            }
        }

        // Wait for the process to finish
        process.waitFor();
    } catch (Exception e) {
        e.printStackTrace();
    }

        return output.toString();
}
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
                this.lineCount++;
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
    public JSONArray getTagsJson(String commandToRun) throws JSONException {
        PowerShellRunner powerShellRunner = new PowerShellRunner();
        String connectCommand = "Connect-VIServer -Server "+ GlobalVars.serverIP +" -User "+GlobalVars.username
                +" -Password "+GlobalVars.password
                +" | Out-Null ; "
                +commandToRun+" -WarningAction SilentlyContinue -ErrorAction SilentlyContinue";

        String getTagsCommand = powerShellRunner.runCommandAndReturnJsonArrayFormat(connectCommand);
        //System.out.println("Cleaned Output: " + getTagsCommand);
        // Convert to JSONObject if output is valid JSON
        return new JSONArray(getTagsCommand);
    }

    public record Tag(String name, String Description) {}


    @Scheduled(fixedRate = 50000)
    public void tryingPowerShellExec() throws JSONException {
         List<Tag> tags = getTagsList();
            for(Tag tag: tags){
                System.out.println(tag.toString());
                List<String> vmByTag = vmsByTag(tag.name());
                for(String vm: vmByTag){
                    System.out.println("-----------------------------");
                    System.out.println(vm);
                    System.out.println("*-----------------------------*");
                }

            }
    }
    public List<Tag> getTagsList() throws JSONException {
        JSONArray tags = getTagsJson("Get-Tag | ConvertTo-Json");
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < tags.length(); i++) {
            if(!tags.getJSONObject(i).getString("Name").startsWith("SO")){
                continue;
            }
            tagList.add(new Tag(tags.getJSONObject(i).getString("Name"), tags.getJSONObject(i).getString("Description")));
        }
        return tagList;
    }



    public List<String> vmsByTag(String tag) throws JSONException {
        String commandToListVmByTag = "Get-VM -Tag "+ tag+ " -WarningAction SilentlyContinue 2>$null | % { $_.Id -replace '^VirtualMachine-', '' } -WarningAction SilentlyContinue -ErrorAction SilentlyContinue";
        PowerShellRunner powerShellRunner = new PowerShellRunner();
        String outputOfCommand=powerShellRunner.runCommand(connect+commandToListVmByTag);
        List<String> vmList = new ArrayList<>();
        if(this.lineCount>1){
            outputOfCommand=powerShellRunner.runCommandAndReturnJsonArrayFormat(connect+commandToListVmByTag);

            JSONArray vms = new JSONArray(outputOfCommand);
//            System.out.println("888888888888888888888");
//            System.out.println(outputOfCommand);
//            System.out.println("88888888888888888888");
        for (int i = 0; i < vms.length(); i++) {
            vmList.add(vms.get(i).toString());
        }
        return vmList;
        }
        vmList.add(outputOfCommand);
        return vmList;
    }
}