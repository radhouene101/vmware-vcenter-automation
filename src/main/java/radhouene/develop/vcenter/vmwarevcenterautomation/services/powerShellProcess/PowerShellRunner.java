package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.globalVars.GlobalVars;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@AllArgsConstructor
public class PowerShellRunner {
    @Autowired
    private final VmInfoByFolderRepository vmInfoByFolderRepository;
    private static final String connect="Connect-VIServer -Server "+ GlobalVars.serverIP +" -User "+GlobalVars.username
            +" -Password "+GlobalVars.password
            +" 2>$null | Out-Null ; ";

    private static int lineCount=0;



    public static String runCommandAndReturnJsonArrayFormat(String command) {
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
    public static String runCommand(String command) {
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
                lineCount++;
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }



//    public static String runCommand(String command) {
//        StringBuilder output = new StringBuilder();
//
//        Thread thread = new Thread(() -> {
//            try {
//                // Prepare PowerShell command
//                String[] cmd = {"powershell.exe", "/c", command};
//
//                // Use ProcessBuilder to run the command
//                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//                processBuilder.redirectErrorStream(true);
//                Process process = processBuilder.start();
//
//                // Read the output
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    output.append(line).append("\n");
//                }
//
//                // Wait for the process to finish
//                process.waitFor();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        thread.start();
//        try {
//            thread.join();  // Wait for the thread to finish
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return output.toString();
//    }
//
//    public static String runCommandAndReturnJsonArrayFormat(String command) {
//        StringBuilder output = new StringBuilder();
//
//        Thread thread = new Thread(() -> {
//            try {
//                // Prepare PowerShell command
//                String[] cmd = {"powershell.exe", "/c", command};
//
//                // Use ProcessBuilder to run the command
//                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//                processBuilder.redirectErrorStream(true);
//                Process process = processBuilder.start();
//
//                // Read the output
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//
//                boolean isFROMjson = false;
//                while ((line = reader.readLine()) != null) {
//
//                    if (line.contains("[")) {
//                        isFROMjson = true;
//
//                    } else if (line.contains("]")) {
//                        output.append(line).append("\n");
//                        break;
//                    }
//                    if (isFROMjson) {
//                        output.append(line).append("\n");
//                    }
//                }
//
//                // Wait for the process to finish
//                process.waitFor();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        thread.start();
//        try {
//            thread.join();  // Wait for the thread to finish
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return output.toString();
//    }


    public JSONArray getTagsJson(String commandToRun) throws JSONException {

        String connectCommand = "Connect-VIServer -Server "+ GlobalVars.serverIP +" -User "+GlobalVars.username
                +" -Password "+GlobalVars.password
                +" | Out-Null ; "
                +commandToRun+" -WarningAction SilentlyContinue -ErrorAction SilentlyContinue";

        String getTagsCommand = runCommandAndReturnJsonArrayFormat(connectCommand);
        //System.out.println("Cleaned Output: " + getTagsCommand);
        // Convert to JSONObject if output is valid JSON
        return new JSONArray(getTagsCommand);
    }

    public record Tag(String name, String Description) {}


    @Scheduled(fixedDelay = 50000)
    public void tryingPowerShellExec() throws JSONException, IOException {
         List<Tag> tags = getTagsList();
            for(Tag tag: tags){
                System.out.println(tag.toString());
                List<String> vmByTag =  vmsByTag(tag.name());
                System.out.println("size of array is "+vmByTag.size());
                for(String vm: vmByTag){
                    System.out.println(vm);
                    VmInfoByFolder vmInfoByFolder = vmInfoByFolderRepository.findById(vm).orElse(null);
                    if(vmInfoByFolder==null){
                        continue;
                    }
                    String reservedStorage = getReservedStorage(vmInfoByFolder.getVmName());
                    String usedStorage = getUsedStorage(vmInfoByFolder.getVmName());
                    String diskTypes = getDiskTypes(vmInfoByFolder.getVmName());

                    vmInfoByFolder.setUseddiscSpaceGB(usedStorage.substring(0,usedStorage.indexOf(".")));
                    vmInfoByFolder.setReserveDdiscSpaceGB(reservedStorage.substring(0,reservedStorage.indexOf(".")));

                    vmInfoByFolder.setDiscType(diskTypes);
                    vmInfoByFolder.setTag_SO(tag.name());
                    vmInfoByFolder.setTag_SO_Client(tag.Description());
                    vmInfoByFolderRepository.save(vmInfoByFolder);
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

//TODO: Set-PowerCLIConfiguration -Scope User -ParticipateInCEIP $true or $false. put this in every server sinon data matjich shyha
    public List<String> vmsByTag(String tag) throws JSONException, IOException {
        String commandToListVmByTag = "Get-VM -Tag "+ tag+ " -WarningAction SilentlyContinue 2>$null | % { $_.Id -replace '^VirtualMachine-', '' } -WarningAction SilentlyContinue -ErrorAction SilentlyContinue | Out-String";

        String outputOfCommand=runCommand(connect+commandToListVmByTag);
        List<String> vmList = new ArrayList<>();
        if(lineCount>1){
            outputOfCommand=runCommand(connect+commandToListVmByTag);
            //String[] vms = outputOfCommand.split("\n");
            String[] splittedIds = outputOfCommand.split("\n");
            vmList.addAll(List.of(splittedIds));

            return vmList;
        }
        String[] splittedIds = outputOfCommand.split("\n");
        vmList.addAll(List.of(splittedIds));
        return vmList;

    }

    public String getReservedStorage(String vmName) throws JSONException {
        String commandToGetStorage = " Get-VM -Name "+ vmName +" | Get-HardDisk | Measure-Object -Property Capcity -Sum).Sum\n";
        String outputOfCommand=runCommand(connect+commandToGetStorage);
        return outputOfCommand;
    }
    public String getUsedStorage(String vmName) throws JSONException {
        String commandToGetStorage = " (Get-VM -Name "+vmName+" | Select-Object -ExpandProperty usedSpaceGB)";
        String outputOfCommand=runCommand(connect+commandToGetStorage);
        return outputOfCommand;
    }
    public String getDiskTypes(String vmName) throws JSONException {
        String commandToGetStorage = " (Get-VM -Name "+vmName+" | Get-HardDisk | Select-Object -ExpandProperty DiskType | Group-Object | ForEach-Object { \"$($_.Count) $($_.Name)\" }) -join \", \"";
        String outputOfCommand=runCommand(connect+commandToGetStorage);
        return outputOfCommand;
    }
}