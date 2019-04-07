package com.lottery.web.service;

import com.lottery.web.service.dto.LotteryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Lottery.
 */
public interface LotteryService {

    /**
     * Save a lottery.
     *
     * @param lotteryDTO the entity to save
     * @return the persisted entity
     */
    LotteryDTO save(LotteryDTO lotteryDTO);

    /**
     * Get all the lotteries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LotteryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" lottery.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LotteryDTO> findOne(Long id);

    /**
     * Delete the "id" lottery.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lottery corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LotteryDTO> search(String query, Pageable pageable);
}
