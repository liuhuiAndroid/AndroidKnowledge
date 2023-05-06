import 'package:flutter/material.dart';
import 'package:flutter_bili_app/core/hi_base_tab_state.dart';
import 'package:flutter_bili_app/http/dao/home_dao.dart';
import 'package:flutter_bili_app/model/home_mo.dart';
import 'package:flutter_bili_app/model/video_model.dart';
import 'package:flutter_bili_app/widget/hi_banner.dart';
import 'package:flutter_bili_app/widget/video_card.dart';
import 'package:flutter_nested/flutter_nested.dart';

class HomeTabPage extends StatefulWidget {
  final String categoryName;
  final List<BannerMo>? bannerList;

  const HomeTabPage({Key? key, required this.categoryName, this.bannerList})
      : super(key: key);

  @override
  _HomeTabPageState createState() => _HomeTabPageState();
}

class _HomeTabPageState
    extends HiBaseTabState<HomeMo, VideoModel, HomeTabPage> {
  @override
  void initState() {
    super.initState();
    print(widget.categoryName);
    print(widget.bannerList);
  }

  _banner(List<BannerMo> bannerList) {
    return HiBanner(bannerList, padding: EdgeInsets.only(left: 5, right: 5));
  }

  @override
  bool get wantKeepAlive => true;

  @override
  get contentChild => HiNestedScrollView(
      controller: scrollController,
      itemCount: dataList.length,
      padding: EdgeInsets.only(top: 10, left: 10, right: 10),
      headers: [
        if (widget.bannerList != null)
          Padding(
              padding: EdgeInsets.only(bottom: 8),
              child: _banner(widget.bannerList!))
      ],
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2, childAspectRatio: 0.82),
      itemBuilder: (BuildContext context, int index) {
        return VideoCard(videoMo: dataList[index]);
      });

  @override
  Future<HomeMo> getData(int pageIndex) async {
    HomeMo result = await HomeDao.get(widget.categoryName,
        pageIndex: pageIndex, pageSize: 10);
    return result;
  }

  @override
  List<VideoModel> parseList(HomeMo result) {
    return result.videoList;
  }
}
