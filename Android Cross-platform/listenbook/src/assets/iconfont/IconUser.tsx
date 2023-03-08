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

const IconUser: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M368.5 386.6c47.1 40.1 95.2 60.5 143.9 60.5 48.8 0 96.9-20.5 143.9-60.5l-24.9-29.3c-40.5 34.5-80.1 51.3-119 51.3-38.9 0-78.4-16.8-119-51.3l-24.9 29.3z m198.8 184.1c191 27 339 193.8 339.9 395.8 0.1 22.7-18 41.3-40.3 41.4l-707.8 3.2c-22.4 0.1-40.6-18.3-40.7-41-0.9-201.9 145.7-370.1 336.4-398.8-126.4-26-222.2-139.7-222.8-276-0.7-156 123.4-283.4 276.6-284.1 153.2-0.7 278.4 125.6 279 281.6 0.6 136.3-94.1 250.9-220.3 277.9z"
        fill={getIconColor(color, 0, '#595757')}
      />
    </Svg>
  );
};

IconUser.defaultProps = {
  size: 18,
};

export default IconUser;
