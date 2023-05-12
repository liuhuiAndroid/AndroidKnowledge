import 'package:flutter_test/flutter_test.dart';
import 'package:tiktok_player/tiktok_player.dart';
import 'package:tiktok_player/tiktok_player_platform_interface.dart';
import 'package:tiktok_player/tiktok_player_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockTiktokPlayerPlatform
    with MockPlatformInterfaceMixin
    implements TiktokPlayerPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final TiktokPlayerPlatform initialPlatform = TiktokPlayerPlatform.instance;

  test('$MethodChannelTiktokPlayer is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelTiktokPlayer>());
  });

  test('getPlatformVersion', () async {
    TiktokPlayer tiktokPlayerPlugin = TiktokPlayer();
    MockTiktokPlayerPlatform fakePlatform = MockTiktokPlayerPlatform();
    TiktokPlayerPlatform.instance = fakePlatform;

    expect(await tiktokPlayerPlugin.getPlatformVersion(), '42');
  });
}
