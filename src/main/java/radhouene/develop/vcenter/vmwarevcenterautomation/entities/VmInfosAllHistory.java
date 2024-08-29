package radhouene.develop.vcenter.vmwarevcenterautomation.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class VmInfosAllHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String VmId;
    private String folder_clientName;
    private String VmName;
    private String powerState;
    private String cpuCount;
    private String memorySizeMB;
    private String UseddiscSpaceGB;
    private String ReserveDdiscSpaceGB;
    private String OSType;
    public String ips;
    public String tag_SO;
    public String tag_SO_Client;
    private String discType;

    public VmInfosAllHistory(VmInfoByFolder vmInfoByFolder) {
        this.VmId = vmInfoByFolder.getVmId();
        this.folder_clientName = vmInfoByFolder.getFolder_clientName();
        this.VmName = vmInfoByFolder.getVmName();
        this.powerState = vmInfoByFolder.getPowerState();
        this.cpuCount = vmInfoByFolder.getCpuCount();
        this.memorySizeMB = vmInfoByFolder.getMemorySizeMB();
        this.OSType = vmInfoByFolder.getOSType();
        this.ips = vmInfoByFolder.getIps();
        this.discType = vmInfoByFolder.getDiscType();
        this.UseddiscSpaceGB = vmInfoByFolder.getUseddiscSpaceGB();
        this.ReserveDdiscSpaceGB = vmInfoByFolder.getReserveDdiscSpaceGB();
        this.tag_SO = vmInfoByFolder.getTag_SO();
        this.tag_SO_Client = vmInfoByFolder.getTag_SO_Client();
    }
}
