package com.lottery.web.web.rest;

import com.lottery.web.LotteryApp;

import com.lottery.web.domain.Lottery;
import com.lottery.web.domain.UserProfile;
import com.lottery.web.domain.Prize;
import com.lottery.web.domain.History;
import com.lottery.web.repository.LotteryRepository;
import com.lottery.web.repository.search.LotterySearchRepository;
import com.lottery.web.service.LotteryService;
import com.lottery.web.service.dto.LotteryDTO;
import com.lottery.web.service.mapper.LotteryMapper;
import com.lottery.web.web.rest.errors.ExceptionTranslator;
import com.lottery.web.service.dto.LotteryCriteria;
import com.lottery.web.service.LotteryQueryService;

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
 * Test class for the LotteryResource REST controller.
 *
 * @see LotteryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApp.class)
public class LotteryResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MINPARTICIPANTS = 1;
    private static final Integer UPDATED_MINPARTICIPANTS = 2;

    private static final Integer DEFAULT_MAXPARTICIPNTS = 1;
    private static final Integer UPDATED_MAXPARTICIPNTS = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    @Autowired
    private LotteryRepository lotteryRepository;


    @Autowired
    private LotteryMapper lotteryMapper;
    

    @Autowired
    private LotteryService lotteryService;

    /**
     * This repository is mocked in the com.lottery.web.repository.search test package.
     *
     * @see com.lottery.web.repository.search.LotterySearchRepositoryMockConfiguration
     */
    @Autowired
    private LotterySearchRepository mockLotterySearchRepository;

    @Autowired
    private LotteryQueryService lotteryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLotteryMockMvc;

    private Lottery lottery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LotteryResource lotteryResource = new LotteryResource(lotteryService, lotteryQueryService);
        this.restLotteryMockMvc = MockMvcBuilders.standaloneSetup(lotteryResource)
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
    public static Lottery createEntity(EntityManager em) {
        Lottery lottery = new Lottery()
            .title(DEFAULT_TITLE)
            .minparticipants(DEFAULT_MINPARTICIPANTS)
            .maxparticipnts(DEFAULT_MAXPARTICIPNTS)
            .price(DEFAULT_PRICE);
        return lottery;
    }

    @Before
    public void initTest() {
        lottery = createEntity(em);
    }

    @Test
    @Transactional
    public void createLottery() throws Exception {
        int databaseSizeBeforeCreate = lotteryRepository.findAll().size();

        // Create the Lottery
        LotteryDTO lotteryDTO = lotteryMapper.toDto(lottery);
        restLotteryMockMvc.perform(post("/api/lotteries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lotteryDTO)))
            .andExpect(status().isCreated());

        // Validate the Lottery in the database
        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeCreate + 1);
        Lottery testLottery = lotteryList.get(lotteryList.size() - 1);
        assertThat(testLottery.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLottery.getMinparticipants()).isEqualTo(DEFAULT_MINPARTICIPANTS);
        assertThat(testLottery.getMaxparticipnts()).isEqualTo(DEFAULT_MAXPARTICIPNTS);
        assertThat(testLottery.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Lottery in Elasticsearch
        verify(mockLotterySearchRepository, times(1)).save(testLottery);
    }

    @Test
    @Transactional
    public void createLotteryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lotteryRepository.findAll().size();

        // Create the Lottery with an existing ID
        lottery.setId(1L);
        LotteryDTO lotteryDTO = lotteryMapper.toDto(lottery);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotteryMockMvc.perform(post("/api/lotteries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lotteryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lottery in the database
        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lottery in Elasticsearch
        verify(mockLotterySearchRepository, times(0)).save(lottery);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotteryRepository.findAll().size();
        // set the field null
        lottery.setTitle(null);

        // Create the Lottery, which fails.
        LotteryDTO lotteryDTO = lotteryMapper.toDto(lottery);

        restLotteryMockMvc.perform(post("/api/lotteries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lotteryDTO)))
            .andExpect(status().isBadRequest());

        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLotteries() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList
        restLotteryMockMvc.perform(get("/api/lotteries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lottery.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].minparticipants").value(hasItem(DEFAULT_MINPARTICIPANTS)))
            .andExpect(jsonPath("$.[*].maxparticipnts").value(hasItem(DEFAULT_MAXPARTICIPNTS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }
    

    @Test
    @Transactional
    public void getLottery() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get the lottery
        restLotteryMockMvc.perform(get("/api/lotteries/{id}", lottery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lottery.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.minparticipants").value(DEFAULT_MINPARTICIPANTS))
            .andExpect(jsonPath("$.maxparticipnts").value(DEFAULT_MAXPARTICIPNTS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    public void getAllLotteriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where title equals to DEFAULT_TITLE
        defaultLotteryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the lotteryList where title equals to UPDATED_TITLE
        defaultLotteryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLotteriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultLotteryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the lotteryList where title equals to UPDATED_TITLE
        defaultLotteryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLotteriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where title is not null
        defaultLotteryShouldBeFound("title.specified=true");

        // Get all the lotteryList where title is null
        defaultLotteryShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotteriesByMinparticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where minparticipants equals to DEFAULT_MINPARTICIPANTS
        defaultLotteryShouldBeFound("minparticipants.equals=" + DEFAULT_MINPARTICIPANTS);

        // Get all the lotteryList where minparticipants equals to UPDATED_MINPARTICIPANTS
        defaultLotteryShouldNotBeFound("minparticipants.equals=" + UPDATED_MINPARTICIPANTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMinparticipantsIsInShouldWork() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where minparticipants in DEFAULT_MINPARTICIPANTS or UPDATED_MINPARTICIPANTS
        defaultLotteryShouldBeFound("minparticipants.in=" + DEFAULT_MINPARTICIPANTS + "," + UPDATED_MINPARTICIPANTS);

        // Get all the lotteryList where minparticipants equals to UPDATED_MINPARTICIPANTS
        defaultLotteryShouldNotBeFound("minparticipants.in=" + UPDATED_MINPARTICIPANTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMinparticipantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where minparticipants is not null
        defaultLotteryShouldBeFound("minparticipants.specified=true");

        // Get all the lotteryList where minparticipants is null
        defaultLotteryShouldNotBeFound("minparticipants.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotteriesByMinparticipantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where minparticipants greater than or equals to DEFAULT_MINPARTICIPANTS
        defaultLotteryShouldBeFound("minparticipants.greaterOrEqualThan=" + DEFAULT_MINPARTICIPANTS);

        // Get all the lotteryList where minparticipants greater than or equals to UPDATED_MINPARTICIPANTS
        defaultLotteryShouldNotBeFound("minparticipants.greaterOrEqualThan=" + UPDATED_MINPARTICIPANTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMinparticipantsIsLessThanSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where minparticipants less than or equals to DEFAULT_MINPARTICIPANTS
        defaultLotteryShouldNotBeFound("minparticipants.lessThan=" + DEFAULT_MINPARTICIPANTS);

        // Get all the lotteryList where minparticipants less than or equals to UPDATED_MINPARTICIPANTS
        defaultLotteryShouldBeFound("minparticipants.lessThan=" + UPDATED_MINPARTICIPANTS);
    }


    @Test
    @Transactional
    public void getAllLotteriesByMaxparticipntsIsEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where maxparticipnts equals to DEFAULT_MAXPARTICIPNTS
        defaultLotteryShouldBeFound("maxparticipnts.equals=" + DEFAULT_MAXPARTICIPNTS);

        // Get all the lotteryList where maxparticipnts equals to UPDATED_MAXPARTICIPNTS
        defaultLotteryShouldNotBeFound("maxparticipnts.equals=" + UPDATED_MAXPARTICIPNTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMaxparticipntsIsInShouldWork() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where maxparticipnts in DEFAULT_MAXPARTICIPNTS or UPDATED_MAXPARTICIPNTS
        defaultLotteryShouldBeFound("maxparticipnts.in=" + DEFAULT_MAXPARTICIPNTS + "," + UPDATED_MAXPARTICIPNTS);

        // Get all the lotteryList where maxparticipnts equals to UPDATED_MAXPARTICIPNTS
        defaultLotteryShouldNotBeFound("maxparticipnts.in=" + UPDATED_MAXPARTICIPNTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMaxparticipntsIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where maxparticipnts is not null
        defaultLotteryShouldBeFound("maxparticipnts.specified=true");

        // Get all the lotteryList where maxparticipnts is null
        defaultLotteryShouldNotBeFound("maxparticipnts.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotteriesByMaxparticipntsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where maxparticipnts greater than or equals to DEFAULT_MAXPARTICIPNTS
        defaultLotteryShouldBeFound("maxparticipnts.greaterOrEqualThan=" + DEFAULT_MAXPARTICIPNTS);

        // Get all the lotteryList where maxparticipnts greater than or equals to UPDATED_MAXPARTICIPNTS
        defaultLotteryShouldNotBeFound("maxparticipnts.greaterOrEqualThan=" + UPDATED_MAXPARTICIPNTS);
    }

    @Test
    @Transactional
    public void getAllLotteriesByMaxparticipntsIsLessThanSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where maxparticipnts less than or equals to DEFAULT_MAXPARTICIPNTS
        defaultLotteryShouldNotBeFound("maxparticipnts.lessThan=" + DEFAULT_MAXPARTICIPNTS);

        // Get all the lotteryList where maxparticipnts less than or equals to UPDATED_MAXPARTICIPNTS
        defaultLotteryShouldBeFound("maxparticipnts.lessThan=" + UPDATED_MAXPARTICIPNTS);
    }


    @Test
    @Transactional
    public void getAllLotteriesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where price equals to DEFAULT_PRICE
        defaultLotteryShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the lotteryList where price equals to UPDATED_PRICE
        defaultLotteryShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllLotteriesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultLotteryShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the lotteryList where price equals to UPDATED_PRICE
        defaultLotteryShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllLotteriesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where price is not null
        defaultLotteryShouldBeFound("price.specified=true");

        // Get all the lotteryList where price is null
        defaultLotteryShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllLotteriesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where price greater than or equals to DEFAULT_PRICE
        defaultLotteryShouldBeFound("price.greaterOrEqualThan=" + DEFAULT_PRICE);

        // Get all the lotteryList where price greater than or equals to UPDATED_PRICE
        defaultLotteryShouldNotBeFound("price.greaterOrEqualThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllLotteriesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        // Get all the lotteryList where price less than or equals to DEFAULT_PRICE
        defaultLotteryShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the lotteryList where price less than or equals to UPDATED_PRICE
        defaultLotteryShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }


    @Test
    @Transactional
    public void getAllLotteriesByUserProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        UserProfile userProfile = UserProfileResourceIntTest.createEntity(em);
        em.persist(userProfile);
        em.flush();
        lottery.setUserProfile(userProfile);
        lotteryRepository.saveAndFlush(lottery);
        Long userProfileId = userProfile.getId();

        // Get all the lotteryList where userProfile equals to userProfileId
        defaultLotteryShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the lotteryList where userProfile equals to userProfileId + 1
        defaultLotteryShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }


    @Test
    @Transactional
    public void getAllLotteriesByPrizeIsEqualToSomething() throws Exception {
        // Initialize the database
        Prize prize = PrizeResourceIntTest.createEntity(em);
        em.persist(prize);
        em.flush();
        lottery.addPrize(prize);
        lotteryRepository.saveAndFlush(lottery);
        Long prizeId = prize.getId();

        // Get all the lotteryList where prize equals to prizeId
        defaultLotteryShouldBeFound("prizeId.equals=" + prizeId);

        // Get all the lotteryList where prize equals to prizeId + 1
        defaultLotteryShouldNotBeFound("prizeId.equals=" + (prizeId + 1));
    }


    @Test
    @Transactional
    public void getAllLotteriesByHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        History history = HistoryResourceIntTest.createEntity(em);
        em.persist(history);
        em.flush();
        lottery.addHistory(history);
        lotteryRepository.saveAndFlush(lottery);
        Long historyId = history.getId();

        // Get all the lotteryList where history equals to historyId
        defaultLotteryShouldBeFound("historyId.equals=" + historyId);

        // Get all the lotteryList where history equals to historyId + 1
        defaultLotteryShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLotteryShouldBeFound(String filter) throws Exception {
        restLotteryMockMvc.perform(get("/api/lotteries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lottery.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].minparticipants").value(hasItem(DEFAULT_MINPARTICIPANTS)))
            .andExpect(jsonPath("$.[*].maxparticipnts").value(hasItem(DEFAULT_MAXPARTICIPNTS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLotteryShouldNotBeFound(String filter) throws Exception {
        restLotteryMockMvc.perform(get("/api/lotteries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingLottery() throws Exception {
        // Get the lottery
        restLotteryMockMvc.perform(get("/api/lotteries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLottery() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        int databaseSizeBeforeUpdate = lotteryRepository.findAll().size();

        // Update the lottery
        Lottery updatedLottery = lotteryRepository.findById(lottery.getId()).get();
        // Disconnect from session so that the updates on updatedLottery are not directly saved in db
        em.detach(updatedLottery);
        updatedLottery
            .title(UPDATED_TITLE)
            .minparticipants(UPDATED_MINPARTICIPANTS)
            .maxparticipnts(UPDATED_MAXPARTICIPNTS)
            .price(UPDATED_PRICE);
        LotteryDTO lotteryDTO = lotteryMapper.toDto(updatedLottery);

        restLotteryMockMvc.perform(put("/api/lotteries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lotteryDTO)))
            .andExpect(status().isOk());

        // Validate the Lottery in the database
        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeUpdate);
        Lottery testLottery = lotteryList.get(lotteryList.size() - 1);
        assertThat(testLottery.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLottery.getMinparticipants()).isEqualTo(UPDATED_MINPARTICIPANTS);
        assertThat(testLottery.getMaxparticipnts()).isEqualTo(UPDATED_MAXPARTICIPNTS);
        assertThat(testLottery.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Lottery in Elasticsearch
        verify(mockLotterySearchRepository, times(1)).save(testLottery);
    }

    @Test
    @Transactional
    public void updateNonExistingLottery() throws Exception {
        int databaseSizeBeforeUpdate = lotteryRepository.findAll().size();

        // Create the Lottery
        LotteryDTO lotteryDTO = lotteryMapper.toDto(lottery);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLotteryMockMvc.perform(put("/api/lotteries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lotteryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lottery in the database
        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lottery in Elasticsearch
        verify(mockLotterySearchRepository, times(0)).save(lottery);
    }

    @Test
    @Transactional
    public void deleteLottery() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);

        int databaseSizeBeforeDelete = lotteryRepository.findAll().size();

        // Get the lottery
        restLotteryMockMvc.perform(delete("/api/lotteries/{id}", lottery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Lottery> lotteryList = lotteryRepository.findAll();
        assertThat(lotteryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lottery in Elasticsearch
        verify(mockLotterySearchRepository, times(1)).deleteById(lottery.getId());
    }

    @Test
    @Transactional
    public void searchLottery() throws Exception {
        // Initialize the database
        lotteryRepository.saveAndFlush(lottery);
        when(mockLotterySearchRepository.search(queryStringQuery("id:" + lottery.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lottery), PageRequest.of(0, 1), 1));
        // Search the lottery
        restLotteryMockMvc.perform(get("/api/_search/lotteries?query=id:" + lottery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lottery.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].minparticipants").value(hasItem(DEFAULT_MINPARTICIPANTS)))
            .andExpect(jsonPath("$.[*].maxparticipnts").value(hasItem(DEFAULT_MAXPARTICIPNTS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lottery.class);
        Lottery lottery1 = new Lottery();
        lottery1.setId(1L);
        Lottery lottery2 = new Lottery();
        lottery2.setId(lottery1.getId());
        assertThat(lottery1).isEqualTo(lottery2);
        lottery2.setId(2L);
        assertThat(lottery1).isNotEqualTo(lottery2);
        lottery1.setId(null);
        assertThat(lottery1).isNotEqualTo(lottery2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LotteryDTO.class);
        LotteryDTO lotteryDTO1 = new LotteryDTO();
        lotteryDTO1.setId(1L);
        LotteryDTO lotteryDTO2 = new LotteryDTO();
        assertThat(lotteryDTO1).isNotEqualTo(lotteryDTO2);
        lotteryDTO2.setId(lotteryDTO1.getId());
        assertThat(lotteryDTO1).isEqualTo(lotteryDTO2);
        lotteryDTO2.setId(2L);
        assertThat(lotteryDTO1).isNotEqualTo(lotteryDTO2);
        lotteryDTO1.setId(null);
        assertThat(lotteryDTO1).isNotEqualTo(lotteryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(lotteryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(lotteryMapper.fromId(null)).isNull();
    }
}
