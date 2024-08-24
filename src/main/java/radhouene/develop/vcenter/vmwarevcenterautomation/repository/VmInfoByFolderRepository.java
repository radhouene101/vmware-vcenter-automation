package radhouene.develop.vcenter.vmwarevcenterautomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;

@Repository
public interface VmInfoByFolderRepository extends JpaRepository<VmInfoByFolder, Integer>{
}
