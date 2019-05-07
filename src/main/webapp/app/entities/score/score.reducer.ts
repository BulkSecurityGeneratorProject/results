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

import { IScore, defaultValue } from 'app/shared/model/score.model';

export const ACTION_TYPES = {
  FETCH_SCORE_LIST: 'score/FETCH_SCORE_LIST',
  FETCH_SCORE: 'score/FETCH_SCORE',
  CREATE_SCORE: 'score/CREATE_SCORE',
  UPDATE_SCORE: 'score/UPDATE_SCORE',
  DELETE_SCORE: 'score/DELETE_SCORE',
  RESET: 'score/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IScore>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ScoreState = Readonly<typeof initialState>;

// Reducer

export default (state: ScoreState = initialState, action): ScoreState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SCORE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCORE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SCORE):
    case REQUEST(ACTION_TYPES.UPDATE_SCORE):
    case REQUEST(ACTION_TYPES.DELETE_SCORE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SCORE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCORE):
    case FAILURE(ACTION_TYPES.CREATE_SCORE):
    case FAILURE(ACTION_TYPES.UPDATE_SCORE):
    case FAILURE(ACTION_TYPES.DELETE_SCORE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCORE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCORE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCORE):
    case SUCCESS(ACTION_TYPES.UPDATE_SCORE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCORE):
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

const apiUrl = 'api/scores';

// Actions

export const getEntities: ICrudGetAllAction<IScore> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCORE_LIST,
    payload: axios.get<IScore>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IScore> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCORE,
    payload: axios.get<IScore>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IScore> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCORE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IScore> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCORE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IScore> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCORE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
