@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms

import com.chihsuanwu.maps.compose.web.jsobject.JsLatLngBoundsLiteral
import com.chihsuanwu.maps.compose.web.jsobject.JsLatLngLiteral
import com.chihsuanwu.maps.compose.web.jsobject.JsPoint
import com.chihsuanwu.maps.compose.web.jsobject.MapView
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


/**
 * Returns the markers visible in a padded map viewport
 *
 * @param map
 * @param mapCanvasProjection
 * @param markers The list of markers to filter
 * @param viewportPaddingPixels The padding in pixel
 * @returns The list of markers in the padded viewport
 */
// internal external val filterMarkersToPaddedViewport: (map: MapView, mapCanvasProjection: google.maps.MapCanvasProjection, markers: Array<JsMarker>, viewportPaddingPixels: Double) -> Array<Marker>

/**
 * Extends a bounds by a number of pixels in each direction
 */
// internal external val extendBoundsToPaddedViewport: (bounds: JsLatLngBoundsLiteral, projection: google.maps.MapCanvasProjection, numPixels: Double) -> JsLatLngBoundsLiteral

/**
 * Gets the extended bounds as a bbox [westLng, southLat, eastLng, northLat]
 */
// internal external val getPaddedViewport: (bounds: JsLatLngBoundsLiteral, projection: google.maps.MapCanvasProjection, pixels: Double) -> /* [
//    number,
//    number,
//    number,
//    number
//] */

/**
 * Returns the distance between 2 positions.
 *
 * @hidden
 */
internal external val distanceBetweenPoints: (p1: JsLatLngLiteral, p2: JsLatLngLiteral) -> Double


internal external interface PixelBounds {
  var northEast: JsPoint
  var southWest: JsPoint
}


/**
 * Extends a pixel bounds by numPixels in all directions.
 *
 * @hidden
 */
internal external val extendPixelBounds: (options: PixelBounds, numPixels: Double) -> PixelBounds

/**
 * @hidden
 */
// internal external val pixelBoundsToLatLngBounds: (options: PixelBounds, projection: google.maps.MapCanvasProjection) -> JsLatLngBoundsLiteral
