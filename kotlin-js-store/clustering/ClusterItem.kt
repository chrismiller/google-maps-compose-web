package com.chihsuanwu.maps.compose.web.clustering

import com.chihsuanwu.maps.compose.web.LatLng

interface ClusterItem {
  /**
   * The position of this marker. This must always return the same value.
   */
  val position: LatLng

  /**
   * The title of this marker.
   */
  val title: String?

  /**
   * The description of this marker.
   */
  val snippet: String?

  /**
   * The z-index of this marker.
   */
  val zIndex: Float?
}