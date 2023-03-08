import {DvaLoadingState} from 'dva-loading-ts';
import app from './app';
import category from './category';
import home from './home';
import player from './player';
import guress from './guress';
import found from './found';
import album from './album';
import login from './login';
import subscription from './subscription';

const models = [
  app,
  category,
  home,
  player,
  guress,
  found,
  album,
  login,
  subscription,
];

export type RootState = {
  app: typeof app.state;
  category: typeof category.state;
  home: typeof home.state;
  player: typeof player.state;
  guress: typeof guress.state;
  found: typeof found.state;
  album: typeof album.state;
  login: typeof login.state;
  subscription: typeof subscription.state;
  loading: DvaLoadingState;
} & {
  [key: string]: typeof home.state;
};

export default models;
