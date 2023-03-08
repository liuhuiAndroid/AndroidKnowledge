import React, {PureComponent} from 'react';
import {StyleSheet, View, StyleProp, ViewProps} from 'react-native';
import BarrageItem from './BarrageItem';

interface Message {
  id: number;
  title: string;
}

export interface IBarrage extends Message {
  trackIndex: number;
  isFree: boolean;
}

let barrages: IBarrage[][] = [];

// 获取新弹幕的轨道值 如果为-1 说明当前没有空闲轨道，丢弃当前弹幕
const getTrackIndex = (maxTrack: number) => {
  if (barrages.length === 0 || barrages[0].length === 0) {
    return 0;
  }
  // console.log('----maxTrack', maxTrack, barrages)
  for (let i = 0; i < maxTrack; i += 1) {
    const barragesOfLine = barrages[i];
    if (!barragesOfLine || !barragesOfLine.length) {
      return i;
    }
    const lastItemOfLine = barragesOfLine[barragesOfLine.length - 1];
    if (!lastItemOfLine) {
      return i;
    }
    if (lastItemOfLine.isFree) {
      return i;
    }
  }
  return -1;
};

// 添加数据
const addBarrage = (data: Message[], maxTrack: number) => {
  for (let index = 0; index < data.length; index += 1) {
    const message = data[index];
    const trackIndex = getTrackIndex(maxTrack);
    if (trackIndex < 0) {
      continue;
    }
    const barrage = {
      ...message,
      trackIndex,
      isFree: false,
    };
    if (!barrages[trackIndex]) {
      barrages[trackIndex] = [];
    }
    barrages[trackIndex].push(barrage);
  }
  return barrages;
};

interface IProps {
  style: StyleProp<ViewProps>;
  data: Message[];
  maxTrack: number;
}

interface IState {
  data: Message[];
  list: IBarrage[][];
}

export default class Barrage extends PureComponent<IProps, IState> {
  constructor(props: IProps) {
    super(props);
    this.state = {
      data: props.data,
      list: [],
    };
  }

  static getDerivedStateFromProps(nextProps: IProps, prevState: IState) {
    const {data, maxTrack} = nextProps;
    // 当传入的type发生变化的时候，更新state
    if (data !== prevState.data) {
      return {
        data,
        list: addBarrage(data, maxTrack),
      };
    }
    // 否则，对于state不进行任何操作
    return null;
  }

  componentWillUnmount() {
    if (barrages) {
      barrages = [];
    }
  }

  outside = (data: IBarrage) => {
    // console.log('-----outside data', data);
    if (barrages.length > 0) {
      const {trackIndex} = data;
      if (barrages[trackIndex]) {
        barrages[trackIndex] = barrages[trackIndex].filter(item => {
          return item.id !== data.id;
        });
        this.setState({
          list: barrages,
        });
      }
    }
  };

  animtedListener = (data: IBarrage) => {
    // console.log('-----animtedListener data', data);
    data.isFree = true;
  };

  renderItem = (item: IBarrage[]) => {
    return item.map(b => {
      return (
        <BarrageItem
          key={b.id}
          data={b}
          heightOfLine={25}
          outside={this.outside}
          animtedListener={this.animtedListener}
        />
      );
    });
  };

  render() {
    const {style} = this.props;
    const {list} = this.state;

    return (
      <View style={[styles.container, style]}>
        {list && list.map(this.renderItem)}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    overflow: 'hidden',
    position: 'absolute',
    left: 0,
    right: 0,
  },
});
