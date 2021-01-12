package io.flutter.plugins.camera.types;

// Mirrors iso_mode.dart
public enum IsoMode {
  auto("auto"),
  locked("locked");

  private final String strValue;

  IsoMode(String strValue) {
    this.strValue = strValue;
  }

  public static IsoMode getValueForString(String modeStr) {
    for (IsoMode value : values()) {
      if (value.strValue.equals(modeStr)) return value;
    }
    return null;
  }

  @Override
  public String toString() {
    return strValue;
  }
}
