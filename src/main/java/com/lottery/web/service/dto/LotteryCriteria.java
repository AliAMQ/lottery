package com.lottery.web.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Lottery entity. This class is used in LotteryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /lotteries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LotteryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private IntegerFilter minparticipants;

    private IntegerFilter maxparticipnts;

    private IntegerFilter price;

    private LongFilter userProfileId;

    private LongFilter prizeId;

    private LongFilter historyId;

    public LotteryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getMinparticipants() {
        return minparticipants;
    }

    public void setMinparticipants(IntegerFilter minparticipants) {
        this.minparticipants = minparticipants;
    }

    public IntegerFilter getMaxparticipnts() {
        return maxparticipnts;
    }

    public void setMaxparticipnts(IntegerFilter maxparticipnts) {
        this.maxparticipnts = maxparticipnts;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(LongFilter prizeId) {
        this.prizeId = prizeId;
    }

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }

    @Override
    public String toString() {
        return "LotteryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (minparticipants != null ? "minparticipants=" + minparticipants + ", " : "") +
                (maxparticipnts != null ? "maxparticipnts=" + maxparticipnts + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
                (prizeId != null ? "prizeId=" + prizeId + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
            "}";
    }

}
