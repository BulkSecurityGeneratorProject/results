package nz.co.reed.score.web.rest;

import com.codahale.metrics.annotation.Timed;
import nz.co.reed.score.domain.CompSession;
import nz.co.reed.score.repository.CompSessionRepository;
import nz.co.reed.score.web.rest.errors.BadRequestAlertException;
import nz.co.reed.score.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CompSession.
 */
@RestController
@RequestMapping("/api")
public class CompSessionResource {

    private final Logger log = LoggerFactory.getLogger(CompSessionResource.class);

    private static final String ENTITY_NAME = "compSession";

    private final CompSessionRepository compSessionRepository;

    public CompSessionResource(CompSessionRepository compSessionRepository) {
        this.compSessionRepository = compSessionRepository;
    }

    /**
     * POST  /comp-sessions : Create a new compSession.
     *
     * @param compSession the compSession to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compSession, or with status 400 (Bad Request) if the compSession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comp-sessions")
    @Timed
    public ResponseEntity<CompSession> createCompSession(@RequestBody CompSession compSession) throws URISyntaxException {
        log.debug("REST request to save CompSession : {}", compSession);
        if (compSession.getId() != null) {
            throw new BadRequestAlertException("A new compSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompSession result = compSessionRepository.save(compSession);
        return ResponseEntity.created(new URI("/api/comp-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comp-sessions : Updates an existing compSession.
     *
     * @param compSession the compSession to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compSession,
     * or with status 400 (Bad Request) if the compSession is not valid,
     * or with status 500 (Internal Server Error) if the compSession couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comp-sessions")
    @Timed
    public ResponseEntity<CompSession> updateCompSession(@RequestBody CompSession compSession) throws URISyntaxException {
        log.debug("REST request to update CompSession : {}", compSession);
        if (compSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompSession result = compSessionRepository.save(compSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compSession.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comp-sessions : get all the compSessions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compSessions in body
     */
    @GetMapping("/comp-sessions")
    @Timed
    public List<CompSession> getAllCompSessions() {
        log.debug("REST request to get all CompSessions");
        return compSessionRepository.findAll();
    }

    /**
     * GET  /comp-sessions/:id : get the "id" compSession.
     *
     * @param id the id of the compSession to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compSession, or with status 404 (Not Found)
     */
    @GetMapping("/comp-sessions/{id}")
    @Timed
    public ResponseEntity<CompSession> getCompSession(@PathVariable Long id) {
        log.debug("REST request to get CompSession : {}", id);
        Optional<CompSession> compSession = compSessionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(compSession);
    }

    /**
     * DELETE  /comp-sessions/:id : delete the "id" compSession.
     *
     * @param id the id of the compSession to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comp-sessions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompSession(@PathVariable Long id) {
        log.debug("REST request to delete CompSession : {}", id);

        compSessionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
