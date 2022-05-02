'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var tslib = require('tslib');
var core$1 = require('@angular/core');
var core = require('@awesome-cordova-plugins/core');

var ICameraPreview = /** @class */ (function (_super) {
    tslib.__extends(ICameraPreview, _super);
    function ICameraPreview() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.CAMERA_DIRECTION = {
            BACK: 'back',
            FRONT: 'front',
        };
        return _this;
    }
    ICameraPreview.prototype.startCamera = function (options) { return core.cordova(this, "startCamera", { "successIndex": 1, "errorIndex": 2 }, arguments); };
    ICameraPreview.prototype.stopCamera = function () { return core.cordova(this, "stopCamera", {}, arguments); };
    ICameraPreview.prototype.hide = function () { return core.cordova(this, "hide", {}, arguments); };
    ICameraPreview.prototype.show = function () { return core.cordova(this, "show", {}, arguments); };
    ICameraPreview.prototype.takePicture = function (options) { return core.cordova(this, "takePicture", { "successIndex": 1, "errorIndex": 2 }, arguments); };
    ICameraPreview.prototype.setZoom = function (zoom) { return core.cordova(this, "setZoom", { "successIndex": 1, "errorIndex": 2 }, arguments); };
    ICameraPreview.prototype.getMaxZoom = function () { return core.cordova(this, "getMaxZoom", {}, arguments); };
    ICameraPreview.prototype.setPreviewSize = function (dimensions) { return core.cordova(this, "setPreviewSize", { "successIndex": 1, "errorIndex": 2 }, arguments); };
    ICameraPreview.prototype.tapToFocus = function (xPoint, yPoint) { return core.cordova(this, "tapToFocus", {}, arguments); };
    ICameraPreview.prototype.onBackButton = function () { return core.cordova(this, "onBackButton", {}, arguments); };
    ICameraPreview.prototype.getRectangleDimensions = function () { return core.cordova(this, "getRectangleDimensions", {}, arguments); };
    ICameraPreview.pluginName = "ICameraPreview";
    ICameraPreview.plugin = "inflexsys-camera-preview";
    ICameraPreview.pluginRef = "cordova.plugins.ICameraPreview";
    ICameraPreview.repo = "";
    ICameraPreview.platforms = ["Android"];
    ICameraPreview.decorators = [
        { type: core$1.Injectable }
    ];
    return ICameraPreview;
}(core.AwesomeCordovaNativePlugin));

exports.ICameraPreview = ICameraPreview;
