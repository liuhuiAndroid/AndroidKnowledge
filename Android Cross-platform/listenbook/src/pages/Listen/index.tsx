import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {connect, ConnectedProps} from 'react-redux';
import Touchable from '@/components/Touchable';
import Authorized from '@/components/Authorized';
import {ModalStackNavigation} from '@/navigator/index';
import {RootState} from '@/models/index';
import Subscription from './Subscription';

const mapStateToProps = ({login}: RootState) => ({
  user: login.user,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: ModalStackNavigation;
}

/**
 * 我听模块
 */
class Listen extends React.PureComponent<IProps> {
  goHistory = () => {
    const {navigation} = this.props;
    navigation.navigate('History');
  };

  goLogin = () => {
    const {navigation} = this.props;
    navigation.navigate('Login');
  };

  renderUnLogin = () => {
    return (
      <View style={styles.unLoginView}>
        <Text>登录后同步订阅内容</Text>
        <Touchable onPress={this.goLogin}>
          <Text style={styles.loginText}>请登录</Text>
        </Touchable>
      </View>
    );
  };

  render() {
    const {user} = this.props;
    return (
      <View style={styles.container}>
        <View style={styles.actions}>
          <Touchable style={styles.action} onPress={this.goHistory}>
            <Text>历史</Text>
          </Touchable>
        </View>
        <Authorized authority={!!user} noMatch={this.renderUnLogin}>
          <Subscription />
        </Authorized>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  actions: {
    padding: 25,
    margin: 15,
    flexDirection: 'row',
    shadowOffset: {width: 0, height: 5},
    shadowOpacity: 0.5,
    shadowRadius: 10,
    shadowColor: '#ccc',
    elevation: 4,
    backgroundColor: '#fff',
    borderRadius: 2,
  },
  action: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  unLoginView: {
    padding: 10,
    alignItems: 'center',
  },
  loginText: {
    margin: 10,
    color: '#f86442',
  },
});

export default connect(mapStateToProps)(Listen);
