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

import com.lottery.web.domain.Lottery;
import com.lottery.web.domain.*; // for static metamodels
import com.lottery.web.repository.LotteryRepository;
import com.lottery.web.repository.search.LotterySearchRepository;
import com.lottery.web.service.dto.LotteryCriteria;

import com.lottery.web.service.dto.LotteryDTO;
import com.lottery.web.service.mapper.LotteryMapper;

/**
 * Service for executing complex queries for Lottery entities in the database.
 * The main input is a {@link LotteryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LotteryDTO} or a {@link Page} of {@link LotteryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LotteryQueryService extends QueryService<Lottery> {

    private final Logger log = LoggerFactory.getLogger(LotteryQueryService.class);

    private final LotteryRepository lotteryRepository;

    private final LotteryMapper lotteryMapper;

    private final LotterySearchRepository lotterySearchRepository;

    public LotteryQueryService(LotteryRepository lotteryRepository, LotteryMapper lotteryMapper, LotterySearchRepository lotterySearchRepository) {
        this.lotteryRepository = lotteryRepository;
        this.lotteryMapper = lotteryMapper;
        this.lotterySearchRepository = lotterySearchRepository;
    }

    /**
     * Return a {@link List} of {@link LotteryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LotteryDTO> findByCriteria(LotteryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lottery> specification = createSpecification(criteria);
        return lotteryMapper.toDto(lotteryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LotteryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LotteryDTO> findByCriteria(LotteryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lottery> specification = createSpecification(criteria);
        return lotteryRepository.findAll(specification, page)
            .map(lotteryMapper::toDto);
    }

    /**
     * Function to convert LotteryCriteria to a {@link Specification}
     */
    private Specification<Lottery> createSpecification(LotteryCriteria criteria) {
        Specification<Lottery> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Lottery_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Lottery_.title));
            }
            if (criteria.getMinparticipants() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinparticipants(), Lottery_.minparticipants));
            }
            if (criteria.getMaxparticipnts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxparticipnts(), Lottery_.maxparticipnts));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Lottery_.price));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserProfileId(), Lottery_.userProfile, UserProfile_.id));
            }
            if (criteria.getPrizeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPrizeId(), Lottery_.prizes, Prize_.id));
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getHistoryId(), Lottery_.histories, History_.id));
            }
        }
        return specification;
    }

}
