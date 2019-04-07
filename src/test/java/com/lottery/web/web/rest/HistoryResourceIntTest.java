package com.lottery.web.web.rest;

import com.lottery.web.LotteryApp;

import com.lottery.web.domain.History;
import com.lottery.web.domain.Lottery;
import com.lottery.web.domain.Prize;
import com.lottery.web.repository.HistoryRepository;
import com.lottery.web.repository.search.HistorySearchRepository;
import com.lottery.web.service.HistoryService;
import com.lottery.web.service.dto.HistoryDTO;
import com.lottery.web.service.mapper.HistoryMapper;
import com.lottery.web.web.rest.errors.ExceptionTranslator;
import com.lottery.web.service.dto.HistoryCriteria;
import com.lottery.web.service.HistoryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.lottery.web.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApp.class)
public class HistoryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HistoryRepository historyRepository;


    @Autowired
    private HistoryMapper historyMapper;
    

    @Autowired
    private HistoryService historyService;

    /**
     * This repository is mocked in the com.lottery.web.repository.search test package.
     *
     * @see com.lottery.web.repository.search.HistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private HistorySearchRepository mockHistorySearchRepository;

    @Autowired
    private HistoryQueryService historyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoryMockMvc;

    private History history;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoryResource historyResource = new HistoryResource(historyService, historyQueryService);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
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
    public static History createEntity(EntityManager em) {
        History history = new History()
            .date(DEFAULT_DATE);
        return history;
    }

    @Before
    public void initTest() {
        history = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).save(testHistory);
    }

    @Test
    @Transactional
    public void createHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History with an existing ID
        history.setId(1L);
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(0)).save(history);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyRepository.findAll().size();
        // set the field null
        history.setDate(null);

        // Create the History, which fails.
        HistoryDTO historyDTO = historyMapper.toDto(history);

        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistories() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllHistoriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList where date equals to DEFAULT_DATE
        defaultHistoryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the historyList where date equals to UPDATED_DATE
        defaultHistoryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllHistoriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList where date in DEFAULT_DATE or UPDATED_DATE
        defaultHistoryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the historyList where date equals to UPDATED_DATE
        defaultHistoryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllHistoriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList where date is not null
        defaultHistoryShouldBeFound("date.specified=true");

        // Get all the historyList where date is null
        defaultHistoryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllHistoriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList where date greater than or equals to DEFAULT_DATE
        defaultHistoryShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the historyList where date greater than or equals to UPDATED_DATE
        defaultHistoryShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllHistoriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList where date less than or equals to DEFAULT_DATE
        defaultHistoryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the historyList where date less than or equals to UPDATED_DATE
        defaultHistoryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllHistoriesByLotteryIsEqualToSomething() throws Exception {
        // Initialize the database
        Lottery lottery = LotteryResourceIntTest.createEntity(em);
        em.persist(lottery);
        em.flush();
        history.setLottery(lottery);
        historyRepository.saveAndFlush(history);
        Long lotteryId = lottery.getId();

        // Get all the historyList where lottery equals to lotteryId
        defaultHistoryShouldBeFound("lotteryId.equals=" + lotteryId);

        // Get all the historyList where lottery equals to lotteryId + 1
        defaultHistoryShouldNotBeFound("lotteryId.equals=" + (lotteryId + 1));
    }


    @Test
    @Transactional
    public void getAllHistoriesByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        Prize prize = PrizeResourceIntTest.createEntity(em);
        em.persist(prize);
        em.flush();
        history.addPrize(prize);
        historyRepository.saveAndFlush(history);
        Long prizeId = prize.getId();

        // Get all the historyList where prize equals to prizeId
        defaultHistoryShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the historyList where prize equals to prizeId + 1
        defaultHistoryShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHistoryShouldBeFound(String filter) throws Exception {
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHistoryShouldNotBeFound(String filter) throws Exception {
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        History updatedHistory = historyRepository.findById(history.getId()).get();
        // Disconnect from session so that the updates on updatedHistory are not directly saved in db
        em.detach(updatedHistory);
        updatedHistory
            .date(UPDATED_DATE);
        HistoryDTO historyDTO = historyMapper.toDto(updatedHistory);

        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).save(testHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(0)).save(history);
    }

    @Test
    @Transactional
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/histories/{id}", history.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).deleteById(history.getId());
    }

    @Test
    @Transactional
    public void searchHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);
        when(mockHistorySearchRepository.search(queryStringQuery("id:" + history.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(history), PageRequest.of(0, 1), 1));
        // Search the history
        restHistoryMockMvc.perform(get("/api/_search/histories?query=id:" + history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(History.class);
        History history1 = new History();
        history1.setId(1L);
        History history2 = new History();
        history2.setId(history1.getId());
        assertThat(history1).isEqualTo(history2);
        history2.setId(2L);
        assertThat(history1).isNotEqualTo(history2);
        history1.setId(null);
        assertThat(history1).isNotEqualTo(history2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryDTO.class);
        HistoryDTO historyDTO1 = new HistoryDTO();
        historyDTO1.setId(1L);
        HistoryDTO historyDTO2 = new HistoryDTO();
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO2.setId(historyDTO1.getId());
        assertThat(historyDTO1).isEqualTo(historyDTO2);
        historyDTO2.setId(2L);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO1.setId(null);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historyMapper.fromId(null)).isNull();
    }
}
