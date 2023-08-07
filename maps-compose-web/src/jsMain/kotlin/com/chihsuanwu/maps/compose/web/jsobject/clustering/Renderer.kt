@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering

import com.chihsuanwu.maps.compose.web.jsobject.MapView
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


/**
 * Provides statistics on all clusters in the current render cycle for use in {@link Renderer.render}.
 */

internal external class ClusterStats(markers: Array<JsMarker>, clusters: Array<JsCluster>) {
  val markers: ClusterStatsMarkers
  val clusters: ClusterStatsClusters
}


internal external interface Renderer {
  /**
   * Turn a {@link Cluster} into a `Marker`.
   */
  fun render(cluster: JsCluster, stats: ClusterStats, map: MapView): JsMarker
}


internal external class DefaultRenderer : Renderer {
  /**
   * The default render function for the library used by {@link MarkerClusterer}.
   */
  override fun render(cluster: JsCluster, stats: ClusterStats, map: MapView): JsMarker
}


internal external interface ClusterStatsMarkers {
  var sum: Double
}


internal external interface Temp4 {
  var mean: Double
  var sum: Double
  var min: Double
  var max: Double
}


internal external interface ClusterStatsClusters {
  var count: Double
  var markers: Temp4
}
