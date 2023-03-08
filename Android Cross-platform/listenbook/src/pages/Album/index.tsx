import React from 'react';
import {
  PanGestureHandler,
  PanGestureHandlerStateChangeEvent,
  State,
  TapGestureHandler,
  NativeViewGestureHandler,
} from 'react-native-gesture-handler';
import {connect, ConnectedProps} from 'react-redux';
import {RootStackParamList, ModalStackNavigation} from '@/navigator/index';
import {RouteProp} from '@react-navigation/native';
import {
  View,
  StyleSheet,
  Animated,
  Text,
  Image,
  findNodeHandle,
} from 'react-native';
import {viewportHeight} from '@/utils/index';
import {useHeaderHeight} from '@react-navigation/stack';
import {RootState} from '@/models/index';
import Tab from './Tab';
import {BlurView} from '@react-native-community/blur';
import {IAlbum} from '@/models/album';
const coverRight = require('@/assets/cover-right.png');
const HEADER_HEIGHT = 260;
const USE_NATIVE_DRIVER = true;
const mapStateToProps = ({album}: RootState) => ({
  data: album,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: ModalStackNavigation;
  route: RouteProp<RootStackParamList, 'Album'>;
  headerHeight: number;
}

interface IState {
  viewRef: number | null;
}

/**
 * 频道列表模块
 */
class Album extends React.Component<IProps, IState> {
  headerHeight = this.props.headerHeight;
  START = -(HEADER_HEIGHT - this.headerHeight);
  END = 0;
  // 手指拖动的Y轴距离
  dragY = new Animated.Value(0);
  // 上一次拖动的Y轴距离
  translateYOffset = new Animated.Value(0);
  backgroundImage = React.createRef<Image>();
  tapRef = React.createRef<TapGestureHandler>();
  panRef = React.createRef<PanGestureHandler>();
  nativeRef = React.createRef<NativeViewGestureHandler>();
  lastScrollYValue = 0;
  lastScrollY = new Animated.Value(0);
  reverseLastScrollY = Animated.multiply(
    new Animated.Value(-1),
    this.lastScrollY,
  );
  translateYValue = 0;
  translateYInterpolateValue = 0;
  translate = Animated.add(
    Animated.add(this.dragY, this.reverseLastScrollY),
    this.translateYOffset,
  );
  translateY = this.translate.interpolate({
    inputRange: [this.START, this.END],
    outputRange: [this.START, this.END],
    extrapolate: 'clamp',
  });
  // 监听FlatList滚动
  onScrollBeginDrag = Animated.event<{contentOffset: {y: number}}>(
    [{nativeEvent: {contentOffset: {y: this.lastScrollY}}}],
    {
      useNativeDriver: USE_NATIVE_DRIVER,
      listener: ({nativeEvent}) => {
        this.lastScrollYValue = nativeEvent.contentOffset.y;
      },
    },
  );

  constructor(props: IProps) {
    super(props);
    this.state = {
      viewRef: null,
    };
    props.navigation.setParams({
      opacity: this.translateY.interpolate({
        inputRange: [this.START, this.END],
        outputRange: [1, 0],
      }),
    });
  }

  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'album/fetchList',
    });
    this.translate.addListener(({value}) => {
      this.translateYValue = value;
    });
    this.translateY.addListener(({value}) => {
      this.translateYInterpolateValue = value;
    });
  }

  onGestureEvent = Animated.event([{nativeEvent: {translationY: this.dragY}}], {
    useNativeDriver: USE_NATIVE_DRIVER,
  });
  onHandlerStateChange = ({nativeEvent}: PanGestureHandlerStateChangeEvent) => {
    if (nativeEvent.oldState === State.ACTIVE) {
      let {translationY} = nativeEvent;
      translationY -= this.lastScrollYValue;
      this.translateYOffset.extractOffset();
      this.translateYOffset.setValue(translationY);
      this.translateYOffset.flattenOffset();
      this.dragY.setValue(0);
      let maxDeltaY = 0;

      if (translationY < 0 && this.translateYValue < this.START) {
        Animated.timing(this.translateYOffset, {
          toValue: this.START,
          useNativeDriver: USE_NATIVE_DRIVER,
        }).start();
        maxDeltaY = this.END;
      } else if (translationY > 0 && this.translateYValue > this.END) {
        Animated.timing(this.translateYOffset, {
          toValue: this.END,
          useNativeDriver: USE_NATIVE_DRIVER,
        }).start();
        maxDeltaY = -this.START;
      } else {
        maxDeltaY = -this.translateYInterpolateValue;
      }
      if (this.tapRef.current) {
        this.tapRef.current.setNativeProps({
          maxDeltaY,
        });
      }
    }
  };

  onItemPress = (item: IAlbum, index: number) => {
    const {navigation, dispatch, data} = this.props;
    const previousItem: IAlbum = data.list[index - 1];
    const nextItem: IAlbum = data.list[index + 1];
    let params = {
      id: item.id,
    };
    dispatch({
      type: 'player/setState',
      payload: {
        playList: data.list.map(item => item.id),
        currentId: item.id,
        previousId: previousItem && previousItem.id,
        nextId: nextItem && nextItem.id,
      },
    });
    navigation.navigate('ProgramDetail', params);
  };

  imageLoaded = () => {
    this.setState({viewRef: findNodeHandle(this.backgroundImage.current)});
  };

  renderHeader = () => {
    const {data, route, headerHeight} = this.props;
    const item = route.params.item;
    return (
      <View style={[styles.header, {paddingTop: headerHeight}]}>
        <Image
          source={{uri: item.image}}
          ref={this.backgroundImage}
          onLoadEnd={this.imageLoaded}
          style={[StyleSheet.absoluteFillObject, styles.image]}
        />
        {this.state.viewRef && (
          <BlurView
            style={StyleSheet.absoluteFillObject}
            viewRef={this.state.viewRef}
            blurType="light"
            blurAmount={10}
          />
        )}
        <View style={styles.imageView}>
          <Image source={{uri: item.image}} style={styles.thumbnail} />
          <Image
            source={coverRight}
            style={styles.coverRight}
            resizeMode="contain"
          />
        </View>
        <View style={styles.headerRight}>
          <Text style={styles.title}>{data.title}</Text>
          <View style={styles.summary}>
            <Text style={styles.summaryText} numberOfLines={1}>
              {data.summary}
            </Text>
          </View>
          <View style={styles.author}>
            <Image source={{uri: data.author.avatar}} style={styles.avatar} />
            <Text style={styles.name}>{data.author.name}</Text>
          </View>
        </View>
      </View>
    );
  };
  render() {
    const {route} = this.props;
    return (
      <TapGestureHandler maxDeltaY={-this.START} ref={this.tapRef}>
        <View style={StyleSheet.absoluteFillObject} pointerEvents="box-none">
          <Animated.View
            style={[
              StyleSheet.absoluteFillObject,
              {transform: [{translateY: this.translateY}]},
            ]}>
            <PanGestureHandler
              ref={this.panRef}
              simultaneousHandlers={[this.tapRef, this.nativeRef]}
              shouldCancelWhenOutside={false}
              onGestureEvent={this.onGestureEvent}
              onHandlerStateChange={this.onHandlerStateChange}>
              <Animated.View>
                {this.renderHeader()}
                <View
                  style={[
                    styles.tab,
                    {height: viewportHeight - this.headerHeight},
                  ]}>
                  <Tab
                    nativeRef={this.nativeRef}
                    tapRef={this.tapRef}
                    panRef={this.panRef}
                    route={route}
                    onScrollBeginDrag={this.onScrollBeginDrag}
                    onItemPress={this.onItemPress}
                  />
                </View>
              </Animated.View>
            </PanGestureHandler>
          </Animated.View>
        </View>
      </TapGestureHandler>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  image: {
    backgroundColor: '#eee',
  },
  header: {
    height: HEADER_HEIGHT,
    flexDirection: 'row',
    paddingHorizontal: 20,
    // backgroundColor: 'red',
  },
  headerRight: {
    flex: 1,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: 'rgba(255, 255, 255, 0.4)',
  },
  coverRight: {height: 98, position: 'absolute', right: -23},
  tab: {
    backgroundColor: '#fff',
  },
  imageView: {
    marginRight: 26,
    alignItems: 'center',
    flexDirection: 'row',
  },
  thumbnail: {
    height: 98,
    width: 98,
    borderColor: '#fff',
    borderWidth: StyleSheet.hairlineWidth,
    borderRadius: 8,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 18,
    fontWeight: '900',
    color: '#fff',
  },
  summary: {
    width: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    padding: 10,
    marginVertical: 10,
    borderRadius: 4,
  },
  summaryText: {
    color: '#fff',
    fontSize: 12,
  },
  author: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  avatar: {
    borderRadius: 13,
    height: 26,
    width: 26,
    marginRight: 8,
  },
  name: {
    color: '#fff',
    fontSize: 12,
  },
});

const Wrapper = function(props: IProps) {
  const headerHeight = useHeaderHeight();
  return <Album {...props} headerHeight={headerHeight} />;
};

export default connector(Wrapper);
