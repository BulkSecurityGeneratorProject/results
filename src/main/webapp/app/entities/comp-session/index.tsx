import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CompSession from './comp-session';
import CompSessionDetail from './comp-session-detail';
import CompSessionUpdate from './comp-session-update';
import CompSessionDeleteDialog from './comp-session-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CompSessionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CompSessionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CompSessionDetail} />
      <ErrorBoundaryRoute path={match.url} component={CompSession} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CompSessionDeleteDialog} />
  </>
);

export default Routes;
