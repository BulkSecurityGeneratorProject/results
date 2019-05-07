package nz.co.reed.score.web.rest;

import nz.co.reed.score.ResultsjhApp;

import nz.co.reed.score.domain.Apparatus;
import nz.co.reed.score.repository.ApparatusRepository;
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
 * Test class for the ApparatusResource REST controller.
 *
 * @see ApparatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResultsjhApp.class)
public class ApparatusResourceIntTest {

    private static final String DEFAULT_APPARATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPARATUS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPETITION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMPETITION_TYPE = "BBBBBBBBBB";

    @Autowired
    private ApparatusRepository apparatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApparatusMockMvc;

    private Apparatus apparatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApparatusResource apparatusResource = new ApparatusResource(apparatusRepository);
        this.restApparatusMockMvc = MockMvcBuilders.standaloneSetup(apparatusResource)
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
    public static Apparatus createEntity(EntityManager em) {
        Apparatus apparatus = new Apparatus()
            .apparatusName(DEFAULT_APPARATUS_NAME)
            .competitionType(DEFAULT_COMPETITION_TYPE);
        return apparatus;
    }

    @Before
    public void initTest() {
        apparatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createApparatus() throws Exception {
        int databaseSizeBeforeCreate = apparatusRepository.findAll().size();

        // Create the Apparatus
        restApparatusMockMvc.perform(post("/api/apparatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apparatus)))
            .andExpect(status().isCreated());

        // Validate the Apparatus in the database
        List<Apparatus> apparatusList = apparatusRepository.findAll();
        assertThat(apparatusList).hasSize(databaseSizeBeforeCreate + 1);
        Apparatus testApparatus = apparatusList.get(apparatusList.size() - 1);
        assertThat(testApparatus.getApparatusName()).isEqualTo(DEFAULT_APPARATUS_NAME);
        assertThat(testApparatus.getCompetitionType()).isEqualTo(DEFAULT_COMPETITION_TYPE);
    }

    @Test
    @Transactional
    public void createApparatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apparatusRepository.findAll().size();

        // Create the Apparatus with an existing ID
        apparatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApparatusMockMvc.perform(post("/api/apparatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apparatus)))
            .andExpect(status().isBadRequest());

        // Validate the Apparatus in the database
        List<Apparatus> apparatusList = apparatusRepository.findAll();
        assertThat(apparatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApparatuses() throws Exception {
        // Initialize the database
        apparatusRepository.saveAndFlush(apparatus);

        // Get all the apparatusList
        restApparatusMockMvc.perform(get("/api/apparatuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apparatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].apparatusName").value(hasItem(DEFAULT_APPARATUS_NAME.toString())))
            .andExpect(jsonPath("$.[*].competitionType").value(hasItem(DEFAULT_COMPETITION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getApparatus() throws Exception {
        // Initialize the database
        apparatusRepository.saveAndFlush(apparatus);

        // Get the apparatus
        restApparatusMockMvc.perform(get("/api/apparatuses/{id}", apparatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apparatus.getId().intValue()))
            .andExpect(jsonPath("$.apparatusName").value(DEFAULT_APPARATUS_NAME.toString()))
            .andExpect(jsonPath("$.competitionType").value(DEFAULT_COMPETITION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApparatus() throws Exception {
        // Get the apparatus
        restApparatusMockMvc.perform(get("/api/apparatuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApparatus() throws Exception {
        // Initialize the database
        apparatusRepository.saveAndFlush(apparatus);

        int databaseSizeBeforeUpdate = apparatusRepository.findAll().size();

        // Update the apparatus
        Apparatus updatedApparatus = apparatusRepository.findById(apparatus.getId()).get();
        // Disconnect from session so that the updates on updatedApparatus are not directly saved in db
        em.detach(updatedApparatus);
        updatedApparatus
            .apparatusName(UPDATED_APPARATUS_NAME)
            .competitionType(UPDATED_COMPETITION_TYPE);

        restApparatusMockMvc.perform(put("/api/apparatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApparatus)))
            .andExpect(status().isOk());

        // Validate the Apparatus in the database
        List<Apparatus> apparatusList = apparatusRepository.findAll();
        assertThat(apparatusList).hasSize(databaseSizeBeforeUpdate);
        Apparatus testApparatus = apparatusList.get(apparatusList.size() - 1);
        assertThat(testApparatus.getApparatusName()).isEqualTo(UPDATED_APPARATUS_NAME);
        assertThat(testApparatus.getCompetitionType()).isEqualTo(UPDATED_COMPETITION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingApparatus() throws Exception {
        int databaseSizeBeforeUpdate = apparatusRepository.findAll().size();

        // Create the Apparatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApparatusMockMvc.perform(put("/api/apparatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apparatus)))
            .andExpect(status().isBadRequest());

        // Validate the Apparatus in the database
        List<Apparatus> apparatusList = apparatusRepository.findAll();
        assertThat(apparatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApparatus() throws Exception {
        // Initialize the database
        apparatusRepository.saveAndFlush(apparatus);

        int databaseSizeBeforeDelete = apparatusRepository.findAll().size();

        // Get the apparatus
        restApparatusMockMvc.perform(delete("/api/apparatuses/{id}", apparatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Apparatus> apparatusList = apparatusRepository.findAll();
        assertThat(apparatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apparatus.class);
        Apparatus apparatus1 = new Apparatus();
        apparatus1.setId(1L);
        Apparatus apparatus2 = new Apparatus();
        apparatus2.setId(apparatus1.getId());
        assertThat(apparatus1).isEqualTo(apparatus2);
        apparatus2.setId(2L);
        assertThat(apparatus1).isNotEqualTo(apparatus2);
        apparatus1.setId(null);
        assertThat(apparatus1).isNotEqualTo(apparatus2);
    }
}
