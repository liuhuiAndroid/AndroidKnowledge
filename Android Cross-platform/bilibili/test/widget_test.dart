// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter_test/flutter_test.dart';
import 'package:hi_cache/hi_cache.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  // testWidgets('Counter increments smoke test', (WidgetTester tester) async {
  //   // Build our app and trigger a frame.
  // });

  TestWidgetsFlutterBinding.ensureInitialized();
  test('test HiCache', () async {
    SharedPreferences.setMockInitialValues({});
    await HiCache.preInit();
    var key = 'testKey', value = 'hello';
    HiCache.getInstance().get(key);
    expect(HiCache.getInstance().get(key), value);
  });
}
