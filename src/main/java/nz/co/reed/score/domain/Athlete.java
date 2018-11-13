package nz.co.reed.score.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Athlete.
 */
@Entity
@Table(name = "athlete")
public class Athlete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "athlete_name")
    private String athleteName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @OneToMany(mappedBy = "athlete")
    private Set<Score> athleteScores = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public Athlete athleteName(String athleteName) {
        this.athleteName = athleteName;
        return this;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Athlete registrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Set<Score> getAthleteScores() {
        return athleteScores;
    }

    public Athlete athleteScores(Set<Score> scores) {
        this.athleteScores = scores;
        return this;
    }

    public Athlete addAthleteScore(Score score) {
        this.athleteScores.add(score);
        score.setAthlete(this);
        return this;
    }

    public Athlete removeAthleteScore(Score score) {
        this.athleteScores.remove(score);
        score.setAthlete(null);
        return this;
    }

    public void setAthleteScores(Set<Score> scores) {
        this.athleteScores = scores;
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
        Athlete athlete = (Athlete) o;
        if (athlete.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), athlete.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Athlete{" +
            "id=" + getId() +
            ", athleteName='" + getAthleteName() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            "}";
    }
}
