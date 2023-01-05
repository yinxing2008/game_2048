import 'dart:ui';

class ColorUtil {
  /// 背景颜色
  static Color bgColor1 = const Color(0xFFFAF8EF);
  static Color bgColor2 = const Color(0xFFBBAD9F);
  static Color bgColor3 = const Color(0xFF8F7B65);

  /// 文字颜色
  static Color textColor1 = const Color(0xFF766E65);
  static Color textColor2 = const Color(0xFFF8F6F2);
  static Color textColor3 = const Color(0xFFFFFFFF);

  /// 数字的背景色
  static Color gc0 = const Color(0xFFCCC1B4);

  /// 不展示数字
  static Color gc2 = const Color(0xFFEDE4DA);
  static Color gc4 = const Color(0xFFEEE0CA);
  static Color gc8 = const Color(0xFFF3B279);
  static Color gc16 = const Color(0xFFF69564);
  static Color gc32 = const Color(0xFFF77C5F);
  static Color gc64 = const Color(0xFFF75F3C);
  static Color gc128 = const Color(0xFFEDD073);
  static Color gc256 = const Color(0xFFEECB62);
  static Color gc512 = const Color(0xFFEDC850);
  static Color gc1024 = const Color(0xFFEDC850);
  static Color gc2048 = const Color(0xFFEDC850);

  static Color mapValueToColor(int value) {
    switch (value) {
      case 0:
        return ColorUtil.gc0;
      case 2:
        return ColorUtil.gc2;
      case 4:
        return ColorUtil.gc4;
      case 8:
        return ColorUtil.gc8;
      case 16:
        return ColorUtil.gc16;
      case 32:
        return ColorUtil.gc32;
      case 64:
        return ColorUtil.gc64;
      case 128:
        return ColorUtil.gc128;
      case 256:
        return ColorUtil.gc256;
      case 512:
        return ColorUtil.gc512;
      case 1024:
        return ColorUtil.gc1024;
      case 2048:
        return ColorUtil.gc2048;
      default:
        return ColorUtil.gc2048;
    }
  }
}
