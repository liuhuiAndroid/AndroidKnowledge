import 'package:flutter/material.dart';
import 'package:flutter_bili_app/core/hi_base_tab_state.dart';
import 'package:flutter_bili_app/http/dao/ranking_dao.dart';
import 'package:flutter_bili_app/model/ranking_mo.dart';
import 'package:flutter_bili_app/model/video_model.dart';
import 'package:flutter_bili_app/widget/video_large_card.dart';

class RankingTabPage extends StatefulWidget {
  final String sort;

  const RankingTabPage({Key? key, required this.sort}) : super(key: key);

  @override
  _RankingTabPageState createState() => _RankingTabPageState();
}

class _RankingTabPageState
    extends HiBaseTabState<RankingMo, VideoModel, RankingTabPage> {
  @override
  get contentChild => Container(
        child: ListView.builder(
            physics: AlwaysScrollableScrollPhysics(),
            padding: EdgeInsets.only(top: 10),
            itemCount: dataList.length,
            controller: scrollController,
            itemBuilder: (BuildContext context, int index) =>
                VideoLargeCard(videoModel: dataList[index])),
      );

  @override
  Future<RankingMo> getData(int pageIndex) async {
    RankingMo result =
        await RankingDao.get(widget.sort, pageIndex: pageIndex, pageSize: 20);
    return result;
  }

  @override
  List<VideoModel> parseList(RankingMo result) {
    return result.list;
  }
}
