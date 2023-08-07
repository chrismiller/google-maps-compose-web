package com.chihsuanwu.maps.compose.web.clustering

import com.chihsuanwu.maps.compose.web.LatLng

/**
 * A collection of ClusterItems that are near each other.
 */
interface Cluster<T : ClusterItem> {
  val position: LatLng
  val items: Collection<T>
  val size: Int
}