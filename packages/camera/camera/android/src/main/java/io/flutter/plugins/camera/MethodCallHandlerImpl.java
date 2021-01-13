// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camera;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugins.camera.CameraPermissions.PermissionsRegistry;
import io.flutter.plugins.camera.types.ExposureMode;
import io.flutter.plugins.camera.types.FlashMode;
import io.flutter.plugins.camera.types.FocusMode;
import io.flutter.plugins.camera.types.WbMode;
import io.flutter.plugins.camera.types.IsoMode;
import io.flutter.view.TextureRegistry;
import java.util.HashMap;
import java.util.Map;

final class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
  private final Activity activity;
  private final BinaryMessenger messenger;
  private final CameraPermissions cameraPermissions;
  private final PermissionsRegistry permissionsRegistry;
  private final TextureRegistry textureRegistry;
  private final MethodChannel methodChannel;
  private final EventChannel imageStreamChannel;
  private @Nullable Camera camera;

  MethodCallHandlerImpl(
      Activity activity,
      BinaryMessenger messenger,
      CameraPermissions cameraPermissions,
      PermissionsRegistry permissionsAdder,
      TextureRegistry textureRegistry) {
    this.activity = activity;
    this.messenger = messenger;
    this.cameraPermissions = cameraPermissions;
    this.permissionsRegistry = permissionsAdder;
    this.textureRegistry = textureRegistry;

    methodChannel = new MethodChannel(messenger, "plugins.flutter.io/camera");
    imageStreamChannel = new EventChannel(messenger, "plugins.flutter.io/camera/imageStream");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    switch (call.method) {
      case "availableCameras":
        try {
          result.success(CameraUtils.getAvailableCameras(activity));
        } catch (Exception e) {
          handleException(e, result);
        }
        break;
      case "create":
        {
          if (camera != null) {
            camera.close();
          }

          cameraPermissions.requestPermissions(
              activity,
              permissionsRegistry,
              call.argument("enableAudio"),
              (String errCode, String errDesc) -> {
                if (errCode == null) {
                  try {
                    instantiateCamera(call, result);
                  } catch (Exception e) {
                    handleException(e, result);
                  }
                } else {
                  result.error(errCode, errDesc, null);
                }
              });
          break;
        }
      case "initialize":
        {
          if (camera != null) {
            try {
              camera.open(call.argument("imageFormatGroup"));
              result.success(null);
            } catch (Exception e) {
              handleException(e, result);
            }
          } else {
            result.error(
                "cameraNotFound",
                "Camera not found. Please call the 'create' method before calling 'initialize'.",
                null);
          }
          break;
        }
      case "takePicture":
        {
          camera.takePicture(result);
          break;
        }
      case "prepareForVideoRecording":
        {
          // This optimization is not required for Android.
          result.success(null);
          break;
        }
      case "startVideoRecording":
        {
          camera.startVideoRecording(result);
          break;
        }
      case "stopVideoRecording":
        {
          camera.stopVideoRecording(result);
          break;
        }
      case "pauseVideoRecording":
        {
          camera.pauseVideoRecording(result);
          break;
        }
      case "resumeVideoRecording":
        {
          camera.resumeVideoRecording(result);
          break;
        }
      case "setFlashMode":
        {
          String modeStr = call.argument("mode");
          FlashMode mode = FlashMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setFlashModeFailed", "Unknown flash mode " + modeStr, null);
            return;
          }
          try {
            camera.setFlashMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setWbMode":
        {
          String modeStr = call.argument("mode");
          WbMode mode = WbMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setWbModeFailed", "Unknown wb mode " + modeStr, null);
            return;
          }
          try {
            camera.setWbMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setExposureMode":
        {
          String modeStr = call.argument("mode");
          ExposureMode mode = ExposureMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setExposureModeFailed", "Unknown exposure mode " + modeStr, null);
            return;
          }
          try {
            camera.setExposureMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setExposurePoint":
        {
          Boolean reset = call.argument("reset");
          Double x = null;
          Double y = null;
          if (reset == null || !reset) {
            x = call.argument("x");
            y = call.argument("y");
          }
          try {
            camera.setExposurePoint(result, x, y);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMinExposureOffset":
        {
          try {
            result.success(camera.getMinExposureOffset());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMaxExposureOffset":
        {
          try {
            result.success(camera.getMaxExposureOffset());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getExposureOffsetStepSize":
        {
          try {
            result.success(camera.getExposureOffsetStepSize());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setExposureOffset":
        {
          try {
            camera.setExposureOffset(result, call.argument("offset"));
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMinExposureTime":
        {
          try {
            result.success(camera.getMinExposureTime());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMaxExposureTime":
        {
          try {
            result.success(camera.getMaxExposureTime());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setExposureTime":
        {
          try {
            camera.setExposureTime(result, Long.valueOf(call.argument("nanosecs").intValue()).longValue());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setIsoMode":
        {
          String modeStr = call.argument("mode");
          IsoMode mode = IsoMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setIsoModeFailed", "Unknown iso mode " + modeStr, null);
            return;
          }
          try {
            camera.setIsoMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMinIsoValue":
        {
          try {
            result.success(camera.getMinIsoValue());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMaxIsoValue":
        {
          try {
            result.success(camera.getMaxIsoValue());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getIsoValueStepSize":
        {
          try {
            result.success(camera.getIsoValueStepSize());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setIsoValue":
        {
          try {
            camera.setIsoValue(result, call.argument("value"));
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setFocusMode":
        {
          String modeStr = call.argument("mode");
          FocusMode mode = FocusMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setFocusModeFailed", "Unknown focus mode " + modeStr, null);
            return;
          }
          try {
            camera.setFocusMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setFocusPoint":
        {
          Boolean reset = call.argument("reset");
          Double x = null;
          Double y = null;
          if (reset == null || !reset) {
            x = call.argument("x");
            y = call.argument("y");
          }
          try {
            camera.setFocusPoint(result, x, y);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "startImageStream":
        {
          try {
            camera.startPreviewWithImageStream(imageStreamChannel);
            result.success(null);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "stopImageStream":
        {
          try {
            camera.stopImageStream();
            result.success(null);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMaxZoomLevel":
        {
          assert camera != null;

          try {
            float maxZoomLevel = camera.getMaxZoomLevel();
            result.success(maxZoomLevel);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "getMinZoomLevel":
        {
          assert camera != null;

          try {
            float minZoomLevel = camera.getMinZoomLevel();
            result.success(minZoomLevel);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "setZoomLevel":
        {
          assert camera != null;

          Double zoom = call.argument("zoom");

          if (zoom == null) {
            result.error(
                "ZOOM_ERROR", "setZoomLevel is called without specifying a zoom level.", null);
            return;
          }

          try {
            camera.setZoomLevel(result, zoom.floatValue());
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "dispose":
        {
          if (camera != null) {
            camera.dispose();
          }
          result.success(null);
          break;
        }
      default:
        result.notImplemented();
        break;
    }
  }

  void stopListening() {
    methodChannel.setMethodCallHandler(null);
  }

  private void instantiateCamera(MethodCall call, Result result) throws CameraAccessException {
    String cameraName = call.argument("cameraName");
    String resolutionPreset = call.argument("resolutionPreset");
    boolean enableAudio = call.argument("enableAudio");
    TextureRegistry.SurfaceTextureEntry flutterSurfaceTexture =
        textureRegistry.createSurfaceTexture();
    DartMessenger dartMessenger = new DartMessenger(messenger, flutterSurfaceTexture.id());
    camera =
        new Camera(
            activity,
            flutterSurfaceTexture,
            dartMessenger,
            cameraName,
            resolutionPreset,
            enableAudio);

    Map<String, Object> reply = new HashMap<>();
    reply.put("cameraId", flutterSurfaceTexture.id());
    result.success(reply);
  }

  // We move catching CameraAccessException out of onMethodCall because it causes a crash
  // on plugin registration for sdks incompatible with Camera2 (< 21). We want this plugin to
  // to be able to compile with <21 sdks for apps that want the camera and support earlier version.
  @SuppressWarnings("ConstantConditions")
  private void handleException(Exception exception, Result result) {
    if (exception instanceof CameraAccessException) {
      result.error("CameraAccess", exception.getMessage(), null);
      return;
    }

    // CameraAccessException can not be cast to a RuntimeException.
    throw (RuntimeException) exception;
  }
}
