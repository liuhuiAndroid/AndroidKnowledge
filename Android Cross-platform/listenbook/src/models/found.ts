import React from 'react';
import {Model, Effect} from 'dva-core-ts';
import {Reducer} from 'redux';
import axios from 'axios';
import {RootState} from './index';
import VideoPlayer from 'react-native-video-player';

const REQUEST_URL = '/mock/11/bear/found/list';

export interface IFound {
  id: string;
  thumbnailUrl: string;
  videoUrl: string;
  forward: string;
  comment: number;
  like: number;
  backgroundColor: string;
  user: {
    id: string;
    name: string;
    avatar: string;
  };
  playerRef: React.RefObject<VideoPlayer>;
}

export interface FoundState {
  list: IFound[];
  refreshing: boolean;
  paused: boolean;
}

/**
 * 发现模块的model
 */
interface FoundModel extends Model {
  namespace: 'found';
  state: FoundState;
  reducers: {
    setState: Reducer<FoundState>;
  };
  effects: {
    fetchList: Effect;
    startPlay: Effect;
  };
}

const initialState: FoundState = {
  list: [],
  refreshing: false,
  paused: true,
};

const foundModel: FoundModel = {
  namespace: 'found',
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
    *fetchList({payload}, {call, put, select}) {
      const {refreshing} = payload;
      // alert(refreshing);
      yield put({
        type: 'setState',
        payload: {
          refreshing,
        },
      });
      let {data}: {data: IFound[]} = yield call(axios.get, REQUEST_URL);
      data = data.map(item => ({
        ...item,
        playerRef: React.createRef<VideoPlayer>(),
      }));
      const list = yield select(({found}: RootState) => found.list);
      const newList = refreshing ? data : list.concat(data);
      yield put({
        type: 'setState',
        payload: {
          list: newList,
          refreshing: false,
        },
      });
    },
    *startPlay({payload}, {put, select}) {
      const list: IFound[] = yield select(({found}: RootState) => found.list);
      list.forEach(item => {
        if (item.id !== payload.currentItem.id && item.playerRef.current) {
          item.playerRef.current.pause();
        }
      });
      yield put({
        type: 'player/pause',
      });
    },
  },
};

export default foundModel;
