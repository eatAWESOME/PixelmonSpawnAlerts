package eatawesome.pixelmonspawnalerts.events;

import eatawesome.pixelmonspawnalerts.PixelmonSpawnAlerts;
import eatawesome.pixelmonspawnalerts.utils.SpawnAlertTracker;
import eatawesome.pixelmonspawnalerts.utils.SpawnAlertUtils;

import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class SpawnListener {
    
    @SubscribeEvent
    public void onSpawn(SpawnEvent event) {
        Entity entity = event.action.getOrCreateEntity();
        if (entity instanceof PixelmonEntity) {
            MutableBlockPos position = event.action.spawnLocation.location.pos;
            processSpawn((PixelmonEntity) entity, position);
        }
    }
    
    @SubscribeEvent
    public void onSpawnerSpawn(PixelmonSpawnerEvent.Post event) {
        PixelmonEntity pixelmonEntity = event.getEntity();
        MutableBlockPos position = event.getSpawnPosition().mutable();
        processSpawn(pixelmonEntity, position);
    }
    
    public void processSpawn(PixelmonEntity pixelmonEntity, MutableBlockPos position) {
        if (!pixelmonEntity.isBossPokemon()) {
            Pokemon pokemon = pixelmonEntity.getPokemon();
            String speciesName = pokemon.getSpecies().getStrippedName();
            if (SpawnAlertTracker.getAllSpawnAlerts().values().stream().anyMatch(targetName -> targetName.equals(speciesName))) {
                ServerPlayer closestPlayer = SpawnAlertUtils.getClosestPlayer(position);
                if (SpawnAlertTracker.getSpawnAlert(closestPlayer.getUUID()).equals(speciesName)) {
                    sendAlerts(pokemon, position, closestPlayer);
                }
            }
            if (pokemon.isShiny() || pokemon.isLegendary() || pokemon.isMythical() || pokemon.isUltraBeast()) {
                    sendMessages(pokemon, position, SpawnAlertUtils.getClosestPlayer(position));
            }
        }
    }
    
    public void sendMessages(Pokemon pokemon, MutableBlockPos position, ServerPlayer closestPlayer) {
        ChatFormatting color = SpawnAlertUtils.getChatFormatting(pokemon);
        MutableComponent formattedPrefix = SpawnAlertUtils.getPrefix(pokemon, color);
        
        String coordinatesMessage = SpawnAlertUtils.formatCoordinates(position);
        String coordinatesCommand = SpawnAlertUtils.formatTeleportCommand(position);
        Style coordinatesStyle = SpawnAlertUtils.createCoordinatesStyle(color, coordinatesMessage, coordinatesCommand);
        
        MutableComponent nullMessage = formattedPrefix.copy()
                .append(Component.literal(" spawned at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        MutableComponent closeMessage = formattedPrefix.copy()
                .append(Component.literal(" spawned near you at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        MutableComponent otherMessage = formattedPrefix.copy()
                .append(Component.literal(" spawned near " + closestPlayer.getName().getString() + "!").withStyle(color));
        
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (closestPlayer == null) {
                player.sendSystemMessage(nullMessage);
                SpawnAlertUtils.sendSound(player, pokemon);
            } else if (player.equals(closestPlayer)) {
                player.sendSystemMessage(closeMessage);
                SpawnAlertUtils.sendSound(player, pokemon);
            } else {
                player.sendSystemMessage(otherMessage);
            }
        }
        PixelmonSpawnAlerts.LOGGER.info("[PixelmonSpawnAlerts]: " + nullMessage.getString().replace("at", "near " + closestPlayer.getName().getString() + " at"));
    }
    
    public void sendAlerts(Pokemon pokemon, MutableBlockPos position, ServerPlayer player) {
        ChatFormatting color = SpawnAlertUtils.getChatFormatting(pokemon);
        MutableComponent formattedPrefix = SpawnAlertUtils.getPrefix(pokemon, color);
        
        String coordinatesMessage = SpawnAlertUtils.formatCoordinates(position);
        String coordinatesCommand = SpawnAlertUtils.formatTeleportCommand(position);
        Style coordinatesStyle = SpawnAlertUtils.createCoordinatesStyle(color, coordinatesMessage, coordinatesCommand);
        
        MutableComponent closeMessage = formattedPrefix
                .append(Component.literal(" spawned near you at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        
        PixelmonSpawnAlerts.LOGGER.info("[PixelmonSpawnAlerts]: " + closeMessage.getString().replace("you", player.getName().getString()));
        player.sendSystemMessage(closeMessage);
        SpawnAlertUtils.sendSound(player, pokemon);
    }
}