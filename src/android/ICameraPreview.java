package com.inflexsyscamerapreview;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.Manifest;
import android.content.pm.PackageManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ICameraPreview extends CordovaPlugin implements CameraActivity.CameraPreviewListener {

  private static final String TAG = "ICameraPreview";

  private static final String ZOOM_ACTION = "setZoom";
  private static final String GET_MAX_ZOOM_ACTION = "getMaxZoom";
  private static final String START_CAMERA_ACTION = "startCamera";
  private static final String STOP_CAMERA_ACTION = "stopCamera";
  private static final String PREVIEW_SIZE_ACTION = "setPreviewSize";
  private static final String TAKE_PICTURE_ACTION = "takePicture";
  private static final String SHOW_CAMERA_ACTION = "showCamera";
  private static final String HIDE_CAMERA_ACTION = "hideCamera";
  private static final String TAP_TO_FOCUS = "tapToFocus";
  private static final String SET_BACK_BUTTON_CALLBACK = "onBackButton";
  private static final String GET_RECTANGLE_DIMENSIONS = "getRectangleDimensions";

  private static final int CAM_REQ_CODE = 0;

  private static final String[] permissions = {
      Manifest.permission.CAMERA
  };

  private CameraActivity fragment;
  private CallbackContext takePictureCallbackContext;
  private CallbackContext setFocusCallbackContext;
  private CallbackContext startCameraCallbackContext;
  private CallbackContext tapBackButtonContext = null;

  private CallbackContext execCallback;
  private JSONArray execArgs;

  private ViewParent webViewParent;

  private final int containerViewId = 20; // <- set to random number to prevent conflict with other plugins

  public ICameraPreview() {
    super();
    Log.d(TAG, "Constructing");
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    if (START_CAMERA_ACTION.equals(action)) {
      if (cordova.hasPermission(permissions[0])) {
        return startCamera(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3), args.getString(4),
            args.getBoolean(5), args.getBoolean(6), args.getBoolean(7), args.getString(8), args.getBoolean(9),
            args.getBoolean(10), args.getBoolean(11), callbackContext);
      } else {
        this.execCallback = callbackContext;
        this.execArgs = args;
        cordova.requestPermissions(this, CAM_REQ_CODE, permissions);
        return true;
      }
    } else if (TAKE_PICTURE_ACTION.equals(action)) {
      return takePicture(args.getInt(0), args.getInt(1), args.getInt(2), callbackContext);
    } else if (ZOOM_ACTION.equals(action)) {
      return setZoom(args.getInt(0), callbackContext);
    } else if (GET_MAX_ZOOM_ACTION.equals(action)) {
      return getMaxZoom(callbackContext);
    } else if (PREVIEW_SIZE_ACTION.equals(action)) {
      return setPreviewSize(args.getInt(0), args.getInt(1), callbackContext);
    } else if (STOP_CAMERA_ACTION.equals(action)) {
      return stopCamera(callbackContext);
    } else if (SHOW_CAMERA_ACTION.equals(action)) {
      return showCamera(callbackContext);
    } else if (HIDE_CAMERA_ACTION.equals(action)) {
      return hideCamera(callbackContext);
    } else if (TAP_TO_FOCUS.equals(action)) {
      return tapToFocus(args.getInt(0), args.getInt(1), callbackContext);
    } else if (SET_BACK_BUTTON_CALLBACK.equals(action)) {
      return setBackButtonListener(callbackContext);
    } else if (GET_RECTANGLE_DIMENSIONS.equals(action)) {
      return getRectangleDimensions(callbackContext);
    }

    return false;
  }

  @Override
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
      throws JSONException {
    for (int r : grantResults) {
      if (r == PackageManager.PERMISSION_DENIED) {
        execCallback.sendPluginResult(new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION));
        return;
      }
    }

    if (requestCode == CAM_REQ_CODE) {
      startCamera(this.execArgs.getInt(0), this.execArgs.getInt(1), this.execArgs.getInt(2), this.execArgs.getInt(3),
          this.execArgs.getString(4), this.execArgs.getBoolean(5), this.execArgs.getBoolean(6),
          this.execArgs.getBoolean(7), this.execArgs.getString(8), this.execArgs.getBoolean(9),
          this.execArgs.getBoolean(10), this.execArgs.getBoolean(11), this.execCallback);
    }
  }

  private boolean hasView(CallbackContext callbackContext) {
    if (fragment == null) {
      callbackContext.error("No preview");
      return false;
    }

    return true;
  }

  private boolean hasCamera(CallbackContext callbackContext) {
    if (this.hasView(callbackContext) == false) {
      return false;
    }

    if (fragment.getCamera() == null) {
      callbackContext.error("No Camera");
      return false;
    }

    return true;
  }

  private boolean startCamera(int x, int y, int width, int height, String defaultCamera, Boolean tapToTakePicture,
      Boolean dragEnabled, final Boolean toBack, String alpha, boolean tapFocus, boolean disableExifHeaderStripping,
      boolean storeToFile, CallbackContext callbackContext) {
    Log.d(TAG, "start camera action");

    if (fragment != null) {
      callbackContext.error("Camera already started");
      return true;
    }

    final float opacity = Float.parseFloat(alpha);

    fragment = new CameraActivity();
    fragment.setEventListener(this);
    fragment.defaultCamera = defaultCamera;
    fragment.tapToTakePicture = tapToTakePicture;
    fragment.dragEnabled = dragEnabled;
    fragment.tapToFocus = tapFocus;
    fragment.disableExifHeaderStripping = disableExifHeaderStripping;
    fragment.storeToFile = storeToFile;
    fragment.toBack = toBack;

    DisplayMetrics metrics = cordova.getActivity().getResources().getDisplayMetrics();

    // offset
    int computedX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, metrics);
    int computedY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y, metrics);

    // size
    int computedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, metrics);
    int computedHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, metrics);

    fragment.setRect(computedX, computedY, computedWidth, computedHeight);

    startCameraCallbackContext = callbackContext;

    cordova.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {

        // create or update the layout params for the container view
        FrameLayout containerView = (FrameLayout) cordova.getActivity().findViewById(containerViewId);
        if (containerView == null) {
          containerView = new FrameLayout(cordova.getActivity().getApplicationContext());
          containerView.setId(containerViewId);

          FrameLayout.LayoutParams containerLayoutParams = new FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
          cordova.getActivity().addContentView(containerView, containerLayoutParams);
        }

        // display camera below the webview
        if (toBack) {
          View view = webView.getView();
          ViewParent rootParent = containerView.getParent();
          ViewParent curParent = view.getParent();

          view.setBackgroundColor(0x00000000);

          // If parents do not match look for.
          if (curParent.getParent() != rootParent) {
            while (curParent != null && curParent.getParent() != rootParent) {
              curParent = curParent.getParent();
            }

            if (curParent != null) {
              ((ViewGroup) curParent).setBackgroundColor(0x00000000);
              ((ViewGroup) curParent).bringToFront();
            } else {
              // Do default...
              curParent = view.getParent();
              webViewParent = curParent;
              ((ViewGroup) view).bringToFront();
            }
          } else {
            // Default
            webViewParent = curParent;
            ((ViewGroup) curParent).bringToFront();
          }

        } else {
          // set camera back to front
          containerView.setAlpha(opacity);
          containerView.bringToFront();
        }

        // add the fragment to the container
        FragmentManager fragmentManager = cordova.getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerView.getId(), fragment);
        fragmentTransaction.commit();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
      }
    });

    return true;
  }

  public void onCameraStarted() {
    Log.d(TAG, "Camera started");

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "Camera started");
    pluginResult.setKeepCallback(false);
    startCameraCallbackContext.sendPluginResult(pluginResult);
  }

  private boolean takePicture(int width, int height, int quality, CallbackContext callbackContext) {
    if (this.hasView(callbackContext) == false) {
      return true;
    }
    takePictureCallbackContext = callbackContext;

    fragment.takePicture(width, height, quality);

    return true;
  }

  public void onPictureTaken(String originalPicture) {
    Log.d(TAG, "returning picture");

    JSONArray data = new JSONArray();
    data.put(originalPicture);

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
    pluginResult.setKeepCallback(fragment.tapToTakePicture);
    takePictureCallbackContext.sendPluginResult(pluginResult);
  }

  public void onPictureTakenError(String message) {
    Log.d(TAG, "CameraPreview onPictureTakenError");
    takePictureCallbackContext.error(message);
  }

  private boolean getMaxZoom(CallbackContext callbackContext) {
    if (this.hasCamera(callbackContext) == false) {
      return true;
    }

    Camera camera = fragment.getCamera();
    Camera.Parameters params = camera.getParameters();

    if (camera.getParameters().isZoomSupported()) {
      int maxZoom = camera.getParameters().getMaxZoom();
      callbackContext.success(maxZoom);
    } else {
      callbackContext.error("Zoom not supported");
    }

    return true;
  }

  private boolean setZoom(int zoom, CallbackContext callbackContext) {
    if (this.hasCamera(callbackContext) == false) {
      return true;
    }

    Camera camera = fragment.getCamera();
    Camera.Parameters params = camera.getParameters();

    if (camera.getParameters().isZoomSupported()) {
      params.setZoom(zoom);
      fragment.setCameraParameters(params);

      callbackContext.success(zoom);
    } else {
      callbackContext.error("Zoom not supported");
    }

    return true;
  }

  private boolean setPreviewSize(int width, int height, CallbackContext callbackContext) {
    if (this.hasCamera(callbackContext) == false) {
      return true;
    }

    Camera camera = fragment.getCamera();
    Camera.Parameters params = camera.getParameters();

    params.setPreviewSize(width, height);
    fragment.setCameraParameters(params);
    camera.startPreview();

    callbackContext.success();
    return true;
  }

  private boolean stopCamera(CallbackContext callbackContext) {
    if (webViewParent != null) {
      cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          ((ViewGroup) webView.getView()).bringToFront();
          webViewParent = null;
        }
      });
    }

    if (this.hasView(callbackContext) == false) {
      return true;
    }

    FragmentManager fragmentManager = cordova.getActivity().getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.remove(fragment);
    fragmentTransaction.commit();
    fragment = null;

    callbackContext.success();
    return true;
  }

  private boolean showCamera(CallbackContext callbackContext) {
    if (this.hasView(callbackContext) == false) {
      return true;
    }

    FragmentManager fragmentManager = cordova.getActivity().getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.show(fragment);
    fragmentTransaction.commit();

    callbackContext.success();
    return true;
  }

  private boolean hideCamera(CallbackContext callbackContext) {
    if (this.hasView(callbackContext) == false) {
      return true;
    }

    FragmentManager fragmentManager = cordova.getActivity().getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.hide(fragment);
    fragmentTransaction.commit();

    callbackContext.success();
    return true;
  }

  private boolean tapToFocus(final int pointX, final int pointY, CallbackContext callbackContext) {
    if (this.hasView(callbackContext) == false) {
      return true;
    }

    setFocusCallbackContext = callbackContext;

    fragment.setFocusArea(pointX, pointY, new Camera.AutoFocusCallback() {
      public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
          onFocusSet(pointX, pointY);
        } else {
          onFocusSetError("fragment.setFocusArea() failed");
        }
      }
    });

    return true;
  }

  public void onFocusSet(final int pointX, final int pointY) {
    Log.d(TAG, "Focus set, returning coordinates");

    JSONObject data = new JSONObject();
    try {
      data.put("x", pointX);
      data.put("y", pointY);
    } catch (JSONException e) {
      Log.d(TAG, "onFocusSet failed to set output payload");
    }

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
    pluginResult.setKeepCallback(true);
    setFocusCallbackContext.sendPluginResult(pluginResult);
  }

  public void onFocusSetError(String message) {
    Log.d(TAG, "CameraPreview onFocusSetError");
    setFocusCallbackContext.error(message);
  }

  public boolean setBackButtonListener(CallbackContext callbackContext) {
    tapBackButtonContext = callbackContext;
    return true;
  }

  public void onBackButton() {
    if (tapBackButtonContext == null) {
      return;
    }

    Log.d(TAG, "Back button tapped, notifying");

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "Back button pressed");
    tapBackButtonContext.sendPluginResult(pluginResult);
  }

  public boolean getRectangleDimensions(CallbackContext callback) {
    try {
      JSONObject dimension = new JSONObject();
      dimension.put("top", new Integer(this.fragment.topRect));
      dimension.put("bot", new Integer(this.fragment.botRect));

      if (dimension != null) {
        callback.success(dimension);
      } else {
        callback.error("No rectangle dimension initialized");
      }
    } catch (JSONException e) {
      Log.d(TAG, "getRectangleDimensions failed to set output payload");
    }
    return true;
  }
}
