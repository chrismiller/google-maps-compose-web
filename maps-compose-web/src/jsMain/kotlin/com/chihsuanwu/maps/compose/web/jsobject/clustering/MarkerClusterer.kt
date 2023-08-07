@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering

import com.chihsuanwu.maps.compose.web.jsobject.JsMapMouseEvent
import com.chihsuanwu.maps.compose.web.jsobject.MapView
import com.chihsuanwu.maps.compose.web.jsobject.MapsEventListener
import com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms.AlgorithmOptions
import com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms.JsAlgorithm
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


internal typealias onClusterClickHandler = (event: JsMapMouseEvent, cluster: JsCluster, map: MapView) -> Unit


internal external interface MarkerClustererOptions {
  var markers: Array<JsMarker>?

  /**
   * An algorithm to cluster markers. Default is {@link SuperClusterAlgorithm}. Must
   * provide a `calculate` method accepting {@link AlgorithmInput} and returning
   * an array of {@link Cluster}.
   */
  var algorithm: JsAlgorithm?
  var algorithmOptions: AlgorithmOptions?
  var map: MapView?

  /**
   * An object that converts a {@link Cluster} into a `google.maps.Marker`.
   * Default is {@link DefaultRenderer}.
   */
  var renderer: Renderer?
  var onClusterClick: onClusterClickHandler?
}


internal external val defaultOnClusterClickHandler: onClusterClickHandler

/**
 * MarkerClusterer creates and manages per-zoom-level clusters for large amounts
 * of markers. See {@link MarkerClustererOptions} for more details.
 *
 */
@JsName("MarkerClusterer")
internal external class JsMarkerClusterer(options: MarkerClustererOptions) {
  /** @see {@link MarkerClustererOptions.onClusterClick} */
  var onClusterClick: onClusterClickHandler

  /** @see {@link MarkerClustererOptions.algorithm} */
  var algorithm: JsAlgorithm
  var clusters: Array<JsCluster>
  var markers: Array<JsMarker>

  /** @see {@link MarkerClustererOptions.renderer} */
  var renderer: Renderer

  /** @see {@link MarkerClustererOptions.map} */
  var map: MapView?
  var idleListener: MapsEventListener

  fun addMarker(marker: JsMarker, noDraw: Boolean = definedExternally)
  fun addMarkers(markers: Array<JsMarker>, noDraw: Boolean = definedExternally)
  fun removeMarker(marker: JsMarker, noDraw: Boolean = definedExternally): Boolean
  fun removeMarkers(markers: Array<JsMarker>, noDraw: Boolean = definedExternally): Boolean
  fun clearMarkers(noDraw: Boolean = definedExternally)

  /**
   * Recalculates and draws all the marker clusters.
   */
  fun render(): Unit
  fun onAdd(): Unit
  fun onRemove(): Unit
  fun reset(): Unit
  fun renderClusters(): Unit
}
