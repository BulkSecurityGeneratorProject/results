import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './comp-session.reducer';
import { ICompSession } from 'app/shared/model/comp-session.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompSessionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CompSessionDetail extends React.Component<ICompSessionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { compSessionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            CompSession [<b>{compSessionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="sessionName">Session Name</span>
            </dt>
            <dd>{compSessionEntity.sessionName}</dd>
            <dt>
              <span id="level">Level</span>
            </dt>
            <dd>{compSessionEntity.level}</dd>
          </dl>
          <Button tag={Link} to="/entity/comp-session" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/comp-session/${compSessionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ compSession }: IRootState) => ({
  compSessionEntity: compSession.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompSessionDetail);
