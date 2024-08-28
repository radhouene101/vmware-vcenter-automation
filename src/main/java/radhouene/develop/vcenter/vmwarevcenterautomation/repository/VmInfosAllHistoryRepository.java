package radhouene.develop.vcenter.vmwarevcenterautomation.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radhouene.develop.vcenter.vmwarevcenterautomation.entities.VmInfosAllHistory;

@Repository
public interface VmInfosAllHistoryRepository extends JpaRepository<VmInfosAllHistory, Integer> {
}
