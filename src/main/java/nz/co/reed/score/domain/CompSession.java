package nz.co.reed.score.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CompSession.
 */
@Entity
@Table(name = "comp_session")
public class CompSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "jhi_level")
    private String level;

    @OneToMany(mappedBy = "session")
    private Set<Score> sessionScores = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public CompSession sessionName(String sessionName) {
        this.sessionName = sessionName;
        return this;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getLevel() {
        return level;
    }

    public CompSession level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<Score> getSessionScores() {
        return sessionScores;
    }

    public CompSession sessionScores(Set<Score> scores) {
        this.sessionScores = scores;
        return this;
    }

    public CompSession addSessionScores(Score score) {
        this.sessionScores.add(score);
        score.setSession(this);
        return this;
    }

    public CompSession removeSessionScores(Score score) {
        this.sessionScores.remove(score);
        score.setSession(null);
        return this;
    }

    public void setSessionScores(Set<Score> scores) {
        this.sessionScores = scores;
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
        CompSession compSession = (CompSession) o;
        if (compSession.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compSession.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompSession{" +
            "id=" + getId() +
            ", sessionName='" + getSessionName() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
