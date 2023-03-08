import axios from 'axios';
import {Effect, EffectWithType, Model} from 'dva-core-ts';
import {Reducer} from 'redux';
import {RootState} from '.';
import realm from '@/config/realm';
import {
  sound,
  initPlayer,
  playComplete,
  stop,
  getCurrentTime,
} from '@/config/sound';
const REQUEST_URL = '/mock/11/bear/show';

export interface IShow {
  id: string;
  title: string;
  thumbnailUrl: string;
  soundUrl: string;
}

interface IHistory extends IShow {
  playSeconds: number;
  duration: number;
}

export interface PlayerModelState {
  playState: '' | 'playing' | 'paused' | 'finish'; // 是否显示播放按钮
  playSeconds: number;
  duration: number;
  sliderEditing: boolean;
  percent: number;
  show: IShow;
  previousId: string;
  nextId: string;
  playList: string[];
  currentId: string;
}

export interface PlayerModelType extends Model {
  namespace: 'player';
  state: PlayerModelState;
  reducers: {
    setState: Reducer<PlayerModelState>;
    setShow: Reducer<PlayerModelState>;
  };
  effects: {
    fetchShow: Effect;
    play: Effect;
    timer: EffectWithType;
    next: Effect;
    previous: Effect;
  };
}

const initialState: PlayerModelState = {
  playState: '', // 是否显示播放按钮
  playSeconds: 0,
  duration: 0,
  sliderEditing: false,
  percent: 0,
  show: {
    id: '',
    title: '',
    thumbnailUrl: '',
    soundUrl: '',
  },
  currentId: '',
  previousId: '',
  nextId: '',
  playList: [],
};

/**
 * 播放模块的model
 */
const PlayerModel: PlayerModelType = {
  namespace: 'player',
  state: initialState,
  reducers: {
    setState(state, {payload}) {
      const newState = {
        ...state,
        ...payload,
      };
      const percent = (newState.playSeconds / newState.duration) * 100;
      newState.percent = percent;
      return newState;
    },
    setShow(state, {payload}) {
      const newState = {
        ...state,
        show: payload.show,
      };
      return newState;
    },
  },
  effects: {
    *fetchShow(payload, {call, put, select}) {
      yield put({
        type: 'setState',
        payload: {
          playState: 'paused',
        },
      });
      const {data} = yield call(axios.get, REQUEST_URL);
      // console.log('----------data', data);
      const {show} = yield select(({player}: RootState) => player);
      // console.log('--data', data, show);
      if (data.soundUrl !== show.soundUrl) {
        // 如果已加载，则停止之前播放的音乐，释放资源
        if (sound) {
          yield call(stop);
          sound.stop();
        }
        yield put({
          type: 'setShow',
          payload: {
            show: data,
          },
        });
        // console.log('----------sound', data, sound);
        yield call(initPlayer, data.soundUrl);

        realm.write(() => {
          realm.create('Show', {...data, duration: sound.getDuration()}, true);
        });
      }
      // console.log('---------playState333', playState);
      yield put({
        type: 'play',
      });
    },
    // 播放音频
    *play(_, {call, put, select}) {
      // 暂停视频播放
      const list = yield select(({found}) => found.list);
      list.forEach(item => {
        if (item.playerRef.current) {
          item.playerRef.current.pause();
        }
      });
      yield put({
        type: 'setState',
        payload: {
          playState: 'playing',
          duration: sound.getDuration(),
        },
      });

      yield put({
        type: 'player/timer-start',
      });
      yield call(playComplete);
      yield put({
        type: 'setState',
        payload: {
          playState: 'finish',
          playSeconds: 0,
        },
      });
      yield put({
        type: 'player/timer-end',
      });
    },
    timer: [
      function*(_, {call, put, select}) {
        const {playState} = yield select((state: RootState) => state.player);
        if (playState === 'playing') {
          const seconds = yield call(getCurrentTime);
          yield put({
            type: 'setState',
            payload: {
              playSeconds: seconds,
            },
          });
        }
      },
      {type: 'poll', delay: 1000},
    ],
    *next(payload, {put, call, select}) {
      yield call(stop);
      const {show, playSeconds, playList, currentId} = yield select(
        ({player}: RootState) => player,
      );
      const index = playList.indexOf(currentId);
      let currentIndex = index + 1;
      realm.write(() => {
        realm.create('Show', {...show, playSeconds}, true);
      });

      yield put({
        type: 'setState',
        payload: {
          playState: 'paused',
          previousId: currentId,
          currentId: playList[currentIndex],
          nextId:
            currentIndex + 1 < playList.length
              ? playList[currentIndex + 1]
              : undefined,
        },
      });
      yield put({
        type: 'fetchShow',
        payload,
      });
    },
    *previous(payload, {put, call, select}) {
      yield call(stop);
      const {show, playSeconds, playList, currentId} = yield select(
        ({player}: RootState) => player,
      );
      const index = playList.indexOf(currentId);
      let currentIndex = index - 1;
      realm.write(() => {
        realm.create('Show', {...show, playSeconds}, true);
      });

      yield put({
        type: 'setState',
        payload: {
          playState: 'paused',
          previousId:
            currentIndex - 1 > -1 ? playList[currentIndex - 1] : undefined,
          currentId: playList[currentIndex],
          nextId: currentId,
        },
      });
      yield put({
        type: 'fetchShow',
        payload,
      });
    },
    *pause(_, {put, select}) {
      if (sound) {
        sound.pause();
      }
      const {show, playSeconds} = yield select(({player}: RootState) => player);

      realm.write(() => {
        realm.create('Show', {...show, playSeconds}, true);
      });
      yield put({
        type: 'setState',
        payload: {
          playState: 'paused',
        },
      });
      yield put({
        type: 'player/timer-end',
      });
    },
    *changeCurrentTime(_, {put, select}) {
      if (sound) {
        const {playSeconds} = yield select(({player}: RootState) => player);

        sound.setCurrentTime(playSeconds);
      }
      yield put({
        type: 'player/timer-start',
      });
    },
  },
};

export default PlayerModel;
