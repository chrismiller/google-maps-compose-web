@file:JsModule("@googlemaps/markerclusterer")
@file:JsNonModule
@file:Suppress(
  "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package com.chihsuanwu.maps.compose.web.jsobject.clustering.algorithms

import com.chihsuanwu.maps.compose.web.jsobject.clustering.JsCluster
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker


typealias SuperClusterOptions = Any?  // SuperCluster.Options<Temp0, Temp1>

/**
 * A very fast JavaScript algorithm for geospatial point clustering using KD trees.
 *
 * @see https://www.npmjs.com/package/supercluster for more information on options.
 */
internal external class SuperClusterAlgorithm(options: Any /* SuperClusterOptions */) : JsAbstractAlgorithm {
  var superCluster: Any  // SuperCluster
  var markers: Array<JsMarker>
  var clusters: Array<JsCluster>
  var state: SuperClusterAlgorithmState
  // fun transformCluster(options: ClusterFeature<Temp2>): Cluster
}


internal external interface Temp0 {


  @Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
  )
  @nativeGetter
  operator fun get(key: String): Any?


  @Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
  )
  @nativeSetter
  operator fun set(key: String, value: Any?)


}


internal external interface Temp1 {


  @Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
  )
  @nativeGetter
  operator fun get(key: String): Any?


  @Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
  )
  @nativeSetter
  operator fun set(key: String, value: Any?)


}


internal external interface SuperClusterAlgorithmState {
  var zoom: Double
}


internal external interface Temp2 {
  var marker: JsMarker
}
        