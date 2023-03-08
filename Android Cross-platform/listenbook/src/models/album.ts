import {Model, Effect} from 'dva-core-ts';
import {Reducer} from 'redux';
import axios from 'axios';

const REQUEST_URL = '/mock/11/bear/album/list';

export interface IAlbum {
  id: string;
  title: string;
  playVolume: number;
  duration: number;
  date: string;
}

export interface IAuthor {
  id: string;
  name: string;
  attention: string;
  avatar: string;
}

export interface AlbumModelState {
  list: IAlbum[];
  author: IAuthor;
  id: string;
  title: string;
  summary: string;
  thumbnailUrl: string;
  introduction: string;
}

export interface AlbumModelType extends Model {
  namespace: 'album';
  state: AlbumModelState;
  effects: {
    fetchList: Effect;
  };
  reducers: {
    setState: Reducer<AlbumModelState>;
  };
}

const initialState: AlbumModelState = {
  id: '',
  thumbnailUrl: '',
  title: '',
  summary: '',
  introduction: '',
  list: [],
  author: {
    id: '',
    name: '',
    attention: '',
    avatar: '',
  },
};

const AlbumModel: AlbumModelType = {
  namespace: 'album',
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
      const {data} = yield call(axios.get, REQUEST_URL);
      yield put({
        type: 'setState',
        payload: data,
      });
    },
  },
};

export default AlbumModel;
