import React from 'react';
import {View, Text, StyleSheet, Image} from 'react-native';
import VideoPlayer from 'react-native-video-player';
import Icon from '@/assets/iconfont/index';
import {Dispatch} from 'dva-core-ts';
import {IFound} from '@/models/found';

interface IProps {
  dispatch: Dispatch;
  item: IFound;
}

class FoundItem extends React.PureComponent<IProps> {
  state = {
    paused: false,
  };

  togglePaused = () => {
    const {dispatch, item} = this.props;
    dispatch({
      type: 'found/setState',
      payload: {
        currentVideo: item.id,
      },
    });
  };

  onStart = () => {
    const {dispatch, item} = this.props;
    dispatch({
      type: 'found/startPlay',
      payload: {
        currentItem: item,
      },
    });
  };

  onPlayPress = () => {
    this.onStart();
  };

  render() {
    const {paused} = this.state;
    const {item} = this.props;
    return (
      <View style={styles.item}>
        <View style={styles.user}>
          <Image source={{uri: item.user.avatar}} style={styles.avatar} />
          <View>
            <Text>{item.user.name}</Text>
            <Text style={{color: '#ccc', marginTop: 6}}>你可能喜欢</Text>
          </View>
        </View>
        <View>
          <VideoPlayer
            paused={paused}
            endWithThumbnail
            thumbnail={{uri: item.thumbnailUrl}}
            video={{uri: item.videoUrl}}
            videoWidth={300}
            videoHeight={180}
            // duration={300}
            onStart={this.onStart}
            onPlayPress={this.onPlayPress}
            ref={item.playerRef}
            Icon={Icon}
            iconName={{
              'play-arrow': 'icon-play-arrow',
              pause: 'icon-pause',
              'volume-off': 'icon-volume-off',
              'volume-up': 'icon-volume-up',
              fullscreen: 'icon-fullscreen',
            }}
          />
        </View>
        <View style={{flexDirection: 'row', marginVertical: 5}}>
          <View style={styles.tip}>
            <Icon name="icon-play2" size={20} color="#c4c4c4" />
            <Text style={{color: '#c4c4c4', marginLeft: 5}}>
              {item.forward}
            </Text>
          </View>
          <View style={styles.tip}>
            <Icon name="icon-message" size={20} color="#c4c4c4" />
            <Text style={{color: '#c4c4c4', marginLeft: 5}}>
              {item.comment}
            </Text>
          </View>
          <View style={styles.tip}>
            <Icon name="icon-time" size={20} color="#c4c4c4" />
            <Text style={{color: '#c4c4c4', marginLeft: 5}}>{item.like}</Text>
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  item: {
    borderBottomWidth: 5,
    borderColor: '#f3f4f5',
    padding: 10,
  },
  user: {
    flexDirection: 'row',
    marginBottom: 5,
  },
  avatar: {
    width: 48,
    height: 48,
    borderRadius: 24,
    marginRight: 5,
  },
  backgroundVideo: {
    width: '100%',
    height: 180,
    borderRadius: 4,
  },
  tip: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default FoundItem;
