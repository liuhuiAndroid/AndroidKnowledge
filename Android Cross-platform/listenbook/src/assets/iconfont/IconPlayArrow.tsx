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

const IconPlayArrow: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M341.333333 290.986667v442.026666c0 33.706667 37.12 54.186667 65.706667 35.84l347.306667-221.013333a42.666667 42.666667 0 0 0 0-72.106667L407.04 255.146667A42.581333 42.581333 0 0 0 341.333333 290.986667z"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconPlayArrow.defaultProps = {
  size: 18,
};

export default IconPlayArrow;
