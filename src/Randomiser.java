import java.io.*;
import java.util.*;

/**
 * A class to create a set of eight random cups. These cups must satisfy the following rules:
 * <ol>
 * <li>A cup must have 4 tracks.</li>
 * <li>The first track in a cup must be traversed without routes.</li>
 * <li>The other tracks in a cup must be traversed via a route from the previous track. This route must be selectable in VS Race.</li>
 * <li>Every track must be represented at least once.</li>
 * <li>The remaining two slots must be filled by different tracks, and these tracks must use different layouts in each slot.</li>
 * <li>The Special Cup must end with Rainbow Road.</li>
 * </ol>
 */
public class Randomiser {
    public static final String DELIM = ",";
    private static int originColumn;
    private static int destinationColumn;
    private static int routeNumberColumn;

    private static ArrayList<RouteInfo> remainingUniqueTracks;
    private static ArrayList<RouteInfo> remainingDuplicateTracks;

    private static ArrayList<String> unusedTracks;
    private static ArrayList<String> usedOnceTracks;

    private static final HashMap<String,HashMap<Integer, String>> cups = new HashMap<>();

    private static HashMap<Integer, String> mushroomCup;
    private static HashMap<Integer, String> flowerCup;
    private static HashMap<Integer, String> starCup;
    private static HashMap<Integer, String> shellCup;
    private static HashMap<Integer, String> bananaCup;
    private static HashMap<Integer, String> leafCup;
    private static HashMap<Integer, String> lightningCup;
    private static HashMap<Integer, String> specialCup;

    private static boolean complete = false;

    /**
     * Converts routes.csv into an ArrayList and returns it.
     * @return routes.csv in ArrayList form.
     */
    private static ArrayList<RouteInfo> GetRouteArrayList() throws IOException {
        ArrayList<RouteInfo> allRoutes = new ArrayList<>();
        try {
            File routeCSV = new File("src/routes.csv");
            FileReader routeFileReader = new FileReader(routeCSV);
            BufferedReader routeBufferedReader = new BufferedReader(routeFileReader);
            String nextRoute = routeBufferedReader.readLine();
            String[] routeElements = nextRoute.split(DELIM);
            for (int i = 0; i < routeElements.length; i++) {
                switch (routeElements[i]) {
                    case "Origin", "ORIGIN", "origin" -> originColumn = i;
                    case "Destination", "DESTINATION", "destination" -> destinationColumn = i;
                    case "Route Number", "ROUTE NUMBER", "route number", "routeNumber", "Number", "NUMBER", "number",
                         "Num", "NUM", "num", "#" -> routeNumberColumn = i;
                }
            }
            while (routeBufferedReader.ready()) {
                nextRoute = routeBufferedReader.readLine();
                routeElements = nextRoute.split(DELIM);
                RouteInfo nextRouteInfo = new RouteInfo(routeElements[originColumn], routeElements[destinationColumn], routeElements[routeNumberColumn]);
                allRoutes.add(nextRouteInfo);
            }
            routeBufferedReader.close();
            routeFileReader.close();
        } catch (FileNotFoundException f) {
            System.out.println("routes.csv not found. Please ensure that the routes file is present and contained within the same folder as this file.");
            throw f;
        } catch (IOException i) {
            System.out.println("An IO exception has occurred. Please read the message and ensure that it is fixed.");
            System.out.println("The message is as follows:");
            System.out.println(i.getMessage());
            throw i;
        }
        return allRoutes;
    }

