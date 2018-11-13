import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Apparatus from './apparatus';
import ApparatusDetail from './apparatus-detail';
import ApparatusUpdate from './apparatus-update';
import ApparatusDeleteDialog from './apparatus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApparatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApparatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApparatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={Apparatus} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ApparatusDeleteDialog} />
  </>
);

export default Routes;
