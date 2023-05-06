import 'package:flutter/material.dart';
import 'package:flutter_bili_app/core/hi_base_tab_state.dart';
import 'package:flutter_bili_app/http/dao/notice_dao.dart';
import 'package:flutter_bili_app/model/home_mo.dart';
import 'package:flutter_bili_app/model/notice_mo.dart';
import 'package:flutter_bili_app/widget/notice_card.dart';

///通知列表页
class NoticePage extends StatefulWidget {
  @override
  _NoticePageState createState() => _NoticePageState();
}

class _NoticePageState extends HiBaseTabState<NoticeMo, BannerMo, NoticePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Column(
      children: [_buildNavigationBar(), Expanded(child: super.build(context))],
    ));
  }

  _buildNavigationBar() {
    return AppBar(
      title: Text('通知'),
    );
  }

  @override
  get contentChild => ListView.builder(
        padding: EdgeInsets.only(top: 10),
        itemCount: dataList.length,
        controller: scrollController,
        physics: const AlwaysScrollableScrollPhysics(),
        itemBuilder: (BuildContext context, int index) =>
            NoticeCard(bannerMo: dataList[index]),
      );

  @override
  Future<NoticeMo> getData(int pageIndex) async {
    NoticeMo result =
        await NoticeDao.noticeList(pageIndex: pageIndex, pageSize: 10);
    return result;
  }

  @override
  List<BannerMo> parseList(NoticeMo result) {
    return result.list;
  }
}
