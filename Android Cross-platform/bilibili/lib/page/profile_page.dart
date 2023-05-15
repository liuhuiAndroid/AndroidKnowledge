import 'package:bilibili/http/dao/profile_dao.dart';
import 'package:bilibili/model/profile_mo.dart';
import 'package:bilibili/util/toast.dart';
import 'package:bilibili/widget/benefit_card.dart';
import 'package:bilibili/widget/course_card.dart';
import 'package:bilibili/widget/dark_mode_item.dart';
import 'package:bilibili/widget/hi_banner.dart';
import 'package:bilibili/widget/hi_blur.dart';
import 'package:bilibili/widget/hi_flexible_header.dart';
import 'package:flutter/material.dart';
import 'package:hi_base/view_util.dart';
import 'package:hi_net/core/hi_error.dart';

///我的
class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<StatefulWidget> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage>
    with AutomaticKeepAliveClientMixin {
  ProfileMo? _profileMo;
  final ScrollController _controller = ScrollController();

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      body: NestedScrollView(
        controller: _controller,
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          return <Widget>[_buildAppBar()];
        },
        body: ListView(
          padding: const EdgeInsets.only(top: 10),
          children: [..._buildContentList()],
        ),
      ),
    );
  }

  void _loadData() async {
    try {
      ProfileMo result = await ProfileDao.get();
      print(result);
      setState(() {
        _profileMo = result;
      });
    } on NeedAuth catch (e) {
      print(e);
      showWarnToast(e.message);
    } on HiNetError catch (e) {
      print(e);
      showWarnToast(e.message);
    }
  }

  _buildHead() {
    if (_profileMo == null) return Container();
    return HiFlexibleHeader(
        name: _profileMo!.name,
        face: _profileMo!.face,
        controller: _controller);
  }

  @override
  bool get wantKeepAlive => true;

  _buildAppBar() {
    return SliverAppBar(
      //扩展高度
      expandedHeight: 160,
      //标题栏是否固定
      pinned: true,
      //定义滚动空间
      flexibleSpace: FlexibleSpaceBar(
        collapseMode: CollapseMode.parallax,
        titlePadding: const EdgeInsets.only(left: 0),
        title: _buildHead(),
        background: Stack(
          children: [
            Positioned.fill(
                child: cachedImage(
                    'https://www.devio.org/img/beauty_camera/beauty_camera4.jpg')),
            const Positioned.fill(child: HiBlur(sigma: 20)),
            Positioned(bottom: 0, left: 0, right: 0, child: _buildProfileTab())
          ],
        ),
      ),
    );
  }

  _buildContentList() {
    if (_profileMo == null) {
      return [];
    }
    return [
      _buildBanner(),
      CourseCard(courseList: _profileMo!.courseList),
      BenefitCard(benefitList: _profileMo!.benefitList),
      DarkModelItem()
    ];
  }

  _buildBanner() {
    return HiBanner(_profileMo!.bannerList,
        bannerHeight: 120, padding: const EdgeInsets.only(left: 10, right: 10));
  }

  _buildProfileTab() {
    if (_profileMo == null) return Container();
    return Container(
      padding: const EdgeInsets.only(top: 5, bottom: 5),
      decoration: const BoxDecoration(color: Colors.white54),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _buildIconText('收藏', _profileMo!.favorite),
          _buildIconText('点赞', _profileMo!.like),
          _buildIconText('浏览', _profileMo!.browsing),
          _buildIconText('金币', _profileMo!.coin),
          _buildIconText('粉丝', _profileMo!.fans),
        ],
      ),
    );
  }

  _buildIconText(String text, int count) {
    return Column(
      children: [
        Text('$count',
            style: const TextStyle(fontSize: 15, color: Colors.black87)),
        Text(text, style: TextStyle(fontSize: 12, color: Colors.grey[600])),
      ],
    );
  }
}
