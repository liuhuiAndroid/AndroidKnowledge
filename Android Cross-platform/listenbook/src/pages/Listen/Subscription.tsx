import React from 'react';
import {FlatList, View, Text, Image, StyleSheet} from 'react-native';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';

const mapStateToProps = ({subscription, loading}: RootState) => ({
  list: subscription.subscriptionList,
  loading: loading.effects['subscription/fetchList'] || false,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {}

class Subscription extends React.PureComponent<IProps> {
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'subscription/fetchList',
    });
  }

  onRefresh = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'subscription/fetchList',
    });
  };

  renderItem = ({item}) => {
    return (
      <View style={styles.item}>
        <Image source={{uri: item.image}} style={styles.image} />
        <Text>{item.title}</Text>
      </View>
    );
  };

  render() {
    const {list, loading} = this.props;
    return (
      <FlatList
        onRefresh={this.onRefresh}
        refreshing={loading}
        data={list}
        renderItem={this.renderItem}
        keyExtractor={item => item.id}
      />
    );
  }
}

const styles = StyleSheet.create({
  item: {
    padding: 10,
    flexDirection: 'row',
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderColor: '#ccc',
    alignItems: 'center',
  },
  image: {
    width: 24,
    height: 24,
    marginRight: 10,
  },
});

export default connect(mapStateToProps)(Subscription);
