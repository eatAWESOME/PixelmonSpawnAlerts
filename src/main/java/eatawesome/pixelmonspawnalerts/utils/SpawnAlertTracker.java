package eatawesome.pixelmonspawnalerts.utils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnAlertTracker {

    private static final Map<UUID, String> SPAWN_ALERTS = new ConcurrentHashMap<>();

    public static void updateSpawnAlert(UUID playerId, String targetName) {
        SPAWN_ALERTS.put(playerId, targetName);
    }

    public static void removeSpawnAlert(UUID playerId) {
        SPAWN_ALERTS.remove(playerId);
    }

    public static String getSpawnAlert(UUID playerId) {
        return SPAWN_ALERTS.get(playerId);
    }

    public static Map<UUID, String> getAllSpawnAlerts() {
        return Collections.unmodifiableMap(SPAWN_ALERTS);
    }
}