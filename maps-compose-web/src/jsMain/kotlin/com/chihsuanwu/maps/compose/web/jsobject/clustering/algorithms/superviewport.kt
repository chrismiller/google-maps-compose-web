@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms

import com.chihsuanwu.maps.compose.web.jsobject.clustering.JsCluster
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


internal external interface SuperClusterViewportOptions : ViewportAlgorithmOptions  // , SuperClusterOptions


internal external interface SuperClusterViewportState {
  var zoom: Double
//  var view: [
//    number,
//    number,
//    number,
//    number
//]
}


/**
 * A very fast JavaScript algorithm for geospatial point clustering using KD trees.
 *
 * @see https://www.npmjs.com/package/supercluster for more information on options.
 */
internal external class SuperClusterViewportAlgorithm(options: SuperClusterViewportOptions) : AbstractViewportAlgorithm {
  var superCluster: Any  // SuperCluster
  var markers: Array<JsMarker>
  var clusters: Array<JsCluster>
  var state: SuperClusterViewportState
  // fun transformCluster(options: ClusterFeature<Temp3>): Cluster
}

internal external interface Temp3 {
  var marker: JsMarker
}
        