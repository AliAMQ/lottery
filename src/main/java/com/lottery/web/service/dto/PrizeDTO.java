package com.lottery.web.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Prize entity.
 */
public class PrizeDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Integer value;

    private Long categoryId;

    private Long lotteryId;

    private Long historyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrizeDTO prizeDTO = (PrizeDTO) o;
        if (prizeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prizeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrizeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", value=" + getValue() +
            ", category=" + getCategoryId() +
            ", lottery=" + getLotteryId() +
            ", history=" + getHistoryId() +
            "}";
    }
}
