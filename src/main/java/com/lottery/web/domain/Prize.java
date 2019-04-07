package com.lottery.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Prize.
 */
@Entity
@Table(name = "prize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "prize")
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "jhi_value")
    private Integer value;

    @ManyToOne
    @JsonIgnoreProperties("prizes")
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties("prizes")
    private Lottery lottery;

    @ManyToOne
    @JsonIgnoreProperties("prizes")
    private History history;

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

    public Prize title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public Prize value(Integer value) {
        this.value = value;
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public Prize category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public Prize lottery(Lottery lottery) {
        this.lottery = lottery;
        return this;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public History getHistory() {
        return history;
    }

    public Prize history(History history) {
        this.history = history;
        return this;
    }

    public void setHistory(History history) {
        this.history = history;
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
        Prize prize = (Prize) o;
        if (prize.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prize.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prize{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
