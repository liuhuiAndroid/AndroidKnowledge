/* tslint:disable */
/* eslint-disable */

import React, { FunctionComponent } from 'react';
import { ViewProps } from 'react-native';
import { Svg, GProps, Path } from 'react-native-svg';
import { getIconColor } from './helper';

interface Props extends GProps, ViewProps {
  size?: number;
  color?: string | string[];
}

const IconFaxian: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M512 85.333333c235.637333 0 426.666667 191.029333 426.666667 426.666667S747.637333 938.666667 512 938.666667 85.333333 747.637333 85.333333 512 276.362667 85.333333 512 85.333333z m149.162667 222.901334L444.16 386.357333a96 96 0 0 0-57.802667 57.813334l-78.122666 216.992a42.666667 42.666667 0 0 0 54.602666 54.602666l217.002667-78.122666a96 96 0 0 0 57.802667-57.813334l78.122666-216.992a42.666667 42.666667 0 0 0-54.602666-54.602666zM512 565.333333a53.333333 53.333333 0 1 0 0-106.666666 53.333333 53.333333 0 0 0 0 106.666666z"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconFaxian.defaultProps = {
  size: 18,
};

export default IconFaxian;
