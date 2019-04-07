package com.lottery.web.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the History entity.
 */
public class HistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Long lotteryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoryDTO historyDTO = (HistoryDTO) o;
        if (historyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", lottery=" + getLotteryId() +
            "}";
    }
}
