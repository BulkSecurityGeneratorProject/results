package nz.co.reed.score.repository;

import nz.co.reed.score.domain.CompSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompSessionRepository extends JpaRepository<CompSession, Long> {

}
