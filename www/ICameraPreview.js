var exec = require('cordova/exec');

var PLUGIN_NAME = "ICameraPreview";

var ICameraPreview = function () { };

function isFunction(obj) {
    return !!(obj && obj.constructor && obj.call && obj.apply);
};

ICameraPreview.startCamera = function (options, onSuccess, onError) {
    if (!options) {
        options = {};
    } else if (isFunction(options)) {
        onSuccess = options;
        options = {};
    }

    options.x = options.x || 0;
    options.y = options.y || 0;

    options.width = options.width || window.screen.width;
    options.height = options.height || window.screen.height;

    options.camera = options.camera || CameraPreview.CAMERA_DIRECTION.FRONT;

    if (typeof (options.tapPhoto) === 'undefined') {
        options.tapPhoto = true;
    }

    if (typeof (options.tapFocus) == 'undefined') {
        options.tapFocus = false;
    }

    options.previewDrag = options.previewDrag || false;

    options.toBack = options.toBack || false;

    if (typeof (options.alpha) === 'undefined') {
        options.alpha = 1;
    }

    options.disableExifHeaderStripping = options.disableExifHeaderStripping || false;

    options.storeToFile = options.storeToFile || false;

    exec(onSuccess, onError, PLUGIN_NAME, "startCamera", [
        options.x,
        options.y,
        options.width,
        options.height,
        options.camera,
        options.tapPhoto,
        options.previewDrag,
        options.toBack,
        options.alpha,
        options.tapFocus,
        options.disableExifHeaderStripping,
        options.storeToFile
    ]);
};

ICameraPreview.stopCamera = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "stopCamera", []);
};

ICameraPreview.hide = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "hideCamera", []);
};

ICameraPreview.show = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "showCamera", []);
};

ICameraPreview.takePicture = function (opts, onSuccess, onError) {
    if (!opts) {
        opts = {};
    } else if (isFunction(opts)) {
        onSuccess = opts;
        opts = {};
    }

    if (!isFunction(onSuccess)) {
        return false;
    }

    opts.width = opts.width || 0;
    opts.height = opts.height || 0;

    if (!opts.quality || opts.quality > 100 || opts.quality < 0) {
        opts.quality = 85;
    }

    exec(onSuccess, onError, PLUGIN_NAME, "takePicture", [opts.width, opts.height, opts.quality]);
};

ICameraPreview.setZoom = function (zoom, onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "setZoom", [zoom]);
};

ICameraPreview.getMaxZoom = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "getMaxZoom", []);
};

ICameraPreview.setPreviewSize = function (dimensions, onSuccess, onError) {
    dimensions = dimensions || {};
    dimensions.width = dimensions.width || window.screen.width;
    dimensions.height = dimensions.height || window.screen.height;

    exec(onSuccess, onError, PLUGIN_NAME, "setPreviewSize", [dimensions.width, dimensions.height]);
};

ICameraPreview.tapToFocus = function (xPoint, yPoint, onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "tapToFocus", [xPoint, yPoint]);
};

ICameraPreview.onBackButton = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "onBackButton");
};


ICameraPreview.getRectangleDimensions = function (onSuccess, onError) {
    exec(onSuccess, onError, PLUGIN_NAME, "getRectangleDimensions", []);
};

ICameraPreview.CAMERA_DIRECTION = {
    BACK: 'back',
    FRONT: 'front'
};

module.exports = ICameraPreview;