import {Model} from 'dva-core-ts';
import {Reducer} from 'redux';

export interface AppState {
  isShowPlay: boolean;
}

interface AppModel extends Model {
  namespace: 'app';
  state: AppState;
  reducers: {
    setIsShowPlay: Reducer<AppState>;
  };
}

const initialState: AppState = {
  isShowPlay: false, // 是否显示播放按钮
};

const appModel: AppModel = {
  namespace: 'app',
  state: initialState,
  reducers: {
    setIsShowPlay(state, {payload}) {
      return {
        ...state,
        isShowPlay: payload,
      };
    },
  },
};

export default appModel;
