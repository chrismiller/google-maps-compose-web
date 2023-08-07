@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering

import com.chihsuanwu.maps.compose.web.clustering.Cluster
import com.chihsuanwu.maps.compose.web.drawing.MarkerState
import com.chihsuanwu.maps.compose.web.jsobject.JsLatLng
import com.chihsuanwu.maps.compose.web.jsobject.JsLatLngBoundsLiteral
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker
import com.chihsuanwu.maps.compose.web.jsobject.toLatLng
import com.chihsuanwu.maps.compose.web.jsobject.toLatLngBounds


internal external interface ClusterOptions {
  var position: Any /* google.maps.LatLng | google.maps.LatLngLiteral */
  var markers: Array<JsMarker>?
}


@JsName("Cluster")
internal external class JsCluster(options: ClusterOptions) {
  var marker: JsMarker
  val markers: Array<JsMarker>
  var _position: JsLatLng
  val bounds: JsLatLngBoundsLiteral
  val position: JsLatLng

  /**
   * Get the count of **visible** markers.
   */
  val count: Double

  /**
   * Add a marker to the cluster.
   */
  fun push(marker: JsMarker): Unit

  /**
   * Cleanup references and remove marker from map.
   */
  fun delete(): Unit
}

internal fun JsCluster.toCluster(): Cluster {
  return Cluster(position.toLatLng(), markers.map { MarkerState(it.toLatLng()) }, bounds.toLatLngBounds())
}