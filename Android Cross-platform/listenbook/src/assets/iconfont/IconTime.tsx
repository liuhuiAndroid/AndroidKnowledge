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

const IconTime: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M512 12C236.31 12 12 236.3 12 512s224.31 500 500 500 500-224.3 500-500S787.69 12 512 12z m0 944.44C266.94 956.44 67.56 757.06 67.56 512S266.94 67.56 512 67.56 956.44 266.94 956.44 512 757.06 956.44 512 956.44z"
        fill={getIconColor(color, 0, '#333333')}
      />
      <Path
        d="M762 484.22H539.78v-250a27.78 27.78 0 1 0-55.56 0V512A27.77 27.77 0 0 0 512 539.78h250a27.78 27.78 0 0 0 0-55.56z"
        fill={getIconColor(color, 1, '#333333')}
      />
    </Svg>
  );
};

IconTime.defaultProps = {
  size: 18,
};

export default IconTime;
