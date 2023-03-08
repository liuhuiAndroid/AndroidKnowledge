import React from 'react';
import {View} from 'react-native';
import SnapCarousel, {
  Pagination,
  AdditionalParallaxProps,
} from 'react-native-snap-carousel';
import SliderEntry, {sliderWidth, itemWidth} from '@/components/SliderEntry';
import styles, {colors} from '@/assets/styles/index.style';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import {ICarousel} from '@/models/home';

const mapStateToProps = (state: RootState, {modelNamespace}: IProps) => {
  const modelState = state[modelNamespace];
  return {
    carouselList: modelState.carouselList,
    activeSlide: modelState.activeSlide,
  };
};

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  modelNamespace: string;
}

class Carousel extends React.Component<IProps> {
  carouselRef = React.createRef<SnapCarousel<any>>();
  componentDidMount() {
    const {dispatch, modelNamespace} = this.props;
    dispatch({
      type: `${modelNamespace}/fetchCarouselList`,
    });
  }

  onSnapToItem = (index: number) => {
    const {dispatch, modelNamespace} = this.props;
    if (typeof index !== 'number') return;
    dispatch({
      type: `${modelNamespace}/setState`,
      payload: {
        activeSlide: index,
      },
    });
  };

  _renderItemWithParallax(
    {item}: {item: ICarousel},
    parallaxProps?: AdditionalParallaxProps,
  ) {
    return <SliderEntry data={item} parallaxProps={parallaxProps} />;
  }

  render() {
    const {carouselList, activeSlide} = this.props;
    // console.log('----this.props', this.props);
    return (
      <View style={styles.exampleContainer}>
        <SnapCarousel
          ref={this.carouselRef}
          data={carouselList}
          renderItem={this._renderItemWithParallax}
          sliderWidth={sliderWidth}
          itemWidth={itemWidth}
          hasParallaxImages={true}
          inactiveSlideScale={0.94}
          inactiveSlideOpacity={0.7}
          containerCustomStyle={styles.slider}
          contentContainerCustomStyle={styles.sliderContentContainer}
          loop={true}
          loopClonesPerSide={2}
          autoplayDelay={500}
          autoplayInterval={3000}
          onSnapToItem={this.onSnapToItem}
          removeClippedSubviews={false}
        />
        <View style={{justifyContent: 'center', alignItems: 'center'}}>
          <Pagination
            dotsLength={carouselList.length}
            activeDotIndex={activeSlide}
            containerStyle={styles.paginationContainer}
            dotColor={'rgba(255, 255, 255, 0.92)'}
            dotStyle={styles.paginationDot}
            inactiveDotColor={colors.black}
            inactiveDotOpacity={0.4}
            inactiveDotScale={0.6}
            carouselRef={this.carouselRef.current}
            tappableDots={!!this.carouselRef.current}
          />
        </View>
      </View>
    );
  }
}

export default connect(mapStateToProps)(Carousel);
