package nz.co.reed.score.web.rest;

import nz.co.reed.score.ResultsjhApp;

import nz.co.reed.score.domain.Score;
import nz.co.reed.score.repository.ScoreRepository;
import nz.co.reed.score.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;


import static nz.co.reed.score.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScoreResource REST controller.
 *
 * @see ScoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResultsjhApp.class)
public class ScoreResourceIntTest {

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DIFFICULTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_DIFFICULTY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NEUTRAL_DEDUCTIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_NEUTRAL_DEDUCTIONS = new BigDecimal(2);

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScoreMockMvc;

    private Score score;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScoreResource scoreResource = new ScoreResource(scoreRepository);
        this.restScoreMockMvc = MockMvcBuilders.standaloneSetup(scoreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Score createEntity(EntityManager em) {
        Score score = new Score()
            .total(DEFAULT_TOTAL)
            .difficulty(DEFAULT_DIFFICULTY)
            .neutralDeductions(DEFAULT_NEUTRAL_DEDUCTIONS);
        return score;
    }

    @Before
    public void initTest() {
        score = createEntity(em);
    }

    @Test
    @Transactional
    public void createScore() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isCreated());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate + 1);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testScore.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testScore.getNeutralDeductions()).isEqualTo(DEFAULT_NEUTRAL_DEDUCTIONS);
    }

    @Test
    @Transactional
    public void createScoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score with an existing ID
        score.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllScores() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList
        restScoreMockMvc.perform(get("/api/scores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.intValue())))
            .andExpect(jsonPath("$.[*].neutralDeductions").value(hasItem(DEFAULT_NEUTRAL_DEDUCTIONS.intValue())));
    }
    
    @Test
    @Transactional
    public void getScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", score.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(score.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.intValue()))
            .andExpect(jsonPath("$.neutralDeductions").value(DEFAULT_NEUTRAL_DEDUCTIONS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingScore() throws Exception {
        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score
        Score updatedScore = scoreRepository.findById(score.getId()).get();
        // Disconnect from session so that the updates on updatedScore are not directly saved in db
        em.detach(updatedScore);
        updatedScore
            .total(UPDATED_TOTAL)
            .difficulty(UPDATED_DIFFICULTY)
            .neutralDeductions(UPDATED_NEUTRAL_DEDUCTIONS);

        restScoreMockMvc.perform(put("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScore)))
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testScore.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testScore.getNeutralDeductions()).isEqualTo(UPDATED_NEUTRAL_DEDUCTIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Create the Score

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreMockMvc.perform(put("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeDelete = scoreRepository.findAll().size();

        // Get the score
        restScoreMockMvc.perform(delete("/api/scores/{id}", score.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Score.class);
        Score score1 = new Score();
        score1.setId(1L);
        Score score2 = new Score();
        score2.setId(score1.getId());
        assertThat(score1).isEqualTo(score2);
        score2.setId(2L);
        assertThat(score1).isNotEqualTo(score2);
        score1.setId(null);
        assertThat(score1).isNotEqualTo(score2);
    }
}
