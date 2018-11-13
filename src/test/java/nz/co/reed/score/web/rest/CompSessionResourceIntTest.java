package nz.co.reed.score.web.rest;

import nz.co.reed.score.ResultsjhApp;

import nz.co.reed.score.domain.CompSession;
import nz.co.reed.score.repository.CompSessionRepository;
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
 * Test class for the CompSessionResource REST controller.
 *
 * @see CompSessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResultsjhApp.class)
public class CompSessionResourceIntTest {

    private static final String DEFAULT_SESSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    @Autowired
    private CompSessionRepository compSessionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompSessionMockMvc;

    private CompSession compSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompSessionResource compSessionResource = new CompSessionResource(compSessionRepository);
        this.restCompSessionMockMvc = MockMvcBuilders.standaloneSetup(compSessionResource)
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
    public static CompSession createEntity(EntityManager em) {
        CompSession compSession = new CompSession()
            .sessionName(DEFAULT_SESSION_NAME)
            .level(DEFAULT_LEVEL);
        return compSession;
    }

    @Before
    public void initTest() {
        compSession = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompSession() throws Exception {
        int databaseSizeBeforeCreate = compSessionRepository.findAll().size();

        // Create the CompSession
        restCompSessionMockMvc.perform(post("/api/comp-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compSession)))
            .andExpect(status().isCreated());

        // Validate the CompSession in the database
        List<CompSession> compSessionList = compSessionRepository.findAll();
        assertThat(compSessionList).hasSize(databaseSizeBeforeCreate + 1);
        CompSession testCompSession = compSessionList.get(compSessionList.size() - 1);
        assertThat(testCompSession.getSessionName()).isEqualTo(DEFAULT_SESSION_NAME);
        assertThat(testCompSession.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createCompSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compSessionRepository.findAll().size();

        // Create the CompSession with an existing ID
        compSession.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompSessionMockMvc.perform(post("/api/comp-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compSession)))
            .andExpect(status().isBadRequest());

        // Validate the CompSession in the database
        List<CompSession> compSessionList = compSessionRepository.findAll();
        assertThat(compSessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompSessions() throws Exception {
        // Initialize the database
        compSessionRepository.saveAndFlush(compSession);

        // Get all the compSessionList
        restCompSessionMockMvc.perform(get("/api/comp-sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }
    
    @Test
    @Transactional
    public void getCompSession() throws Exception {
        // Initialize the database
        compSessionRepository.saveAndFlush(compSession);

        // Get the compSession
        restCompSessionMockMvc.perform(get("/api/comp-sessions/{id}", compSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compSession.getId().intValue()))
            .andExpect(jsonPath("$.sessionName").value(DEFAULT_SESSION_NAME.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompSession() throws Exception {
        // Get the compSession
        restCompSessionMockMvc.perform(get("/api/comp-sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompSession() throws Exception {
        // Initialize the database
        compSessionRepository.saveAndFlush(compSession);

        int databaseSizeBeforeUpdate = compSessionRepository.findAll().size();

        // Update the compSession
        CompSession updatedCompSession = compSessionRepository.findById(compSession.getId()).get();
        // Disconnect from session so that the updates on updatedCompSession are not directly saved in db
        em.detach(updatedCompSession);
        updatedCompSession
            .sessionName(UPDATED_SESSION_NAME)
            .level(UPDATED_LEVEL);

        restCompSessionMockMvc.perform(put("/api/comp-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompSession)))
            .andExpect(status().isOk());

        // Validate the CompSession in the database
        List<CompSession> compSessionList = compSessionRepository.findAll();
        assertThat(compSessionList).hasSize(databaseSizeBeforeUpdate);
        CompSession testCompSession = compSessionList.get(compSessionList.size() - 1);
        assertThat(testCompSession.getSessionName()).isEqualTo(UPDATED_SESSION_NAME);
        assertThat(testCompSession.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingCompSession() throws Exception {
        int databaseSizeBeforeUpdate = compSessionRepository.findAll().size();

        // Create the CompSession

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompSessionMockMvc.perform(put("/api/comp-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compSession)))
            .andExpect(status().isBadRequest());

        // Validate the CompSession in the database
        List<CompSession> compSessionList = compSessionRepository.findAll();
        assertThat(compSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompSession() throws Exception {
        // Initialize the database
        compSessionRepository.saveAndFlush(compSession);

        int databaseSizeBeforeDelete = compSessionRepository.findAll().size();

        // Get the compSession
        restCompSessionMockMvc.perform(delete("/api/comp-sessions/{id}", compSession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompSession> compSessionList = compSessionRepository.findAll();
        assertThat(compSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompSession.class);
        CompSession compSession1 = new CompSession();
        compSession1.setId(1L);
        CompSession compSession2 = new CompSession();
        compSession2.setId(compSession1.getId());
        assertThat(compSession1).isEqualTo(compSession2);
        compSession2.setId(2L);
        assertThat(compSession1).isNotEqualTo(compSession2);
        compSession1.setId(null);
        assertThat(compSession1).isNotEqualTo(compSession2);
    }
}
