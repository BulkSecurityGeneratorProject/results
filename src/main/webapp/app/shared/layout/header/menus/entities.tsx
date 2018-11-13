import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/score">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Score
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/comp-session">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Comp Session
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/athlete">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Athlete
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/apparatus">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Apparatus
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
