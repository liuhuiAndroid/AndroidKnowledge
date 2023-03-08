import React from 'react';
import {Image as RNImage, ImageProps} from 'react-native';

const Image: React.FC<ImageProps> = props => <RNImage {...props} />;

export default Image;
