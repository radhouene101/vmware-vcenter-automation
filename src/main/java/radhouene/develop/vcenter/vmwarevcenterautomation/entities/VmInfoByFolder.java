package radhouene.develop.vcenter.vmwarevcenterautomation.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class VmInfoByFolder {
    @Id
    private Long Vmid;
    private String folder_clientName;
    private String VmName;
    private String powerState;
    private String cpuCount;
    private String memorySizeMB;
    private String discSpaceGB;
    private String OSType;
    @ElementCollection // otherwise we try @ElementCollection
    public Set<String> ips;

}
