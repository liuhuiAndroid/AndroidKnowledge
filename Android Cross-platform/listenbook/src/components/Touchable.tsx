import React from 'react';
import {
  TouchableOpacity,
  TouchableOpacityProps,
  StyleSheet,
} from 'react-native';

const Touchable: React.FC<TouchableOpacityProps> = React.memo(props => {
  const {style, disabled, ...rest} = props;
  const disabledStyle = disabled && styles.disabled;
  return (
    <TouchableOpacity
      disabled={disabled}
      style={[style, disabledStyle]}
      activeOpacity={0.8}
      {...rest}
    />
  );
});

const styles = StyleSheet.create({
  disabled: {
    opacity: 0.5,
  },
});

export default Touchable;
