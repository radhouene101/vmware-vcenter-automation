package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerShellService {

//    @Autowired
//    private final VmInfoByFolderRepository vmInfoByFolderRepository;
//    @Scheduled(fixedRate = 50000)
//    public  void tryingPowerShellExec() throws JSONException, IOException {
//        List<PowerShellRunner.Tag> tags = PowerShellRunner.getTagsList();
//        for(PowerShellRunner.Tag tag: tags){
//            System.out.println(tag.toString());
//            List<String> vmByTag =  PowerShellRunner.vmsByTag(tag.name());
//            System.out.println("size of array is "+vmByTag.size());
//            for(String vm: vmByTag){
//                System.out.println(vm);
//                VmInfoByFolder vmInfoByFolder = vmInfoByFolderRepository.findById(vm).orElseThrow(EntityNotFoundException::new);
//                vmInfoByFolder.setTag_SO(tag.name());
//                vmInfoByFolder.setTag_SO_Client(tag.Description());
//                vmInfoByFolderRepository.save(vmInfoByFolder);
//            }
//
//        }
//    }

}