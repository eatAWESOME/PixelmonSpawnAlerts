package eatAWESOME.pixelmonspawnalerts.events;

import eatAWESOME.pixelmonspawnalerts.data.AttachmentHandler;
import eatAWESOME.pixelmonspawnalerts.data.SpawnAlertData;
import eatAWESOME.pixelmonspawnalerts.utils.SpawnAlertTracker;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerListener {
    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        SpawnAlertData data = player.getData(AttachmentHandler.SPAWN_ALERT_DATA);

        SpawnAlertTracker.updateSpawnAlert(
                player.getUUID(),
                data.getSpawnAlert()
        );
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        SpawnAlertTracker.removeSpawnAlert(
                event.getEntity().getUUID()
        );
    }
}