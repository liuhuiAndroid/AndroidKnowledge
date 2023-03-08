import {StyleSheet} from 'react-native';
import {viewportHeight} from '@/utils/index';

export const colors = {
  black: '#1a1917',
  gray: '#888888',
  background1: '#B721FF',
  background2: '#21D4FD',
};

export default StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  container: {
    flex: 1,

    // backgroundColor: colors.background1
  },

  scrollview: {
    flex: 1,
  },
  exampleContainer: {
    // paddingVertical: 30
    height: viewportHeight * 0.26,
  },
  exampleContainerDark: {
    backgroundColor: colors.black,
  },
  exampleContainerLight: {
    backgroundColor: 'white',
  },
  title: {
    paddingHorizontal: 30,
    backgroundColor: 'transparent',
    color: 'rgba(255, 255, 255, 0.9)',
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  titleDark: {
    color: colors.black,
  },
  subtitle: {
    marginTop: 5,
    paddingHorizontal: 30,
    backgroundColor: 'transparent',
    color: 'rgba(255, 255, 255, 0.75)',
    fontSize: 13,
    fontStyle: 'italic',
    textAlign: 'center',
  },
  slider: {
    // marginTop: 15,
    overflow: 'visible', // for custom animations
  },
  sliderContentContainer: {
    paddingTop: 0, // for custom animation
  },
  paginationContainer: {
    alignContent: 'center',
    position: 'absolute',
    height: 16,
    paddingVertical: 0,
    borderRadius: 8,
    top: -30,
    backgroundColor: 'rgba(0, 0, 0, 0.2)',
  },
  paginationDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginHorizontal: 0,
  },
});
