package com.lottery.web.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lottery.web.service.PrizeService;
import com.lottery.web.web.rest.errors.BadRequestAlertException;
import com.lottery.web.web.rest.util.HeaderUtil;
import com.lottery.web.web.rest.util.PaginationUtil;
import com.lottery.web.service.dto.PrizeDTO;
import com.lottery.web.service.dto.PrizeCriteria;
import com.lottery.web.service.PrizeQueryService;
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
 * REST controller for managing Prize.
 */
@RestController
@RequestMapping("/api")
public class PrizeResource {

    private final Logger log = LoggerFactory.getLogger(PrizeResource.class);

    private static final String ENTITY_NAME = "prize";

    private final PrizeService prizeService;

    private final PrizeQueryService prizeQueryService;

    public PrizeResource(PrizeService prizeService, PrizeQueryService prizeQueryService) {
        this.prizeService = prizeService;
        this.prizeQueryService = prizeQueryService;
    }

    /**
     * POST  /prizes : Create a new prize.
     *
     * @param prizeDTO the prizeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prizeDTO, or with status 400 (Bad Request) if the prize has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prizes")
    @Timed
    public ResponseEntity<PrizeDTO> createPrize(@Valid @RequestBody PrizeDTO prizeDTO) throws URISyntaxException {
        log.debug("REST request to save Prize : {}", prizeDTO);
        if (prizeDTO.getId() != null) {
            throw new BadRequestAlertException("A new prize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrizeDTO result = prizeService.save(prizeDTO);
        return ResponseEntity.created(new URI("/api/prizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prizes : Updates an existing prize.
     *
     * @param prizeDTO the prizeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prizeDTO,
     * or with status 400 (Bad Request) if the prizeDTO is not valid,
     * or with status 500 (Internal Server Error) if the prizeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prizes")
    @Timed
    public ResponseEntity<PrizeDTO> updatePrize(@Valid @RequestBody PrizeDTO prizeDTO) throws URISyntaxException {
        log.debug("REST request to update Prize : {}", prizeDTO);
        if (prizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrizeDTO result = prizeService.save(prizeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prizeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prizes : get all the prizes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of prizes in body
     */
    @GetMapping("/prizes")
    @Timed
    public ResponseEntity<List<PrizeDTO>> getAllPrizes(PrizeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Prizes by criteria: {}", criteria);
        Page<PrizeDTO> page = prizeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prizes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prizes/:id : get the "id" prize.
     *
     * @param id the id of the prizeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prizeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/prizes/{id}")
    @Timed
    public ResponseEntity<PrizeDTO> getPrize(@PathVariable Long id) {
        log.debug("REST request to get Prize : {}", id);
        Optional<PrizeDTO> prizeDTO = prizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prizeDTO);
    }

    /**
     * DELETE  /prizes/:id : delete the "id" prize.
     *
     * @param id the id of the prizeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prizes/{id}")
    @Timed
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        log.debug("REST request to delete Prize : {}", id);
        prizeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prizes?query=:query : search for the prize corresponding
     * to the query.
     *
     * @param query the query of the prize search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/prizes")
    @Timed
    public ResponseEntity<List<PrizeDTO>> searchPrizes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Prizes for query {}", query);
        Page<PrizeDTO> page = prizeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prizes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
