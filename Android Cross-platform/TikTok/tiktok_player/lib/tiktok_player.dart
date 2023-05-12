import 'package:fijkplayer/fijkplayer.dart';

class Player extends FijkPlayer {
  static const asset_url_suffix = "asset:///";

  void setCommonDataSource(
    String url, {
    SourceType type = SourceType.net,
    bool autoPlay = false,
    bool showCover = false,
  }) {
    if (type == SourceType.asset && !url.startsWith(asset_url_suffix)) {
      url = asset_url_suffix + url;
    }
    setDataSource(url, autoPlay: autoPlay, showCover: showCover);
  }
}

enum SourceType { net, asset }
