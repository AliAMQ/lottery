package com.lottery.web.service;

import com.lottery.web.service.dto.PrizeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Prize.
 */
public interface PrizeService {

    /**
     * Save a prize.
     *
     * @param prizeDTO the entity to save
     * @return the persisted entity
     */
    PrizeDTO save(PrizeDTO prizeDTO);

    /**
     * Get all the prizes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrizeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" prize.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrizeDTO> findOne(Long id);

    /**
     * Delete the "id" prize.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the prize corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrizeDTO> search(String query, Pageable pageable);
}
