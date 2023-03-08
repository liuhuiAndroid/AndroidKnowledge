import React, {PureComponent} from 'react';
import {StyleSheet, View, Text, Animated, Easing} from 'react-native';
import {viewportWidth} from '@/utils/index';
import {IBarrage} from '.';

interface IProps {
  data: IBarrage;
  heightOfLine: number;
  outside: (data: IBarrage) => void;
  animtedListener: (data: IBarrage) => void;
}

interface IState {
  translateX?: Animated.AnimatedInterpolation;
}

export default class BarrageItem extends PureComponent<IProps, IState> {
  anim = new Animated.Value(0);

  trackRef = React.createRef<View>();

  spin: Animated.CompositeAnimation;

  translateX: Animated.AnimatedInterpolation;

  trackStyle = {};

  height = 30;

  constructor(props: IProps) {
    super(props);
    this.state = {
      translateX: undefined,
    };

    this.spin = Animated.timing(this.anim, {
      toValue: 10,
      duration: 6000,
      easing: Easing.linear,
      useNativeDriver: true,
    });

    const {outside, data, animtedListener} = this.props;
    this.anim.setValue(0);
    let isFree = false;

    // 监听弹幕动画
    this.anim.addListener(({value}) => {
      if (parseInt(value + '', 10) === 3 && !isFree) {
        isFree = true;
        animtedListener(data);
      }
    });
    // 启动动画，监听弹幕动画结束
    this.spin.start(({finished}) => {
      if (finished) {
        outside(data);
      }
    });

    const width = data.title.length * 15;
    this.translateX = this.anim.interpolate({
      inputRange: [0, 10],
      outputRange: [viewportWidth, -width],
    });
  }

  getTop = () => {
    const {data} = this.props;
    return this.height * data.trackIndex;
  };

  renderItem = () => {
    const {data} = this.props;
    return (
      <View style={styles.item}>
        <Text>{data.title}</Text>
      </View>
    );
  };

  render() {
    let trackStyle = {
      top: this.getTop(),
      transform: [{translateX: this.translateX}],
    };

    return (
      <Animated.View style={[styles.track, trackStyle]} ref={this.trackRef}>
        {this.renderItem()}
      </Animated.View>
    );
  }
}

const styles = StyleSheet.create({
  track: {
    position: 'absolute',
    flexDirection: 'row',
    transform: [{translateX: viewportWidth}],
  },
  item: {
    margin: 10,
  },
});
