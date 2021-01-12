enum IsoMode {
  /// Automatically determine exposure settings.
  auto,

  /// Lock the currently determined exposure settings.
  locked,
}

/// Returns the exposure mode as a String.
String serializeIsoMode(IsoMode exposureMode) {
  switch (exposureMode) {
    case IsoMode.locked:
      return 'locked';
    case IsoMode.auto:
      return 'auto';
    default:
      throw ArgumentError('Unknown IsoMode value');
  }
}

/// Returns the exposure mode for a given String.
IsoMode deserializeIsoMode(String str) {
  switch (str) {
    case "locked":
      return IsoMode.locked;
    case "auto":
      return IsoMode.auto;
    default:
      throw ArgumentError('"$str" is not a valid IsoMode value');
  }
}