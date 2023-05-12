import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:tiktok_flutter_module/gen/assets.gen.dart';
import 'package:tiktok_flutter_module/page/mine_page/mine_page_controller.dart';
import 'package:tiktok_flutter_module/widget/t_image.dart';
import 'package:tiktok_flutter_module/widget/text_count.dart';
import 'package:tiktok_flutter_module/widget/video_list/controller/favorite_controller.dart';
import 'package:tiktok_flutter_module/widget/video_list/controller/private_controller.dart';
import 'package:tiktok_flutter_module/widget/video_list/controller/public_controller.dart';
import 'package:tiktok_flutter_module/widget/video_list/widget/video_list.dart';
import 'package:get/get.dart';

import '../../widget/video_list/controller/mark_controller.dart';

class MinePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _MinePageState();
}

class _MinePageState extends State<MinePage> with SingleTickerProviderStateMixin {
  static const image_height = 138.5;

  final _controller = Get.put(MinePageController());

  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 4, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return NestedScrollView(
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          return <Widget>[
            SliverAppBar(
              pinned: true,
              floating: true,
              expandedHeight: 420,
              flexibleSpace: FlexibleSpaceBar(
                collapseMode: CollapseMode.pin,
                background: Stack(children: [
                  // 背景墙
                  Container(
                      width: double.infinity,
                      height: image_height,
                      child: GestureDetector(
                          child: TImage(_controller.backgroundUrl, fit: BoxFit.cover),
                          onTap: _controller.onTapBackground)),
                  // 资料页卡
                  Padding(
                    padding: EdgeInsets.only(top: image_height - 4),
                    child: Container(
                        decoration: BoxDecoration(
                            color: Color(0xfffefdfd), borderRadius: BorderRadius.vertical(top: Radius.circular(8))),
                        child: _buildCard()),
                  ),
                  // 头像
                  Padding(
                      padding: EdgeInsets.only(top: 114, left: 19),
                      child: GestureDetector(
                        child: Obx(() => TImage(_controller.avatarUrl, shape: Shape.CIRCLE, radius: 40)),
                        onTap: _controller.onTapAvatar,
                      ))
                ]),
              ),
              bottom: TabBar(
                  labelColor: Color(0xff151822),
                  unselectedLabelColor: Color(0xff77767c),
                  controller: _tabController,
                  tabs: [
                    Tab(text: '作品'),
                    Tab(text: '私密'),
                    Tab(text: '收藏'),
                    Tab(text: '喜欢'),
                  ]),
            )
          ];
        },
        body: TabBarView(
          controller: _tabController,
          children: [
            VideoList(PublicController()),
            VideoList(PrivateController()),
            VideoList(MarkController()),
            VideoList(FavoriteController())
          ],
        ));
  }

  Widget _buildCard() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(height: 18),
        // 赞、关注、粉丝
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            SizedBox(width: 100),
            Expanded(child: TextCount('获赞', _controller.likeCount)),
            Container(width: 1, height: 33, color: Color(0xffe3e2e2)),
            Expanded(child: TextCount('关注', _controller.focusCount)),
            Container(width: 1, height: 33, color: Color(0xffe3e2e2)),
            Expanded(child: TextCount('粉丝', _controller.followCount)),
          ],
        ),
        // 姓名
        Padding(
          padding: EdgeInsets.only(top: 30, left: 19),
          child: Obx(() => Text(
                _controller.name,
                style: TextStyle(
                    color: Colors.black, fontSize: 20, fontWeight: FontWeight.bold, decoration: TextDecoration.none),
              )),
        ),
        // uid
        Padding(
            padding: EdgeInsets.only(top: 5, left: 19),
            child: Obx(() => Text(
                  _controller.uidDesc,
                  style: TextStyle(color: Colors.black, fontSize: 11, decoration: TextDecoration.none),
                ))),
        // 分割线
        Container(
          height: 1 / MediaQueryData.fromWindow(window).devicePixelRatio,
          margin: EdgeInsets.only(left: 19, right: 16, top: 10, bottom: 12),
          color: Color(0xffe1e1e3),
        ),
        // 介绍编辑
        Container(
          height: 18,
          padding: EdgeInsets.only(left: 16),
          child: Row(
            children: [
              Text(
                '点击添加介绍，让大家认识你...',
                style: TextStyle(color: Color(0xff72737a), fontSize: 12, decoration: TextDecoration.none),
              ),
              TImage(Assets.image.edit.path, height: 12)
            ],
          ),
        ),
        // +关注
        Padding(
            padding: EdgeInsets.only(top: 18, left: 16, right: 16),
            child: Container(
              decoration: BoxDecoration(color: Color(0xfffe2d54), borderRadius: BorderRadius.circular(4)),
              child: Text('+ 关注',
                  style: TextStyle(color: Color(0xfffbfbfc), fontSize: 15, decoration: TextDecoration.none)),
              width: double.infinity,
              height: 36,
              alignment: Alignment.center,
            ))
      ],
    );
  }
}
