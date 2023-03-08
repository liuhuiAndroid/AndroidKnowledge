import React from 'react';
import {View, StyleSheet, ScrollView, Button, Text} from 'react-native';
import * as Yup from 'yup';
import {Formik, Field, FormikHelpers} from 'formik';
import Input from '@/components/Input';
import {connect, ConnectedProps} from 'react-redux';
import {RootState} from '@/models/index';
import {RootStackNavigation} from '../navigator';

const customervalidation = Yup.object().shape({
  account: Yup.string().required('请输入账号'),
  password: Yup.string().required('请输入密码'),
});

interface Values {
  account: string;
  password: string;
}

const initialValues: Values = {
  account: '',
  password: '',
};

const mapStateToProps = ({login, loading}: RootState) => ({
  loging: login.loging,
  loading: loading.effects['login/login'],
});

const connector = connect(mapStateToProps);

type ModelState = ConnectedProps<typeof connector>;

interface IProps extends ModelState {
  navigation: RootStackNavigation;
}

/**
 * 登录模块
 */
class Login extends React.Component<IProps> {
  onSubmit = (values: Values, actions: FormikHelpers<Values>) => {
    const {dispatch, navigation} = this.props;
    console.log('---values', values);
    dispatch({
      type: 'login/login',
      payload: values,
      callback: () => {
        actions.setSubmitting(false);
        navigation.goBack();
      },
    });
  };

  render() {
    const {loading} = this.props;
    return (
      <ScrollView keyboardShouldPersistTaps="handled" style={styles.container}>
        <Text style={styles.logo}>听书</Text>
        <Formik
          initialValues={initialValues}
          onSubmit={this.onSubmit}
          validationSchema={customervalidation}>
          {({handleSubmit}) => (
            <View>
              <Field
                name="account"
                placeholder="请输入账号"
                component={Input}
              />
              <Field
                name="password"
                placeholder="请输入密码"
                component={Input}
              />
              <Button
                disabled={loading}
                onPress={handleSubmit}
                title="登录"
                color="#ff4000"
              />
            </View>
          )}
        </Formik>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    paddingBottom: 200,
  },
  logo: {
    color: '#ff4000',
    fontWeight: 'bold',
    fontSize: 50,
    textAlign: 'center',
    marginTop: 40,
  },
});

export default connect(mapStateToProps)(Login);
