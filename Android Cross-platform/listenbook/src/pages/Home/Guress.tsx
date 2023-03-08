import React from 'react';
import {
  View,
  Text,
  Image,
  FlatList,
  StyleSheet,
  ListRenderItemInfo,
} from 'react-native';
import Icon from '@/assets/iconfont/index';
import {connect, ConnectedProps} from 'react-redux';
import Touchable from '@/components/Touchable';
import {RootState} from '@/models/index';
import {IGuess} from '@/models/guress';

const mapStateToProps = ({guress}: RootState) => ({
  list: guress.list,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  onPress: (data: IGuess) => void;
}

/**
 * 猜你喜欢
 */
class Guress extends React.PureComponent<IProps> {
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'guress/fetchList',
    });
  }

  changeBatch = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'guress/fetchList',
    });
  };

  renderItem = ({item}: ListRenderItemInfo<IGuess>) => {
    const {onPress} = this.props;
    return (
      <Touchable style={styles.item} onPress={() => onPress(item)}>
        <Image source={{uri: item.image}} style={styles.thumbnail} />
        <View style={styles.rightContainer}>
          <Text style={styles.title} numberOfLines={2}>
            {item.title}
          </Text>
        </View>
      </Touchable>
    );
  };

  render() {
    const {list} = this.props;
    // console.log('----list', list)
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <View style={{flexDirection: 'row'}}>
            <Icon name="icon-xihuan" />
            <Text style={styles.headerTitle}>猜你喜欢</Text>
          </View>
          <View style={{flexDirection: 'row'}}>
            <Text style={styles.moreTitle}>更多</Text>
            <Icon name="icon-more" />
          </View>
        </View>
        <FlatList
          numColumns={3}
          data={list}
          renderItem={this.renderItem}
          style={styles.list}
          keyExtractor={item => item.id}
        />
        <Touchable onPress={this.changeBatch} style={styles.changeBatch}>
          <Text>
            <Icon name="icon-huanyipi" size={14} color="red" /> 换一批
          </Text>
        </Touchable>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#ffffff',
    borderRadius: 8,
    // padding: 2,
    margin: 16,
    shadowOffset: {width: 0, height: 5},
    shadowOpacity: 0.5,
    shadowRadius: 10,
    shadowColor: '#ccc',
    //注意：这一句是可以让安卓拥有灰色阴影
    elevation: 4,
  },
  header: {
    padding: 15,
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderBottomColor: '#efefef',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  headerTitle: {
    marginLeft: 5,
    color: '#333333',
  },
  moreTitle: {
    color: '#6f6f6f',
  },
  item: {
    flex: 1,
    marginVertical: 6,
    marginHorizontal: 5,
  },
  rightContainer: {
    flex: 1,
  },
  title: {
    // textAlign: "center"
  },
  thumbnail: {
    width: '100%',
    height: 100,
    borderRadius: 8,
    marginBottom: 10,
    backgroundColor: '#dedede',
  },
  list: {
    paddingTop: 10,
    paddingHorizontal: 10,
  },
  changeBatch: {
    padding: 10,
    alignItems: 'center',
  },
});

export default connect(mapStateToProps)(Guress);
