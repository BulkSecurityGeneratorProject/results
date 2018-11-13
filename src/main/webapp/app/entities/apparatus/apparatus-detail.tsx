import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './apparatus.reducer';
import { IApparatus } from 'app/shared/model/apparatus.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IApparatusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ApparatusDetail extends React.Component<IApparatusDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { apparatusEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Apparatus [<b>{apparatusEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="apparatusName">Apparatus Name</span>
            </dt>
            <dd>{apparatusEntity.apparatusName}</dd>
            <dt>
              <span id="competitionType">Competition Type</span>
            </dt>
            <dd>{apparatusEntity.competitionType}</dd>
          </dl>
          <Button tag={Link} to="/entity/apparatus" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/apparatus/${apparatusEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ apparatus }: IRootState) => ({
  apparatusEntity: apparatus.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ApparatusDetail);
