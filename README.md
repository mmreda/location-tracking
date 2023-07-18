# Location Tracking

This Android app tracks the user's location using the LocationServices API and stores the location points in a Room database every 5 seconds. It shows the location points on a Google Map using a Polyline. It also uses the Directions API to get driving directions between two points selected by the user.

The main features are:

Get the user's current location
Start a location tracking by foreground service to get location updates every 5 seconds and store them in a database.
Stop the location tracking.
Show the stored location points on a map using a polyline.
Get driving directions between two points using the Directions API.
Handle user permissions for location access.
The app has a fragment with a Google Map view. It checks for location permissions and enables the map's location functionality. It allows the user to start location tracking which starts a foreground service to get location updates. It stores the locations in a Room database and shows the path on the map. The user can also get directions between points and stop location tracking.

## Some of the Technologies and Techniques Used
The app follows the MVVM Architecture Pattern.

- Room
- Retrofit
- Data Binding
- Foreground Service
- Kotlin Coroutines
- Permissions
- Google Maps
- Directions API
- LocationServices API
- Dependency Injection (Hilt)

### API KEY
You need to generate an API key from Google Cloud Consol by going to https://play.google.com/console

Then go to put MAPS_API_KEY="YOUR_API_KEY" in local.properties.
