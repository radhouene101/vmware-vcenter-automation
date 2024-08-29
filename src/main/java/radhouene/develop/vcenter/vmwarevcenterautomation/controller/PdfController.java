package radhouene.develop.vcenter.vmwarevcenterautomation.controller;

import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;
import radhouene.develop.vcenter.vmwarevcenterautomation.services.PdfGeneratorService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping(value = "/pdf-vcenter-global")
public class PdfController {
    private final PdfGeneratorService pdfGeneratorService;
    @Autowired
    private final VmInfoByFolderRepository vmInfoByFolderRepository;

    @GetMapping
    public ResponseEntity<ByteArrayResource> generatePDFGlobal() throws DocumentException, FileNotFoundException {
        try {
            // Generate PDF and get it as a ByteArrayOutputStream
            List<VmInfoByFolder> soAssociatedVms = vmInfoByFolderRepository.findAll();
            ByteArrayOutputStream pdfOutputStream = pdfGeneratorService.GlobalReportPdf(vmInfoByFolderRepository.findAll());

            // Convert the ByteArrayOutputStream to a ByteArrayResource
            ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

            // Set the headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=GlobalVcenterReport--.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Return the PDF as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfResource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{SO}")
    public ResponseEntity<ByteArrayResource> generatePDFBySO(@PathVariable String SO)  {
        try {
            // Generate PDF and get it as a ByteArrayOutputStream
            List<VmInfoByFolder> soAssociatedVms = vmInfoByFolderRepository.findByTag_SO(SO);
            ByteArrayOutputStream pdfOutputStream = pdfGeneratorService.GlobalReportPdf(soAssociatedVms);

            // Convert the ByteArrayOutputStream to a ByteArrayResource
            ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

            // Set the headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+SO+"VcenterReport--.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Return the PDF as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfResource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
