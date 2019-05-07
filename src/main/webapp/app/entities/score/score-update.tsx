import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICompSession } from 'app/shared/model/comp-session.model';
import { getEntities as getCompSessions } from 'app/entities/comp-session/comp-session.reducer';
import { IAthlete } from 'app/shared/model/athlete.model';
import { getEntities as getAthletes } from 'app/entities/athlete/athlete.reducer';
import { IApparatus } from 'app/shared/model/apparatus.model';
import { getEntities as getApparatuses } from 'app/entities/apparatus/apparatus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './score.reducer';
import { IScore } from 'app/shared/model/score.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IScoreUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IScoreUpdateState {
  isNew: boolean;
  sessionId: string;
  athleteId: string;
  apparatusId: string;
}

export class ScoreUpdate extends React.Component<IScoreUpdateProps, IScoreUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      sessionId: '0',
      athleteId: '0',
      apparatusId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCompSessions();
    this.props.getAthletes();
    this.props.getApparatuses();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { scoreEntity } = this.props;
      const entity = {
        ...scoreEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/score');
  };

  render() {
    const { scoreEntity, compSessions, athletes, apparatuses, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="resultsjhApp.score.home.createOrEditLabel">Create or edit a Score</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : scoreEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="score-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="totalLabel" for="total">
                    Total
                  </Label>
                  <AvField id="score-total" type="text" name="total" />
                </AvGroup>
                <AvGroup>
                  <Label id="difficultyLabel" for="difficulty">
                    Difficulty
                  </Label>
                  <AvField id="score-difficulty" type="text" name="difficulty" />
                </AvGroup>
                <AvGroup>
                  <Label id="neutralDeductionsLabel" for="neutralDeductions">
                    Neutral Deductions
                  </Label>
                  <AvField id="score-neutralDeductions" type="text" name="neutralDeductions" />
                </AvGroup>
                <AvGroup>
                  <Label for="session.sessionName">Session</Label>
                  <AvInput id="score-session" type="select" className="form-control" name="session.id">
                    <option value="" key="0" />
                    {compSessions
                      ? compSessions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.sessionName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="athlete.athleteName">Athlete</Label>
                  <AvInput id="score-athlete" type="select" className="form-control" name="athlete.id">
                    <option value="" key="0" />
                    {athletes
                      ? athletes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.athleteName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="apparatus.apparatusName">Apparatus</Label>
                  <AvInput id="score-apparatus" type="select" className="form-control" name="apparatus.id">
                    <option value="" key="0" />
                    {apparatuses
                      ? apparatuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.apparatusName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/score" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  compSessions: storeState.compSession.entities,
  athletes: storeState.athlete.entities,
  apparatuses: storeState.apparatus.entities,
  scoreEntity: storeState.score.entity,
  loading: storeState.score.loading,
  updating: storeState.score.updating,
  updateSuccess: storeState.score.updateSuccess
});

const mapDispatchToProps = {
  getCompSessions,
  getAthletes,
  getApparatuses,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScoreUpdate);
