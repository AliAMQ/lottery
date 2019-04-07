package com.lottery.web.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Lottery entity.
 */
public class LotteryDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Integer minparticipants;

    private Integer maxparticipnts;

    private Integer price;

    private Long userProfileId;

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

    public Integer getMinparticipants() {
        return minparticipants;
    }

    public void setMinparticipants(Integer minparticipants) {
        this.minparticipants = minparticipants;
    }

    public Integer getMaxparticipnts() {
        return maxparticipnts;
    }

    public void setMaxparticipnts(Integer maxparticipnts) {
        this.maxparticipnts = maxparticipnts;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LotteryDTO lotteryDTO = (LotteryDTO) o;
        if (lotteryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lotteryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LotteryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", minparticipants=" + getMinparticipants() +
            ", maxparticipnts=" + getMaxparticipnts() +
            ", price=" + getPrice() +
            ", userProfile=" + getUserProfileId() +
            "}";
    }
}
