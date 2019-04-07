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

import com.lottery.web.domain.Prize;
import com.lottery.web.domain.*; // for static metamodels
import com.lottery.web.repository.PrizeRepository;
import com.lottery.web.repository.search.PrizeSearchRepository;
import com.lottery.web.service.dto.PrizeCriteria;

import com.lottery.web.service.dto.PrizeDTO;
import com.lottery.web.service.mapper.PrizeMapper;

/**
 * Service for executing complex queries for Prize entities in the database.
 * The main input is a {@link PrizeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrizeDTO} or a {@link Page} of {@link PrizeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrizeQueryService extends QueryService<Prize> {

    private final Logger log = LoggerFactory.getLogger(PrizeQueryService.class);

    private final PrizeRepository prizeRepository;

    private final PrizeMapper prizeMapper;

    private final PrizeSearchRepository prizeSearchRepository;

    public PrizeQueryService(PrizeRepository prizeRepository, PrizeMapper prizeMapper, PrizeSearchRepository prizeSearchRepository) {
        this.prizeRepository = prizeRepository;
        this.prizeMapper = prizeMapper;
        this.prizeSearchRepository = prizeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrizeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrizeDTO> findByCriteria(PrizeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prize> specification = createSpecification(criteria);
        return prizeMapper.toDto(prizeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrizeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrizeDTO> findByCriteria(PrizeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prize> specification = createSpecification(criteria);
        return prizeRepository.findAll(specification, page)
            .map(prizeMapper::toDto);
    }

    /**
     * Function to convert PrizeCriteria to a {@link Specification}
     */
    private Specification<Prize> createSpecification(PrizeCriteria criteria) {
        Specification<Prize> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Prize_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Prize_.title));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), Prize_.value));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCategoryId(), Prize_.category, Category_.id));
            }
            if (criteria.getLotteryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLotteryId(), Prize_.lottery, Lottery_.id));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getHistoryId(), Prize_.history, History_.id));
            }
        }
        return specification;
    }

}
