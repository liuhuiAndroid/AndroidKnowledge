import React from 'react';
import {StyleSheet, Animated, Easing, Image, View} from 'react-native';
import PropTypes from 'prop-types';
import Touchable from '@/components/Touchable';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import ProgressBar from './ProgressBar';
import Icon from '@/assets/iconfont/index';

const mapStateToProps = ({player}: RootState) => ({
  playState: player.playState,
  show: player.show,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  percent: number;
  onPress: () => void;
}

// 播放按钮
class Play extends React.PureComponent<IProps> {
  static propTypes = {
    progress: PropTypes.number,
    height: PropTypes.number,
    color: PropTypes.string,
    borderRadius: PropTypes.number,
  };

  static defaultProps = {
    progress: 10,
    height: 2,
    color: 'green',
    borderRadius: 2,
    width: 100,
    // backgroundColor: 'red',
  };

  anim = new Animated.Value(0);

  spin: Animated.CompositeAnimation;

  constructor(props: IProps) {
    super(props);
    this.spin = Animated.loop(
      Animated.timing(this.anim, {
        toValue: 1,
        duration: 10000,
        easing: Easing.linear,
        useNativeDriver: true,
      }),
      {iterations: -1},
    );
  }

  componentDidMount() {
    const {playState} = this.props;
    if (playState === 'playing') {
      this.spin.start();
    }
  }

  componentDidUpdate() {
    const {playState} = this.props;
    if (playState === 'playing') {
      this.spin.start();
    } else {
      this.spin.stop();
    }
  }

  onPress = () => {
    const {onPress} = this.props;
    if (onPress) {
      onPress();
    }
  };

  render() {
    const {playState, show} = this.props;
    const rotate = this.anim.interpolate({
      inputRange: [0, 1], //输入值
      outputRange: ['0deg', '360deg'], //输出值
    });

    return (
      <Touchable style={styles.touchableView} onPress={this.onPress}>
        <ProgressBar>
          <Animated.View
            style={{
              transform: [{rotate}],
            }}>
            {show.thumbnailUrl ? (
              <Image source={{uri: show.thumbnailUrl}} style={styles.image} />
            ) : (
              <Icon name="icon-bofang3" color="#ededed" size={40} />
            )}
          </Animated.View>
          {playState === 'paused' && (
            <View style={styles.pauseImage}>
              <Icon name="icon-bofang" color="#f86442" size={15} />
            </View>
          )}
        </ProgressBar>
      </Touchable>
    );
  }
}

const styles = StyleSheet.create({
  touchableView: {
    borderRadius: 21,
    justifyContent: 'center',
    alignItems: 'center',
  },
  image: {
    width: 42,
    height: 42,
  },
  pauseImage: {
    // width: 42,
    // height: 42,
    position: 'absolute',
    // top: 10,
    // left: 10,
  },
});

export default connect(mapStateToProps)(Play);
