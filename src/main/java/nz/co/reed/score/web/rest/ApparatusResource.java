package nz.co.reed.score.web.rest;

import com.codahale.metrics.annotation.Timed;
import nz.co.reed.score.domain.Apparatus;
import nz.co.reed.score.repository.ApparatusRepository;
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
 * REST controller for managing Apparatus.
 */
@RestController
@RequestMapping("/api")
public class ApparatusResource {

    private final Logger log = LoggerFactory.getLogger(ApparatusResource.class);

    private static final String ENTITY_NAME = "apparatus";

    private final ApparatusRepository apparatusRepository;

    public ApparatusResource(ApparatusRepository apparatusRepository) {
        this.apparatusRepository = apparatusRepository;
    }

    /**
     * POST  /apparatuses : Create a new apparatus.
     *
     * @param apparatus the apparatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apparatus, or with status 400 (Bad Request) if the apparatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apparatuses")
    @Timed
    public ResponseEntity<Apparatus> createApparatus(@RequestBody Apparatus apparatus) throws URISyntaxException {
        log.debug("REST request to save Apparatus : {}", apparatus);
        if (apparatus.getId() != null) {
            throw new BadRequestAlertException("A new apparatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apparatus result = apparatusRepository.save(apparatus);
        return ResponseEntity.created(new URI("/api/apparatuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apparatuses : Updates an existing apparatus.
     *
     * @param apparatus the apparatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apparatus,
     * or with status 400 (Bad Request) if the apparatus is not valid,
     * or with status 500 (Internal Server Error) if the apparatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apparatuses")
    @Timed
    public ResponseEntity<Apparatus> updateApparatus(@RequestBody Apparatus apparatus) throws URISyntaxException {
        log.debug("REST request to update Apparatus : {}", apparatus);
        if (apparatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Apparatus result = apparatusRepository.save(apparatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apparatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apparatuses : get all the apparatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apparatuses in body
     */
    @GetMapping("/apparatuses")
    @Timed
    public List<Apparatus> getAllApparatuses() {
        log.debug("REST request to get all Apparatuses");
        return apparatusRepository.findAll();
    }

    /**
     * GET  /apparatuses/:id : get the "id" apparatus.
     *
     * @param id the id of the apparatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apparatus, or with status 404 (Not Found)
     */
    @GetMapping("/apparatuses/{id}")
    @Timed
    public ResponseEntity<Apparatus> getApparatus(@PathVariable Long id) {
        log.debug("REST request to get Apparatus : {}", id);
        Optional<Apparatus> apparatus = apparatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apparatus);
    }

    /**
     * DELETE  /apparatuses/:id : delete the "id" apparatus.
     *
     * @param id the id of the apparatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apparatuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteApparatus(@PathVariable Long id) {
        log.debug("REST request to delete Apparatus : {}", id);

        apparatusRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
