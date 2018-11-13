import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAthlete, defaultValue } from 'app/shared/model/athlete.model';

export const ACTION_TYPES = {
  FETCH_ATHLETE_LIST: 'athlete/FETCH_ATHLETE_LIST',
  FETCH_ATHLETE: 'athlete/FETCH_ATHLETE',
  CREATE_ATHLETE: 'athlete/CREATE_ATHLETE',
  UPDATE_ATHLETE: 'athlete/UPDATE_ATHLETE',
  DELETE_ATHLETE: 'athlete/DELETE_ATHLETE',
  RESET: 'athlete/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAthlete>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AthleteState = Readonly<typeof initialState>;

// Reducer

export default (state: AthleteState = initialState, action): AthleteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ATHLETE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ATHLETE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ATHLETE):
    case REQUEST(ACTION_TYPES.UPDATE_ATHLETE):
    case REQUEST(ACTION_TYPES.DELETE_ATHLETE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ATHLETE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ATHLETE):
    case FAILURE(ACTION_TYPES.CREATE_ATHLETE):
    case FAILURE(ACTION_TYPES.UPDATE_ATHLETE):
    case FAILURE(ACTION_TYPES.DELETE_ATHLETE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ATHLETE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ATHLETE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ATHLETE):
    case SUCCESS(ACTION_TYPES.UPDATE_ATHLETE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ATHLETE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/athletes';

// Actions

export const getEntities: ICrudGetAllAction<IAthlete> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ATHLETE_LIST,
    payload: axios.get<IAthlete>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAthlete> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ATHLETE,
    payload: axios.get<IAthlete>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAthlete> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ATHLETE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IAthlete> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ATHLETE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAthlete> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ATHLETE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
