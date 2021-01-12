enum WbMode {
  auto,
  incandescent,
  fluorescent,
  warm_fluorescent,
  daylight,
  cloudy_daylight,
  twilight,
  shade,
}

String serializeWbMode(WbMode exposureMode) {
  switch (exposureMode) {
    case WbMode.auto:
      return 'auto';
    case WbMode.incandescent:
      return 'incandescent';
    case WbMode.fluorescent:
      return 'fluorescent';
    case WbMode.warm_fluorescent:
      return 'warm_fluorescent';
    case WbMode.daylight:
      return 'daylight';
    case WbMode.cloudy_daylight:
      return 'cloudy_daylight';
    case WbMode.twilight:
      return 'twilight';
    case WbMode.shade:
      return 'shade';
    default:
      throw ArgumentError('Unknown WbMode value');
  }
}

/// Returns the exposure mode for a given String.
WbMode deserializeWbMode(String str) {
  switch (str) {
    case "auto":
      return WbMode.auto;
    case "incandescent":
      return WbMode.incandescent;
    case "fluorescent":
      return WbMode.fluorescent;
    case "warm_fluorescent":
      return WbMode.warm_fluorescent;
    case "daylight":
      return WbMode.daylight;
    case "cloudy_daylight":
      return WbMode.cloudy_daylight;
    case "twilight":
      return WbMode.twilight;
    case "shade":
      return WbMode.shade;
    default:
      throw ArgumentError('"$str" is not a valid WbMode value');
  }
}
