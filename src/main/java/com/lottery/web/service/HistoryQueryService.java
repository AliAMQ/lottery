package com.lottery.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.lottery.web.domain.History;
import com.lottery.web.domain.*; // for static metamodels
import com.lottery.web.repository.HistoryRepository;
import com.lottery.web.repository.search.HistorySearchRepository;
import com.lottery.web.service.dto.HistoryCriteria;

import com.lottery.web.service.dto.HistoryDTO;
import com.lottery.web.service.mapper.HistoryMapper;

/**
 * Service for executing complex queries for History entities in the database.
 * The main input is a {@link HistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoryDTO} or a {@link Page} of {@link HistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoryQueryService extends QueryService<History> {

    private final Logger log = LoggerFactory.getLogger(HistoryQueryService.class);

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;

    private final HistorySearchRepository historySearchRepository;

    public HistoryQueryService(HistoryRepository historyRepository, HistoryMapper historyMapper, HistorySearchRepository historySearchRepository) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historySearchRepository = historySearchRepository;
    }

    /**
     * Return a {@link List} of {@link HistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoryDTO> findByCriteria(HistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<History> specification = createSpecification(criteria);
        return historyMapper.toDto(historyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoryDTO> findByCriteria(HistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<History> specification = createSpecification(criteria);
        return historyRepository.findAll(specification, page)
            .map(historyMapper::toDto);
    }

    /**
     * Function to convert HistoryCriteria to a {@link Specification}
     */
    private Specification<History> createSpecification(HistoryCriteria criteria) {
        Specification<History> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), History_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), History_.date));
            }
            if (criteria.getLotteryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLotteryId(), History_.lottery, Lottery_.id));
            }
            if (criteria.getPrizeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPrizeId(), History_.prizes, Prize_.id));
            }
        }
        return specification;
    }

}
