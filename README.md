# Mario-Kart-World-Custom-Cup-Randomiser
This project creates a set of eight random cups for Mario Kart World.\
These cups are represented by HashMap<Integer, String> objects, and functions exist to print their contents, return a single cup, or return a HashMap<String, HashMap<Integer, String>> that contains every cup.\
Every cup must satisfy the following rules:
1. Each cup must have 4 tracks.
2. The first track in a cup must be traversed without routes.
3. The other tracks in a cup must be traversed via a route from the previous track. This route must be selectable in VS Race.
4. Every track must be represented at least once.
5. The remaining two slots must be filled by different tracks, and these tracks must use different layouts in each slot.
6. The Special Cup must end with Rainbow Road.

# Randomiser.java
This is the actual randomiser class.

## Functions
### void main(String[] args)
This acts as a main function, allowing the project to be run without external input. It will create a set of eight random cups, then print them and save them as `Random Cups.csv`.

### void randomise()
This is responsible for creating a set of eight random cups. No output is given.

### void printRandomisedCups()
If randomisation is complete, every cups that was created will be printed. An example of one cup that could be output by this function is as follows:
> Mushroom Cup: Mario Bros. Circuit, Crown City, Whistlestop Summit, DK Spaceport

### void saveRandomisedCups(String pathname)
If randomisation is complete, this saves the cups that were created in a .txt file as specified by `pathname`.\
If randomisation is not complete or the file is unable to be saved for any reason, this instead prints an error message.\
If a file already exists with the provided name, the name of the saved file will have `" (1)"` appended.
If `.txt` is not present at the end of the pathname, it will automatically be appended.

### void saveRandomisedCups()
Acts identically to `void saveRandomisedCups(String pathname)`, except that instead of using a provided name and path, it will always attempt to save to `Random Cups YYYY-MM-DD hh:mm:ss.txt`.

### void saveRandomisedCupsAsCSV(String pathname)
If randomisation is complete, this saves the cups that were created in a .csv file as specified by `pathname`.\
If randomisation is not complete or the file is unable to be saved for any reason, this instead prints an error message.\
If a file already exists with the provided name, the name of the saved file will have `" (1)"` appended.
If `.csv` is not present at the end of the pathname, it will automatically be appended.

### void saveRandomisedCupsAsCSV()
Acts identically to `void saveRandomisedCupsAsCSV(String pathname)`, except that instead of using a provided name and path, it will always attempt to save to `Random Cups YYYY-MM-DD hh:mm:ss.csv`.

### HashMap<Integer, String> getMushroomCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Mushroom Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getFlowerCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Flower Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getStarCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Star Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getShellCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Shell Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getBananaCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Banana Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getLeafCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Leaf Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getLightningCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Lightning Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, String> getSpecialCup()
If randomisation is complete, this returns a HashMap<Integer, String> containing the Special Cup tracks.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### HashMap<Integer, HashMap<Integer, String>> getAllCups()
If randomisation is complete, this returns a HashMap<String, HashMap<Integer, String>> containing every Cup.\
The keys of this HashMap are the names of the cups ("Mushroom", "Flower", et cetera), and the values are the cups, as provided in `getMushroomCup()`, `getFlowerCup()`, et cetera.\
If randomisation is not complete (either due to one not having taken place or due to randomisation currently being in-progress), this instead prints an error message and returns an empty HashMap.

### boolean isComplete()
Returns `true` if randomisation is complete.\
Returns `false` if randomisation is not complete, i.e. it is in-progress or has not started.

# RouteInfo.java
This class holds information regarding routes, which is used by `Randomiser.java` to determine potential tracks to place before or after another track, as well as which tracks to prioritise placement for.

## Functions
### RouteInfo(String origin, String destination, String number)
Creates a new RouteInfo object using the provided information.\
In order, these are the origin (start track) of the route, the destination (end track) of the route and the route number, which corresponds to the layout and entrance for the destination.

### String getOrigin()
Returns the origin for the route.

### Boolean isOrigin(String test)
Returns `true` if the origin for the route is the same as the origin String provided, and `false` otherwise.

### String getDestination()
Returns the destination for the route.

### Boolean isDestination(String test)
Returns `true` if the destination for the route is the same as the destination String provided, and `false` otherwise.

### String getFullNumber()
Returns the route number for the route.

### Boolean isFullNumber(String test)
Returns `true` if the route number for the route is the same as the route number String provided, and `false` otherwise.

### Integer getPrimaryNumber()
Returns the number (i.e. the first character in Integer format) of the route number for the route.

### Boolean isDestination(Integer test)
Returns `true` if the first character (i.e. the number) of the route number for the route is the same as the route number Integer provided, and `false` otherwise.

### boolean equals(object obj)
If `obj` is not a `RouteInfo` object, returns `false`.\
If `obj`'s route shares an origin and destination with this route, returns `true`.\
If `obj`'s route has a different origin or different destination to this route, returns `false`.

# routes.csv
A CSV file containing every route in Mario Kart World.\
The delimiter for this file is a single comma (`,`).\
Route numbers are designated by completing different sections of the track (e.g. Crown City) or travelling through the track in an alternate direction (e.g. Mario Bros. Circuit).

# Contact Me
If you have any questions, notice any bugs, or have any suggestions for additions, please feel free to contact me by accessing [the thread for Mario Kart World in the Manuals for Archipelago Discord server](https://discord.com/channels/1097532591650910289/1379985379939192975) and pinging @belodie_music.

# Thank You
@Karramellie and her friend for inspiring this.
