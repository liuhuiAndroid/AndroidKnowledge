import {Model, Effect} from 'dva-core-ts';
import {Reducer} from 'redux';
import axios from 'axios';

const REQUEST_URL = '/mock/11/bear/guess';

export interface IGuess {
  id: string;
  title: string;
  image: string;
}

export interface GuressState {
  list: IGuess[];
}

interface GuressModel extends Model {
  namespace: 'guress';
  state: GuressState;
  reducers: {
    saveList: Reducer<GuressState>;
  };
  effects: {
    fetchList: Effect;
  };
}

const initialState: GuressState = {
  list: [],
};

const guressModel: GuressModel = {
  namespace: 'guress',
  state: initialState,
  reducers: {
    saveList(state, {payload}) {
      return {
        ...state,
        list: payload,
      };
    },
  },
  effects: {
    *fetchList(_, {call, put}) {
      const {data} = yield call(axios.get, REQUEST_URL);

      yield put({
        type: 'saveList',
        payload: data,
      });
    },
  },
};

export default guressModel;
