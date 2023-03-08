import axios from 'axios';
import {Effect} from 'dva-core-ts';
import {Reducer} from 'redux';

const CAROUSEL_URL = 'mock/11/bear/subscription';

export interface ISubscription {
  id: string;
  image: string;
  title: string;
}

export interface SubscriptionModelState {
  subscriptionList: ISubscription[];
  refreshing: boolean;
}

export interface SubscriptionModelType {
  namespace: 'subscription';
  state: SubscriptionModelState;
  effects: {
    fetchList: Effect;
  };
  reducers: {
    setState: Reducer<SubscriptionModelState>;
  };
}

const initialState = {
  subscriptionList: [],
  refreshing: false,
};

/**
 * 关注model
 */
const Subscription: SubscriptionModelType = {
  namespace: 'subscription',
  state: initialState,
  reducers: {
    setState(state, {payload}) {
      return {
        ...state,
        ...payload,
      };
    },
  },
  effects: {
    *fetchList(_, {call, put}) {
      const {data} = yield call(axios.get, CAROUSEL_URL);

      yield put({
        type: 'setState',
        payload: {
          subscriptionList: data,
        },
      });
    },
  },
};

export default Subscription;
