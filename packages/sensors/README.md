# sensors

**Please set your constraint to `sensors: '>=0.4.y+x <2.0.0'`**

## Backward compatible 1.0.0 version is coming
The sensors plugin has reached a stable API, we guarantee that version `1.0.0` will be backward compatible with `0.4.y+z`.
Please use `sensors: '>=0.4.y+x <2.0.0'` as your dependency constraint to allow a smoother ecosystem migration.
For more details see: https://github.com/flutter/flutter/wiki/Package-migration-to-1.0.0

A Flutter plugin to access the accelerometer and gyroscope sensors.


## Usage

To use this plugin, add `sensors` as a [dependency in your pubspec.yaml
file](https://flutter.dev/docs/development/platform-integration/platform-channels).

This will expose three classes of sensor events, through three different
streams.

- `AccelerometerEvent`s describe the velocity of the device, including the
  effects of gravity. Put simply, you can use accelerometer readings to tell if
  the device is moving in a particular direction.
- `UserAccelerometerEvent`s also describe the velocity of the device, but don't
  include gravity. They can also be thought of as just the user's affect on the
  device.
- `GyroscopeEvent`s describe the rotation of the device.

Each of these is exposed through a `BroadcastStream`: `accelerometerEvents`,
`userAccelerometerEvents`, and `gyroscopeEvents`, respectively.


### Example

``` dart
import 'package:sensors/sensors.dart';

accelerometerEvents.listen((AccelerometerEvent event) {
  print(event);
});
// [AccelerometerEvent (x: 0.0, y: 9.8, z: 0.0)]

userAccelerometerEvents.listen((UserAccelerometerEvent event) {
  print(event);
});
// [UserAccelerometerEvent (x: 0.0, y: 0.0, z: 0.0)]

gyroscopeEvents.listen((GyroscopeEvent event) {
  print(event);
});
// [GyroscopeEvent (x: 0.0, y: 0.0, z: 0.0)]

```

Also see the `example` subdirectory for an example application that uses the
sensor data.
