import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './athlete.reducer';
import { IAthlete } from 'app/shared/model/athlete.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAthleteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AthleteDetail extends React.Component<IAthleteDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { athleteEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Athlete [<b>{athleteEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="athleteName">Athlete Name</span>
            </dt>
            <dd>{athleteEntity.athleteName}</dd>
            <dt>
              <span id="registrationNumber">Registration Number</span>
            </dt>
            <dd>{athleteEntity.registrationNumber}</dd>
          </dl>
          <Button tag={Link} to="/entity/athlete" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/athlete/${athleteEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ athlete }: IRootState) => ({
  athleteEntity: athlete.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AthleteDetail);
