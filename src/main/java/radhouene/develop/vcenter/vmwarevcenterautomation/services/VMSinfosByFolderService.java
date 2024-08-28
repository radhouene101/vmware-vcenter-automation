package radhouene.develop.vcenter.vmwarevcenterautomation.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfosAllHistory;
import radhouene.develop.vcenter.vmwarevcenterautomation.globalVars.GlobalVars;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfosAllHistoryRepository;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class VMSinfosByFolderService {
    private static RestTemplate restTemplate = new RestTemplate();
    private final VmInfoByFolderRepository repository;
    private final VmInfosAllHistoryRepository historyRepository;
    private final String vmByIdUrlPrefix = "https://"+ GlobalVars.serverIP +"/api/vcenter/vm/";
    private final String allVmsUrl = "https://"+ GlobalVars.serverIP +"/rest/vcenter/vm";
    private final String allFoldersUrl = "https://"+ GlobalVars.serverIP +"/rest/vcenter/folder";
    private final String vmByFolderPrefix = "https://"+ GlobalVars.serverIP +"/api/vcenter/vm?folders=";
    private final String vmNetworkUrlPrefix = "https://"+ GlobalVars.serverIP +"/api/vcenter/vm/";
    private final String vmNetworkUrlSuffix = "/guest/networking/interfaces/";
    public ResponseEntity<String> listVMs() {
        // Endpoint URL

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                allVmsUrl,
                HttpMethod.GET,
                AuthenticationManagement.factoryHeader(),
                String.class
        );
        return responseEntity;
    }

    public Integer numberOfVMs() {
        return listVMs().getBody().split("name").length - 1;
    }

    public ResponseEntity<String> allFoldersResponse() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                allFoldersUrl,
                HttpMethod.GET,
                AuthenticationManagement.factoryHeader(),
                String.class
        );
        return responseEntity;
    }


    public List<Folder> allFoldersList() throws JSONException {
        JSONObject foldersReponse = new JSONObject(allFoldersResponse().getBody());
        JSONArray folders = foldersReponse.getJSONArray("value");
        List<Folder> output = new ArrayList<>();
        for(int i = 0 ; i<folders.length() ; i++){
            JSONObject currentFolder = folders.getJSONObject(i);
            Folder folder =  new Folder(currentFolder.getString("folder"),currentFolder.getString("name"),currentFolder.getString("type"));
            output.add(folder);
            System.out.println(folder);
        }
        return output;
    }


    public ResponseEntity<String> listVMsByFolder(String folderId) {
        // Endpoint URL
        String url = vmByFolderPrefix + folderId;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                AuthenticationManagement.factoryHeader(),
                String.class
        );
        return responseEntity;
    }


    public List<String> vmIdsofFolder(String folderId) throws JSONException {
        JSONArray vmsResponse = new JSONArray(listVMsByFolder(folderId).getBody());
        List<String> output = new ArrayList<>();
        for(int i = 0 ; i<vmsResponse.length() ; i++){
            JSONObject currentVm = vmsResponse.getJSONObject(i);
            output.add(currentVm.getString("vm"));
            System.out.println(currentVm.getString("vm"));
        }
        return output;
    }
    public record Folder(String folderId, String folderName, String folderType) { }
    @Scheduled(fixedRate = 20000)
    public void printVMs() throws JSONException {
        saveAllVms();
    }


    public ResponseEntity<String> vmInfoById(String vmId){
        String url = vmByIdUrlPrefix + vmId;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                AuthenticationManagement.factoryHeader(),
                String.class
        );
        return responseEntity;
    }
    public VmInfoByFolder vmInfoByIdObject(ResponseEntity<String> VMjson,Folder folder,String vmID) throws JSONException {
        JSONObject vm = new JSONObject(VMjson.getBody());


        VmInfoByFolder output = new VmInfoByFolder();
        output.setFolder_clientName(folder.folderName);
        output.setVmName(vm.getString("name"));
        output.setOSType(vm.getString("guest_OS"));
        output.setCpuCount(vm.getJSONObject("cpu").getString("count"));
        output.setVmId(vmID);
        output.setPowerState(vm.getString("power_state"));
        String memory = vm.getJSONObject("memory").getString("size_MiB");
        Float memoryLong = Float.parseFloat(memory)/1024;
        output.setMemorySizeMB(memoryLong +"GB");
        output.setDiscSpaceGB(humanReadableByteCountSI(vm.getJSONObject("disks").getJSONObject("2000").getLong("capacity")));
        if(Objects.equals(vm.getString("power_state"), "POWERED_ON")) {
            List<String> vmIps = vmNetworkInterfaces(vmID);
            StringBuilder ips= new StringBuilder();
            for(String s : vmIps){
                ips.append("[").append(s).append("] ");
            }
            output.setIps(ips.toString());
        }


        return  output;
    }
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public List<String> vmNetworkInterfaces(String vmId) throws JSONException {
        String url = vmNetworkUrlPrefix + vmId + vmNetworkUrlSuffix;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                AuthenticationManagement.factoryHeader(),
                String.class
        );
        JSONArray interfaces = new JSONArray(responseEntity.getBody());
        List<String> output = new ArrayList<>();
        for(int i = 0 ; i<interfaces.length() ; i++){
            JSONObject currentInterface = interfaces.getJSONObject(i);
            JSONObject ipObject= (JSONObject) currentInterface.get("ip");
            JSONArray ipAdressesArray= (JSONArray) ipObject.get("ip_addresses");
            for(int j = 0 ; j<ipAdressesArray.length() ; j++){
                JSONObject currentIp = ipAdressesArray.getJSONObject(j);
                output.add(currentIp.getString("ip_address"));
                System.out.println(currentIp.getString("ip_address"));
            }
        }
        return output;
    }

    public void saveAllVms() throws JSONException {
        repository.deleteAll();
        List<Folder> allFolders = allFoldersList();
        for(Folder folder : allFolders){
            List<String> vmsOfCurrentFolder = vmIdsofFolder(folder.folderId);
                for(String vm : vmsOfCurrentFolder){
                    VmInfoByFolder vmToSave = vmInfoByIdObject(vmInfoById(vm),folder,vm);
                    repository.save(vmToSave);
                    historyRepository.save(new VmInfosAllHistory(vmToSave));

                }
        }


    }
}
