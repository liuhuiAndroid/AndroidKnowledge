import React from 'react';
import {StyleSheet, Animated, Platform} from 'react-native';
import Icon, {IconNames} from '@/assets/iconfont/index';

const styles = StyleSheet.create({
  headerBackImage: {
    marginHorizontal: Platform.OS === 'ios' ? 8 : 0,
    marginVertical: 12,
    color: '#000',
  },
});

class BackIcon extends React.Component<IProps> {
  render() {
    const {name = 'icon-back'} = this.props;
    return (
      <Icon
        name={name}
        size={25}
        color={this.props.color}
        style={styles.headerBackImage}
      />
    );
  }
}

const AnimatedIcon = Animated.createAnimatedComponent(BackIcon);

interface IProps {
  name?: IconNames;
  color: Animated.AnimatedInterpolation | string;
}

class HeaderBackImage extends React.PureComponent<IProps> {
  render() {
    return <AnimatedIcon {...this.props} />;
  }
}

export default HeaderBackImage;
