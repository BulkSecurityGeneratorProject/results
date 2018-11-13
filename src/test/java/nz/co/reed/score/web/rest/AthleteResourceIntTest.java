package nz.co.reed.score.web.rest;

import nz.co.reed.score.ResultsjhApp;

import nz.co.reed.score.domain.Athlete;
import nz.co.reed.score.repository.AthleteRepository;
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
import java.util.List;


import static nz.co.reed.score.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AthleteResource REST controller.
 *
 * @see AthleteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResultsjhApp.class)
public class AthleteResourceIntTest {

    private static final String DEFAULT_ATHLETE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATHLETE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAthleteMockMvc;

    private Athlete athlete;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AthleteResource athleteResource = new AthleteResource(athleteRepository);
        this.restAthleteMockMvc = MockMvcBuilders.standaloneSetup(athleteResource)
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
    public static Athlete createEntity(EntityManager em) {
        Athlete athlete = new Athlete()
            .athleteName(DEFAULT_ATHLETE_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER);
        return athlete;
    }

    @Before
    public void initTest() {
        athlete = createEntity(em);
    }

    @Test
    @Transactional
    public void createAthlete() throws Exception {
        int databaseSizeBeforeCreate = athleteRepository.findAll().size();

        // Create the Athlete
        restAthleteMockMvc.perform(post("/api/athletes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isCreated());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeCreate + 1);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getAthleteName()).isEqualTo(DEFAULT_ATHLETE_NAME);
        assertThat(testAthlete.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    public void createAthleteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = athleteRepository.findAll().size();

        // Create the Athlete with an existing ID
        athlete.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAthleteMockMvc.perform(post("/api/athletes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAthletes() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        // Get all the athleteList
        restAthleteMockMvc.perform(get("/api/athletes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(athlete.getId().intValue())))
            .andExpect(jsonPath("$.[*].athleteName").value(hasItem(DEFAULT_ATHLETE_NAME.toString())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        // Get the athlete
        restAthleteMockMvc.perform(get("/api/athletes/{id}", athlete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(athlete.getId().intValue()))
            .andExpect(jsonPath("$.athleteName").value(DEFAULT_ATHLETE_NAME.toString()))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAthlete() throws Exception {
        // Get the athlete
        restAthleteMockMvc.perform(get("/api/athletes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();

        // Update the athlete
        Athlete updatedAthlete = athleteRepository.findById(athlete.getId()).get();
        // Disconnect from session so that the updates on updatedAthlete are not directly saved in db
        em.detach(updatedAthlete);
        updatedAthlete
            .athleteName(UPDATED_ATHLETE_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);

        restAthleteMockMvc.perform(put("/api/athletes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAthlete)))
            .andExpect(status().isOk());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
        Athlete testAthlete = athleteList.get(athleteList.size() - 1);
        assertThat(testAthlete.getAthleteName()).isEqualTo(UPDATED_ATHLETE_NAME);
        assertThat(testAthlete.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingAthlete() throws Exception {
        int databaseSizeBeforeUpdate = athleteRepository.findAll().size();

        // Create the Athlete

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAthleteMockMvc.perform(put("/api/athletes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(athlete)))
            .andExpect(status().isBadRequest());

        // Validate the Athlete in the database
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAthlete() throws Exception {
        // Initialize the database
        athleteRepository.saveAndFlush(athlete);

        int databaseSizeBeforeDelete = athleteRepository.findAll().size();

        // Get the athlete
        restAthleteMockMvc.perform(delete("/api/athletes/{id}", athlete.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Athlete> athleteList = athleteRepository.findAll();
        assertThat(athleteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Athlete.class);
        Athlete athlete1 = new Athlete();
        athlete1.setId(1L);
        Athlete athlete2 = new Athlete();
        athlete2.setId(athlete1.getId());
        assertThat(athlete1).isEqualTo(athlete2);
        athlete2.setId(2L);
        assertThat(athlete1).isNotEqualTo(athlete2);
        athlete1.setId(null);
        assertThat(athlete1).isNotEqualTo(athlete2);
    }
}
