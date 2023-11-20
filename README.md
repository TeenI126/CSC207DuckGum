# CSC207DuckGum

using 17 as jdk

## Use Cases
- sign in to service
  - Amazon
  - Spotify
- transfer playlist from one service to another
- export playlist as .csv
- import playlist from .csv
- simutaneously edit playlist linked across accounts
  - add song
  - remove song
  - reorder?

## How to log a user in Spotify per SpotifyAccount object
 - The openLoginPage will open a webpage on the users browser to the Spotify login service
   - This will redirect the user to our Github, but the URL will have a code in it
 - TEMPORARY MEASURE until we can figure out how to get the code in the url back into DuckGum
   - have the user copy the entire url and paste it back into the program
   - use `getCodeFromURI(String uri)` to extract code from uri, and pass that into `createUserAccessToken(String code)`
     - This will create codes for SpotifyAccount, that will be allow various other methods to work.

## Entities
### Songs
 - We wil be using ISRC codes for our songs (for now), Spotify and Amazon include these in their track objects