import React from 'react';
import {
  createBottomTabNavigator,
  BottomTabNavigationProp,
} from '@react-navigation/bottom-tabs';
import HomeTabs from './HomeTabs';
import Listen from '@/pages/Listen';
import Found from '@/pages/Found';
import Account from '@/pages/Account';
import Play from '@/pages/views/Play';
import Icon from '@/assets/iconfont/index';
import {RootStackNavigation, RootStackParamList} from '.';
import {RouteProp, TabNavigationState} from '@react-navigation/native';
import {BottomTabBarButtonProps} from '@react-navigation/bottom-tabs/lib/typescript/src/types';

export type BottomTabParamList = {
  HomeTabs: undefined;
  Listen: undefined;
  Found: undefined;
  Account: undefined;
};

export type BottomTabNavigation = BottomTabNavigationProp<BottomTabParamList>;

const Tab = createBottomTabNavigator();

function getHeaderTitle(routeName: string | undefined) {
  switch (routeName) {
    case 'HomeTabs':
      return '';
    case 'Listen':
      return '我听';
    case 'Found':
      return '发现';
    case 'Account':
      return '账号';
    default:
      return '';
  }
}

interface IProps {
  navigation: RootStackNavigation;
  route: RouteProp<RootStackParamList, 'BottomTabs'> & {
    state?: TabNavigationState;
  };
}

class BottomTabs extends React.Component<IProps> {
  componentDidMount() {
    this.setOptions();
  }

  componentDidUpdate() {
    this.setOptions();
  }

  setOptions = () => {
    const {navigation, route} = this.props;
    const routeName = route.state
      ? route.state.routes[route.state.index].name
      : route.params
      ? route.params.screen
      : 'HomeTabs';
    if (routeName === 'HomeTabs') {
      navigation.setOptions({
        headerTitle: '',
        headerTransparent: true,
      });
    } else {
      navigation.setOptions({
        headerTitle: getHeaderTitle(routeName),
        headerTransparent: false,
      });
    }
  };

  render() {
    return (
      <Tab.Navigator
        tabBarOptions={{
          activeTintColor: '#f86442',
        }}>
        <Tab.Screen
          name="HomeTabs"
          component={HomeTabs}
          options={{
            tabBarLabel: '首页',
            tabBarIcon: ({color, size}) => (
              <Icon name="icon-shouye" color={color} size={size} />
            ),
          }}
        />
        <Tab.Screen
          name="Listen"
          component={Listen}
          options={{
            tabBarLabel: '我听',
            tabBarIcon: ({color, size}) => (
              <Icon name="icon-shoucang" color={color} size={size} />
            ),
          }}
        />
        <Tab.Screen
          name="Pay"
          component={Play}
          options={({navigation}) => ({
            tabBarButton: (props: BottomTabBarButtonProps) => {
              delete props.onPress;
              return (
                <Play
                  {...props}
                  onPress={() => navigation.navigate('ProgramDetail')}
                />
              );
            },
          })}
        />
        <Tab.Screen
          name="Found"
          component={Found}
          options={{
            tabBarLabel: '发现',
            tabBarIcon: ({color, size}) => (
              <Icon name="icon-faxian" color={color} size={size} />
            ),
          }}
        />
        <Tab.Screen
          name="Account"
          component={Account}
          options={{
            tabBarLabel: '账号',
            tabBarIcon: ({color, size}) => (
              <Icon name="icon-user" color={color} size={size} />
            ),
          }}
        />
      </Tab.Navigator>
    );
  }
}

export default BottomTabs;
