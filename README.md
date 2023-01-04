## Movies App
Search for any Movie you want. 

## Project Description
1. Load movies from TMDB by API. 
2. Search movies by name.
3. Selected movie preview poster image, summary and rating.

## Screenshots
<img src="https://user-images.githubusercontent.com/103647107/210641582-f3d2405a-626d-439b-8dd1-7c8c176049b9.png" width="400" height="600" >
<img src="https://user-images.githubusercontent.com/103647107/210642725-bac37dad-d532-42c3-8e55-5496619f2e00.png" width="400" height="600" >

## Architecture
1. MVVM (Model-View-ViewModel) Architecture is used in this project.
2. Provide an improved user experience by making sure that your app can be used when network connections are unreliable or when the user is offline. One way to do this is to page from the network and from a local database at the same time. This way, your app drives the UI from a local database cache and only makes requests to the network when there is no more data in the database.

<img src="https://user-images.githubusercontent.com/103647107/210646499-97b68dcf-8257-48dc-869a-1b514972632b.png" width="700" height="700" >

## Requirements
Create TMDB API Key from website First (https://www.themoviedb.org/).

## Installation
1. Clone the app using Git or import it to Android Studio.
2. Put API_KEY = { Your api key } in local.properties file.
