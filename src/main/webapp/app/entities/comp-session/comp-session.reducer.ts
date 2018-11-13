import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICompSession, defaultValue } from 'app/shared/model/comp-session.model';

export const ACTION_TYPES = {
  FETCH_COMPSESSION_LIST: 'compSession/FETCH_COMPSESSION_LIST',
  FETCH_COMPSESSION: 'compSession/FETCH_COMPSESSION',
  CREATE_COMPSESSION: 'compSession/CREATE_COMPSESSION',
  UPDATE_COMPSESSION: 'compSession/UPDATE_COMPSESSION',
  DELETE_COMPSESSION: 'compSession/DELETE_COMPSESSION',
  RESET: 'compSession/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICompSession>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CompSessionState = Readonly<typeof initialState>;

// Reducer

export default (state: CompSessionState = initialState, action): CompSessionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COMPSESSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COMPSESSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COMPSESSION):
    case REQUEST(ACTION_TYPES.UPDATE_COMPSESSION):
    case REQUEST(ACTION_TYPES.DELETE_COMPSESSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_COMPSESSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COMPSESSION):
    case FAILURE(ACTION_TYPES.CREATE_COMPSESSION):
    case FAILURE(ACTION_TYPES.UPDATE_COMPSESSION):
    case FAILURE(ACTION_TYPES.DELETE_COMPSESSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPSESSION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPSESSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COMPSESSION):
    case SUCCESS(ACTION_TYPES.UPDATE_COMPSESSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COMPSESSION):
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

const apiUrl = 'api/comp-sessions';

// Actions

export const getEntities: ICrudGetAllAction<ICompSession> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_COMPSESSION_LIST,
  payload: axios.get<ICompSession>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICompSession> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COMPSESSION,
    payload: axios.get<ICompSession>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICompSession> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COMPSESSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICompSession> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COMPSESSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICompSession> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COMPSESSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
