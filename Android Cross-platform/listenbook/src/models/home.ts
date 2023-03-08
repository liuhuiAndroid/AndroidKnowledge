import axios from 'axios';
import {Effect} from 'dva-core-ts';
import {Reducer} from 'redux';
import {RootState} from '.';

const CAROUSEL_URL = '/mock/11/bear/carousel';
const CHANNEL_URL = '/mock/11/bear/channel';
export interface ICarousel {
  id: number;
  image: string;
  colors: [string, string];
}

export interface IChannel {
  id: string;
  image: string;
  title: string;
  played: number;
  playing: number;
  remark: string;
}

export interface HomeModelState {
  activeSlide: number; // 是否显示播放按钮
  carouselList: ICarousel[];
  channelList: IChannel[];
  refreshing: boolean;
  gradientVisible: boolean;
  info: {
    page: number;
    results: number;
  };
}

export interface HomeModelType {
  namespace: string;
  state: HomeModelState;
  effects: {
    fetchCarouselList: Effect;
    fetchChannelList: Effect;
  };
  reducers: {
    setState: Reducer<HomeModelState>;
  };
}

const initialState: HomeModelState = {
  carouselList: [],
  channelList: [],
  activeSlide: 0,
  gradientVisible: true,
  refreshing: false,
  info: {
    page: 0,
    results: 10,
  },
};

/**
 * 首页模块的model
 */
const Home: HomeModelType = {
  namespace: 'home',
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
    // 获取轮播图数据列表
    *fetchCarouselList(_, {call, put}) {
      const {data} = yield call(axios.get, CAROUSEL_URL);

      yield put({
        type: 'setState',
        payload: {
          carouselList: data,
        },
      });
    },
    *fetchChannelList(action, {call, put, select}) {
      // console.log('---action', action);
      const {payload, type} = action;
      const {refreshing} = payload;

      yield put({
        type: 'setState',
        payload: {
          refreshing,
        },
      });
      const namespace = type.split('/')[0];
      const {channelList: list, info} = yield select(
        (state: RootState) => state[namespace],
      );
      const page = refreshing ? 0 : info.page;
      const {data} = yield call(axios.get, CHANNEL_URL, {
        params: {category: payload.category, page},
      });

      const newList = refreshing ? data.results : list.concat(data.results);
      yield put({
        type: 'setState',
        payload: {
          channelList: newList,
          refreshing: false,
          info: data.info,
        },
      });

      if (action.callback) {
        action.callback();
      }
    },
  },
};

export default Home;
