package com.chihsuanwu.maps.compose.web.clustering

/**
 * Groups many items on a map based on zoom level.
 */
class ClusterManager<T : ClusterItem?>(context: Context?, map: GoogleMap, markerManager: MarkerManager) :
  GoogleMap.OnCameraIdleListener,
  GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
  private val mMarkerManager: MarkerManager
  private val mMarkers: MarkerManager.Collection
  private val mClusterMarkers: MarkerManager.Collection
  private var mAlgorithm: ScreenBasedAlgorithm<T>
  private var mRenderer: ClusterRenderer<T>
  private val mMap: GoogleMap
  private var mPreviousCameraPosition: CameraPosition? = null
  private var mClusterTask: ClusterTask
  private val mClusterTaskLock: java.util.concurrent.locks.ReadWriteLock =
    java.util.concurrent.locks.ReentrantReadWriteLock()
  private var mOnClusterItemClickListener: OnClusterItemClickListener<T>? = null
  private var mOnClusterInfoWindowClickListener: OnClusterInfoWindowClickListener<T>? = null
  private var mOnClusterInfoWindowLongClickListener: OnClusterInfoWindowLongClickListener<T>? = null
  private var mOnClusterItemInfoWindowClickListener: OnClusterItemInfoWindowClickListener<T>? = null
  private var mOnClusterItemInfoWindowLongClickListener: OnClusterItemInfoWindowLongClickListener<T>? = null
  private var mOnClusterClickListener: OnClusterClickListener<T>? = null

  constructor(context: Context?, map: GoogleMap) : this(context, map, MarkerManager(map))

  init {
    mMap = map
    mMarkerManager = markerManager
    mClusterMarkers = markerManager.newCollection()
    mMarkers = markerManager.newCollection()
    mRenderer = DefaultClusterRenderer(context, map, this)
    mAlgorithm = ScreenBasedAlgorithmAdapter(
      PreCachingAlgorithmDecorator(
        NonHierarchicalDistanceBasedAlgorithm<T>()
      )
    )
    mClusterTask = ClusterTask()
    mRenderer.onAdd()
  }

  val markerCollection: MarkerManager.Collection
    get() = mMarkers
  val clusterMarkerCollection: MarkerManager.Collection
    get() = mClusterMarkers
  val markerManager: MarkerManager
    get() = mMarkerManager

  fun setAlgorithm(algorithm: ScreenBasedAlgorithm<T>) {
    algorithm.lock()
    try {
      val oldAlgorithm: Algorithm<T> = this.algorithm
      mAlgorithm = algorithm
      if (oldAlgorithm != null) {
        oldAlgorithm.lock()
        try {
          algorithm.addItems(oldAlgorithm.getItems())
        } finally {
          oldAlgorithm.unlock()
        }
      }
    } finally {
      algorithm.unlock()
    }
    if (mAlgorithm.shouldReclusterOnMapMovement()) {
      mAlgorithm.onCameraChange(mMap.getCameraPosition())
    }
    cluster()
  }

  fun setAnimation(animate: Boolean) {
    mRenderer.setAnimation(animate)
  }

  var renderer: ClusterRenderer<T>
    get() = mRenderer
    set(renderer) {
      mRenderer.setOnClusterClickListener(null)
      mRenderer.setOnClusterItemClickListener(null)
      mClusterMarkers.clear()
      mMarkers.clear()
      mRenderer.onRemove()
      mRenderer = renderer
      mRenderer.onAdd()
      mRenderer.setOnClusterClickListener(mOnClusterClickListener)
      mRenderer.setOnClusterInfoWindowClickListener(mOnClusterInfoWindowClickListener)
      mRenderer.setOnClusterInfoWindowLongClickListener(mOnClusterInfoWindowLongClickListener)
      mRenderer.setOnClusterItemClickListener(mOnClusterItemClickListener)
      mRenderer.setOnClusterItemInfoWindowClickListener(mOnClusterItemInfoWindowClickListener)
      mRenderer.setOnClusterItemInfoWindowLongClickListener(mOnClusterItemInfoWindowLongClickListener)
      cluster()
    }
  var algorithm: Algorithm<T>
    get() = mAlgorithm
    set(algorithm) {
      if (algorithm is ScreenBasedAlgorithm) {
        setAlgorithm(algorithm as ScreenBasedAlgorithm<T>)
      } else {
        setAlgorithm(ScreenBasedAlgorithmAdapter(algorithm))
      }
    }

  /**
   * Removes all items from the cluster manager. After calling this method you must invoke
   * [.cluster] for the map to be cleared.
   */
  fun clearItems() {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    try {
      algorithm.clearItems()
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Adds items to clusters. After calling this method you must invoke [.cluster] for the
   * state of the clusters to be updated on the map.
   * @param items items to add to clusters
   * @return true if the cluster manager contents changed as a result of the call
   */
  fun addItems(items: Collection<T>?): Boolean {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    return try {
      algorithm.addItems(items)
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Adds an item to a cluster. After calling this method you must invoke [.cluster] for
   * the state of the clusters to be updated on the map.
   * @param myItem item to add to clusters
   * @return true if the cluster manager contents changed as a result of the call
   */
  fun addItem(myItem: T): Boolean {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    return try {
      algorithm.addItem(myItem)
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Removes items from clusters. After calling this method you must invoke [.cluster] for
   * the state of the clusters to be updated on the map.
   * @param items items to remove from clusters
   * @return true if the cluster manager contents changed as a result of the call
   */
  fun removeItems(items: Collection<T>?): Boolean {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    return try {
      algorithm.removeItems(items)
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Removes an item from clusters. After calling this method you must invoke [.cluster]
   * for the state of the clusters to be updated on the map.
   * @param item item to remove from clusters
   * @return true if the item was removed from the cluster manager as a result of this call
   */
  fun removeItem(item: T): Boolean {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    return try {
      algorithm.removeItem(item)
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Updates an item in clusters. After calling this method you must invoke [.cluster] for
   * the state of the clusters to be updated on the map.
   * @param item item to update in clusters
   * @return true if the item was updated in the cluster manager, false if the item is not
   * contained within the cluster manager and the cluster manager contents are unchanged
   */
  fun updateItem(item: T): Boolean {
    val algorithm: Algorithm<T> = algorithm
    algorithm.lock()
    return try {
      algorithm.updateItem(item)
    } finally {
      algorithm.unlock()
    }
  }

  /**
   * Force a re-cluster on the map. You should call this after adding, removing, updating,
   * or clearing item(s).
   */
  fun cluster() {
    mClusterTaskLock.writeLock().lock()
    try {
      // Attempt to cancel the in-flight request.
      mClusterTask.cancel(true)
      mClusterTask = ClusterTask()
      mClusterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMap.getCameraPosition().zoom)
    } finally {
      mClusterTaskLock.writeLock().unlock()
    }
  }

  /**
   * Might re-cluster.
   */
  fun onCameraIdle() {
    if (mRenderer is GoogleMap.OnCameraIdleListener) {
      (mRenderer as GoogleMap.OnCameraIdleListener).onCameraIdle()
    }
    mAlgorithm.onCameraChange(mMap.getCameraPosition())

    // delegate clustering to the algorithm
    if (mAlgorithm.shouldReclusterOnMapMovement()) {
      cluster()

      // Don't re-compute clusters if the map has just been panned/tilted/rotated.
    } else if (mPreviousCameraPosition == null || mPreviousCameraPosition.zoom !== mMap.getCameraPosition().zoom) {
      mPreviousCameraPosition = mMap.getCameraPosition()
      cluster()
    }
  }

  fun onMarkerClick(@NonNull marker: Marker?): Boolean {
    return markerManager.onMarkerClick(marker)
  }

  fun onInfoWindowClick(@NonNull marker: Marker?) {
    markerManager.onInfoWindowClick(marker)
  }

  /**
   * Runs the clustering algorithm in a background thread, then re-paints when results come back.
   */
  private inner class ClusterTask : AsyncTask<Float?, java.lang.Void?, Set<Cluster<T>?>?>() {
    protected fun doInBackground(vararg zoom: Float?): Set<Cluster<T>?> {
      val algorithm: Algorithm<T> = algorithm
      algorithm.lock()
      return try {
        algorithm.getClusters(zoom[0])
      } finally {
        algorithm.unlock()
      }
    }

    protected fun onPostExecute(clusters: Set<Cluster<T>?>?) {
      mRenderer.onClustersChanged(clusters)
    }
  }

  /**
   * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
   * the ClusterManager must be added as a click listener to the map.
   */
  fun setOnClusterClickListener(listener: OnClusterClickListener<T>?) {
    mOnClusterClickListener = listener
    mRenderer.setOnClusterClickListener(listener)
  }

  /**
   * Sets a callback that's invoked when a Cluster info window is tapped. Note: For this listener to function,
   * the ClusterManager must be added as a info window click listener to the map.
   */
  fun setOnClusterInfoWindowClickListener(listener: OnClusterInfoWindowClickListener<T>?) {
    mOnClusterInfoWindowClickListener = listener
    mRenderer.setOnClusterInfoWindowClickListener(listener)
  }

  /**
   * Sets a callback that's invoked when a Cluster info window is long-pressed. Note: For this listener to function,
   * the ClusterManager must be added as a info window click listener to the map.
   */
  fun setOnClusterInfoWindowLongClickListener(listener: OnClusterInfoWindowLongClickListener<T>?) {
    mOnClusterInfoWindowLongClickListener = listener
    mRenderer.setOnClusterInfoWindowLongClickListener(listener)
  }

  /**
   * Sets a callback that's invoked when an individual ClusterItem is tapped. Note: For this
   * listener to function, the ClusterManager must be added as a click listener to the map.
   */
  fun setOnClusterItemClickListener(listener: OnClusterItemClickListener<T>?) {
    mOnClusterItemClickListener = listener
    mRenderer.setOnClusterItemClickListener(listener)
  }

  /**
   * Sets a callback that's invoked when an individual ClusterItem's Info Window is tapped. Note: For this
   * listener to function, the ClusterManager must be added as a info window click listener to the map.
   */
  fun setOnClusterItemInfoWindowClickListener(listener: OnClusterItemInfoWindowClickListener<T>?) {
    mOnClusterItemInfoWindowClickListener = listener
    mRenderer.setOnClusterItemInfoWindowClickListener(listener)
  }

  /**
   * Sets a callback that's invoked when an individual ClusterItem's Info Window is long-pressed. Note: For this
   * listener to function, the ClusterManager must be added as a info window click listener to the map.
   */
  fun setOnClusterItemInfoWindowLongClickListener(listener: OnClusterItemInfoWindowLongClickListener<T>?) {
    mOnClusterItemInfoWindowLongClickListener = listener
    mRenderer.setOnClusterItemInfoWindowLongClickListener(listener)
  }

  /**
   * Called when a Cluster is clicked.
   */
  interface OnClusterClickListener<T : ClusterItem?> {
    /**
     * Called when cluster is clicked.
     * Return true if click has been handled
     * Return false and the click will dispatched to the next listener
     */
    fun onClusterClick(cluster: Cluster<T>?): Boolean
  }

  /**
   * Called when a Cluster's Info Window is clicked.
   */
  interface OnClusterInfoWindowClickListener<T : ClusterItem?> {
    fun onClusterInfoWindowClick(cluster: Cluster<T>?)
  }

  /**
   * Called when a Cluster's Info Window is long clicked.
   */
  interface OnClusterInfoWindowLongClickListener<T : ClusterItem?> {
    fun onClusterInfoWindowLongClick(cluster: Cluster<T>?)
  }

  /**
   * Called when an individual ClusterItem is clicked.
   */
  interface OnClusterItemClickListener<T : ClusterItem?> {
    /**
     * Called when `item` is clicked.
     *
     * @param item the item clicked
     *
     * @return true if the listener consumed the event (i.e. the default behavior should not
     * occur), false otherwise (i.e. the default behavior should occur).  The default behavior
     * is for the camera to move to the marker and an info window to appear.
     */
    fun onClusterItemClick(item: T): Boolean
  }

  /**
   * Called when an individual ClusterItem's Info Window is clicked.
   */
  interface OnClusterItemInfoWindowClickListener<T : ClusterItem?> {
    fun onClusterItemInfoWindowClick(item: T)
  }

  /**
   * Called when an individual ClusterItem's Info Window is long clicked.
   */
  interface OnClusterItemInfoWindowLongClickListener<T : ClusterItem?> {
    fun onClusterItemInfoWindowLongClick(item: T)
  }
}