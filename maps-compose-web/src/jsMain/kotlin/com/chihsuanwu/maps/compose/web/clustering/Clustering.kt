package com.chihsuanwu.maps.compose.web.clustering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import com.chihsuanwu.maps.compose.web.MapApplier
import com.chihsuanwu.maps.compose.web.MapMouseEvent
import com.chihsuanwu.maps.compose.web.MapNode
import com.chihsuanwu.maps.compose.web.drawing.MarkerState
import com.chihsuanwu.maps.compose.web.jsobject.clustering.JsMarkerClusterer
import com.chihsuanwu.maps.compose.web.jsobject.clustering.toCluster
import com.chihsuanwu.maps.compose.web.jsobject.drawing.JsMarker
import com.chihsuanwu.maps.compose.web.jsobject.drawing.newMarker
import com.chihsuanwu.maps.compose.web.jsobject.toJsLatLngLiteral
import com.chihsuanwu.maps.compose.web.jsobject.toMouseEvent
import js.core.jso

internal class ClusterNode(
  val clusterer: JsMarkerClusterer
) : MapNode {
  override fun onRemoved() {
    clusterer.map = null
  }
}

internal fun Collection<MarkerState>.toJsMarkerArray(): Array<JsMarker> {
  return map {
    newMarker(
      jso {
        this.position = it.position.toJsLatLngLiteral()
        // this.title = it.getTitle()
      }
    )
  }.toTypedArray()
}

@Composable
fun MarkerClusterer(
  markers: Collection<MarkerState>,
  algorithm: Algorithm? = null,
  onClusterClick: (MapMouseEvent, Cluster) -> Unit = { _, _ -> },
  onClusterItemClick: (MarkerState) -> Boolean = { false },
  onClusterItemInfoWindowClick: (MarkerState) -> Unit = { },
  onClusterItemInfoWindowLongClick: (MarkerState) -> Unit = { },
  clusterContent: @Composable ((Cluster) -> Unit)? = null,
  clusterItemContent: @Composable ((MarkerState) -> Unit)? = null,
) {
  val mapApplier = currentComposer.applier as MapApplier?
  ComposeNode<ClusterNode, MapApplier>(
    factory = {
      val clusterer = JsMarkerClusterer(
        jso {
          this.markers = markers.toJsMarkerArray()
          this.map = mapApplier?.map
//          this.algorithm = newSuperClusterAlgorithm(
//            jso {
//            }
//          )
//          this.renderer = newDefaultRenderer()
        }
      )
      ClusterNode(clusterer)
    },
    update = {
      set(onClusterClick) {
        clusterer.onClusterClick = { event, cluster, map -> onClusterClick(event.toMouseEvent(), cluster.toCluster()) }
      }
    }
  )
}
