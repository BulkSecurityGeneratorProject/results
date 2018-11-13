import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Score from './score';
import CompSession from './comp-session';
import Athlete from './athlete';
import Apparatus from './apparatus';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/score`} component={Score} />
      <ErrorBoundaryRoute path={`${match.url}/comp-session`} component={CompSession} />
      <ErrorBoundaryRoute path={`${match.url}/athlete`} component={Athlete} />
      <ErrorBoundaryRoute path={`${match.url}/apparatus`} component={Apparatus} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
