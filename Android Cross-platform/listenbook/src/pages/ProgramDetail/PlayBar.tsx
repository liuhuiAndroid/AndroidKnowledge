import React from 'react';
import {StyleSheet, Animated, Easing, View, Platform} from 'react-native';
import Icon from '@/assets/iconfont/index';
import Slider from 'react-native-slider-x';
import Touchable from '@/components/Touchable';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import Thumb from './Thumb';

const mapStateToProps = ({player, loading}: RootState) => ({
  playState: player.playState,
  playSeconds: player.playSeconds,
  duration: player.duration,
  previousId: player.previousId,
  nextId: player.nextId,
  loading: loading.effects['player/fetchShow'],
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {}

/**
 * 循环动画
 */
class LoopView extends React.PureComponent {
  spin: Animated.CompositeAnimation;
  anim = new Animated.Value(0);
  constructor(props: IProps) {
    super(props);
    this.spin = Animated.loop(
      Animated.timing(this.anim, {
        toValue: 1,
        duration: 1000,
        easing: Easing.linear,
        useNativeDriver: true,
      }),
      {iterations: -1},
    );
  }

  componentDidMount() {
    this.spin.start();
  }

  render() {
    const rotate = this.anim.interpolate({
      inputRange: [0, 1], //输入值
      outputRange: ['0deg', '360deg'], //输出值
    });
    const {children} = this.props;
    return (
      <Animated.View
        style={{
          transform: [{rotate}],
        }}>
        {children}
      </Animated.View>
    );
  }
}

/**
 * 播放条
 */
class PlayBar extends React.PureComponent<IProps> {
  state = {
    currentTime: 0,
  };

  onSliderEditStart = () => {
    console.log('------开始拖拽');
    const {dispatch} = this.props;
    dispatch({
      type: 'player/timer-end',
    });
  };
  onSliderEditEnd = () => {
    console.log('------结束拖拽');
    const {dispatch} = this.props;
    dispatch({
      type: 'player/changeCurrentTime',
    });
  };
  onSliderEditing = (value: number) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'player/setState',
      payload: {
        playSeconds: value,
      },
    });
  };

  pause = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'player/pause',
    });
  };

  play = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'player/play',
    });
  };

  previous = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'player/previous',
    });
  };

  next = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'player/next',
    });
  };

  renderThumb = () => {
    return <Thumb currentTime={this.state.currentTime} />;
  };

  render() {
    const {
      playState,
      playSeconds,
      duration,
      loading,
      previousId,
      nextId,
    } = this.props;

    return (
      <View>
        <View style={styles.container}>
          <Slider
            onSlidingStart={this.onSliderEditStart}
            onSlidingComplete={this.onSliderEditEnd}
            onValueChange={this.onSliderEditing}
            value={playSeconds}
            maximumValue={duration < 0 ? 100 : duration}
            maximumTrackTintColor="rgba(255, 255, 255, 0.3)"
            minimumTrackTintColor="white"
            thumbTintColor="white"
            thumbStyle={styles.thumb}
            renderThumb={this.renderThumb}
            style={styles.slider}
          />
        </View>
        <View style={styles.control}>
          <Touchable
            onPress={this.previous}
            style={styles.button}
            disabled={!previousId}>
            <Icon name="icon-shangyishou" size={30} color="#fff" />
          </Touchable>
          {loading && (
            <LoopView>
              <Icon name="icon-loading" size={40} color="#fff" />
            </LoopView>
          )}
          {!loading && playState === 'playing' && (
            <Touchable onPress={this.pause} style={styles.button}>
              <Icon name="icon-paste" size={40} color="#fff" />
            </Touchable>
          )}
          {!loading && (playState === 'paused' || playState === 'finish') && (
            <Touchable onPress={this.play} style={styles.button}>
              <Icon name="icon-bofang" size={40} color="#fff" />
            </Touchable>
          )}
          <Touchable
            onPress={this.next}
            style={styles.button}
            disabled={!nextId}>
            <Icon name="icon-xiayishou" size={30} color="#fff" />
          </Touchable>
        </View>
      </View>
    );
  }
}
const styles = StyleSheet.create({
  container: {
    marginVertical: 15,
    marginHorizontal: 15,
    flexDirection: 'row',
  },
  control: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginVertical: 15,
    marginHorizontal: 90,
  },
  slider: {
    flex: 1,
    alignSelf: 'center',
    marginHorizontal: Platform.select({ios: 5}),
  },
  thumb: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 76,
    height: 20,
  },
  button: {
    marginHorizontal: 10,
  },
});

export default connect(mapStateToProps)(PlayBar);
