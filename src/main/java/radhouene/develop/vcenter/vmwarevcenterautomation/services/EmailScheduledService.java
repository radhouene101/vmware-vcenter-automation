package radhouene.develop.vcenter.vmwarevcenterautomation.services;

import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@AllArgsConstructor
@Component
public class EmailScheduledService {
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final PdfGeneratorService pdfGeneratorService;

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 8,17 * * *" )
    public void sendDailyEmail() throws DocumentException, IOException, URISyntaxException, MessagingException {
        ByteArrayOutputStream pdfContent =pdfGeneratorService.GlobalReportPdf();
        emailService.sendPdfReport("fberrzig@gmail.com", "Global Report", "Please find the attached report.", pdfContent);
        System.out.println("===========================================mail sent========================");
        System.out.println("mail sent");
        System.out.println("===========================================mail sent=====================================");
    }

}
