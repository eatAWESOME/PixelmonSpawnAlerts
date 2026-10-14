package eatawesome.pixelmonspawnalerts.events;

import eatawesome.pixelmonspawnalerts.data.AttachmentHandler;
import eatawesome.pixelmonspawnalerts.data.SpawnAlertData;
import eatawesome.pixelmonspawnalerts.utils.SpawnAlertTracker;

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