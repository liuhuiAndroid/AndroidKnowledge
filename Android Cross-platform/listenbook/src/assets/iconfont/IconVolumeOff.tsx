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

const IconVolumeOff: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M704 240l0 544q0 13-9.5 22.5t-22.5 9.5-22.5-9.5l-166.5-166.5-131 0q-13 0-22.5-9.5t-9.5-22.5l0-192q0-13 9.5-22.5t22.5-9.5l131 0 166.5-166.5q9.5-9.5 22.5-9.5t22.5 9.5 9.5 22.5z"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconVolumeOff.defaultProps = {
  size: 18,
};

export default IconVolumeOff;
