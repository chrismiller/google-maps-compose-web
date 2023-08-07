import androidx.compose.runtime.*
import com.chihsuanwu.maps.compose.web.*
import com.chihsuanwu.maps.compose.web.clustering.MarkerClusterer
import com.chihsuanwu.maps.compose.web.drawing.*
import org.jetbrains.compose.web.css.*
import kotlin.random.Random


private const val range = 3.0

private class MyState {
  var cameraPositionState: CameraPositionState = CameraPositionState(
    CameraPosition(
      center = LatLng(23.5, 120.8),
      zoom = 7.6,
    )
  )
  var markers: List<MarkerState> by mutableStateOf(createMarkers())

  private fun createMarkers(): List<MarkerState> {
    val result = mutableListOf<MarkerState>()
    for (i in 1..1000) {
      result += listOf(
        MarkerState(
          position = LatLng(
            cameraPositionState.position.center.lat + Random.nextDouble(-range, range),
            cameraPositionState.position.center.lng + Random.nextDouble(-range, range),
          )
        )
      )
    }
    return result
  }
}

@Composable
fun ClusteringExample(
  apiKey: String,
) {
  val state = remember { MyState() }

  GoogleMap(
    apiKey = apiKey,
    cameraPositionState = state.cameraPositionState,
    attrs = {
      style {
        width(100.percent)
        flex(1) // Fill the remaining height
        property("margin", "0 auto") // Center the map
      }
    }
  ) {
    MarkerClusterer(
      state.markers,
      onClusterClick = { event, cluster ->
        console.info("Cluster with ${cluster.items.size} clicked: ${event.latLng}")
      }
      ) {}
  }
}
