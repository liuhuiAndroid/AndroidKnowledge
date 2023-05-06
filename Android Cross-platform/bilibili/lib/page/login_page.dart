import 'package:flutter/material.dart';
import 'package:flutter_bili_app/http/dao/login_dao.dart';
import 'package:flutter_bili_app/navigator/hi_navigator.dart';
import 'package:flutter_bili_app/util/toast.dart';
import 'package:flutter_bili_app/widget/appbar.dart';
import 'package:flutter_bili_app/widget/login_button.dart';
import 'package:flutter_bili_app/widget/login_effect.dart';
import 'package:flutter_bili_app/widget/login_input.dart';
import 'package:hi_base/string_util.dart';
import 'package:hi_net/core/hi_error.dart';

///登录页面
class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  bool protect = false;
  bool loginEnable = false;
  String? userName;
  String? password;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar('密码登录', '注册', () {
        HiNavigator.getInstance().onJumpTo(RouteStatus.registration);
      }, key: Key('registration')),
      body: Container(
        child: ListView(
          children: [
            LoginEffect(protect: protect),
            LoginInput(
              '用户名',
              '请输入用户',
              onChanged: (text) {
                userName = text;
                checkInput();
              },
            ),
            LoginInput(
              '密码',
              '请输入密码',
              obscureText: true,
              onChanged: (text) {
                password = text;
                checkInput();
              },
              focusChanged: (focus) {
                this.setState(() {
                  protect = focus;
                });
              },
            ),
            Padding(
                padding: EdgeInsets.only(left: 20, right: 20, top: 20),
                child: LoginButton(
                  '登录',
                  enable: loginEnable,
                  onPressed: send,
                ))
          ],
        ),
      ),
    );
  }

  void checkInput() {
    bool enable;
    if (isNotEmpty(userName) && isNotEmpty(password)) {
      enable = true;
    } else {
      enable = false;
    }
    setState(() {
      loginEnable = enable;
    });
  }

  void send() async {
    try {
      var result = await LoginDao.login(userName!, password!);
      print(result);
      if (result['code'] == 0) {
        print('登录成功');
        showToast('登录成功');
        HiNavigator.getInstance().onJumpTo(RouteStatus.home);
      } else {
        print(result['msg']);
        showWarnToast(result['msg']);
      }
    } on NeedAuth catch (e) {
      print(e);
      showWarnToast(e.message);
    } on HiNetError catch (e) {
      print(e);
      showWarnToast(e.message);
    }
  }
}
