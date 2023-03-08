import React from 'react';
import {
  createMaterialTopTabNavigator,
  MaterialTopTabNavigationProp,
} from '@react-navigation/material-top-tabs';
import TopTabBarWrapper from '@/pages/views/TopTabBarWrapper';
import Home from '@/pages/Home';
import {RootState} from '@/models/index';
import {connect, ConnectedProps} from 'react-redux';
import {createModel} from '@/config/dva';
import {StyleSheet} from 'react-native';

export type HomeTabParamList = {
  [key: string]: {
    modelNamespace: string;
    category: string;
  };
};

export type HomeTabNavigation = MaterialTopTabNavigationProp<HomeTabParamList>;

const Tab = createMaterialTopTabNavigator<HomeTabParamList>();

const mapStateToProps = ({category}: RootState) => {
  return {
    myCategorys: category.myCategorys,
  };
};

const connector = connect(mapStateToProps);

type IProps = ConnectedProps<typeof connector>;

function HomeTabs({myCategorys}: IProps) {
  return (
    <Tab.Navigator
      tabBar={props => <TopTabBarWrapper {...props} />}
      lazy
      sceneContainerStyle={styles.sceneContainer}
      tabBarOptions={{
        scrollEnabled: true,
        tabStyle: {
          padding: 0,
          width: 80,
        },
        indicatorStyle: {
          height: 4,
          width: 20,
          marginLeft: 30,
          borderRadius: 2,
          backgroundColor: '#fff',
        },
        activeTintColor: '#fff',
        inactiveTintColor: '#fff',
      }}>
      {myCategorys.map(category => {
        createModel('tab-' + category.id);
        return (
          <Tab.Screen
            key={category.id}
            name={'tab-' + category.id}
            component={Home}
            options={{
              tabBarLabel: category.name,
            }}
            initialParams={{
              modelNamespace: 'tab-' + category.id,
              category: category.id,
            }}
          />
        );
      })}
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  sceneContainer: {
    backgroundColor: 'transparent',
  },
});

export default connector(HomeTabs);
