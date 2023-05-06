import 'package:flutter/material.dart';
import 'package:flutter_bili_app/core/hi_base_tab_state.dart';
import 'package:flutter_bili_app/http/dao/favorite_dao.dart';
import 'package:flutter_bili_app/model/ranking_mo.dart';
import 'package:flutter_bili_app/model/video_model.dart';
import 'package:flutter_bili_app/navigator/hi_navigator.dart';
import 'package:flutter_bili_app/page/video_detail_page.dart';
import 'package:flutter_bili_app/widget/navigation_bar.dart';
import 'package:flutter_bili_app/widget/video_large_card.dart';

import '../util/view_util.dart';

///收藏

class FavoritePage extends StatefulWidget {
  @override
  _FavoritePageState createState() => _FavoritePageState();
}

class _FavoritePageState
    extends HiBaseTabState<RankingMo, VideoModel, FavoritePage> {
  late RouteChangeListener listener;

  @override
  void initState() {
    super.initState();
    HiNavigator.getInstance().addListener(this.listener = (current, pre) {
      if (pre?.page is VideoDetailPage && current.page is FavoritePage) {
        loadData();
      }
    });
  }

  @override
  void dispose() {
    HiNavigator.getInstance().removeListener(this.listener);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [_buildNavigationBar(), Expanded(child: super.build(context))],
    );
  }

  _buildNavigationBar() {
    return NavigationBar(
      child: Container(
        decoration: bottomBoxShadow(context),
        alignment: Alignment.center,
        child: Text('收藏', style: TextStyle(fontSize: 16)),
      ),
    );
  }

  @override
  get contentChild => ListView.builder(
        padding: EdgeInsets.only(top: 10),
        itemCount: dataList.length,
        controller: scrollController,
        physics: const AlwaysScrollableScrollPhysics(),
        itemBuilder: (BuildContext context, int index) =>
            VideoLargeCard(videoModel: dataList[index]),
      );

  @override
  Future<RankingMo> getData(int pageIndex) async {
    RankingMo result =
        await FavoriteDao.favoriteList(pageIndex: pageIndex, pageSize: 10);
    return result;
  }

  @override
  List<VideoModel> parseList(RankingMo result) {
    return result.list;
  }
}
