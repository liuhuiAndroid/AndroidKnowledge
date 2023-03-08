import React, {Fragment} from 'react';

import {
  StyleSheet,
  Text,
  View,
  Animated,
  Platform,
  Easing,
  StyleProp,
  ViewStyle,
} from 'react-native';
import LinearGradient from 'react-native-linear-gradient';
import Barrage from '@/components/Barrage';
import Touchable from '@/components/Touchable';
import {connect, ConnectedProps} from 'react-redux';
import {viewportWidth, statusBarHeight} from '@/utils/index';
import PlayBar from './PlayBar';
import {RootStackNavigation, ModalStackParamList} from '@/navigator/index';
import {RouteProp} from '@react-navigation/native';
import {RootState} from '@/models/index';

const mapStateToProps = ({player, loading}: RootState) => ({
  playState: player.playState,
  show: player.show,
  loading: loading.effects['player/fetchShow'],
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: RootStackNavigation;
  route: RouteProp<ModalStackParamList, 'ProgramDetail'>;
}

const IMAGE_WIDTH = 180;

const SCALE = viewportWidth / (IMAGE_WIDTH * 0.9);

const data: string[] = [
  '最灵繁的人也看不见自己的背脊',
  '朝闻道，夕死可矣',
  '阅读是人类进步的阶梯',
  '内外相应，言行相称',
  '人的一生是短的',
  '抛弃时间的人，时间也抛弃他',
  '自信在于沉稳',
  '过犹不及',
  '开卷有益',
  '有志者事竟成',
  '合理安排时间，就等于节约时间',
  '成功源于不懈的努力',
];

const getRundomNumber = (max: number) => {
  return Math.floor(Math.random() * (max + 1));
};

const getText = () => {
  const number = getRundomNumber(data.length - 1);
  return data[number];
};

interface IState {
  data: {
    id: number;
    title: string;
  }[];
  barrage: boolean;
}

/**
 * 频道详情模块
 */
class ProgramDetail extends React.Component<IProps, IState> {
  state = {
    barrage: false,
    data: [],
  };

  id = 0;

  interval = -1;

  imageStyle: StyleProp<ViewStyle>;

  anim = new Animated.Value(1);

  componentDidMount() {
    const {dispatch, playState, route} = this.props;
    this.addBarrageWithInterval();
    if (playState !== 'playing' || (route.params && route.params.id)) {
      dispatch({
        type: 'player/fetchShow',
        payload: {
          id: route.params && route.params.id,
        },
      });
    }
  }

  componentDidUpdate(prevProps: IProps) {
    // 如果数据发生变化，则更新图表
    if (prevProps.show.title !== this.props.show.title) {
      this.props.navigation.setOptions({
        headerTitle: this.props.show.title,
      });
    }
  }

  componentWillUnmount() {
    this.interval && clearInterval(this.interval);
  }

  addBarrageWithInterval = () => {
    this.interval = setInterval(() => {
      const {barrage} = this.state;
      if (barrage) {
        this.id = Date.now();
        const text = getText();
        const newData = [{title: text, id: this.id}];
        this.setState({data: newData});
      } else if (!barrage && this.state.data.length !== 0) {
        this.setState({
          data: [],
        });
      }
    }, 500);
  };

  goBack = () => {
    const {navigation} = this.props;
    navigation.goBack();
  };

  danmu = () => {
    const {barrage} = this.state;
    this.setState({
      barrage: !barrage,
    });

    Animated.timing(this.anim, {
      toValue: !barrage ? SCALE : 1,
      duration: 100,
      easing: Easing.linear, // 线性的渐变函数
    }).start();
  };

  render() {
    const {show} = this.props;
    const {barrage, data} = this.state;
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <View style={styles.imageView}>
            <Animated.Image
              style={[
                styles.image,
                {
                  transform: [
                    {
                      scale: this.anim,
                    },
                  ],
                },
              ]}
              source={{
                uri: show.thumbnailUrl,
              }}
            />
          </View>
          {barrage && (
            <Fragment>
              <LinearGradient
                colors={[
                  'rgba(128, 104, 102, 0.5)',
                  'rgba(128, 104, 102, 0.8)',
                  '#807c66',
                  '#807c66',
                ]}
                style={styles.linear}
              />
              <Barrage style={styles.barrage} data={data} maxTrack={5} />
            </Fragment>
          )}
        </View>
        <View>
          <Touchable onPress={this.danmu} style={styles.danmuBtn}>
            <Text style={styles.danmuText}>弹幕</Text>
          </Touchable>
        </View>
        <PlayBar />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#807c66',
  },
  scroll: {
    flex: 1,
  },
  header: {
    position: 'absolute',
    right: 0,
    left: 0,
  },
  back: {
    marginTop: statusBarHeight,
    marginHorizontal: 20,
  },
  content: {
    paddingTop: 95,
    alignItems: 'center',
  },
  linear: {
    position: 'absolute',
    top: 0,
    height: viewportWidth + 100,
    width: viewportWidth,
  },
  barrage: {
    height: 400,
    top: 100,
  },
  title: {
    fontSize: 18,
    fontWeight: '900',
    color: '#fff',
  },
  imageView: {
    flexDirection: 'row',
    ...Platform.select({
      ios: {
        shadowOffset: {width: 0, height: 5},
        shadowOpacity: 0.5,
        shadowRadius: 10,
        shadowColor: '#000',
      },
    }),
    backgroundColor: '#fff',
    borderRadius: 8,
  },
  image: {
    width: IMAGE_WIDTH,
    height: IMAGE_WIDTH,
    borderRadius: 8,
  },
  control: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginVertical: 15,
    marginHorizontal: 90,
  },
  thumb: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 76,
    height: 20,
  },
  danmuBtn: {
    marginLeft: 10,
    height: 20,
    width: 40,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    borderColor: '#fff',
    borderWidth: 1,
  },
  danmuText: {
    color: '#fff',
  },
});

export default connect(mapStateToProps)(ProgramDetail);
