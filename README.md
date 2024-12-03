Key Features:

Discover Movies:

Browse through a list of recent and top-rated movies.
View movie posters, titles, overviews, release dates, and ratings.
Movie Details:

Access detailed information about each movie, including genres, language, and a full overview.
View high-resolution backdrops and posters.
Search Functionality:

Search for movies by title, overview, genre, or actor names.
Filter movies based on specific genres like Action, Animation, Drama, and Crime.
Favorites and Watchlist:

Add movies to a favorites list for quick access.
Manage a watchlist to keep track of movies you plan to watch.
Remove movies from favorites or watchlist as desired.
User Ratings:

Rate movies using a guest session with TMDb.
View your personal rating alongside the movie's average rating.
Ratings are stored locally for reference.
Multi-Language Support:

Toggle between English and Arabic languages within the app.
The app adapts its layout and text direction based on the selected language.
Persistence and Caching:

Uses a local SQLite database to store favorites, watchlist, and personal ratings.
Ensures data is available offline and persists between sessions.
Responsive Design:

Supports various screen sizes and orientations.
Provides a consistent user experience across different Android devices.
Technical Highlights:

Architecture:

Follows Android best practices for activity and layout management.
Utilizes RecyclerView for efficient list rendering.
Networking:

Uses OkHttpClient for making network requests to TMDb API.
Handles asynchronous data fetching with ExecutorService and Handlers.
Image Loading:

Implements Glide library for efficient image loading and caching.
Displays images with transformations like center cropping and rounded corners.
Data Parsing:

Parses JSON responses using JSONObject and JSONArray.
Maps API data to custom model classes like Movie and Person.
Localization:

Stores language preference in SharedPreferences.
Updates app locale dynamically without the need for app restart.
Error Handling:

Provides user feedback through Toast messages.
Handles network errors and API failures gracefully.
Third-Party Libraries:

Glide for image loading.
OkHttp for network calls.
Setup and Installation:

Prerequisites:

Android Studio installed on your development machine.
An API key from The Movie Database (TMDb).
Clone the Repository:

git clone https://github.com/isulimanm/Movie_Explorer_Android_App.git
Configure API Key:

Replace "API_KEY" in the Movie.java and other relevant files with your TMDb API key.
Build and Run:

Open the project in Android Studio.
Sync Gradle and build the project.
Run the app on an emulator or physical device.
Usage:

Launch the app to view recent movies by default.
Use the toggle button at the top to switch between recent and top-rated movies.
Tap on a movie to view its details.
Use the search bar to find movies by title, genre, or actor.
Add movies to your favorites or watchlist using the heart and watch icons.
Rate movies by tapping the rate button in the movie details screen.
Access your favorites and watchlist from the options menu.
Change the app language from the main page options menu.
