package com.lottery.web.service.impl;

import com.lottery.web.service.LotteryService;
import com.lottery.web.domain.Lottery;
import com.lottery.web.repository.LotteryRepository;
import com.lottery.web.repository.search.LotterySearchRepository;
import com.lottery.web.service.dto.LotteryDTO;
import com.lottery.web.service.mapper.LotteryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Lottery.
 */
@Service
@Transactional
public class LotteryServiceImpl implements LotteryService {

    private final Logger log = LoggerFactory.getLogger(LotteryServiceImpl.class);

    private final LotteryRepository lotteryRepository;

    private final LotteryMapper lotteryMapper;

    private final LotterySearchRepository lotterySearchRepository;

    public LotteryServiceImpl(LotteryRepository lotteryRepository, LotteryMapper lotteryMapper, LotterySearchRepository lotterySearchRepository) {
        this.lotteryRepository = lotteryRepository;
        this.lotteryMapper = lotteryMapper;
        this.lotterySearchRepository = lotterySearchRepository;
    }

    /**
     * Save a lottery.
     *
     * @param lotteryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LotteryDTO save(LotteryDTO lotteryDTO) {
        log.debug("Request to save Lottery : {}", lotteryDTO);
        Lottery lottery = lotteryMapper.toEntity(lotteryDTO);
        lottery = lotteryRepository.save(lottery);
        LotteryDTO result = lotteryMapper.toDto(lottery);
        lotterySearchRepository.save(lottery);
        return result;
    }

    /**
     * Get all the lotteries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LotteryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lotteries");
        return lotteryRepository.findAll(pageable)
            .map(lotteryMapper::toDto);
    }


    /**
     * Get one lottery by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LotteryDTO> findOne(Long id) {
        log.debug("Request to get Lottery : {}", id);
        return lotteryRepository.findById(id)
            .map(lotteryMapper::toDto);
    }

    /**
     * Delete the lottery by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lottery : {}", id);
        lotteryRepository.deleteById(id);
        lotterySearchRepository.deleteById(id);
    }

    /**
     * Search for the lottery corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LotteryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lotteries for query {}", query);
        return lotterySearchRepository.search(queryStringQuery(query), pageable)
            .map(lotteryMapper::toDto);
    }
}
