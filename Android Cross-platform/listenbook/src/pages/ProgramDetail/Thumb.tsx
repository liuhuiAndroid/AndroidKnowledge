import React from 'react';
import {View, Text} from 'react-native';
import {getTimeString} from '@/utils/index';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';

const mapStateToProps = ({player}: RootState) => ({
  playSeconds: player.playSeconds,
  duration: player.duration,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  currentTime: number;
}

// 播放条自定义Thumb
class Thumb extends React.PureComponent<IProps> {
  render() {
    const {playSeconds, duration, currentTime} = this.props;
    const currentTimeString = getTimeString(currentTime || playSeconds);
    const durationString = getTimeString(duration);
    return (
      <View>
        <Text style={{fontSize: 10}}>
          {currentTimeString}/{durationString}
        </Text>
      </View>
    );
  }
}

export default connect(mapStateToProps)(Thumb);
