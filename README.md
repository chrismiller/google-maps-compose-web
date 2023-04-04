# Google Maps Compose Web

[![](https://jitpack.io/v/chihsuanwu/google-maps-compose-web.svg)](https://jitpack.io/#chihsuanwu/google-maps-compose-web)

A library for using Google Maps in [Compose for Web](https://github.com/JetBrains/compose-jb).

This library is heavily inspired by [Maps Compose for Android](https://github.com/googlemaps/android-maps-compose).

**Note that this library is still in its early stages, and the API is subject to change.**

# Usage

Adding a `GoogleMap` to your Compose UI as follows:

```kotlin
val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition(
        center = LatLng(23.2, 120.5),
        zoom = 8.0,
    )
}
GoogleMap(
    apiKey = "YOUR_API_KEY",
    cameraPositionState = cameraPositionState,
    attrs = {
        style {
            width(500.px)
            height(500.px)
        }
    }
)
```

## Configuring the map

Configuring the map can be done by passing a `MapOptions` object to the `GoogleMap` composable.

```kotlin
val mapOptions = remember {
    MapOptions(
        fullscreenControl = false,
        // ...
    )
}
GoogleMap(
    // ...
    mapOptions = mapOptions,
) {
    // ...
}
```

Map events can be handled by passing a lambda expression to the `GoogleMap` composable.

```kotlin
GoogleMap(
    // ...
    onClick = {
        console.log("Map clicked!")
    },
    onDrag = {
        console.log("Map dragged!")
    },
    onIdle = {
        console.log("Map idle!")
    },
    // Add more events here
) {
    // ...
}
```



## Drawing on the map

Adding child composable, such as `Marker`, to the `GoogleMap` composable.

```kotlin
GoogleMap(
    // ...
) {
    Marker(
        state = MarkerState(position = LatLng(23.2, 120.5)),
        // ...
    )
}
```

### Handling component events

Components can be configured to handle events by passing a lambda expression to the component's parameters.

```kotlin
Marker(
    // ...
    onClick = {
        console.log("Marker clicked!")
    },
    onDragEnd = {
        console.log("Marker dragged!")
    },
    // Add more events here
)
```

### Marker's Info Window

An info window can be added to a marker by passing a composable to the `infoContent` parameter.

To show the info window, call `showInfoWindow()` on the `MarkerState`.

```kotlin
state = rememberMarkerState()

Marker(
    state = state,
    // ...
    infoContent = {
        Div {
            Span({ style { fontSize(20.px) } }) {
                Text("Info Window Title")
            }
            Text("Info Window Content")
        }
    }
) 

// show the info window
state.showInfoWindow()
```

# Setup

Add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    maven("https://jitpack.io")
}

kotlin {
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("com.github.chihsuanwu:google-maps-compose-web:<version>")
            }
        }
    }
}
```

# Contributing

Contributions are highly appreciated! Please create a feature/bugfix branch on 
your own fork and submit a pull request.