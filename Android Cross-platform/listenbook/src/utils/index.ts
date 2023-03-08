import React from 'react';
import {Dimensions, StatusBar} from 'react-native';
import {ModalStackParamList, ModalStackNavigation} from '../navigator';
import {NavigationState} from '@react-navigation/native';

const {width: viewportWidth, height: viewportHeight} = Dimensions.get('window');

function getTimeString(seconds: number) {
  const m = parseInt((seconds % (60 * 60)) / 60 + '', 10);
  const s = parseInt((seconds % 60) + '', 10);

  return (m < 10 ? '0' + m : m) + ':' + (s < 10 ? '0' + s : s);
}

// 根据当前状态查找当前处于焦点的页面名字
function findRouteNameFromNavigatorState({routes, index}: NavigationState) {
  let route = routes[index];
  while (route.state) {
    route = route.state.routes[route.state.index];
  }
  return route.name;
}

const statusBarHeight = StatusBar.currentHeight;

const navigationRef = React.createRef<ModalStackNavigation>();

function navigate(name: keyof ModalStackParamList, params?: any) {
  if (navigationRef.current) {
    navigationRef.current.navigate(name, params);
  }
}

function back() {
  if (navigationRef.current) {
    navigationRef.current.goBack();
  }
}

export {
  viewportWidth,
  viewportHeight,
  findRouteNameFromNavigatorState,
  getTimeString,
  statusBarHeight,
  navigationRef,
  navigate,
  back,
};
