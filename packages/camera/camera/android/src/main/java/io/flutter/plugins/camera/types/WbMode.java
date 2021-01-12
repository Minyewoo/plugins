package io.flutter.plugins.camera.types;

// Mirrors wb_mode.dart
public enum WbMode {
  auto("auto"),
  incandescent("incandescent"),
  fluorescent("fluorescent"),
  warm_fluorescent("warm_fluorescent"),
  daylight("daylight"),
  cloudy_daylight("cloudy_daylight"),
  twilight("twilight"),
  shade("shade");
  
  private final String strValue;

  WbMode(String strValue) {
    this.strValue = strValue;
  }

  public static WbMode getValueForString(String modeStr) {
    for (WbMode value : values()) {
      if (value.strValue.equals(modeStr)) return value;
    }
    return null;
  }

  @Override
  public String toString() {
    return strValue;
  }
}