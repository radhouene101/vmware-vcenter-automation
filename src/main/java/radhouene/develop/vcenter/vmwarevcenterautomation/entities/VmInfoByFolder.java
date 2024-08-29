package radhouene.develop.vcenter.vmwarevcenterautomation.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class VmInfoByFolder {
    @Id
    private String VmId;
    private String folder_clientName;
    private String VmName;
    private String powerState;
    private String cpuCount;
    private String memorySizeMB;
    private String discSpaceGB;
    private String OSType;
    public String ips;
    public String tag_SO;
    public String tag_SO_Client;

}
