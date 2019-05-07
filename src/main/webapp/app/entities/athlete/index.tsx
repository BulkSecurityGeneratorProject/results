import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Athlete from './athlete';
import AthleteDetail from './athlete-detail';
import AthleteUpdate from './athlete-update';
import AthleteDeleteDialog from './athlete-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AthleteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AthleteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AthleteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Athlete} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AthleteDeleteDialog} />
  </>
);

export default Routes;
