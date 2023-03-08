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

const IconPaste: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M428.539658 833.494155c0 15.954367-13.053294 29.007661-29.007661 29.007661L285.613458 862.501816c-15.954367 0-29.007661-13.053294-29.007661-29.007661l0-639.423111c0-15.954367 13.053294-29.007661 29.007661-29.007661l113.918539 0c15.954367 0 29.007661 13.053294 29.007661 29.007661L428.539658 833.494155z"
        fill={getIconColor(color, 0, '#333333')}
      />
      <Path
        d="M760.124635 833.494155c0 15.954367-13.053294 29.007661-29.007661 29.007661l-113.918539 0c-15.954367 0-29.007661-13.053294-29.007661-29.007661l0-639.423111c0-15.954367 13.053294-29.007661 29.007661-29.007661l113.918539 0c15.954367 0 29.007661 13.053294 29.007661 29.007661L760.124635 833.494155z"
        fill={getIconColor(color, 1, '#333333')}
      />
    </Svg>
  );
};

IconPaste.defaultProps = {
  size: 18,
};

export default IconPaste;
