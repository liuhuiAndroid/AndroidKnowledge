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

const IconDown: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M838.4 355.2 512 681.6c-6.4 6.4-14.4 9.6-22.4 9.6-8 0-16.8-3.2-22.4-9.6L140 355.2c-12-12-14.4-32.8-2.4-45.6 12-13.6 33.6-13.6 46.4-0.8l305.6 305.6c0 0 0 0 0.8 0l305.6-305.6c12.8-12.8 33.6-12.8 46.4 0.8C852.8 322.4 851.2 342.4 838.4 355.2z"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconDown.defaultProps = {
  size: 18,
};

export default IconDown;
