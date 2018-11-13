import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './score.reducer';
import { IScore } from 'app/shared/model/score.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IScoreProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IScoreState = IPaginationBaseState;

export class Score extends React.Component<IScoreProps, IScoreState> {
  state: IScoreState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { scoreList, match } = this.props;
    return (
      <div>
        <h2 id="score-heading">
          Scores
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Score
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('total')}>
                    Total <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('difficulty')}>
                    Difficulty <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('neutralDeductions')}>
                    Neutral Deductions <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Session <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Athlete <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Apparatus <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {scoreList.map((score, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${score.id}`} color="link" size="sm">
                        {score.id}
                      </Button>
                    </td>
                    <td>{score.total}</td>
                    <td>{score.difficulty}</td>
                    <td>{score.neutralDeductions}</td>
                    <td>{score.session ? <Link to={`comp-session/${score.session.id}`}>{score.session.sessionName}</Link> : ''}</td>
                    <td>{score.athlete ? <Link to={`athlete/${score.athlete.id}`}>{score.athlete.athleteName}</Link> : ''}</td>
                    <td>{score.apparatus ? <Link to={`apparatus/${score.apparatus.id}`}>{score.apparatus.apparatusName}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${score.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${score.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${score.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ score }: IRootState) => ({
  scoreList: score.entities,
  totalItems: score.totalItems,
  links: score.links,
  entity: score.entity,
  updateSuccess: score.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Score);
