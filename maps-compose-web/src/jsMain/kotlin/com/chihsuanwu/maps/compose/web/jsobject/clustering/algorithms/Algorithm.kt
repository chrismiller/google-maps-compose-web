@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms

import com.chihsuanwu.maps.compose.web.jsobject.MapView
import com.chihsuanwu.maps.compose.web.jsobject.clustering.JsCluster
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker

internal external interface MarkersInput {
  var markers: Array<JsMarker>
}

@JsName("AlgorithmInput")
internal external interface JsAlgorithmInput {
  /**
   * The map containing the markers and clusters.
   */
  var map: MapView

  /**
   * An array of markers to be clustered.
   *
   * There are some specific edge cases to be aware of including the following:
   * * Markers that are not visible.
   */
  var markers: Array<JsMarker>

  /**
   * The `mapCanvasProjection` enables easy conversion from lat/lng to pixel.
   *
   * @see [MapCanvasProjection](https://developers.google.com/maps/documentation/javascript/reference/overlay-view#MapCanvasProjection)
   */
//  var mapCanvasProjection: google.maps.MapCanvasProjection
}


@JsName("AlgorithmOutput")
internal external interface JsAlgorithmOutput {
  /**
   * The clusters returned based upon the {@link AlgorithmInput}.
   */
  var clusters: Array<JsCluster>

  /**
   * A boolean flag indicating that the clusters have not changed.
   */
  var changed: Boolean?
}


@JsName("Algorithm")
internal external interface JsAlgorithm {
  /**
   * Calculates an array of {@link Cluster}.
   */
  var calculate: (options: JsAlgorithmInput) -> JsAlgorithmOutput
}


@JsName("AlgorithmOptions")
internal external interface AlgorithmOptions {
  var maxZoom: Double?
}


/**
 * @hidden
 */
@JsName("AbstractAlgorithm")
internal abstract external class JsAbstractAlgorithm : JsAlgorithm {
  var maxZoom: Double

  /**
   * Helper function to bypass clustering based upon some map state such as
   * zoom, number of markers, etc.
   *
   * ```typescript
   *  cluster({markers, map}: AlgorithmInput): Cluster[] {
   *    if (shouldBypassClustering(map)) {
   *      return this.noop({markers})
   *    }
   * }
   * ```
   */
  fun <T : MarkersInput> noop(options: T): Array<JsCluster>

  /**
   * Calculates an array of {@link Cluster}. Calculate is separate from
   * {@link cluster} as it does preprocessing on the markers such as filtering
   * based upon the viewport as in {@link AbstractViewportAlgorithm}. Caching
   * and other optimizations can also be done here.
   */
  override var calculate: (options: JsAlgorithmInput) -> JsAlgorithmOutput

  /**
   * Clusters the markers and called from {@link calculate}.
   */
  fun cluster(options: JsAlgorithmInput): Array<JsCluster>
}


/**
 * @hidden
 */
internal external interface ViewportAlgorithmOptions : AlgorithmOptions {
  /**
   * The number of pixels to extend beyond the viewport bounds when filtering
   * markers prior to clustering.
   */
  var viewportPadding: Double?
}


/**
 * Abstract viewport algorithm proves a class to filter markers by a padded
 * viewport. This is a common optimization.
 *
 * @hidden
 */
internal abstract external class AbstractViewportAlgorithm(options: ViewportAlgorithmOptions) : JsAbstractAlgorithm {
  var viewportPadding: Double
}


/**
 * @hidden
 */
internal external val noop: (markers: Array<JsMarker>) -> Array<JsCluster>

/**
 * Noop algorithm does not generate any clusters or filter markers.
 */
internal external class NoopAlgorithm(options: AlgorithmOptions) : JsAbstractAlgorithm {
}