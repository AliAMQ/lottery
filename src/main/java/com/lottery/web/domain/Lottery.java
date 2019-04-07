package com.lottery.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Lottery.
 */
@Entity
@Table(name = "lottery")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "lottery")
public class Lottery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "minparticipants")
    private Integer minparticipants;

    @Column(name = "maxparticipnts")
    private Integer maxparticipnts;

    @Column(name = "price")
    private Integer price;

    @ManyToOne
    @JsonIgnoreProperties("lotteries")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "lottery")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Prize> prizes = new HashSet<>();

    @OneToMany(mappedBy = "lottery")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<History> histories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Lottery title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMinparticipants() {
        return minparticipants;
    }

    public Lottery minparticipants(Integer minparticipants) {
        this.minparticipants = minparticipants;
        return this;
    }

    public void setMinparticipants(Integer minparticipants) {
        this.minparticipants = minparticipants;
    }

    public Integer getMaxparticipnts() {
        return maxparticipnts;
    }

    public Lottery maxparticipnts(Integer maxparticipnts) {
        this.maxparticipnts = maxparticipnts;
        return this;
    }

    public void setMaxparticipnts(Integer maxparticipnts) {
        this.maxparticipnts = maxparticipnts;
    }

    public Integer getPrice() {
        return price;
    }

    public Lottery price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Lottery userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Set<Prize> getPrizes() {
        return prizes;
    }

    public Lottery prizes(Set<Prize> prizes) {
        this.prizes = prizes;
        return this;
    }

    public Lottery addPrize(Prize prize) {
        this.prizes.add(prize);
        prize.setLottery(this);
        return this;
    }

    public Lottery removePrize(Prize prize) {
        this.prizes.remove(prize);
        prize.setLottery(null);
        return this;
    }

    public void setPrizes(Set<Prize> prizes) {
        this.prizes = prizes;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public Lottery histories(Set<History> histories) {
        this.histories = histories;
        return this;
    }

    public Lottery addHistory(History history) {
        this.histories.add(history);
        history.setLottery(this);
        return this;
    }

    public Lottery removeHistory(History history) {
        this.histories.remove(history);
        history.setLottery(null);
        return this;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lottery lottery = (Lottery) o;
        if (lottery.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lottery.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Lottery{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", minparticipants=" + getMinparticipants() +
            ", maxparticipnts=" + getMaxparticipnts() +
            ", price=" + getPrice() +
            "}";
    }
}
