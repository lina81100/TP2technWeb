package comptoirs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import comptoirs.entity.Ligne;

public interface LigneRepository extends JpaRepository<Ligne, Integer> {
    
}
