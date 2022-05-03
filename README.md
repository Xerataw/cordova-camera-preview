# cordova-camera-preview

Custom Cordova plugin that allows camera preview with an aiming rectangle inside.

## Features
- Start a camera preview form HTML code
- Take photo
- Maintain HML interactiity
- Drag the preview box
- Set a custom position for the preview
- Set a custom size for the preview
- Tap to focus

## Installation
`ionic cordova plugin add http://gitlab.inflexsys.com/third-party/cordova/cordova-plugins/cordova-camera-preview.git`

## Methods
**startCamera(options,[successCallback, errorCallback]**

Starts the camera preview instance.
**Options:** All the options are required

- `x` - the x point of the top left corner
- `y` - the y point of the top left corner
- `width` - the width of the preview
- `height` - the height of the preview
- `camera` - See `CAMERA_DIRECTION` 
- `alpha` - The alpha value of the preview (0 = invisible, 0 = fully visible) 
- `toBack` - Put the preview in the back :exclamation: Put it on `false`
- `tapPhoto` - Allows user to take photo while taping on the preview
- `tapFocus` - Allows user to focus while taping on the preview
- `previewDrag`- Allow user to drag te preview
- `storeToFile` - Capture images to a file a nd return back the file path instead of returning base64 encoded data :exclamation: Put it on `false` 
- `disableExifHeaderStripping` Disable automatic rotation of the image :exclamation: Put it on `false`

```Javascript
let options = {
  x: 0,
  y: 0,
  width: window.screen.width,
  height: window.screen.height,
  camera: CameraPreview.CAMERA_DIRECTION.BACK,
  toBack: false,
  tapPhoto: true,
  tapFocus: false,
  alpha: 1,
  previewDrag: false,
  storeToFile: false,
  disableExifHeaderStripping: false
};

CameraPreview.startCamera(options);
```

When both `tapFocus` and `tapPhoto` are true, the camera will focus, and take a picture as soon as the camera is done focusing.

If you capture large images in Android you may notice that performace is poor, in those cases you can set disableExifHeaderStripping to true and instead just add some extra Javascript/HTML to get a proper display of your captured images without risking your application speed.

**stopCamera([succesCallback, errorCallBack])**

Stops the camera preview instance.
```Javascript
CameraPreview.stopCamera();
```

**hide([successCallback, errorCallback])**

Hide the camera preview box.
```Javascript
CameraPreview.hide();
```

**show([successCallback, errorCallback])**

Show the camera preview box.
```Javascript
CameraPreview.show();
```

**takePicture(optons, [successCallback, errorCallback])**

Take the piture. If width and height are not specified, it will use the defaults. If width and height are specified, it will choose a supported photo size that is closest to width and height specifed and has closest aspect ratio to the preview. The argument quality defaults to `85` and specifies the quality/compression value: `0=max compression`, `100= max quality`.

```Javascript
CameraPreview.takePicture({width:640, height:640, quality: 85}, function(base64PictureData|filePath) {
  /*
    if the storeToFile option is false (the default), then base64PictureData is returned.
    base64PictureData is base64 encoded jpeg image. Use this data to store to a file or upload.
    Its up to the you to figure out the best way to save it to disk or whatever for your application.
  */

  /*
    if the storeToFile option is set to true, then a filePath is returned. Note that the file
    is stored in temporary storage, so you should move it to a permanent location if you
    don't want the OS to remove it arbitrarily.
  */

  // One simple example is if you are going to use it inside an HTML img src attribute then you would do the following:
  imageSrcData = 'data:image/jpeg;base64,' + base64PictureData;
  $('img#my-img').attr('src', imageSrcData);
});

// OR if you want to use the default options.

CameraPreview.takePicture(function(base64PictureData){
  /* code here */
});
```

**setZoom(zoomMultiplier, [successCallback, errorCallback])**

Set the zoom level of the camera device currently strted. zoomMultiplier option accepts an integer. Zoom level is initially at 1.
```Javascript
CameraPreview.setZoom(2);
```

**getMaxZoom(cb, [errorCallback])**

Get the maximum zoom level for the camera device currently started. Returns an integer representing the manimum zoom level.

```Javascript
CameraPreview.getMaxZoom(function(maxZoom){
  console.log(maxZoom);
});
```

**setPreviewSize(dimensions, [successCallback, errorCallback])**

Change the size of the preview box.

```Javascript
CameraPreview.setPreviewSize({width: window.screen.width, height: window.screen.height});
```

**tapToFocus(xPoint, yPoint, [successCallback, errorCallback])**

Set specific focus point. Note, this assumes the camera is full-screen.

```Javascript
let xPoint = event.x;
let yPoint = event.y
CameraPreview.tapToFocus(xPoint, yPoint);
```

**onBackButton([successCallback, errorCallback])**

Callback event for the bak button tap.

```Javascript
CameraPreview.onBackButton(function() {
  console.log('Back button pushed');
});
```

**getRectangleDimensions([successCallback, errorCallback])**

Return the top X axis and he bottom X axis of the aiming rectangle in the preview.

```Javascript
var rect = await CameraPreview.getRectangleDimensions();

console.log('Rectangle\'s top line: ' rect.top);
console.log('Rectangle\'s bottom line: ' rect.bot);
```
