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

const IconBofang2: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M175.50336 138.24a40.96 40.96 0 1 1 81.92 0v747.52a40.96 40.96 0 0 1-81.92 0V138.24zM771.69664 138.24a40.96 40.96 0 1 1 81.92 0v747.52a40.96 40.96 0 1 1-81.92 0V138.24z"
        fill={getIconColor(color, 0, '#304156')}
      />
    </Svg>
  );
};

IconBofang2.defaultProps = {
  size: 18,
};

export default IconBofang2;
