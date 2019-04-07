package com.lottery.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A History.
 */
@Entity
@Table(name = "history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties("histories")
    private Lottery lottery;

    @OneToMany(mappedBy = "history")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Prize> prizes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public History date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public History lottery(Lottery lottery) {
        this.lottery = lottery;
        return this;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public Set<Prize> getPrizes() {
        return prizes;
    }

    public History prizes(Set<Prize> prizes) {
        this.prizes = prizes;
        return this;
    }

    public History addPrize(Prize prize) {
        this.prizes.add(prize);
        prize.setHistory(this);
        return this;
    }

    public History removePrize(Prize prize) {
        this.prizes.remove(prize);
        prize.setHistory(null);
        return this;
    }

    public void setPrizes(Set<Prize> prizes) {
        this.prizes = prizes;
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
        History history = (History) o;
        if (history.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), history.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "History{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
