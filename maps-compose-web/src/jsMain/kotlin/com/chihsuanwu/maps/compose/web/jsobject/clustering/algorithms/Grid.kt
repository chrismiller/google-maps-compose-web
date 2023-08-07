@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms

import com.chihsuanwu.maps.compose.web.jsobject.MapView
import com.chihsuanwu.maps.compose.web.jsobject.clustering.JsCluster
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


internal external interface GridOptions : ViewportAlgorithmOptions {
  var gridSize: Double?

  /**
   * Max distance between cluster center and point in meters.
   * @default 10000
   */
  var maxDistance: Double?
}

/**
 * The default Grid algorithm historically used in Google Maps marker
 * clustering.
 *
 * The Grid algorithm does not implement caching and markers may flash as the
 * viewport changes. Instead, use {@link SuperClusterAlgorithm}.
 */
internal external class GridAlgorithm(options: GridOptions) : AbstractViewportAlgorithm {
  var gridSize: Double
  var maxDistance: Double
  var clusters: Array<JsCluster>
  var state: GridAlgorithmState
  // projection: google.maps.MapCanvasProjection
  fun addToClosestCluster(marker: JsMarker, map: MapView, projection: Any)
}


external interface GridAlgorithmState {
  var zoom: Double
}
        