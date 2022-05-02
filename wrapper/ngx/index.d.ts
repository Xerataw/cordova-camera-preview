import { AwesomeCordovaNativePlugin } from '@awesome-cordova-plugins/core';
export interface CameraPreviewDimensions {
    /** The width of the camera preview, default to window.screen.width */
    width?: number;
    /** The height of the camera preview, default to window.screen.height */
    height?: number;
}
export interface CameraPreviewOptions {
    /** The left edge in pixels, default 0 */
    x?: number;
    /** The top edge in pixels, default 0 */
    y?: number;
    /** The width in pixels, default window.screen.width */
    width?: number;
    /** The height in pixels, default window.screen.height */
    height?: number;
    /** Choose the camera to use 'front' or 'rear', default 'front' */
    camera?: string;
    /** Tap to take a photo, default true (picture quality by default : 85) */
    tapPhoto?: boolean;
    /** Preview box drag across the screen, default 'false' */
    previewDrag?: boolean;
    /** Capture images to a file and return back the file path instead of returning base64 encoded data. */
    storeToFile: boolean;
    /** Preview box to the back of the webview (true => back, false => front) , default false */
    toBack?: boolean;
    /** Alpha channel of the preview box, float, [0,1], default 1 */
    alpha?: number;
    /** Tap to set specific focus point. Note, this assumes the camera is full-screen. default false */
    tapFocus?: boolean;
    /** On Android disable automatic rotation of the image and stripping of Exit header. default false */
    disableExifHeaderStripping?: boolean;
}
export interface CameraPreviewPictureOptions {
    /** The width in pixels, default 0 */
    width?: number;
    /** The height in pixels, default 0 */
    height?: number;
    /** The picture quality, 0 - 100, default 85 */
    quality?: number;
}
export declare class ICameraPreview extends AwesomeCordovaNativePlugin {
    CAMERA_DIRECTION: {
        BACK: string;
        FRONT: string;
    };
    /**
     *  Starts te camera preview
     * @param options
     * @returns
     */
    startCamera(options: CameraPreviewOptions): Promise<any>;
    /**
     * Stops the camera preview instance. (iOS & Android)
     *
     * @returns {Promise<any>}
     */
    stopCamera(): Promise<any>;
    /**
     * Hide the camera preview box.
     *
     * @returns {Promise<any>}
     */
    hide(): Promise<any>;
    /**
     * Show the camera preview box.
     *
     * @returns {Promise<any>}
     */
    show(): Promise<any>;
    /**
     * Take the picture (base64)
     *
     * @param {CameraPreviewPictureOptions} [options] size and quality of the picture to take
     * @returns {Promise<any>}
     */
    takePicture(options?: CameraPreviewPictureOptions): Promise<any>;
    /**
     * Set the zoom (Android)
     *
     * @param [zoom] {number} Zoom value
     * @returns {Promise<any>}
     */
    setZoom(zoom?: number): Promise<any>;
    /**
     * Get the maximum zoom (Android)
     *
     * @returns {Promise<any>}
     */
    getMaxZoom(): Promise<any>;
    /**
     * Set the preview Size
     *
     * @param {CameraPreviewDimensions} [dimensions]
     * @returns {Promise<any>}
     */
    setPreviewSize(dimensions?: CameraPreviewDimensions): Promise<any>;
    /**
     * Set specific focus point. Note, this assumes the camera is full-screen.
     *
     * @param {number} xPoint
     * @param {number} yPoint
     * @returns {Promise<any>}
     */
    tapToFocus(xPoint: number, yPoint: number): Promise<any>;
    /**
     * Add a listener for the back event for the preview
     *
     * @returns {Promise<any>} if back button pressed
     */
    onBackButton(): Promise<any>;
    /**
     *  Get the top and bot line of the aim rectangle
     *
     * @returns {Promise<any>}
     */
    getRectangleDimensions(): Promise<any>;
}
