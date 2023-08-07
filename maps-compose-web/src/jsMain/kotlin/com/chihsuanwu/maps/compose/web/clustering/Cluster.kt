package com.chihsuanwu.maps.compose.web.clustering

import com.chihsuanwu.maps.compose.web.LatLng
import com.chihsuanwu.maps.compose.web.LatLngBounds
import com.chihsuanwu.maps.compose.web.drawing.MarkerState

/**
 * A collection of [ClusterItem]s that are nearby each other.
 */
data class Cluster(val position: LatLng, val items: Collection<MarkerState>, val bounds: LatLngBounds)