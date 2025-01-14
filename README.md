# MovieMate    Application  
Information Systems Project  

**Author**: Sofía Martínez Pastor  
**Student ID**: 70093905 

---

## Project Description  

**MovieMate**: is a system composed of a mobile application and a web application. The system allows users to: 
- **Create Movies** by providing come details of the movie.
- **View a list of movies**, displaying the information.
- The web application handles the backend API and the database, while the mobile app provides an intuitive interface for users to interact with the system.
  
---

## Functionality Overview 
1. **Mobile Application**
  - Users can create movies by entering their details in a simple form
  - A list of all movies is displayed, allowing users to see the collection they've built.
  - Error handling ensures a smooth user experience.

2. **Web Application and API**
   - The backend API, built with ASP.NET Core, provides endpoints for creating and retrieving movies.
   - The database stores movie information, including title, director, genre and ratings.
   - The web application offers a basic interface to manage movies through the API.

---

## Screenshots

1. **Home Screen**

2. **Create Movie**

3. **Show movies**

---

## Code Explanation
- **App Structure:**
  - MainActivity.kt: Contains the user interface (UI) using Jetpack Compose.
  - ApiService.java: Handles all communication with your server.
  - ApiCallback.java: Interface for handling success/failure responses.

- **Creating a Movie:**  
User fills in the text fields (title, genre, director, rating)
When "Create Movie" button is clicked:
  - Validates all fields are filled.
  - Creates a JSON object with the movie data.
  - Sends a POST request to your API with the JWT token.
  - On success: Clears fields and shows success message.
  - On failure: Shows error message.

- **Showing Movies:**  
When "Show Movies" button is clicked:
  - Sends a GET request to your API.
  - Gets back a list of movies in JSON format.
  - Converts the JSON data into readable text.
  - Displays each movie's details in the app.

- **Error Handling:**  
Shows user-friendly messages if something goes wrong.
Validates input before sending to server.
Handles network errors and server errors.