    /**
     * Attempts to add a track to a cup in a random position. This will fail if a duplicate layout of the track would be added to the overall set of cups.
     * @param cupName The name of the cup to add the track to
     * @param track The track to add
     * @return {@code true} if the track was successfully added to the cup.
     */
    private static boolean addTrack(String cupName, String track) {
        int startAt = new Random().nextInt(4);
        int successCount = 0;
        if (addTrack(cupName, track, startAt)) {
            successCount++;
        }
        switch (startAt) {
            case 0 -> {
                String track1 = getRandomTrackFrom(track, cupName);
                if (addTrack(cupName, track1, 1)) {
                    successCount++;
                }

                String track2 = getRandomTrackFrom(track1, cupName);
                if (addTrack(cupName, track2, 2)) {
                    successCount++;
                }

                String track3 = getRandomTrackFrom(track2, cupName);
                if (addTrack(cupName, track3, 3)) {
                    successCount++;
                }
            }
            case 1 -> {
                switch (new Random().nextInt(3)) {
                    case 0 -> {
                        String track0 = getRandomTrackTo(track, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }

                        String track2 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track2, 2)) {
                            successCount++;
                        }

                        String track3 = getRandomTrackFrom(track2, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }
                    }
                    case 1 -> {
                        String track2 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track2, 2)) {
                            successCount++;
                        }

                        String track0 = getRandomTrackTo(track, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }

                        String track3 = getRandomTrackFrom(track2, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }
                    }
                    default -> {
                        String track2 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track2, 2)) {
                            successCount++;
                        }

                        String track3 = getRandomTrackFrom(track2, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }

                        String track0 = getRandomTrackTo(track, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }
                    }
                }
            }
            case 2 -> {
                switch (new Random().nextInt(3)) {
                    case 0 -> {
                        String track3 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }

                        String track1 = getRandomTrackTo(track, cupName, false);
                        if (addTrack(cupName, track1, 1)) {
                            successCount++;
                        }

                        String track0 = getRandomTrackTo(track1, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }
                    }
                    case 1 -> {
                        String track1 = getRandomTrackTo(track, cupName, false);
                        if (addTrack(cupName, track1, 1)) {
                            successCount++;
                        }

                        String track3 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }

                        String track0 = getRandomTrackTo(track1, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }
                    }
                    default -> {
                        String track1 = getRandomTrackTo(track, cupName, false);
                        if (addTrack(cupName, track1, 1)) {
                            successCount++;
                        }

                        String track0 = getRandomTrackTo(track1, cupName, true);
                        if (addTrack(cupName, track0, 0)) {
                            successCount++;
                        }

                        String track3 = getRandomTrackFrom(track, cupName);
                        if (addTrack(cupName, track3, 3)) {
                            successCount++;
                        }
                    }
                }
            }
            case 3 -> {
                String track2 = getRandomTrackTo(track, cupName, false);
                if (addTrack(cupName, track2, 2)) {
                    successCount++;
                }

                String track1 = getRandomTrackTo(track2, cupName, false);
                if (addTrack(cupName, track1, 1)) {
                    successCount++;
                }

                String track0 = getRandomTrackTo(track1, cupName, true);
                if (addTrack(cupName, track0, 0)) {
                    successCount++;
                }
            }
        }
        return successCount == 4;
    }

    /**
     * Attempts to add a track to a cup in the specified position. This will fail if a duplicate layout of the track would be added to the overall set of cups.
     * @param cupName The name of the cup to add the track to
     * @param track The track to add
     * @param position The position in the cup to add it in
     * @return {@code true} if the track was successfully added to the cup.
     */
    private static boolean addTrack(String cupName, String track, Integer position) {
        HashMap<Integer, String> cup = cups.get(cupName);
        if (position < 3 && cup.get(position+1) != null && !cup.get(position+1).isEmpty()) {
            int followingLayoutNum = -1;
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isOrigin(track) && route.isDestination(cup.get(position+1))) {
                    followingLayoutNum = route.getPrimaryNumber();
                }
            }
            if (followingLayoutNum == -1) {
                return false;
            }
            ArrayList<RouteInfo> toRemove = new ArrayList<>();
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isDestination(cup.get(position+1)) && route.isPrimaryNumber(followingLayoutNum) && !route.isOrigin(track)) {
                    toRemove.add(route);
                }
            }
            remainingDuplicateTracks.removeAll(toRemove);
            remainingUniqueTracks.removeIf(route -> route.isOrigin(track));
        } else if (position == 3) {
            remainingUniqueTracks.removeIf(route -> route.isOrigin(track));
        }
        if (position > 0 && cup.get(position-1) != null && !cup.get(position-1).isEmpty()) {
            int priorLayoutNum = -1;
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isOrigin(cup.get(position-1)) && route.isDestination(track)) {
                    priorLayoutNum = route.getPrimaryNumber();
                }
            }
            if (priorLayoutNum == -1) {
                return false;
            }
            ArrayList<RouteInfo> toRemove = new ArrayList<>();
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isDestination(track) && route.isPrimaryNumber(priorLayoutNum) && !route.isOrigin(cup.get(position-1))) {
                    toRemove.add(route);
                }
            }
            remainingDuplicateTracks.removeAll(toRemove);
            remainingUniqueTracks.removeIf(route -> route.isDestination(track));
        } else if (position == 0) {
            int selfLayoutNum = -1;
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isOrigin(track) && route.isDestination(track)) {
                    selfLayoutNum = route.getPrimaryNumber();
                }
            }
            if (selfLayoutNum == -1) {
                return false;
            }
            ArrayList<RouteInfo> toRemove = new ArrayList<>();
            for (RouteInfo route : remainingDuplicateTracks) {
                if (route.isDestination(track) && route.isPrimaryNumber(selfLayoutNum) && !route.isOrigin(track)) {
                    toRemove.add(route);
                }
            }
            remainingDuplicateTracks.removeAll(toRemove);
            remainingUniqueTracks.removeIf(route -> route.isDestination(track));
        }
        if (unusedTracks.contains(track)) {
            unusedTracks.remove(track);
            usedOnceTracks.add(track);
        } else {
            usedOnceTracks.remove(track);
        }
        if (position == 0) {
            remainingDuplicateTracks.removeIf(route -> route.isOrigin(track) && route.isDestination(track));
        } else if (cup.get(position-1) != null && !cup.get(position-1).isEmpty()) {
            remainingDuplicateTracks.removeIf(route -> route.isOrigin(cup.get(position-1)) && route.isDestination(track));
        }
        if (position != 3 && cup.get(position+1) != null && !cup.get(position+1).isEmpty()) {
            remainingDuplicateTracks.removeIf(route -> route.isOrigin(track) && route.isDestination(cup.get(position+1)));
        }
        cup.put(position,track);
        cups.put(cupName,cup);
        return true;
    }

    /**
     * Gets a random track that has a route to the specified destination.
     * @param destination The track to connect to
     * @return A random track that has a route connecting to the specified destination, or the empty string if no such track exists.
     */
    private static String getRandomTrackTo(String destination, String cupName, boolean positionZero) {
        String toUnique = randomTrackToDestination(destination, remainingUniqueTracks, true, cupName, positionZero);
        if (toUnique.isEmpty()) {
            return randomTrackToDestination(destination, remainingDuplicateTracks, false, cupName, positionZero);
        }
        return toUnique;
    }


    /**
     * Gets a random track that has a route to the specified destination in the specified ArrayList.
     * @param destination The track to connect to
     * @return A random track that has a route connecting to the specified destination in the specified ArrayList, or the empty string if no such track exists.
     */
    private static String randomTrackToDestination(String destination, ArrayList<RouteInfo> remainingTracks, boolean onlyUniques, String cupName, boolean positionZero) {
        ArrayList<RouteInfo> validRoutes = new ArrayList<>();
        for (RouteInfo route : remainingTracks) {
            if (route.isDestination(destination)) {
                if (((!onlyUniques && usedOnceTracks.contains(route.getOrigin())) || unusedTracks.contains(route.getOrigin())) && !cups.get(cupName).containsValue(route.getOrigin()) && (!positionZero || remainingTracks.contains(new RouteInfo(route.getOrigin(), route.getDestination(), "0")))) {
                    validRoutes.add(route);
                }
            }
        }
        if (validRoutes.isEmpty()) {
            return "";
        }
        return validRoutes.get(new Random().nextInt(validRoutes.size())).getOrigin();
    }

    /**
     * Gets a random track that has a route from the specified destination.
     * @param origin The track to connect from
     * @return A random track that has a route connecting from the specified destination, or the empty string if no such track exists.
     */
    private static String getRandomTrackFrom(String origin, String cupName) {
        String fromUnique = randomTrackFromOrigin(origin, remainingUniqueTracks, true, cupName);
        if (fromUnique.isEmpty()) {
            return randomTrackFromOrigin(origin, remainingDuplicateTracks, false, cupName);
        }
        return fromUnique;
    }

    /**
     * Gets a random track that has a route from the specified destination in the specified ArrayList.
     * @param origin The track to connect from
     * @return A random track that has a route connecting from the specified destination in the specified ArrayList, or the empty string if no such track exists.
     */
    private static String randomTrackFromOrigin(String origin, ArrayList<RouteInfo> remainingDuplicateTracks, boolean onlyUniques, String cupName) {
        ArrayList<RouteInfo> validRoutes = new ArrayList<>();
        for (RouteInfo route : remainingDuplicateTracks) {
            if (route.isOrigin(origin)) {
                if (((!onlyUniques && usedOnceTracks.contains(route.getDestination())) || unusedTracks.contains(route.getDestination())) && !cups.get(cupName).containsValue(route.getDestination())) {
                    validRoutes.add(route);
                }
            }
        }
        if (validRoutes.isEmpty()) {
            return "";
        }
        return validRoutes.get(new Random().nextInt(validRoutes.size())).getDestination();
    }

    /**
     * Gets the track with the fewest onward connections remaining, or a random one of those tied if applicable. Unless every track has already been chosen once, only tracks that have yet to be selected will be considered.
     * @return A track with the fewest onward connections remaining.
     */
    private static String getMinimallyVisitedTrack() {
        ArrayList<String> minConnectionTracks = getMinimallyVisitedTrackFromSet(remainingUniqueTracks, true);

        if (minConnectionTracks.isEmpty()) {
            ArrayList<String> minDuplicateConnectionTracks = getMinimallyVisitedTrackFromSet(remainingDuplicateTracks, false);

            if (minDuplicateConnectionTracks.isEmpty()) {
                return "";
            } else {
                return minDuplicateConnectionTracks.get(new Random().nextInt(minDuplicateConnectionTracks.size()));
            }
        } else {
            return minConnectionTracks.get(new Random().nextInt(minConnectionTracks.size()));
        }
    }

    /**
     * Gets the track with the fewest onward connections remaining, or a random one of those tied if applicable.
     * @param remainingTracks the set of routes to consider tracks from
     * @param onlyUnique whether only tracks that have yet to be selected should be considered
     * @return A track with the fewest onward connections remaining.
     */
    private static ArrayList<String> getMinimallyVisitedTrackFromSet(ArrayList<RouteInfo> remainingTracks, boolean onlyUnique) {
        HashMap<String, Integer> onwardConnectionsRemaining = new HashMap<>();

        for (RouteInfo route : remainingTracks) {
            String origin = route.getOrigin();
            if (!onlyUnique || unusedTracks.contains(origin)) {
                if (!onwardConnectionsRemaining.containsKey(origin)) {
                    onwardConnectionsRemaining.put(origin, 1);
                } else {
                    onwardConnectionsRemaining.put(origin, (onwardConnectionsRemaining.get(origin) + 1));
                }
            }
        }

        int minimalConnections = Collections.min(onwardConnectionsRemaining.values());
        ArrayList<String> minConnectionTracks = new ArrayList<>();

        for (String origin : onwardConnectionsRemaining.keySet()) {
            if (onwardConnectionsRemaining.get(origin) == minimalConnections) {
                minConnectionTracks.add(origin);
            }
        }
        return minConnectionTracks;
    }

    /**
     * Attempts to create eight random cups under the following rules:
     * <ol>
     * <li>A cup must have 4 tracks.</li>
     * <li>The first track in a cup must be traversed without routes.</li>
     * <li>The other tracks in a cup must be traversed via a route from the previous track. This route must be selectable in VS Race.</li>
     * <li>Every track must be represented at least once.</li>
     * <li>The remaining two slots must be filled by different tracks, and these tracks must use different layouts in each slot.</li>
     * <li>The Special Cup must end with Rainbow Road.</li>
     * </ol>
     * @return {@code true} if a set of eight random cups was successfully created.
     */
    private static boolean randomiseCups() {
        cups.put("1",new HashMap<>());
        cups.put("2",new HashMap<>());
        cups.put("3",new HashMap<>());
        cups.put("4",new HashMap<>());
        cups.put("5",new HashMap<>());
        cups.put("6",new HashMap<>());
        cups.put("7",new HashMap<>());
        cups.put("Special",new HashMap<>());

        if (!addTrack("Special", "Rainbow Road", 3)) {
            System.out.println("Something very wrong is going on.");
        }
        if (!addTrack("Special", "Peach Stadium", 2)) {
            System.out.println("Something quite wrong is going on.");
        }
        String special1 = getRandomTrackTo(cups.get("Special").get(2),"Special", false);
        if (special1.isEmpty()) {
            return false;
        }
        if (!addTrack("Special",special1,1)) {
            return false;
        }
        String special0 = getRandomTrackTo(cups.get("Special").get(1),"Special", true);
        if (special0.isEmpty()) {
            return false;
        }
        if (!addTrack("Special",special0,0)) {
            return false;
        }

        if (!addTrack("1",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("2",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("3",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("4",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("5",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("6",getMinimallyVisitedTrack())) {
            return false;
        }
        if (!addTrack("7",getMinimallyVisitedTrack())) {
            return false;
        }

        if (!unusedTracks.isEmpty()) {
            return false;
        }

        ArrayList<HashMap<Integer, String>> cupOrder = new ArrayList<>();
        cupOrder.add(cups.get("1"));
        cupOrder.add(cups.get("2"));
        cupOrder.add(cups.get("3"));
        cupOrder.add(cups.get("4"));
        cupOrder.add(cups.get("5"));
        cupOrder.add(cups.get("6"));
        cupOrder.add(cups.get("7"));
        Collections.shuffle(cupOrder);

        mushroomCup = cupOrder.get(0);
        flowerCup = cupOrder.get(1);
        starCup = cupOrder.get(2);
        shellCup = cupOrder.get(3);
        bananaCup = cupOrder.get(4);
        leafCup = cupOrder.get(5);
        lightningCup = cupOrder.get(6);
        specialCup = cups.get("Special");

        cups.clear();
        cups.put("Mushroom",mushroomCup);
        cups.put("Flower",flowerCup);
        cups.put("Star",starCup);
        cups.put("Shell",shellCup);
        cups.put("Banana",bananaCup);
        cups.put("Leaf",leafCup);
        cups.put("Lightning",lightningCup);
        cups.put("Special",specialCup);

        return true;
    }

    /**
     * Performs setup to create eight random cups, then repeatedly calls {@code randomiseCups()} until it successfully creates a valid set of eight random cups.
     */
    public static void randomise() {
        complete = false;
        ArrayList<RouteInfo> allRoutes;
        try {
            allRoutes = GetRouteArrayList();
        } catch (IOException _) {
            return;
        }
        remainingUniqueTracks = new ArrayList<>();
        remainingDuplicateTracks = new ArrayList<>();

        remainingUniqueTracks.addAll(allRoutes);
        remainingDuplicateTracks.addAll(allRoutes);

        unusedTracks = new ArrayList<>();
        usedOnceTracks = new ArrayList<>();

        for (RouteInfo route : allRoutes) {
            String destination = route.getDestination();
            if (!unusedTracks.contains(destination)) {
                unusedTracks.add(destination);
            }
        }

        boolean validRandomisation = randomiseCups();;
        while (!validRandomisation) {
            usedOnceTracks.clear();
            unusedTracks.clear();
            cups.clear();

            remainingUniqueTracks.clear();
            remainingDuplicateTracks.clear();
            remainingUniqueTracks.addAll(allRoutes);
            remainingDuplicateTracks.addAll(allRoutes);

            for (RouteInfo route : allRoutes) {
                String destination = route.getDestination();
                if (!unusedTracks.contains(destination)) {
                    unusedTracks.add(destination);
                }
            }
            validRandomisation = randomiseCups();
        }
        complete = true;
    }

    /**
     * If randomisation is complete, prints the cups that were created. Otherwise, prints an error message.
     */
    public static void printRandomisedCups() {
        if (complete) {
            System.out.println("Mushroom Cup: " + mushroomCup.get(0) + ", " + mushroomCup.get(1) + ", " + mushroomCup.get(2) + ", " + mushroomCup.get(3));
            System.out.println("Flower Cup: " + flowerCup.get(0) + ", " + flowerCup.get(1) + ", " + flowerCup.get(2) + ", " + flowerCup.get(3));
            System.out.println("Star Cup: " + starCup.get(0) + ", " + starCup.get(1) + ", " + starCup.get(2) + ", " + starCup.get(3));
            System.out.println("Shell Cup: " + shellCup.get(0) + ", " + shellCup.get(1) + ", " + shellCup.get(2) + ", " + shellCup.get(3));
            System.out.println("Banana Cup: " + bananaCup.get(0) + ", " + bananaCup.get(1) + ", " + bananaCup.get(2) + ", " + bananaCup.get(3));
            System.out.println("Leaf Cup: " + leafCup.get(0) + ", " + leafCup.get(1) + ", " + leafCup.get(2) + ", " + leafCup.get(3));
            System.out.println("Lightning Cup: " + lightningCup.get(0) + ", " + lightningCup.get(1) + ", " + lightningCup.get(2) + ", " + lightningCup.get(3));
            System.out.println("Special Cup: " + specialCup.get(0) + ", " + specialCup.get(1) + ", " + specialCup.get(2) + ", " + specialCup.get(3));
        }
        else {
            System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        }
    }

    /**
     * If randomisation is complete, saves the cups that were created to src/Random Cups YYYY-MM-DD hh:mm:ss.txt. Otherwise, prints an error message.
     */
    public static void saveRandomisedCups() {
        saveRandomisedCups("src/Random Cups YYYY-MM-DD hh:mm:ss.txt");
    }

    /**
     * If randomisation is complete, saves the cups that were created. Otherwise, prints an error message.<p>
     * If a file already exists with the provided name, the name of the saved file will have {@code " (1)"} appended.
     * @param pathname The path and name for the file that is saved. ".txt" will automatically be appended if not present.
     */
    public static void saveRandomisedCups(String pathname) {
        if (!complete) {
            System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
            return;
        }
        if (!pathname.endsWith(".txt")) {
            pathname += ".txt";
        }
        File toSave = new File(pathname);
        try {
            while (!toSave.createNewFile()) {
                toSave = new File(pathname.substring(0,pathname.length()-4)+" (1).txt");
            }
            if (!toSave.canWrite()) {
                if (!toSave.setWritable(true)) {
                    System.out.println("The file was created; however, this program does not have permission to write to it.");
                    return;
                }
            }
            FileWriter cupFileWriter = new FileWriter(toSave);
            BufferedWriter cupBufferedWriter = new BufferedWriter(cupFileWriter);
            cupBufferedWriter.write("Mushroom Cup: " + mushroomCup.get(0) + ", " + mushroomCup.get(1) + ", " + mushroomCup.get(2) + ", " + mushroomCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Flower Cup: " + flowerCup.get(0) + ", " + flowerCup.get(1) + ", " + flowerCup.get(2) + ", " + flowerCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Star Cup: " + starCup.get(0) + ", " + starCup.get(1) + ", " + starCup.get(2) + ", " + starCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Shell Cup: " + shellCup.get(0) + ", " + shellCup.get(1) + ", " + shellCup.get(2) + ", " + shellCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Banana Cup: " + bananaCup.get(0) + ", " + bananaCup.get(1) + ", " + bananaCup.get(2) + ", " + bananaCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Leaf Cup: " + leafCup.get(0) + ", " + leafCup.get(1) + ", " + leafCup.get(2) + ", " + leafCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Lightning Cup: " + lightningCup.get(0) + ", " + lightningCup.get(1) + ", " + lightningCup.get(2) + ", " + lightningCup.get(3));
            cupBufferedWriter.newLine();
            cupBufferedWriter.write("Special Cup: " + specialCup.get(0) + ", " + specialCup.get(1) + ", " + specialCup.get(2) + ", " + specialCup.get(3));
            cupBufferedWriter.close();
            cupFileWriter.close();
        } catch (IOException e) {
            System.out.println("An IO exception has occurred.");
            System.out.println("The accompanying message is as follows: "+e.getMessage());
        }
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Mushroom Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Mushroom Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getMushroomCup() {
        if (complete) {
            return mushroomCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Flower Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Flower Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getFlowerCup() {
        if (complete) {
            return flowerCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Star Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Star Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getStarCup() {
        if (complete) {
            return starCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Shell Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Shell Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getShellCup() {
        if (complete) {
            return shellCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Banana Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Banana Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getBananaCup() {
        if (complete) {
            return bananaCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Leaf Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Leaf Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getLeafCup() {
        if (complete) {
            return leafCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Lightning Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Lightning Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getLightningCup() {
        if (complete) {
            return lightningCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing the Special Cup tracks. Otherwise, prints an error message and returns an empty HashMap.
     * @return The Special Cup HashMap if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<Integer, String> getSpecialCup() {
        if (complete) {
            return specialCup;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * If randomisation is complete, returns a HashMap containing every cup's HashMaps. Otherwise, prints an error message and returns an empty HashMap.
     * @return The HashMap containing every cup if randomisation is complete, or an empty HashMap otherwise.
     */
    public static HashMap<String, HashMap<Integer, String>> getAllCups() {
        if (complete) {
            return cups;
        }
        System.out.println("Randomisation is not complete - either it has not begun or it is still in-progress.");
        return new HashMap<>();
    }

    /**
     * Returns whether randomisation is complete.
     * @return {@code true} if randomisation is complete.
     */
    public static boolean isComplete() {
        return complete;
    }

    public static void main(String[] args) {
        randomise();
        printRandomisedCups();
    }
}