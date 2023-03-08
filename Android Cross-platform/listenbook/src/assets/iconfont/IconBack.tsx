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

const IconBack: FunctionComponent<Props> = ({ size, color, ...rest }) => {
  return (
    <Svg viewBox="0 0 1024 1024" width={size} height={size} {...rest}>
      <Path
        d="M708.42516 957.314205c-13.715373 0-27.426653-5.215792-37.895075-15.678074L277.717627 548.823674c-20.925588-20.893866-20.925588-54.821583 0-75.747171L670.530085 80.225159c20.931728-20.925588 54.821583-20.925588 75.752288 0 20.925588 20.893866 20.925588 54.821583 0 75.747171L391.359874 510.934738l354.922499 354.915335c20.925588 20.931728 20.925588 54.821583 0 75.753311C735.852836 952.098413 722.135416 957.314205 708.42516 957.314205L708.42516 957.314205zM708.42516 957.314205"
        fill={getIconColor(color, 0, '#333333')}
      />
    </Svg>
  );
};

IconBack.defaultProps = {
  size: 18,
};

export default IconBack;
