package radhouene.develop.vcenter.vmwarevcenterautomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfoByFolder;

import java.util.List;

@Repository
public interface VmInfoByFolderRepository extends JpaRepository<VmInfoByFolder, String>{
    @Query("SELECT v FROM VmInfoByFolder v WHERE v.tag_SO = :tag")
    List<VmInfoByFolder> findByTag_SO(String tag);
}
