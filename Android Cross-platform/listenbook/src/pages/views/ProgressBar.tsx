import React from 'react';
import {AnimatedCircularProgress} from 'react-native-circular-progress';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';

const mapStateToProps = ({player}: RootState) => ({
  percent: player.percent,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {}

class ProgressBar extends React.PureComponent<IProps> {
  render() {
    const {percent, children} = this.props;
    return (
      <AnimatedCircularProgress
        size={40}
        width={2}
        fill={percent}
        tintColor="#f86442"
        backgroundColor="#ededed">
        {() => children}
      </AnimatedCircularProgress>
    );
  }
}

export default connect(mapStateToProps)(ProgressBar);
