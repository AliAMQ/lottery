package com.lottery.web.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lottery.web.service.LotteryService;
import com.lottery.web.web.rest.errors.BadRequestAlertException;
import com.lottery.web.web.rest.util.HeaderUtil;
import com.lottery.web.web.rest.util.PaginationUtil;
import com.lottery.web.service.dto.LotteryDTO;
import com.lottery.web.service.dto.LotteryCriteria;
import com.lottery.web.service.LotteryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Lottery.
 */
@RestController
@RequestMapping("/api")
public class LotteryResource {

    private final Logger log = LoggerFactory.getLogger(LotteryResource.class);

    private static final String ENTITY_NAME = "lottery";

    private final LotteryService lotteryService;

    private final LotteryQueryService lotteryQueryService;

    public LotteryResource(LotteryService lotteryService, LotteryQueryService lotteryQueryService) {
        this.lotteryService = lotteryService;
        this.lotteryQueryService = lotteryQueryService;
    }

    /**
     * POST  /lotteries : Create a new lottery.
     *
     * @param lotteryDTO the lotteryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lotteryDTO, or with status 400 (Bad Request) if the lottery has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lotteries")
    @Timed
    public ResponseEntity<LotteryDTO> createLottery(@Valid @RequestBody LotteryDTO lotteryDTO) throws URISyntaxException {
        log.debug("REST request to save Lottery : {}", lotteryDTO);
        if (lotteryDTO.getId() != null) {
            throw new BadRequestAlertException("A new lottery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LotteryDTO result = lotteryService.save(lotteryDTO);
        return ResponseEntity.created(new URI("/api/lotteries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lotteries : Updates an existing lottery.
     *
     * @param lotteryDTO the lotteryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lotteryDTO,
     * or with status 400 (Bad Request) if the lotteryDTO is not valid,
     * or with status 500 (Internal Server Error) if the lotteryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lotteries")
    @Timed
    public ResponseEntity<LotteryDTO> updateLottery(@Valid @RequestBody LotteryDTO lotteryDTO) throws URISyntaxException {
        log.debug("REST request to update Lottery : {}", lotteryDTO);
        if (lotteryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LotteryDTO result = lotteryService.save(lotteryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lotteryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lotteries : get all the lotteries.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of lotteries in body
     */
    @GetMapping("/lotteries")
    @Timed
    public ResponseEntity<List<LotteryDTO>> getAllLotteries(LotteryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Lotteries by criteria: {}", criteria);
        Page<LotteryDTO> page = lotteryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lotteries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lotteries/:id : get the "id" lottery.
     *
     * @param id the id of the lotteryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lotteryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lotteries/{id}")
    @Timed
    public ResponseEntity<LotteryDTO> getLottery(@PathVariable Long id) {
        log.debug("REST request to get Lottery : {}", id);
        Optional<LotteryDTO> lotteryDTO = lotteryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lotteryDTO);
    }

    /**
     * DELETE  /lotteries/:id : delete the "id" lottery.
     *
     * @param id the id of the lotteryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lotteries/{id}")
    @Timed
    public ResponseEntity<Void> deleteLottery(@PathVariable Long id) {
        log.debug("REST request to delete Lottery : {}", id);
        lotteryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lotteries?query=:query : search for the lottery corresponding
     * to the query.
     *
     * @param query the query of the lottery search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/lotteries")
    @Timed
    public ResponseEntity<List<LotteryDTO>> searchLotteries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Lotteries for query {}", query);
        Page<LotteryDTO> page = lotteryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lotteries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
