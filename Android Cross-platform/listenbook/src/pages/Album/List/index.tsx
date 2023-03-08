import React from 'react';
import {
  StyleSheet,
  ListRenderItemInfo,
  Animated,
  NativeSyntheticEvent,
  NativeScrollEvent,
} from 'react-native';
import {
  TapGestureHandler,
  PanGestureHandler,
  NativeViewGestureHandler,
} from 'react-native-gesture-handler';
import {connect, ConnectedProps} from 'react-redux';
import Item from './Item';
import {IAlbum} from '@/models/album';
import {RootState} from '@/models/index';

const mapStateToProps = ({album}: RootState) => ({
  list: album.list,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  nativeRef: React.RefObject<NativeViewGestureHandler>;
  tapRef: React.RefObject<TapGestureHandler>;
  panRef: React.RefObject<PanGestureHandler>;
  onScrollBeginDrag: (event: NativeSyntheticEvent<NativeScrollEvent>) => void;
  onItemPress: (item: IAlbum, index: number) => void;
}

class List extends React.PureComponent<IProps> {
  onPress = (item: IAlbum, index: number) => {
    const {onItemPress} = this.props;
    onItemPress(item, index);
  };

  renderItem = ({item, index}: ListRenderItemInfo<IAlbum>) => {
    return (
      <Item
        item={item}
        index={index}
        onPress={() => this.onPress(item, index)}
      />
    );
  };

  render() {
    const {list, nativeRef, tapRef, panRef, onScrollBeginDrag} = this.props;
    return (
      <NativeViewGestureHandler
        ref={nativeRef}
        waitFor={tapRef}
        simultaneousHandlers={panRef}>
        <Animated.FlatList
          style={styles.container}
          data={list}
          renderItem={this.renderItem}
          bounces={false}
          scrollEventThrottle={1}
          onScrollBeginDrag={onScrollBeginDrag}
        />
      </NativeViewGestureHandler>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default connect(mapStateToProps)(List);
