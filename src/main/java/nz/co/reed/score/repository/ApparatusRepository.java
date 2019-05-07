package nz.co.reed.score.repository;

import nz.co.reed.score.domain.Apparatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Apparatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApparatusRepository extends JpaRepository<Apparatus, Long> {

}
