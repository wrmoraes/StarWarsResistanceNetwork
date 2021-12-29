package resistance.star.wars.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resistance.star.wars.socialnetwork.model.entity.Rebel;

public interface RebelRepository extends JpaRepository<Rebel, Long> {
}
