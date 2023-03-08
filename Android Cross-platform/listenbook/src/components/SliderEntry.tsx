import React, {Component} from 'react';
import {TouchableOpacity, StyleSheet, Platform} from 'react-native';
import {
  ParallaxImage,
  AdditionalParallaxProps,
} from 'react-native-snap-carousel';
import {colors} from '@/assets/styles/index.style';
import {viewportWidth, viewportHeight} from '@/utils/index';
import {ICarousel} from '@/models/home';

export interface SliderEntryProps {
  data: ICarousel;
  parallaxProps?: AdditionalParallaxProps;
}

export default class SliderEntry extends Component<SliderEntryProps> {
  get image() {
    const {
      data: {image},
      parallaxProps,
    } = this.props;

    return (
      <ParallaxImage
        source={{uri: image}}
        containerStyle={styles.imageContainer}
        style={styles.image}
        parallaxFactor={0.35}
        showSpinner={true}
        spinnerColor="rgba(0, 0, 0, 0.25)"
        {...parallaxProps}
      />
    );
  }

  render() {
    return (
      <TouchableOpacity activeOpacity={1} style={styles.slideInnerContainer}>
        {this.image}
      </TouchableOpacity>
    );
  }
}

const IS_IOS = Platform.OS === 'ios';

// 根据半分比计算
function wp(percentage: number) {
  const value = (percentage * viewportWidth) / 100;
  return Math.round(value);
}

export const slideHeight = viewportHeight * 0.26;
const slideWidth = wp(90);
const itemHorizontalMargin = wp(2);

export const sliderWidth = viewportWidth;
export const itemWidth = slideWidth + itemHorizontalMargin * 2;

const entryBorderRadius = 8;

const styles = StyleSheet.create({
  slideInnerContainer: {
    width: itemWidth,
    height: slideHeight,
    borderRadius: entryBorderRadius,
  },

  imageContainer: {
    flex: 1,
    marginBottom: IS_IOS ? 0 : -1, // Prevent a random Android rendering issue
    backgroundColor: 'white',
    borderRadius: entryBorderRadius,
  },
  image: {
    ...StyleSheet.absoluteFillObject,
    resizeMode: 'cover',
    borderRadius: IS_IOS ? entryBorderRadius : 0,
    borderTopLeftRadius: entryBorderRadius,
    borderTopRightRadius: entryBorderRadius,
  },

  radiusMaskEven: {
    backgroundColor: colors.black,
  },
  textContainer: {
    justifyContent: 'center',
    paddingTop: 20 - entryBorderRadius,
    paddingBottom: 20,
    paddingHorizontal: 16,
    backgroundColor: 'white',
    borderBottomLeftRadius: entryBorderRadius,
    borderBottomRightRadius: entryBorderRadius,
  },
  textContainerEven: {
    backgroundColor: colors.black,
  },
  title: {
    color: colors.black,
    fontSize: 13,
    fontWeight: 'bold',
    letterSpacing: 0.5,
  },
  titleEven: {
    color: 'white',
  },
  subtitle: {
    marginTop: 6,
    color: colors.gray,
    fontSize: 12,
    fontStyle: 'italic',
  },
  subtitleEven: {
    color: 'rgba(255, 255, 255, 0.7)',
  },
});
