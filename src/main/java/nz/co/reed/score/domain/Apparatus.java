package nz.co.reed.score.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Apparatus.
 */
@Entity
@Table(name = "apparatus")
public class Apparatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apparatus_name")
    private String apparatusName;

    @Column(name = "competition_type")
    private String competitionType;

    @OneToMany(mappedBy = "apparatus")
    private Set<Score> apparatusScores = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApparatusName() {
        return apparatusName;
    }

    public Apparatus apparatusName(String apparatusName) {
        this.apparatusName = apparatusName;
        return this;
    }

    public void setApparatusName(String apparatusName) {
        this.apparatusName = apparatusName;
    }

    public String getCompetitionType() {
        return competitionType;
    }

    public Apparatus competitionType(String competitionType) {
        this.competitionType = competitionType;
        return this;
    }

    public void setCompetitionType(String competitionType) {
        this.competitionType = competitionType;
    }

    public Set<Score> getApparatusScores() {
        return apparatusScores;
    }

    public Apparatus apparatusScores(Set<Score> scores) {
        this.apparatusScores = scores;
        return this;
    }

    public Apparatus addApparatusScore(Score score) {
        this.apparatusScores.add(score);
        score.setApparatus(this);
        return this;
    }

    public Apparatus removeApparatusScore(Score score) {
        this.apparatusScores.remove(score);
        score.setApparatus(null);
        return this;
    }

    public void setApparatusScores(Set<Score> scores) {
        this.apparatusScores = scores;
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
        Apparatus apparatus = (Apparatus) o;
        if (apparatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apparatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Apparatus{" +
            "id=" + getId() +
            ", apparatusName='" + getApparatusName() + "'" +
            ", competitionType='" + getCompetitionType() + "'" +
            "}";
    }
}
