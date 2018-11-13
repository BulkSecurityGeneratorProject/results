package nz.co.reed.score.web.rest;

import com.codahale.metrics.annotation.Timed;
import nz.co.reed.score.domain.Athlete;
import nz.co.reed.score.repository.AthleteRepository;
import nz.co.reed.score.web.rest.errors.BadRequestAlertException;
import nz.co.reed.score.web.rest.util.HeaderUtil;
import nz.co.reed.score.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Athlete.
 */
@RestController
@RequestMapping("/api")
public class AthleteResource {

    private final Logger log = LoggerFactory.getLogger(AthleteResource.class);

    private static final String ENTITY_NAME = "athlete";

    private AthleteRepository athleteRepository;

    public AthleteResource(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    /**
     * POST  /athletes : Create a new athlete.
     *
     * @param athlete the athlete to create
     * @return the ResponseEntity with status 201 (Created) and with body the new athlete, or with status 400 (Bad Request) if the athlete has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/athletes")
    @Timed
    public ResponseEntity<Athlete> createAthlete(@RequestBody Athlete athlete) throws URISyntaxException {
        log.debug("REST request to save Athlete : {}", athlete);
        if (athlete.getId() != null) {
            throw new BadRequestAlertException("A new athlete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Athlete result = athleteRepository.save(athlete);
        return ResponseEntity.created(new URI("/api/athletes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /athletes : Updates an existing athlete.
     *
     * @param athlete the athlete to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated athlete,
     * or with status 400 (Bad Request) if the athlete is not valid,
     * or with status 500 (Internal Server Error) if the athlete couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/athletes")
    @Timed
    public ResponseEntity<Athlete> updateAthlete(@RequestBody Athlete athlete) throws URISyntaxException {
        log.debug("REST request to update Athlete : {}", athlete);
        if (athlete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Athlete result = athleteRepository.save(athlete);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, athlete.getId().toString()))
            .body(result);
    }

    /**
     * GET  /athletes : get all the athletes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of athletes in body
     */
    @GetMapping("/athletes")
    @Timed
    public ResponseEntity<List<Athlete>> getAllAthletes(Pageable pageable) {
        log.debug("REST request to get a page of Athletes");
        Page<Athlete> page = athleteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/athletes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /athletes/:id : get the "id" athlete.
     *
     * @param id the id of the athlete to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the athlete, or with status 404 (Not Found)
     */
    @GetMapping("/athletes/{id}")
    @Timed
    public ResponseEntity<Athlete> getAthlete(@PathVariable Long id) {
        log.debug("REST request to get Athlete : {}", id);
        Optional<Athlete> athlete = athleteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(athlete);
    }

    /**
     * DELETE  /athletes/:id : delete the "id" athlete.
     *
     * @param id the id of the athlete to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/athletes/{id}")
    @Timed
    public ResponseEntity<Void> deleteAthlete(@PathVariable Long id) {
        log.debug("REST request to delete Athlete : {}", id);

        athleteRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
