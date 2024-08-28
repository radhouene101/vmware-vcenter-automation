package radhouene.develop.vcenter.vmwarevcenterautomation.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;
import radhouene.develop.vcenter.vmwarevcenterautomation.repository.VmInfoByFolderRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
@Getter
@Setter
@AllArgsConstructor

public class PdfGeneratorService {
    @Autowired
    private  VmInfoByFolderRepository vmInfoByFolderRepository;


    public  ByteArrayOutputStream GlobalReportPdf() throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.setPageSize(new Rectangle(2000,800));
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 32);
        Paragraph header = new Paragraph("Global Report Vcenter", font);
        //header.add("Global Report Vcenter");
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20);

        PdfPTable table1 = new PdfPTable(1);
        addCustomImage(table1);
        document.add(table1);
        document.add(header);
        PdfPTable table = new PdfPTable(9);
        tableHeaderTenants(table);
        List<VmInfoByFolder> vmInfoByFolderList = vmInfoByFolderRepository.findAll();
        for(VmInfoByFolder vm : vmInfoByFolderList) {
            addRowTenants(table, vm);
        }


        //addRowTenants(table, "next Step", "Tenant1", "4", "OK");
        document.add(table);
        document.close();
        return outputStream;
    }
    public static void tableHeaderTenants(PdfPTable table) {
        Stream.of("Client name", "VM Name", "Power State", "Cpu", "Memory (RAM)","Used Space GB" ,"Guest_OS" ,"IPs","VM ID")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(0.2F);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    //    @Scheduled(fixedRate = 10000)
//    public void test() {
//        List<TenantAllLogs> tenantAllLogs = tenantAllLogsRepository.tenants();
//        for(TenantAllLogs tenant : tenantAllLogs) {
//            System.out.println(tenant.getName());
//        }
//        System.out.println(tenantAllLogsRepository.tenants().toString());
//    }
    // specific lel pdf stamalna el record eli snaneh louta
    public static void addRowTenants(PdfPTable table, VmInfoByFolder vm) {
        VmToDisplay vmToDisplay = new VmToDisplay(vm);
        table.addCell(vmToDisplay.getFolder_clientName());
        table.addCell(vmToDisplay.getVmName());
        table.addCell(vmToDisplay.getPowerState());
        table.addCell(vmToDisplay.getCpuCount());
        table.addCell(vmToDisplay.getMemorySizeMB());
        table.addCell(vmToDisplay.getDiscSpaceGB());
        table.addCell(vmToDisplay.getOSType());
        table.addCell(vmToDisplay.ips);
        table.addCell(vmToDisplay.getVmId());
    }
    public static void addCustomImage(PdfPTable table) throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("images/next_step-logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(50);
        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imageCell.setBorder(0);
        table.addCell(imageCell);

        PdfPCell descriptionCell = new PdfPCell(new Phrase("contact our support team for any info"));
        descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }



}



// we used this to behave as a record specific for el pdf
@Data
class VmToDisplay {
    private String VmId;
    private String folder_clientName;
    private String VmName;
    private String powerState;
    private String cpuCount;
    private String memorySizeMB;
    private String discSpaceGB;
    private String OSType;
    public String ips;

    public VmToDisplay(VmInfoByFolder vm){
        this.VmId = vm.getVmId();
        this.folder_clientName = vm.getFolder_clientName();
        this.VmName = vm.getVmName();
        this.powerState = vm.getPowerState();
        this.cpuCount = vm.getCpuCount();
        this.memorySizeMB = vm.getMemorySizeMB();
        this.discSpaceGB = vm.getDiscSpaceGB();
        this.OSType = vm.getOSType();
        this.ips = vm.getIps();
    }
}