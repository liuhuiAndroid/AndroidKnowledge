import {Model, Effect, SubscriptionsMapObject} from 'dva-core-ts';
import {Reducer} from 'redux';
import axios from 'axios';
import storage, {load} from '@/config/storage';
import {RootState} from '.';

const CATEGORY_URL = '/mock/11/bear/category';

export interface ICategory {
  id: string;
  name: string;
  classify?: string;
}

export interface CategoryModelState {
  isEdit: boolean; // 编辑状态
  categorys: ICategory[];
  myCategorys: ICategory[];
}

export interface CategoryModelType extends Model {
  namespace: 'category';
  state: CategoryModelState;
  effects: {
    toggle: Effect;
    loadData: Effect;
  };
  reducers: {
    setState: Reducer<CategoryModelState>;
  };
  subscriptions: SubscriptionsMapObject;
}

const initialState: CategoryModelState = {
  isEdit: false, // 是否处于编辑状态
  categorys: [],
  myCategorys: [
    {
      id: 'home',
      name: '推荐',
    },
    {
      id: 'vip',
      name: 'Vip',
    },
  ],
};

const Category: CategoryModelType = {
  namespace: 'category',
  state: initialState,
  reducers: {
    setState(state = initialState, {payload}) {
      return {
        ...state,
        ...payload,
      };
    },
  },
  effects: {
    *loadData(_, {call, put}) {
      const categorys: ICategory[] = yield call(load, {
        key: 'categorys',
      });

      const myCategorys: ICategory[] = yield call(load, {
        key: 'myCategorys',
      });
      let payload;
      if (myCategorys) {
        payload = {
          myCategorys,
          categorys,
          isEdit: false,
        };
      } else {
        payload = {
          categorys,
          isEdit: false,
        };
      }
      yield put({
        type: 'setState',
        payload,
      });
    },
    *toggle({payload}, {put, select}) {
      const category: CategoryModelState = yield select(
        (state: RootState) => state.category,
      );
      yield put({
        type: 'setState',
        payload: {
          isEdit: !category.isEdit,
          myCategorys: payload.myCategorys,
        },
      });
      if (category.isEdit) {
        storage.save({
          key: 'myCategorys',
          data: payload.myCategorys,
        });
      }
    },
  },
  subscriptions: {
    setup({dispatch}) {
      dispatch({type: 'loadData'});
    },
    asyncStorage() {
      // load categoryList时执行的异步请求
      storage.sync.categorys = async () => {
        const {data} = await axios.get(CATEGORY_URL);
        return data;
      };
      // load myCategoryList时执行的异步请求,其实这里直接写一个固定的数组就可以了，因为选择的类别默认都会有两个推荐和vip
      storage.sync.myCategorys = async () => {
        return null;
      };
    },
  },
};

export default Category;
