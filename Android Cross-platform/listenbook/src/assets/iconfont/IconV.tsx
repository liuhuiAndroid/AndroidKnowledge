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

const IconV: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M938.1 554.7c-14.4-14.4-31.9-21.6-52-21.6h-54.9c0-99.6-28.9-182.5-86.9-248.6-57.9-66-134.8-99.2-230.8-99.2s-173.4 33-232.1 99.3c-58.8 66.1-88.2 148.9-88.2 248.6h-57.6c-22 0-39.3 7.3-52.2 21.6-12.9 14.3-19.3 31.5-19.3 51.3v162.5c0 19.8 6.8 36.5 20.3 50 13.4 13.4 30.2 20.2 50 20.2h94.8v-306c0-92 25-168.3 75.1-228.8 50-60.5 118.9-90.7 206.3-90.7s156.6 30.7 207.7 92c51 61.4 76.5 137.2 76.5 227.4v306h92.1c19.9 0 37-7.1 51.5-21.6 14.4-14.4 21.6-30.6 21.6-48.7h0.1V606c0-19.7-7.4-36.9-22-51.3z"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconV.defaultProps = {
  size: 18,
};

export default IconV;
