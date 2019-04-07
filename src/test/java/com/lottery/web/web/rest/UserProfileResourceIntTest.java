package com.lottery.web.web.rest;

import com.lottery.web.LotteryApp;

import com.lottery.web.domain.UserProfile;
import com.lottery.web.domain.User;
import com.lottery.web.domain.Lottery;
import com.lottery.web.repository.UserProfileRepository;
import com.lottery.web.repository.search.UserProfileSearchRepository;
import com.lottery.web.service.UserProfileService;
import com.lottery.web.service.dto.UserProfileDTO;
import com.lottery.web.service.mapper.UserProfileMapper;
import com.lottery.web.web.rest.errors.ExceptionTranslator;
import com.lottery.web.service.dto.UserProfileCriteria;
import com.lottery.web.service.UserProfileQueryService;

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

import com.lottery.web.domain.enumeration.State;
/**
 * Test class for the UserProfileResource REST controller.
 *
 * @see UserProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApp.class)
public class UserProfileResourceIntTest {

    private static final State DEFAULT_STATE = State.AK;
    private static final State UPDATED_STATE = State.AL;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGEPATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEPATH = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SINCE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Autowired
    private UserProfileMapper userProfileMapper;
    

    @Autowired
    private UserProfileService userProfileService;

    /**
     * This repository is mocked in the com.lottery.web.repository.search test package.
     *
     * @see com.lottery.web.repository.search.UserProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserProfileSearchRepository mockUserProfileSearchRepository;

    @Autowired
    private UserProfileQueryService userProfileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserProfileResource userProfileResource = new UserProfileResource(userProfileService, userProfileQueryService);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
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
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .state(DEFAULT_STATE)
            .city(DEFAULT_CITY)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .imagepath(DEFAULT_IMAGEPATH)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .email(DEFAULT_EMAIL)
            .username(DEFAULT_USERNAME)
            .since(DEFAULT_SINCE);
        return userProfile;
    }

    @Before
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserProfile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserProfile.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testUserProfile.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUserProfile.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserProfile.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserProfile.getSince()).isEqualTo(DEFAULT_SINCE);

        // Validate the UserProfile in Elasticsearch
        verify(mockUserProfileSearchRepository, times(1)).save(testUserProfile);
    }

    @Test
    @Transactional
    public void createUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserProfile in Elasticsearch
        verify(mockUserProfileSearchRepository, times(0)).save(userProfile);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())));
    }
    

    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.imagepath").value(DEFAULT_IMAGEPATH.toString()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.since").value(DEFAULT_SINCE.toString()));
    }

    @Test
    @Transactional
    public void getAllUserProfilesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state equals to DEFAULT_STATE
        defaultUserProfileShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the userProfileList where state equals to UPDATED_STATE
        defaultUserProfileShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state in DEFAULT_STATE or UPDATED_STATE
        defaultUserProfileShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the userProfileList where state equals to UPDATED_STATE
        defaultUserProfileShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state is not null
        defaultUserProfileShouldBeFound("state.specified=true");

        // Get all the userProfileList where state is null
        defaultUserProfileShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city equals to DEFAULT_CITY
        defaultUserProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city is not null
        defaultUserProfileShouldBeFound("city.specified=true");

        // Get all the userProfileList where city is null
        defaultUserProfileShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address equals to DEFAULT_ADDRESS
        defaultUserProfileShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the userProfileList where address equals to UPDATED_ADDRESS
        defaultUserProfileShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultUserProfileShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the userProfileList where address equals to UPDATED_ADDRESS
        defaultUserProfileShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address is not null
        defaultUserProfileShouldBeFound("address.specified=true");

        // Get all the userProfileList where address is null
        defaultUserProfileShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone equals to DEFAULT_PHONE
        defaultUserProfileShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userProfileList where phone equals to UPDATED_PHONE
        defaultUserProfileShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserProfileShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userProfileList where phone equals to UPDATED_PHONE
        defaultUserProfileShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone is not null
        defaultUserProfileShouldBeFound("phone.specified=true");

        // Get all the userProfileList where phone is null
        defaultUserProfileShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imagepath equals to DEFAULT_IMAGEPATH
        defaultUserProfileShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the userProfileList where imagepath equals to UPDATED_IMAGEPATH
        defaultUserProfileShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultUserProfileShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the userProfileList where imagepath equals to UPDATED_IMAGEPATH
        defaultUserProfileShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imagepath is not null
        defaultUserProfileShouldBeFound("imagepath.specified=true");

        // Get all the userProfileList where imagepath is null
        defaultUserProfileShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstname equals to DEFAULT_FIRSTNAME
        defaultUserProfileShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the userProfileList where firstname equals to UPDATED_FIRSTNAME
        defaultUserProfileShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultUserProfileShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the userProfileList where firstname equals to UPDATED_FIRSTNAME
        defaultUserProfileShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstname is not null
        defaultUserProfileShouldBeFound("firstname.specified=true");

        // Get all the userProfileList where firstname is null
        defaultUserProfileShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastname equals to DEFAULT_LASTNAME
        defaultUserProfileShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the userProfileList where lastname equals to UPDATED_LASTNAME
        defaultUserProfileShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultUserProfileShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the userProfileList where lastname equals to UPDATED_LASTNAME
        defaultUserProfileShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastname is not null
        defaultUserProfileShouldBeFound("lastname.specified=true");

        // Get all the userProfileList where lastname is null
        defaultUserProfileShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email equals to DEFAULT_EMAIL
        defaultUserProfileShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the userProfileList where email equals to UPDATED_EMAIL
        defaultUserProfileShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUserProfileShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the userProfileList where email equals to UPDATED_EMAIL
        defaultUserProfileShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email is not null
        defaultUserProfileShouldBeFound("email.specified=true");

        // Get all the userProfileList where email is null
        defaultUserProfileShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where username equals to DEFAULT_USERNAME
        defaultUserProfileShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the userProfileList where username equals to UPDATED_USERNAME
        defaultUserProfileShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultUserProfileShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the userProfileList where username equals to UPDATED_USERNAME
        defaultUserProfileShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where username is not null
        defaultUserProfileShouldBeFound("username.specified=true");

        // Get all the userProfileList where username is null
        defaultUserProfileShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesBySinceIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where since equals to DEFAULT_SINCE
        defaultUserProfileShouldBeFound("since.equals=" + DEFAULT_SINCE);

        // Get all the userProfileList where since equals to UPDATED_SINCE
        defaultUserProfileShouldNotBeFound("since.equals=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesBySinceIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where since in DEFAULT_SINCE or UPDATED_SINCE
        defaultUserProfileShouldBeFound("since.in=" + DEFAULT_SINCE + "," + UPDATED_SINCE);

        // Get all the userProfileList where since equals to UPDATED_SINCE
        defaultUserProfileShouldNotBeFound("since.in=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesBySinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where since is not null
        defaultUserProfileShouldBeFound("since.specified=true");

        // Get all the userProfileList where since is null
        defaultUserProfileShouldNotBeFound("since.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesBySinceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where since greater than or equals to DEFAULT_SINCE
        defaultUserProfileShouldBeFound("since.greaterOrEqualThan=" + DEFAULT_SINCE);

        // Get all the userProfileList where since greater than or equals to UPDATED_SINCE
        defaultUserProfileShouldNotBeFound("since.greaterOrEqualThan=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesBySinceIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where since less than or equals to DEFAULT_SINCE
        defaultUserProfileShouldNotBeFound("since.lessThan=" + DEFAULT_SINCE);

        // Get all the userProfileList where since less than or equals to UPDATED_SINCE
        defaultUserProfileShouldBeFound("since.lessThan=" + UPDATED_SINCE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        userProfileRepository.saveAndFlush(userProfile);
        Long userId = user.getId();

        // Get all the userProfileList where user equals to userId
        defaultUserProfileShouldBeFound("userId.equals=" + userId);

        // Get all the userProfileList where user equals to userId + 1
        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByLotteryIsEqualToSomething() throws Exception {
        // Initialize the database
        Lottery lottery = LotteryResourceIntTest.createEntity(em);
        em.persist(lottery);
        em.flush();
        userProfile.addLottery(lottery);
        userProfileRepository.saveAndFlush(userProfile);
        Long lotteryId = lottery.getId();

        // Get all the userProfileList where lottery equals to lotteryId
        defaultUserProfileShouldBeFound("lotteryId.equals=" + lotteryId);

        // Get all the userProfileList where lottery equals to lotteryId + 1
        defaultUserProfileShouldNotBeFound("lotteryId.equals=" + (lotteryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).get();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .imagepath(UPDATED_IMAGEPATH)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .email(UPDATED_EMAIL)
            .username(UPDATED_USERNAME)
            .since(UPDATED_SINCE);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserProfile.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testUserProfile.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUserProfile.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserProfile.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserProfile.getSince()).isEqualTo(UPDATED_SINCE);

        // Validate the UserProfile in Elasticsearch
        verify(mockUserProfileSearchRepository, times(1)).save(testUserProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserProfile in Elasticsearch
        verify(mockUserProfileSearchRepository, times(0)).save(userProfile);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Get the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserProfile in Elasticsearch
        verify(mockUserProfileSearchRepository, times(1)).deleteById(userProfile.getId());
    }

    @Test
    @Transactional
    public void searchUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        when(mockUserProfileSearchRepository.search(queryStringQuery("id:" + userProfile.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userProfile), PageRequest.of(0, 1), 1));
        // Search the userProfile
        restUserProfileMockMvc.perform(get("/api/_search/user-profiles?query=id:" + userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setId(1L);
        UserProfile userProfile2 = new UserProfile();
        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);
        userProfile2.setId(2L);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
        userProfile1.setId(null);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileDTO.class);
        UserProfileDTO userProfileDTO1 = new UserProfileDTO();
        userProfileDTO1.setId(1L);
        UserProfileDTO userProfileDTO2 = new UserProfileDTO();
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO2.setId(userProfileDTO1.getId());
        assertThat(userProfileDTO1).isEqualTo(userProfileDTO2);
        userProfileDTO2.setId(2L);
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO1.setId(null);
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userProfileMapper.fromId(null)).isNull();
    }
}
