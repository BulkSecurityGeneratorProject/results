import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IApparatus, defaultValue } from 'app/shared/model/apparatus.model';

export const ACTION_TYPES = {
  FETCH_APPARATUS_LIST: 'apparatus/FETCH_APPARATUS_LIST',
  FETCH_APPARATUS: 'apparatus/FETCH_APPARATUS',
  CREATE_APPARATUS: 'apparatus/CREATE_APPARATUS',
  UPDATE_APPARATUS: 'apparatus/UPDATE_APPARATUS',
  DELETE_APPARATUS: 'apparatus/DELETE_APPARATUS',
  RESET: 'apparatus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IApparatus>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ApparatusState = Readonly<typeof initialState>;

// Reducer

export default (state: ApparatusState = initialState, action): ApparatusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPARATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPARATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_APPARATUS):
    case REQUEST(ACTION_TYPES.UPDATE_APPARATUS):
    case REQUEST(ACTION_TYPES.DELETE_APPARATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_APPARATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPARATUS):
    case FAILURE(ACTION_TYPES.CREATE_APPARATUS):
    case FAILURE(ACTION_TYPES.UPDATE_APPARATUS):
    case FAILURE(ACTION_TYPES.DELETE_APPARATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPARATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPARATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPARATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_APPARATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPARATUS):
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

const apiUrl = 'api/apparatuses';

// Actions

export const getEntities: ICrudGetAllAction<IApparatus> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_APPARATUS_LIST,
  payload: axios.get<IApparatus>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IApparatus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPARATUS,
    payload: axios.get<IApparatus>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IApparatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPARATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IApparatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPARATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IApparatus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPARATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
