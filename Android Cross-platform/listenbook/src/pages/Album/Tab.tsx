import React from 'react';
import {
  StyleSheet,
  Dimensions,
  NativeSyntheticEvent,
  NativeScrollEvent,
  Platform,
} from 'react-native';
import {
  TabView,
  TabBar,
  NavigationState,
  SceneRendererProps,
} from 'react-native-tab-view';
import {
  TapGestureHandler,
  PanGestureHandler,
  NativeViewGestureHandler,
} from 'react-native-gesture-handler';
import {RootStackParamList} from '@/navigator/index';
import Introduction from './Introduction';
import List from './List';
import {RouteProp} from '@react-navigation/native';
import {IAlbum} from '@/models/album';

const initialLayout = {width: Dimensions.get('window').width};

interface IProps {
  onItemPress: (item: IAlbum, index: number) => void;
  route: RouteProp<RootStackParamList, 'Album'>;
  nativeRef: React.RefObject<NativeViewGestureHandler>;
  tapRef: React.RefObject<TapGestureHandler>;
  panRef: React.RefObject<PanGestureHandler>;
  onScrollBeginDrag: (event: NativeSyntheticEvent<NativeScrollEvent>) => void;
}

interface IRoute {
  key: string;
  title: string;
}

type IState = NavigationState<IRoute>;

class Tab extends React.PureComponent<IProps, IState> {
  state = {
    index: 1,
    routes: [
      {key: 'introduction', title: '简介'},
      {key: 'albums', title: '节目'},
    ],
  };

  onIndexChange = (index: number) => {
    this.setState({index});
  };

  renderTabBar = (props: SceneRendererProps & {navigationState: IState}) => (
    <TabBar
      {...props}
      scrollEnabled
      indicatorStyle={styles.indicator}
      style={styles.tabbar}
      labelStyle={styles.label}
      tabStyle={styles.tabStyle}
    />
  );

  renderScene = ({route}: {route: IRoute}) => {
    const {
      nativeRef,
      panRef,
      tapRef,
      onItemPress,
      onScrollBeginDrag,
    } = this.props;
    switch (route.key) {
      case 'introduction':
        return <Introduction />;
      case 'albums':
        return (
          <List
            nativeRef={nativeRef}
            panRef={panRef}
            tapRef={tapRef}
            onItemPress={onItemPress}
            onScrollBeginDrag={onScrollBeginDrag}
          />
        );
    }
  };

  render() {
    return (
      <TabView
        navigationState={this.state}
        renderScene={this.renderScene}
        renderTabBar={this.renderTabBar}
        onIndexChange={this.onIndexChange}
        initialLayout={initialLayout}
        // style={{marginTop: -1}}
      />
    );
  }
}

const styles = StyleSheet.create({
  scene: {
    flex: 1,
  },
  tabbar: {
    backgroundColor: '#fff',

    ...Platform.select({
      android: {
        elevation: 0,
        borderBottomWidth: StyleSheet.hairlineWidth,
        borderBottomColor: '#e3e3e3',
      },
    }),
  },
  indicator: {
    backgroundColor: '#eb6d48',
    borderColor: '#fff',
    borderLeftWidth: 15,
    borderRightWidth: 15,
    // height: 4,
    // borderRadius: 2,
  },
  label: {
    // fontWeight: '400',
    color: '#333',
  },
  tabStyle: {
    width: 'auto',
  },
});

export default Tab;
