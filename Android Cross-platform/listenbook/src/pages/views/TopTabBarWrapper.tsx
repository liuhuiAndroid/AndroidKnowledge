import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {getStatusBarHeight} from 'react-native-iphone-x-helper';
import Touchable from '@/components/Touchable';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import {findRouteNameFromNavigatorState} from '@/utils/index';
import LinearAnimatedGradientTransition from 'react-native-linear-animated-gradient-transition';
import {
  MaterialTopTabBarProps,
  MaterialTopTabBar,
} from '@react-navigation/material-top-tabs';

const mapStateToProps = (state: RootState, props: MaterialTopTabBarProps) => {
  const activeRouteName = findRouteNameFromNavigatorState(props.state);
  const modelState = state[activeRouteName];
  return {
    activeColor:
      modelState.carouselList && modelState.carouselList.length > 0
        ? modelState.carouselList[modelState.activeSlide].colors
        : undefined,
    gradientVisible: modelState.gradientVisible,
    modelNamespace: activeRouteName,
  };
};

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

type IProps = MaterialTopTabBarProps & ModelState;

/**
 * 自定义顶部标签导航器头部
 */
class TopTabBarWrapper extends React.Component<IProps> {
  goSortPage = () => {
    const {navigation} = this.props;
    navigation.navigate('Category');
  };

  goHistory = () => {
    const {navigation} = this.props;
    navigation.navigate('History');
  };

  get gradient() {
    let {activeColor = ['#ccc', '#e2e2e2'], gradientVisible} = this.props;
    // console.log('activeColor', activeColor, showLinears, modelNamespace);
    if (!gradientVisible) {
      return null;
    }
    return (
      <LinearAnimatedGradientTransition
        colors={activeColor}
        style={styles.gradient}
      />
    );
  }

  render() {
    let {
      gradientVisible,
      activeTintColor,
      inactiveTintColor,
      indicatorStyle,
    } = this.props;
    // console.log('this.props', this.props);
    let textStyle = styles.text;
    if (!gradientVisible) {
      textStyle = styles.blackText;
      activeTintColor = '#000';
      inactiveTintColor = '#333';
      if (indicatorStyle) {
        indicatorStyle = StyleSheet.compose(
          indicatorStyle,
          styles.grayBackgroundColor,
        );
      }
    } else {
      indicatorStyle = StyleSheet.compose(
        indicatorStyle,
        styles.whiteBackgroundColor,
      );
    }

    return (
      <View style={styles.container}>
        {this.gradient}
        <View style={styles.topTabBarView}>
          <MaterialTopTabBar
            {...this.props}
            indicatorStyle={indicatorStyle}
            activeTintColor={activeTintColor}
            inactiveTintColor={inactiveTintColor}
            style={styles.tabbar}
          />
          <Touchable onPress={this.goSortPage} style={styles.sortBtn}>
            <Text style={textStyle}>分类</Text>
          </Touchable>
        </View>

        <View style={styles.searchBar}>
          <Touchable style={styles.search}>
            <Text style={textStyle}>搜索按钮</Text>
          </Touchable>
          <Touchable style={styles.history} onPress={this.goHistory}>
            <Text style={textStyle}>历史记录</Text>
          </Touchable>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    paddingTop: getStatusBarHeight(),
  },
  gradient: {
    ...StyleSheet.absoluteFillObject,
    height: 260,
  },
  topTabBarView: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  tabbar: {
    flex: 1,
    overflow: 'hidden',
    backgroundColor: 'transparent',
  },
  searchBar: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 7,
    paddingHorizontal: 15,
  },
  search: {
    flex: 1,
    justifyContent: 'center',
    paddingLeft: 12,
    height: 30,
    borderRadius: 15,
    backgroundColor: 'rgba(0, 0, 0, 0.1)',
  },
  history: {
    marginLeft: 24,
  },
  sortBtn: {
    paddingHorizontal: 8,
    borderLeftColor: '#eee',
    borderLeftWidth: StyleSheet.hairlineWidth,
  },
  text: {
    color: '#fff',
  },
  blackText: {
    color: '#333',
  },
  grayBackgroundColor: {
    backgroundColor: '#333',
  },
  whiteBackgroundColor: {
    backgroundColor: '#fff',
  },
});

export default connector(TopTabBarWrapper);
