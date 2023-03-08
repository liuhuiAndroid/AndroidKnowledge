import React from 'react';
import {FlatList, ListRenderItemInfo} from 'react-native';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import FoundItem from './FoundItem';
import {IFound} from '@/models/found';

const mapStateToProps = ({found}: RootState) => ({
  list: found.list,
  refreshing: found.refreshing,
});
const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {}

/**
 * 发现模块
 */
class Found extends React.PureComponent<IProps> {
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'found/fetchList',
      payload: {
        refreshing: true,
      },
    });
  }

  onRefresh = () => {
    const {dispatch} = this.props;

    dispatch({
      type: 'found/fetchList',
      payload: {
        refreshing: true,
      },
    });
  };

  onEndReached = () => {
    const {dispatch} = this.props;
    // alert('onEndReached');
    dispatch({
      type: 'found/fetchList',
      payload: {
        refreshing: false,
      },
    });
  };

  renderItem = ({item}: ListRenderItemInfo<IFound>) => {
    return <FoundItem item={item} dispatch={this.props.dispatch} />;
  };

  render() {
    const {list, refreshing} = this.props;
    return (
      <FlatList
        data={list}
        renderItem={this.renderItem}
        keyExtractor={item => `item-${item.id}`}
        refreshing={refreshing}
        onRefresh={this.onRefresh}
        onEndReached={this.onEndReached}
        onEndReachedThreshold={0.1}
      />
    );
  }
}

export default connector(Found);
