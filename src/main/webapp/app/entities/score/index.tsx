import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Score from './score';
import ScoreDetail from './score-detail';
import ScoreUpdate from './score-update';
import ScoreDeleteDialog from './score-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ScoreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ScoreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ScoreDetail} />
      <ErrorBoundaryRoute path={match.url} component={Score} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ScoreDeleteDialog} />
  </>
);

export default Routes;
