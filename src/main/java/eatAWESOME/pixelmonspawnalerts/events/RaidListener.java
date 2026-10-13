package eatAWESOME.pixelmonspawnalerts.events;

import eatAWESOME.pixelmonspawnalerts.PixelmonSpawnAlerts;
import eatAWESOME.pixelmonspawnalerts.utils.SpawnAlertTracker;
import eatAWESOME.pixelmonspawnalerts.utils.SpawnAlertUtils;

import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class RaidListener {

    
    /*@SubscribeEvent
    public void onRaidRandomize (RandomizeRaidEvent.ChooseSpecies event) {
        Species species = event.getRaid().getSpecies();
        String speciesName = species.getStrippedName();
        MutableBlockPos position = event.den.blockPosition().mutable();
        if (SpawnAlertTracker.getAllSpawnAlerts().values().stream().anyMatch(targetName -> targetName.equals(speciesName))) {
            ServerPlayer closestPlayer = SpawnAlertUtils.getClosestPlayer(position);
            if (SpawnAlertTracker.getSpawnAlert(closestPlayer.getUUID()).equals(speciesName)) {
                sendAlerts(species, position, closestPlayer);
            }
        }
        if (species.isLegendary() || species.isMythical() || species.isUltraBeast()) {
            sendMessages(species, position, SpawnAlertUtils.getClosestPlayer(position));
        }
    }*/
    
    
    public void sendMessages(Species species, MutableBlockPos position, ServerPlayer closestPlayer) {
        ChatFormatting color = SpawnAlertUtils.getChatFormatting(species);
        MutableComponent formattedPrefix = SpawnAlertUtils.getPrefix(species, color);
        
        String coordinatesMessage = SpawnAlertUtils.formatCoordinates(position);
        String coordinatesCommand = SpawnAlertUtils.formatTeleportCommand(position);
        Style coordinatesStyle = SpawnAlertUtils.createCoordinatesStyle(color, coordinatesMessage, coordinatesCommand);
        
        MutableComponent nullMessage = formattedPrefix.copy()
                .append(Component.literal(species.getTranslatedName().getString() + " raid appeared at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        MutableComponent closeMessage = formattedPrefix.copy()
                .append(Component.literal(species.getTranslatedName().getString() + " raid appeared near you at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        MutableComponent otherMessage = formattedPrefix.copy()
                .append(Component.literal(species.getTranslatedName().getString() + " raid appeared near " + closestPlayer.getName().getString() + " at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (closestPlayer == null) {
                player.sendSystemMessage(nullMessage);
                SpawnAlertUtils.sendSound(player, species);
            } else if (player.equals(closestPlayer)) {
                player.sendSystemMessage(closeMessage);
                SpawnAlertUtils.sendSound(player, species);
            } else {
                player.sendSystemMessage(otherMessage);
            }
        }
        PixelmonSpawnAlerts.LOGGER.info("[PixelmonSpawnAlerts]: " + nullMessage.getString().replace("at", "near " + closestPlayer.getName().getString() + " at"));
    }
    
    public void sendAlerts(Species species, MutableBlockPos position, ServerPlayer player) {
        ChatFormatting color = SpawnAlertUtils.getChatFormatting(species);
        MutableComponent formattedPrefix = SpawnAlertUtils.getPrefix(species, color);
        
        String coordinatesMessage = SpawnAlertUtils.formatCoordinates(position);
        String coordinatesCommand = SpawnAlertUtils.formatTeleportCommand(position);
        Style coordinatesStyle = SpawnAlertUtils.createCoordinatesStyle(color, coordinatesMessage, coordinatesCommand);
        
        MutableComponent closeMessage = formattedPrefix
                .append(Component.literal(species.getTranslatedName().getString() + " raid appeared near you at ").withStyle(color))
                .append(Component.literal(coordinatesMessage).withStyle(coordinatesStyle))
                .append(Component.literal("!").withStyle(color));
        
        PixelmonSpawnAlerts.LOGGER.info("[PixelmonSpawnAlerts]: " + closeMessage.getString().replace("you", player.getName().getString()));
        player.sendSystemMessage(closeMessage);
        SpawnAlertUtils.sendSound(player, species);
    }
}