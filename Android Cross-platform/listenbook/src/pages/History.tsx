import React from 'react';
import {
  View,
  Text,
  FlatList,
  Image,
  StyleSheet,
  ListRenderItemInfo,
} from 'react-native';
import realm from '@/config/realm';
import Touchable from '@/components/Touchable';
import Icon from '@/assets/iconfont/index';
import {connect, ConnectedProps} from 'react-redux';
import {getTimeString} from '@/utils/index';
import {ModalStackNavigation} from '@/navigator/index';
import {IShow} from '@/models/player';

interface IHistory extends IShow {
  playSeconds: number;
  duration: number;
}

const connector = connect();

type ModelState = ConnectedProps<typeof connector>;
interface IProps extends ModelState {
  navigation: ModalStackNavigation;
}

class History extends React.Component<IProps> {
  onPress = (item: IHistory, index: number) => {
    const {dispatch, navigation} = this.props;
    const showList = realm.objects<IHistory>('Show');
    const previousItem = showList[index - 1];
    const nextItem = showList[index + 1];
    let params = {
      id: item.id,
    };
    dispatch({
      type: 'player/setState',
      payload: {
        currentId: item.id,
        playList: showList.map(item => item.id),
        previousId: previousItem && previousItem.id,
        nextId: nextItem && nextItem.id,
      },
    });
    navigation.navigate('ProgramDetail', params);
  };

  renderItem = ({item, index}: ListRenderItemInfo<IHistory>) => {
    const rate =
      item.duration > 0
        ? Math.floor(((item.playSeconds * 100) / item.duration) * 100) / 100
        : 0;
    return (
      <Touchable style={styles.item} onPress={() => this.onPress(item, index)}>
        <Image source={{uri: item.thumbnailUrl}} style={styles.image} />
        <View style={styles.right}>
          <Text style={styles.title}>{item.title}</Text>
          <Text numberOfLines={1} style={{color: '#999', marginVertical: 10}}>
            在水一方
          </Text>
          <View style={{flexDirection: 'row'}}>
            <Text style={styles.text}>
              <Icon name="icon-time" size={14} style={{marginRight: 5}} />{' '}
              {getTimeString(item.duration)}
            </Text>
            <Text style={styles.rate}>已播：{rate}%</Text>
          </View>
        </View>
      </Touchable>
    );
  };

  render() {
    const showList = realm.objects<IHistory>('Show');
    return <FlatList data={showList} renderItem={this.renderItem} />;
  }
}

const styles = StyleSheet.create({
  item: {
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 15,
  },
  image: {
    width: 65,
    height: 65,
    borderRadius: 3,
  },
  right: {
    flex: 1,
    paddingRight: 15,
    paddingVertical: 20,
    marginLeft: 10,
    borderBottomColor: '#ccc',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  title: {
    color: '#999',
  },

  text: {
    color: '#999',
  },
  rate: {
    marginLeft: 20,
    color: '#f6a624',
  },
});

export default connector(History);
