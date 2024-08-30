package radhouene.develop.vcenter.vmwarevcenterautomation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import radhouene.develop.vcenter.vmwarevcenterautomation.controller.records.ClientSO;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/UI-Access")
@RequiredArgsConstructor
public class UIController {
    @Autowired
    VmInfoByFolderRepository vmInfoByFolderRepository;
    @Autowired
    PdfController pdfController;
    @GetMapping
    public String indexForm(Model model) {
        model.addAttribute("clientSO",new ClientSO());
        //pdfController.generatePDFBySO(Objects.requireNonNull(model.getAttribute("tag_SO")).toString());
        //System.out.println(model.getAttribute("tag_SO").toString());
        return "hello";
    }
    @PostMapping
    public String indexSubmit(@ModelAttribute ClientSO clientSO, Model model) {
        model.addAttribute("clientSO",clientSO);
        System.out.println(clientSO.getTag_SO());
        //pdfController.generatePDFBySO(clientSO.getTag_SO());
        String tagExist= vmInfoByFolderRepository.checkIfTagExist(clientSO.getTag_SO());
        String clientExist= vmInfoByFolderRepository.checkIfClientNameExist(clientSO.getClientName());



        if(tagExist==null && clientExist==null){
            return "alert";
        }

        if(clientSO.getEmail().isEmpty() && tagExist!=null){
            return "redirect:/pdf-vcenter-global/"+clientSO.getTag_SO();
        }
        if(clientSO.getEmail().isEmpty() && clientExist!=null){
            return "redirect:/pdf-vcenter-global/client-name/"+clientSO.getClientName();
        }
        if(!clientSO.getEmail().isEmpty() && clientExist!=null){
            return "redirect:/pdf-vcenter-global/client-name/"+clientSO.getClientName()+"/"+clientSO.getEmail();
        }

        return "redirect:/pdf-vcenter-global/"+clientSO.getTag_SO()+"/"+clientSO.getEmail();
    }



}

