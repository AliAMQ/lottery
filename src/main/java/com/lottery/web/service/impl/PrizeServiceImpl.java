package com.lottery.web.service.impl;

import com.lottery.web.service.PrizeService;
import com.lottery.web.domain.Prize;
import com.lottery.web.repository.PrizeRepository;
import com.lottery.web.repository.search.PrizeSearchRepository;
import com.lottery.web.service.dto.PrizeDTO;
import com.lottery.web.service.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Prize.
 */
@Service
@Transactional
public class PrizeServiceImpl implements PrizeService {

    private final Logger log = LoggerFactory.getLogger(PrizeServiceImpl.class);

    private final PrizeRepository prizeRepository;

    private final PrizeMapper prizeMapper;

    private final PrizeSearchRepository prizeSearchRepository;

    public PrizeServiceImpl(PrizeRepository prizeRepository, PrizeMapper prizeMapper, PrizeSearchRepository prizeSearchRepository) {
        this.prizeRepository = prizeRepository;
        this.prizeMapper = prizeMapper;
        this.prizeSearchRepository = prizeSearchRepository;
    }

    /**
     * Save a prize.
     *
     * @param prizeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrizeDTO save(PrizeDTO prizeDTO) {
        log.debug("Request to save Prize : {}", prizeDTO);
        Prize prize = prizeMapper.toEntity(prizeDTO);
        prize = prizeRepository.save(prize);
        PrizeDTO result = prizeMapper.toDto(prize);
        prizeSearchRepository.save(prize);
        return result;
    }

    /**
     * Get all the prizes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrizeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prizes");
        return prizeRepository.findAll(pageable)
            .map(prizeMapper::toDto);
    }


    /**
     * Get one prize by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrizeDTO> findOne(Long id) {
        log.debug("Request to get Prize : {}", id);
        return prizeRepository.findById(id)
            .map(prizeMapper::toDto);
    }

    /**
     * Delete the prize by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prize : {}", id);
        prizeRepository.deleteById(id);
        prizeSearchRepository.deleteById(id);
    }

    /**
     * Search for the prize corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrizeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Prizes for query {}", query);
        return prizeSearchRepository.search(queryStringQuery(query), pageable)
            .map(prizeMapper::toDto);
    }
}
