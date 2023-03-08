import React from 'react';
import {
  View,
  FlatList,
  NativeSyntheticEvent,
  NativeScrollEvent,
  ListRenderItemInfo,
} from 'react-native';
import styles from '@/assets/styles/index.style';
import {connect, ConnectedProps} from 'react-redux';
import ChannelItem from './ChannelItem';

import Guress from './Guress';
import Carousel from './Carousel';
import {RootState} from '@/models/index';
import Empty from '@/components/Empty';
import More from '@/components/More';
import End from '@/components/End';
import {RootStackNavigation} from '@/navigator/index';
import {RouteProp} from '@react-navigation/native';
import {HomeTabParamList} from '@/navigator/HomeTabs';
import {IChannel} from '@/models/home';
import {slideHeight} from '@/components/SliderEntry';
import { IGuess } from '@/models/guress';

const mapStateToProps = (
  state: RootState,
  {route}: {route: RouteProp<HomeTabParamList, string>},
) => {
  const modelNamespace = route.params.modelNamespace;
  const modelState = state[modelNamespace];
  return {
    modelNamespace,
    carouselList: modelState.carouselList,
    activeSlide: modelState.activeSlide,
    gradientVisible: modelState.gradientVisible,
    channelList: modelState.channelList,
    refreshing: modelState.refreshing,
    hasMore: modelState.info.page < modelState.info.results,
    loading: state.loading.effects[`${modelState}/fetchChannelList`],
  };
};

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: RootStackNavigation;
}

interface IState {
  loading: boolean;
  hasMore: boolean;
  endReached: boolean;
}

/**
 * 顶部标签导航器里的页面模版
 *
 */
class Home extends React.PureComponent<IProps, IState> {
  constructor(props: IProps) {
    super(props);
    this.state = {
      loading: false,
      hasMore: true,
      endReached: false,
    };
  }

  static getDerivedStateFromProps(nextProps: IProps, prevState: IState) {
    const {loading, hasMore} = nextProps;
    // 当传入的type发生变化的时候，更新state
    if (loading !== prevState.loading) {
      return {
        loading,
      };
    }
    if (hasMore !== prevState.hasMore) {
      return {
        hasMore,
      };
    }
    // 否则，对于state不进行任何操作
    return null;
  }

  componentDidMount() {
    this.loadData(true);
  }

  loadData = (refreshing: boolean, callback?: () => void) => {
    const {dispatch, modelNamespace} = this.props;
    dispatch({
      type: `${modelNamespace}/fetchChannelList`,
      payload: {
        refreshing,
        category: modelNamespace,
      },
      callback,
    });
  };

  onScroll = ({nativeEvent}: NativeSyntheticEvent<NativeScrollEvent>) => {
    const {dispatch, gradientVisible, modelNamespace} = this.props;
    let newLinearVisible = nativeEvent.contentOffset.y < slideHeight;
    if (newLinearVisible !== gradientVisible) {
      dispatch({
        type: `${modelNamespace}/setState`,
        payload: {
          gradientVisible: newLinearVisible,
        },
      });
    }
  };

  onRefresh = () => {
    this.loadData(true);
  };

  onEndReached = () => {
    const {hasMore, loading} = this.props;
    if (!hasMore || loading) {
      return;
    }
    this.setState({
      endReached: true,
    });

    this.loadData(false, () => {
      this.setState({
        endReached: false,
      });
    });
  };

  onPress = (item: IChannel | IGuess) => {
    const {navigation} = this.props;
    navigation.navigate('Album', {item});
  };

  get ListHeaderComponent() {
    const {modelNamespace} = this.props;
    return (
      <View>
        <Carousel modelNamespace={modelNamespace} />
        <View style={{backgroundColor: '#fff'}}>
          <Guress onPress={this.onPress} />
        </View>
      </View>
    );
  }

  renderItem = ({item}: ListRenderItemInfo<IChannel>) => {
    return <ChannelItem item={item} onPress={this.onPress} />;
  };

  renderFooter = () => {
    const {endReached, hasMore} = this.state;
    if (endReached) {
      return <More />;
    }
    if (!hasMore) {
      return <End />;
    }
    return null;
  };

  renderEmpty = () => {
    const {loading} = this.state;
    if (loading !== undefined) {
      return <Empty />;
    }
    return null;
  };

  render() {
    const {channelList, refreshing} = this.props;
    return (
      <FlatList
        style={styles.scrollview}
        data={channelList}
        extraData={this.state}
        keyExtractor={item => `item-${item.id}`}
        renderItem={this.renderItem}
        refreshing={refreshing}
        onRefresh={this.onRefresh}
        onEndReached={this.onEndReached}
        onEndReachedThreshold={0.1}
        ListHeaderComponent={this.ListHeaderComponent}
        ListEmptyComponent={this.renderEmpty}
        ListFooterComponent={this.renderFooter}
        onScroll={this.onScroll}
      />
    );
  }
}

export default connect(mapStateToProps)(Home);
