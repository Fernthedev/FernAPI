package com.github.fernthedev.fernapi.universal.util;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class UUIDFetcher {
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%name%";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%uuid%/names";

    private static final Map<String, PlayerUUID> playerUUIDCache = new HashMap<>();
    private static final Map<UUID, PlayerName> playerNameCache = new HashMap<>();
    private static final Map<UUID, List<PlayerHistory>> playerHistoryCache = new HashMap<>();

    // Use compiled pattern to improve performance of bulk operations
    private static final Pattern pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    private static int requests = 0;

    public static int maxRequestsAllowed = 600;
    public static int minimumRequestsNeededForCache = (int) (maxRequestsAllowed * 0.8);

    public static int CLEAR_CACHE_TIME_MINS = 90;

    private static UUIDFetchManager fetchManager = new UUIDFetchManager(() -> {
        playerHistoryCache.clear();
        playerNameCache.clear();
        playerUUIDCache.clear();
    });

    /**
     * @param fetchManager
     * @deprecated It is now deprecated because it is no longer needed to use specific-interface
     * uuid fetchers with the use of com.github.fernthedev.fernapi.com.github.fernthedev.fernapi.universal schedulers now.
     * Use only to override default behaviour
     */
    @Deprecated
    public static void setFetchManager(UUIDFetchManager fetchManager) {
        UUIDFetcher.fetchManager = fetchManager;
    }

    public static void setRequests(int requests) {
        UUIDFetcher.requests = requests;
    }



    @Getter
    @Setter
    private static boolean hourRan = true;

    @Getter
    @Setter
    private static boolean didHourCheck = false;

    private UUIDFetcher() { }

    public static void addRequestTimer() {
        try {
            fetchManager.runTimerRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UUIDFetchManager getFetchManager() {
        return fetchManager;
    }

    public static void stopRequestTimer() {
        fetchManager.stopTimerRequest();
    }

    public static void stopHourTask() {
        fetchManager.stopHourTask();
    }

    public static UUID getUUID(String name) {
        if (Universal.getMethods().getUUIDFromPlayerName(name) != null) {
            return Universal.getMethods().getUUIDFromPlayerName(name);
        }

        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // read JSON file dataInfo as String

        if (name == null) return null;

        try {
            String fileData = readUrl(UUID_URL.replace("%name%", name));

            if (fileData == null) {
                if (playerUUIDCache.containsKey(name)) {
//                    String hexStringWithoutHyphens = playerUUIDCache.get(name).id;
//// Use regex to format the hex string by inserting hyphens in the canonical format: 8-4-4-4-12
////                    String hexStringWithInsertedHyphens =  hexStringWithoutHyphens.replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
//
//                    String hexStringWithInsertedHyphens = pattern.matcher(hexStringWithoutHyphens).replaceAll("$1-$2-$3-$4-$5");

                    return uuidFromString ( playerUUIDCache.get(name).id );
                } else return null;
            } else {

                PlayerUUID uuidResponse = gson.fromJson(fileData, PlayerUUID.class);

                if (uuidResponse == null) {
                    return null;
                }

                Universal.debug("The uuid for " + name + " is " + uuidResponse.getId());

                playerUUIDCache.put(name, uuidResponse);

//                String hexStringWithoutHyphens = uuidResponse.getId();
//// Use regex to format the hex string by inserting hyphens in the canonical format: 8-4-4-4-12
////                String hexStringWithInsertedHyphens =  hexStringWithoutHyphens.replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
//
//                String hexStringWithInsertedHyphens = pattern.matcher(hexStringWithoutHyphens).replaceAll("$1-$2-$3-$4-$5");

                return uuidFromString( uuidResponse.getId() );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getName(@NonNull String uuid) {
        return getName(uuidFromString(uuid));
    }

    public static String getName(@NonNull UUID uuid) {
        try {
            if (Universal.getMethods().getNameFromPlayer(uuid) != null) {
                return Universal.getMethods().getNameFromPlayer(uuid);
            }
        } catch (Exception e) {
            throw new FernRuntimeException("Unable to find name, perhaps the UUID is invalid?", e);
        }

        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String uuidStr = uuid.toString().replaceAll("-", "");
        // read JSON file dataInfo as String
        try {
            String fileData = readUrl(NAME_URL.replace("%uuid%", uuidStr));

            if (fileData == null) {
                if (playerNameCache.containsKey(uuid)) return playerNameCache.get(uuid).name;
                else return null;
            } else {

                Universal.debug("The url of name is " + NAME_URL.replace("%uuid%", uuidStr));

                PlayerName[] uuidResponse = gson.fromJson(fileData, PlayerName[].class);

                if (uuidResponse != null) {

                    Universal.debug("The max length of response is " + uuidResponse.length);

                    if (uuidResponse.length > 0) {

                        PlayerName currentName = uuidResponse[uuidResponse.length - 1];

                        Universal.debug("The current name is " + currentName.name);

//                        if (playerNameCache.get(uuid) != currentName) playerHistoryCache.remove(uuid);
                        playerNameCache.put(uuid, currentName);

                        return currentName.name;
                    } else {
                        return null;
                    }
                } else {
                    Universal.debug("The response was empty");
                    Universal.debug("The response received is " + fileData);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PlayerHistory> getNameHistory(String uuid) {
        return getNameHistory(uuidFromString(uuid));
    }

    /**
     * This can be used to get name history
     *
     * @param uuid The uuid of the player
     * @return The name history.
     */
    @Nullable
    public static List<PlayerHistory> getNameHistory(UUID uuid) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (uuid == null) return null;

        String uuidStr = uuid.toString().replaceAll("-", "");
        // read JSON file dataInfo as String
        try {
            String fileData = readUrl(NAME_URL.replace("%uuid%", uuidStr));

            if (fileData == null) {
                if (playerHistoryCache.containsKey(uuid)) {
                    return playerHistoryCache.get(uuid);
                }

            } else {
                Universal.debug("The url of namehistory is " + NAME_URL.replace("%uuid%", uuidStr));

                PlayerName[] uuidResponse = gson.fromJson(fileData, PlayerName[].class);

                if (uuidResponse != null) {

                    Universal.debug("The max length of response is " + uuidResponse.length);
                    for (int i = 0; i < uuidResponse.length; i++) {
                        PlayerName playerUUID = uuidResponse[i];
                        Universal.debug("A name from uuid " + uuid + " is " + playerUUID.name + " at length " + i);
                    }

                    if (uuidResponse.length > 0) {

                        List<PlayerHistory> names = new ArrayList<>();
                        for (PlayerName currentResponse : uuidResponse) {
                            names.add(new PlayerHistory(currentResponse.name, new Date(currentResponse.getChangedToAt()), currentResponse.changedToAt));
                        }

//                        if (playerHistoryCache.get(uuid) != names) playerHistoryCache.remove(uuid);
                        playerHistoryCache.put(uuid, names);

                        return names;
                    } else {
                        return null;
                    }
                } else {
                    Universal.debug("The response was empty");
                    Universal.debug("The response received is " + fileData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String readUrl(String urlString) throws Exception {
        if (requests <= minimumRequestsNeededForCache) {
            URL url = new URL(urlString);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder buffer = new StringBuilder();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);

                return buffer.toString();
            } catch (IOException e) {
                if (e.getMessage().contains("code: 429") || e.getMessage().contains("429")) {
                    requests = 601;
                    debug("Received error 429, waiting for an hour to continue checking for uuids");
                    addBanHourTask();
                    return null;
                } else {
                    e.printStackTrace();
                }
            } finally {
                requests++;
            }
        } else {
            debug("There is over " + minimumRequestsNeededForCache + " requests sent, waiting for requests to be refreshed.");
            return null;
        }
        return null;
    }

    public static class PlayerHistory {
        private String name;
        private Date date;
        private long timeDate;

        PlayerHistory(String name, Date time, long timeDate) {
            this.name = name;
            this.date = time;
            this.timeDate = timeDate;
        }

        public long getTimeDateInt() {
            return timeDate;
        }

        public String getName() {
            return name;
        }

        public Date getTime() {
            return date;
        }
    }

    public static class PlayerName {
        private String name;
        private long changedToAt;

        public String getName() {
            return name;
        }

        public long getChangedToAt() {
            return changedToAt;
        }
    }


    public static class PlayerUUID {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }

    private static void addBanHourTask() {
        stopRequestTimer();

        hourRan = false;
        didHourCheck = false;

        fetchManager.runHourTask();


    }

    protected static void debug(Object log) {
        Universal.debug("[UUIDFetcher/FernAPI] " + log);
    }

    public static UUID uuidFromString(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            String hexStringWithInsertedHyphens = pattern.matcher(str).replaceAll("$1-$2-$3-$4-$5");

            return UUID.fromString(hexStringWithInsertedHyphens);
        }
    }
}