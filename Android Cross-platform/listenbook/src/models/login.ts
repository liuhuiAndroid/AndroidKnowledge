import axios from 'axios';
import {Model, Effect, SubscriptionsMapObject} from 'dva-core-ts';
import {Reducer} from 'redux';
import storage, {load} from '@/config/storage';

const REQUEST_URL = '/mock/11/bear/login';

export interface IUser {
  name: string;
  avatar: string;
}

export interface LoginModelState {
  loging: boolean;
  user?: IUser;
}

export interface LoginModelType extends Model {
  namespace: 'login';
  state: LoginModelState;
  effects: {
    login: Effect;
    logout: Effect;
    loadStorage: Effect;
  };
  reducers: {
    setState: Reducer<LoginModelState>;
  };
  subscriptions: SubscriptionsMapObject;
}

const initialState = {
  loging: false,
  user: undefined,
};

/**
 * 登录模块的model
 */
const PlayerModel: LoginModelType = {
  namespace: 'login',
  state: initialState,
  reducers: {
    setState(state, {payload}) {
      const newState = {
        ...state,
        ...payload,
      };

      return newState;
    },
  },
  effects: {
    *loadStorage(_, {call, put}) {
      const user = yield call(load, {key: 'user'});
      yield put({
        type: 'setState',
        payload: {
          loging: true,
          user: user,
        },
      });
    },
    *login({payload, callback}, {call, put}) {
      const {data} = yield call(axios.post, REQUEST_URL, payload);
      yield put({
        type: 'setState',
        payload: {
          loging: true,
          user: data,
        },
      });
      storage.save({
        key: 'user', // 注意:请不要在key中使用_下划线符号!
        data: data,
      });
      if (callback) {
        callback();
      }
    },
    *logout(_, {put}) {
      yield put({
        type: 'setState',
        payload: {
          loging: false,
          user: undefined,
        },
      });
      storage.save({
        key: 'user', // 注意:请不要在key中使用_下划线符号!
        data: null,
      });
    },
  },
  subscriptions: {
    setup({dispatch}) {
      dispatch({type: 'loadStorage'});
    },
  },
};

export default PlayerModel;
