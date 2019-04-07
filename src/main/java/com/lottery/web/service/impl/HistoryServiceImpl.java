package com.lottery.web.service.impl;

import com.lottery.web.service.HistoryService;
import com.lottery.web.domain.History;
import com.lottery.web.repository.HistoryRepository;
import com.lottery.web.repository.search.HistorySearchRepository;
import com.lottery.web.service.dto.HistoryDTO;
import com.lottery.web.service.mapper.HistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing History.
 */
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;

    private final HistorySearchRepository historySearchRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper, HistorySearchRepository historySearchRepository) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historySearchRepository = historySearchRepository;
    }

    /**
     * Save a history.
     *
     * @param historyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HistoryDTO save(HistoryDTO historyDTO) {
        log.debug("Request to save History : {}", historyDTO);
        History history = historyMapper.toEntity(historyDTO);
        history = historyRepository.save(history);
        HistoryDTO result = historyMapper.toDto(history);
        historySearchRepository.save(history);
        return result;
    }

    /**
     * Get all the histories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Histories");
        return historyRepository.findAll(pageable)
            .map(historyMapper::toDto);
    }


    /**
     * Get one history by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryDTO> findOne(Long id) {
        log.debug("Request to get History : {}", id);
        return historyRepository.findById(id)
            .map(historyMapper::toDto);
    }

    /**
     * Delete the history by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete History : {}", id);
        historyRepository.deleteById(id);
        historySearchRepository.deleteById(id);
    }

    /**
     * Search for the history corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Histories for query {}", query);
        return historySearchRepository.search(queryStringQuery(query), pageable)
            .map(historyMapper::toDto);
    }
}
