package nz.co.reed.score.web.rest;

import com.codahale.metrics.annotation.Timed;
import nz.co.reed.score.domain.Score;
import nz.co.reed.score.repository.ScoreRepository;
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
 * REST controller for managing Score.
 */
@RestController
@RequestMapping("/api")
public class ScoreResource {

    private final Logger log = LoggerFactory.getLogger(ScoreResource.class);

    private static final String ENTITY_NAME = "score";

    private final ScoreRepository scoreRepository;

    public ScoreResource(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * POST  /scores : Create a new score.
     *
     * @param score the score to create
     * @return the ResponseEntity with status 201 (Created) and with body the new score, or with status 400 (Bad Request) if the score has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scores")
    @Timed
    public ResponseEntity<Score> createScore(@RequestBody Score score) throws URISyntaxException {
        log.debug("REST request to save Score : {}", score);
        if (score.getId() != null) {
            throw new BadRequestAlertException("A new score cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Score result = scoreRepository.save(score);
        return ResponseEntity.created(new URI("/api/scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scores : Updates an existing score.
     *
     * @param score the score to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated score,
     * or with status 400 (Bad Request) if the score is not valid,
     * or with status 500 (Internal Server Error) if the score couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scores")
    @Timed
    public ResponseEntity<Score> updateScore(@RequestBody Score score) throws URISyntaxException {
        log.debug("REST request to update Score : {}", score);
        if (score.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Score result = scoreRepository.save(score);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, score.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scores : get all the scores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scores in body
     */
    @GetMapping("/scores")
    @Timed
    public ResponseEntity<List<Score>> getAllScores(Pageable pageable) {
        log.debug("REST request to get a page of Scores");
        Page<Score> page = scoreRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scores");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /scores/:id : get the "id" score.
     *
     * @param id the id of the score to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the score, or with status 404 (Not Found)
     */
    @GetMapping("/scores/{id}")
    @Timed
    public ResponseEntity<Score> getScore(@PathVariable Long id) {
        log.debug("REST request to get Score : {}", id);
        Optional<Score> score = scoreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(score);
    }

    /**
     * DELETE  /scores/:id : delete the "id" score.
     *
     * @param id the id of the score to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scores/{id}")
    @Timed
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        log.debug("REST request to delete Score : {}", id);

        scoreRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
