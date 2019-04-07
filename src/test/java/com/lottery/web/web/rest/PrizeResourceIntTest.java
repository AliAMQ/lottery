package com.lottery.web.web.rest;

import com.lottery.web.LotteryApp;

import com.lottery.web.domain.Prize;
import com.lottery.web.domain.Category;
import com.lottery.web.domain.Lottery;
import com.lottery.web.domain.History;
import com.lottery.web.repository.PrizeRepository;
import com.lottery.web.repository.search.PrizeSearchRepository;
import com.lottery.web.service.PrizeService;
import com.lottery.web.service.dto.PrizeDTO;
import com.lottery.web.service.mapper.PrizeMapper;
import com.lottery.web.web.rest.errors.ExceptionTranslator;
import com.lottery.web.service.dto.PrizeCriteria;
import com.lottery.web.service.PrizeQueryService;

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
 * Test class for the PrizeResource REST controller.
 *
 * @see PrizeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApp.class)
public class PrizeResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    @Autowired
    private PrizeRepository prizeRepository;


    @Autowired
    private PrizeMapper prizeMapper;
    

    @Autowired
    private PrizeService prizeService;

    /**
     * This repository is mocked in the com.lottery.web.repository.search test package.
     *
     * @see com.lottery.web.repository.search.PrizeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrizeSearchRepository mockPrizeSearchRepository;

    @Autowired
    private PrizeQueryService prizeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrizeMockMvc;

    private Prize prize;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrizeResource prizeResource = new PrizeResource(prizeService, prizeQueryService);
        this.restPrizeMockMvc = MockMvcBuilders.standaloneSetup(prizeResource)
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
    public static Prize createEntity(EntityManager em) {
        Prize prize = new Prize()
            .title(DEFAULT_TITLE)
            .value(DEFAULT_VALUE);
        return prize;
    }

    @Before
    public void initTest() {
        prize = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrize() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // Create the Prize
        PrizeDTO prizeDTO = prizeMapper.toDto(prize);
        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isCreated());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate + 1);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPrize.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Prize in Elasticsearch
        verify(mockPrizeSearchRepository, times(1)).save(testPrize);
    }

    @Test
    @Transactional
    public void createPrizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // Create the Prize with an existing ID
        prize.setId(1L);
        PrizeDTO prizeDTO = prizeMapper.toDto(prize);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prize in Elasticsearch
        verify(mockPrizeSearchRepository, times(0)).save(prize);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = prizeRepository.findAll().size();
        // set the field null
        prize.setTitle(null);

        // Create the Prize, which fails.
        PrizeDTO prizeDTO = prizeMapper.toDto(prize);

        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isBadRequest());

        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrizes() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    

    @Test
    @Transactional
    public void getPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", prize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prize.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getAllPrizesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where title equals to DEFAULT_TITLE
        defaultPrizeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the prizeList where title equals to UPDATED_TITLE
        defaultPrizeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPrizesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPrizeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the prizeList where title equals to UPDATED_TITLE
        defaultPrizeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPrizesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where title is not null
        defaultPrizeShouldBeFound("title.specified=true");

        // Get all the prizeList where title is null
        defaultPrizeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrizesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where value equals to DEFAULT_VALUE
        defaultPrizeShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the prizeList where value equals to UPDATED_VALUE
        defaultPrizeShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrizesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPrizeShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the prizeList where value equals to UPDATED_VALUE
        defaultPrizeShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrizesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where value is not null
        defaultPrizeShouldBeFound("value.specified=true");

        // Get all the prizeList where value is null
        defaultPrizeShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrizesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where value greater than or equals to DEFAULT_VALUE
        defaultPrizeShouldBeFound("value.greaterOrEqualThan=" + DEFAULT_VALUE);

        // Get all the prizeList where value greater than or equals to UPDATED_VALUE
        defaultPrizeShouldNotBeFound("value.greaterOrEqualThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrizesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList where value less than or equals to DEFAULT_VALUE
        defaultPrizeShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the prizeList where value less than or equals to UPDATED_VALUE
        defaultPrizeShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllPrizesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        prize.setCategory(category);
        prizeRepository.saveAndFlush(prize);
        Long categoryId = category.getId();

        // Get all the prizeList where category equals to categoryId
        defaultPrizeShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the prizeList where category equals to categoryId + 1
        defaultPrizeShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllPrizesByLotteryIsEqualToSomething() throws Exception {
        // Initialize the database
        Lottery lottery = LotteryResourceIntTest.createEntity(em);
        em.persist(lottery);
        em.flush();
        prize.setLottery(lottery);
        prizeRepository.saveAndFlush(prize);
        Long lotteryId = lottery.getId();

        // Get all the prizeList where lottery equals to lotteryId
        defaultPrizeShouldBeFound("lotteryId.equals=" + lotteryId);

        // Get all the prizeList where lottery equals to lotteryId + 1
        defaultPrizeShouldNotBeFound("lotteryId.equals=" + (lotteryId + 1));
    }


    @Test
    @Transactional
    public void getAllPrizesByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        History history = HistoryResourceIntTest.createEntity(em);
        em.persist(history);
        em.flush();
        prize.setHistory(history);
        prizeRepository.saveAndFlush(prize);
        Long historyId = history.getId();

        // Get all the prizeList where history equals to historyId
        defaultPrizeShouldBeFound("historyId.equals=" + historyId);

        // Get all the prizeList where history equals to historyId + 1
        defaultPrizeShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPrizeShouldBeFound(String filter) throws Exception {
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPrizeShouldNotBeFound(String filter) throws Exception {
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingPrize() throws Exception {
        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize
        Prize updatedPrize = prizeRepository.findById(prize.getId()).get();
        // Disconnect from session so that the updates on updatedPrize are not directly saved in db
        em.detach(updatedPrize);
        updatedPrize
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE);
        PrizeDTO prizeDTO = prizeMapper.toDto(updatedPrize);

        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPrize.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Prize in Elasticsearch
        verify(mockPrizeSearchRepository, times(1)).save(testPrize);
    }

    @Test
    @Transactional
    public void updateNonExistingPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Create the Prize
        PrizeDTO prizeDTO = prizeMapper.toDto(prize);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prize in Elasticsearch
        verify(mockPrizeSearchRepository, times(0)).save(prize);
    }

    @Test
    @Transactional
    public void deletePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        int databaseSizeBeforeDelete = prizeRepository.findAll().size();

        // Get the prize
        restPrizeMockMvc.perform(delete("/api/prizes/{id}", prize.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prize in Elasticsearch
        verify(mockPrizeSearchRepository, times(1)).deleteById(prize.getId());
    }

    @Test
    @Transactional
    public void searchPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);
        when(mockPrizeSearchRepository.search(queryStringQuery("id:" + prize.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prize), PageRequest.of(0, 1), 1));
        // Search the prize
        restPrizeMockMvc.perform(get("/api/_search/prizes?query=id:" + prize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prize.class);
        Prize prize1 = new Prize();
        prize1.setId(1L);
        Prize prize2 = new Prize();
        prize2.setId(prize1.getId());
        assertThat(prize1).isEqualTo(prize2);
        prize2.setId(2L);
        assertThat(prize1).isNotEqualTo(prize2);
        prize1.setId(null);
        assertThat(prize1).isNotEqualTo(prize2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrizeDTO.class);
        PrizeDTO prizeDTO1 = new PrizeDTO();
        prizeDTO1.setId(1L);
        PrizeDTO prizeDTO2 = new PrizeDTO();
        assertThat(prizeDTO1).isNotEqualTo(prizeDTO2);
        prizeDTO2.setId(prizeDTO1.getId());
        assertThat(prizeDTO1).isEqualTo(prizeDTO2);
        prizeDTO2.setId(2L);
        assertThat(prizeDTO1).isNotEqualTo(prizeDTO2);
        prizeDTO1.setId(null);
        assertThat(prizeDTO1).isNotEqualTo(prizeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(prizeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(prizeMapper.fromId(null)).isNull();
    }
}
