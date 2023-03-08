import React from 'react';
import {
  Platform,
  StyleSheet,
  Animated,
  ViewStyle,
  StyleProp,
} from 'react-native';
import {
  NavigationContainer,
  RouteProp,
  NavigationState,
  DefaultTheme,
} from '@react-navigation/native';
import {
  createStackNavigator,
  StackNavigationProp,
  HeaderStyleInterpolators,
  CardStyleInterpolators,
  TransitionPresets,
} from '@react-navigation/stack';
import BottomTabs from './BottomTabs';
import Category from '@/pages/Category';
import ProgramDetail from '@/pages/ProgramDetail';
import Album from '@/pages/Album';
import History from '@/pages/History';
import Login from '@/pages/Login';
import {
  statusBarHeight,
  navigationRef,
  findRouteNameFromNavigatorState,
} from '@/utils/index';
import HeaderBackImage from '@/components/HeaderBackImage';
import PlayView from '@/pages/views/PlayView';

const MyTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    primary: '#333',
    background: '#fff',
  },
};

export type RootStackParamList = {
  BottomTabs: {
    screen?: string;
  };
  Category: undefined;

  Album: {
    item: {
      id: string;
      title: string;
      image: string;
    };
    opacity?: Animated.AnimatedInterpolation;
  };
  History: undefined;
};

export type RootStackNavigation = StackNavigationProp<RootStackParamList>;

const RootStack = createStackNavigator<RootStackParamList>();

export type ModalStackParamList = {
  Root: undefined;
  ProgramDetail: {
    id?: string;
    previousId?: string;
    nextId?: string;
  };
  Login: undefined;
};

export type ModalStackNavigation = StackNavigationProp<ModalStackParamList>;

const ModalStack = createStackNavigator<ModalStackParamList>();

function ModalStackScreen() {
  return (
    <ModalStack.Navigator
      mode="modal"
      headerMode="screen"
      screenOptions={() => ({
        ...TransitionPresets.ModalSlideFromBottomIOS,
        cardOverlayEnabled: true,
        gestureEnabled: true,
        headerTitleAlign: 'center',
        headerStatusBarHeight: statusBarHeight,
        headerBackTitleVisible: false,
      })}>
      <ModalStack.Screen
        name="Root"
        component={RootStackScreen}
        options={{headerShown: false}}
      />
      <ModalStack.Screen
        name="ProgramDetail"
        component={ProgramDetail}
        options={{
          headerTransparent: true,
          headerTitle: '',
          headerTintColor: '#fff',
          headerBackImage: () => (
            <HeaderBackImage color="#fff" name="icon-down" />
          ),
        }}
      />
      <ModalStack.Screen
        name="Login"
        component={Login}
        options={{
          headerTitle: '登录',
        }}
      />
    </ModalStack.Navigator>
  );
}

function getAlbumOptions({
  route,
}: {
  route: RouteProp<RootStackParamList, 'Album'>;
}) {
  return {
    headerTransparent: true,
    headerTitleStyle: {
      opacity: route.params.opacity,
    },
    headerTitle: route.params.item.title,
    headerBackground: () => (
      <Animated.View
        style={StyleSheet.flatten([
          styles.headerBackground,
          {
            opacity: route.params.opacity,
          },
        ])}
      />
    ),
  };
}

const styles = StyleSheet.create({
  headerBackground: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

function RootStackScreen() {
  return (
    <RootStack.Navigator
      headerMode="float"
      screenOptions={{
        headerTitleAlign: 'center',
        headerBackTitleVisible: false,
        headerStyleInterpolator: HeaderStyleInterpolators.forUIKit,
        cardStyleInterpolator: CardStyleInterpolators.forHorizontalIOS,
        gestureEnabled: true,
        gestureDirection: 'horizontal',
        headerStatusBarHeight: statusBarHeight,
        headerStyle: {
          ...Platform.select({
            android: {
              elevation: 0,
              borderBottomWidth: StyleSheet.hairlineWidth,
            },
          }),
        },
      }}>
      <RootStack.Screen name="BottomTabs" component={BottomTabs} />
      <RootStack.Screen
        name="Category"
        component={Category}
        options={{
          headerTitle: '分类',
        }}
      />
      <RootStack.Screen
        name="Album"
        component={Album}
        options={getAlbumOptions}
      />
      <RootStack.Screen
        name="History"
        component={History}
        options={{
          headerTitle: '历史记录',
        }}
      />
    </RootStack.Navigator>
  );
}

interface IState {
  navigationState: NavigationState | undefined;
}

class Navigator extends React.Component<any, IState> {
  state = {
    navigationState: undefined,
  };

  onStateChange = (state: NavigationState | undefined) => {
    this.setState({
      navigationState: state,
    });
  };

  render() {
    let activeScreenName = '';
    const {navigationState} = this.state;
    // console.log('---navigationState', navigationState);
    if (navigationState !== undefined) {
      activeScreenName = findRouteNameFromNavigatorState(navigationState);
    }

    return (
      <NavigationContainer
        theme={MyTheme}
        onStateChange={this.onStateChange}
        ref={navigationRef}>
        <ModalStackScreen />
        <PlayView activeScreenName={activeScreenName} />
      </NavigationContainer>
    );
  }
}

export default Navigator;
