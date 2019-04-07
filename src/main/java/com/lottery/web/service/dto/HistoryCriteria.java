package com.lottery.web.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the History entity. This class is used in HistoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /histories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistoryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter date;

    private LongFilter lotteryId;

    private LongFilter prizeId;

    public HistoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(LongFilter lotteryId) {
        this.lotteryId = lotteryId;
    }

    public LongFilter getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(LongFilter prizeId) {
        this.prizeId = prizeId;
    }

    @Override
    public String toString() {
        return "HistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (lotteryId != null ? "lotteryId=" + lotteryId + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
            "}";
    }

}
