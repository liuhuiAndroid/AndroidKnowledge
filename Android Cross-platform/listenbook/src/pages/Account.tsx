import React from 'react';
import {ScrollView, View, Text, StyleSheet, Image} from 'react-native';
import Touchable from '@/components/Touchable';
import defaultAvatar from '@/assets/default_avatar.png';
import {connect, ConnectedProps} from 'react-redux';
import Authorized from '@/components/Authorized';
import {viewportWidth} from '@/utils/index';
import {ModalStackNavigation} from '@/navigator/index';
import {RootState} from '@/models/index';

const mapStateToProps = ({login}: RootState) => ({
  user: login.user,
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: ModalStackNavigation;
}

/**
 * 我的模块
 */
class Account extends React.PureComponent<IProps> {
  goLogin = () => {
    const {navigation} = this.props;
    navigation.navigate('Login');
  };

  logout = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'login/logout',
    });
  };

  renderUnLoginHeader = () => {
    return (
      <View style={styles.header}>
        <Image source={defaultAvatar} style={styles.image} />
        <View style={styles.headerRight}>
          <Touchable onPress={this.goLogin} style={styles.loginButton}>
            <Text style={styles.loginButtonText}>立即登录</Text>
          </Touchable>
          <Text style={styles.tip}>登录后自动同步所有记录哦～</Text>
        </View>
      </View>
    );
  };

  render() {
    const {user} = this.props;
    return (
      <ScrollView style={styles.container}>
        <Authorized authority={!!user} noMatch={this.renderUnLoginHeader}>
          <View style={styles.header}>
            <Image source={{uri: user && user.avatar}} style={styles.image} />
            <View style={styles.headerRight}>
              <Text style={styles.loginButtonText}>{user && user.name}</Text>
              <Text style={styles.tip} />
            </View>
          </View>
          <View style={styles.logoutView}>
            <Touchable onPress={this.logout} style={styles.logoutButton}>
              <Text>退出登录</Text>
            </Touchable>
          </View>
        </Authorized>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    margin: 15,
  },
  headerRight: {
    marginLeft: 15,
    justifyContent: 'center',
  },
  image: {
    width: 70,
    height: 70,
    borderRadius: 35,
  },
  loginButton: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 26,
    width: 76,
    borderRadius: 13,
    borderColor: '#f86442',
    borderWidth: 1,
    marginBottom: 7,
  },
  loginButtonText: {
    color: '#f86442',
    fontWeight: '900',
  },
  tip: {
    color: '#666',
  },
  logoutView: {
    alignItems: 'center',
    marginTop: 200,
  },
  logoutButton: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 40,
    width: viewportWidth - 40,
    borderRadius: 4,
    borderColor: '#ccc',
    borderWidth: 1,
    marginBottom: 7,
  },
});

export default connect(mapStateToProps)(Account);
