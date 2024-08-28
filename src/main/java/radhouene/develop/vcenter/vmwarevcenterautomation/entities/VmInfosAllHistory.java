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
    private String discSpaceGB;
    private String OSType;
    public String ips;

    public VmInfosAllHistory(VmInfoByFolder vmInfoByFolder) {
        this.VmId = vmInfoByFolder.getVmId();
        this.folder_clientName = vmInfoByFolder.getFolder_clientName();
        this.VmName = vmInfoByFolder.getVmName();
        this.powerState = vmInfoByFolder.getPowerState();
        this.cpuCount = vmInfoByFolder.getCpuCount();
        this.memorySizeMB = vmInfoByFolder.getMemorySizeMB();
        this.discSpaceGB = vmInfoByFolder.getDiscSpaceGB();
        this.OSType = vmInfoByFolder.getOSType();
        this.ips = vmInfoByFolder.getIps();
    }
}
