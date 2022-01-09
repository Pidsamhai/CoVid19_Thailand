# Covid 19 statistics application using jetpack compose

<p align="center">
<img src="art/screen_shot.gif" height="500">
</p>

## API

[rapidapi](https://rapidapi.com/api-sports/api/covid-193/)

[covid19.th](https://covid19.th-stat.com/th/api)

## Stack
* UI
  * [Jetpack compose](https://developer.android.com/jetpack/androidx/releases/compose)
  * [Accompanist](https://github.com/google/accompanist)
    * [Swipe Refresh](https://github.com/google/accompanist/tree/main/swiperefresh)
  * [Navigation Compose](https://developer.android.com/jetpack/androidx/releases/navigation/)
  * [Markwon](https://github.com/noties/Markwon) render readme changelog
  * [AAChart](https://github.com/AAChartModel/AAChartCore-Kotlin)
  * [Glance Appwidget](https://developer.android.com/jetpack/androidx/releases/glance) Compose widget
* Dependencies injection
  * [Koin](https://github.com/InsertKoinIO/koin)
* Network
  * [Retrofit](https://github.com/square/retrofit)
* Cache
  * [Room](https://developer.android.com/jetpack/androidx/releases/room)
* Serialize
  * [Gson](https://github.com/google/gson)
* [WorkManager](https://developer.android.com/jetpack/androidx/releases/work)