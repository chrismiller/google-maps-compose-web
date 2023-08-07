package com.chihsuanwu.maps.compose.web.clustering

import com.chihsuanwu.maps.compose.web.LatLng

/**
 * ClusterItem represents a marker on the map.
 */
interface ClusterItem {
  /**
   * The position of this marker. This must always return the same value.
   */
  fun getPosition(): LatLng

  /**
   * The title of this marker.
   */
  fun getTitle(): String?

  /**
   * The description of this marker.
   */
  fun getSnippet(): String?

  /**
   * The z-index of this marker.
   */
  fun getZIndex(): Float?
}