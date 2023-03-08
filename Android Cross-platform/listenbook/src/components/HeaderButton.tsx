import React from 'react';
import Touchable from './Touchable';
import {TouchableOpacityProps, StyleSheet} from 'react-native';

const styles = StyleSheet.create({
  button: {
    width: 70,
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
});

const HeaderButton: React.FC<TouchableOpacityProps> = props => (
  <Touchable
    {...props}
    style={[styles.button, props.style]}
    activeOpacity={0.8}
  />
);

export default HeaderButton;
