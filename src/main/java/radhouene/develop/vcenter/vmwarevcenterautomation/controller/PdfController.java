package radhouene.develop.vcenter.vmwarevcenterautomation.controller;

import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radhouene.develop.vcenter.vmwarevcenterautomation.services.PdfGeneratorService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping(value = "/pdf-vcenter-global")
public class PdfController {
    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping
    public ResponseEntity<ByteArrayResource> generatePDFTenants() throws DocumentException, FileNotFoundException {
        try {
            // Generate PDF and get it as a ByteArrayOutputStream
            ByteArrayOutputStream pdfOutputStream = pdfGeneratorService.GlobalReportPdf();

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
}
