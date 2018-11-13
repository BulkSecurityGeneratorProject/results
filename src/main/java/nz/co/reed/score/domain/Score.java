package nz.co.reed.score.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Score.
 */
@Entity
@Table(name = "score")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "difficulty", precision = 10, scale = 2)
    private BigDecimal difficulty;

    @Column(name = "neutral_deductions", precision = 10, scale = 2)
    private BigDecimal neutralDeductions;

    @ManyToOne
    @JsonIgnoreProperties("sessionScores")
    private CompSession session;

    @ManyToOne
    @JsonIgnoreProperties("athleteScores")
    private Athlete athlete;

    @ManyToOne
    @JsonIgnoreProperties("apparatusScores")
    private Apparatus apparatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Score total(BigDecimal total) {
        this.total = total;
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDifficulty() {
        return difficulty;
    }

    public Score difficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
    }

    public BigDecimal getNeutralDeductions() {
        return neutralDeductions;
    }

    public Score neutralDeductions(BigDecimal neutralDeductions) {
        this.neutralDeductions = neutralDeductions;
        return this;
    }

    public void setNeutralDeductions(BigDecimal neutralDeductions) {
        this.neutralDeductions = neutralDeductions;
    }

    public CompSession getSession() {
        return session;
    }

    public Score session(CompSession compSession) {
        this.session = compSession;
        return this;
    }

    public void setSession(CompSession compSession) {
        this.session = compSession;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public Score athlete(Athlete athlete) {
        this.athlete = athlete;
        return this;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Apparatus getApparatus() {
        return apparatus;
    }

    public Score apparatus(Apparatus apparatus) {
        this.apparatus = apparatus;
        return this;
    }

    public void setApparatus(Apparatus apparatus) {
        this.apparatus = apparatus;
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
        Score score = (Score) o;
        if (score.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), score.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", difficulty=" + getDifficulty() +
            ", neutralDeductions=" + getNeutralDeductions() +
            "}";
    }
}
