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

const IconHuanyipi: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M512 972.8c-199.68 0-368.64-128-435.2-307.2H307.2v-51.2H0v307.2h51.2v-184.32c81.92 168.96 256 286.72 460.8 286.72 245.76 0 455.68-174.08 501.76-409.6h-51.2c-46.08 204.8-230.4 358.4-450.56 358.4zM972.8 102.4v184.32C890.88 117.76 716.8 0 512 0 266.24 0 56.32 174.08 10.24 409.6h51.2c46.08-204.8 230.4-358.4 450.56-358.4 199.68 0 368.64 128 435.2 307.2H716.8v51.2h307.2V102.4h-51.2z"
        fill={getIconColor(color, 0, '#666666')}
      />
    </Svg>
  );
};

IconHuanyipi.defaultProps = {
  size: 18,
};

export default IconHuanyipi;
