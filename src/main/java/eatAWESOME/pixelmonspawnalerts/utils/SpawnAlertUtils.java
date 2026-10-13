package eatAWESOME.pixelmonspawnalerts.utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class SpawnAlertUtils {
    
    public static ServerPlayer getClosestPlayer(MutableBlockPos position) {
        ServerPlayer closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            double distance = player.distanceToSqr(position.getX(), position.getY(), position.getZ());
            if (distance < closestDistance) {
                closestPlayer = player;
                closestDistance = distance;
            }
        }
        return closestPlayer;
    }
    
    public static ChatFormatting getChatFormatting(Pokemon pokemon) {
        Species species = pokemon.getSpecies();
        ChatFormatting color = ChatFormatting.WHITE;
        if (species.isLegendary()) {
            color = ChatFormatting.GREEN;
        } else if (species.isMythical()) {
            color = ChatFormatting.LIGHT_PURPLE;
        } else if (species.isUltraBeast()) {
            color = ChatFormatting.DARK_GRAY;
        } else if (pokemon.isShiny()) {
            color = ChatFormatting.GOLD;
        }
        return color;
    }
    
    public static ChatFormatting getChatFormatting(Species species) {
        ChatFormatting color = ChatFormatting.WHITE;
        if (species.isLegendary()) {
            color = ChatFormatting.GREEN;
        } else if (species.isMythical()) {
            color = ChatFormatting.LIGHT_PURPLE;
        } else if (species.isUltraBeast()) {
            color = ChatFormatting.DARK_GRAY;
        }
        return color;
    }
    
    public static MutableComponent getPrefix(Pokemon pokemon, ChatFormatting color) {
        Species species = pokemon.getSpecies();
        MutableComponent formattedPrefix = null;
        if (pokemon.isShiny()) {
            formattedPrefix = Component.literal("A shiny " + species.getTranslatedName().getString()).withStyle(ChatFormatting.GOLD);
        } else {
            char firstChar = species.getTranslatedName().getString().toLowerCase().charAt(0);
            if (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u' || firstChar == 'y') {
                formattedPrefix = Component.literal("An " + species.getTranslatedName().getString()).withStyle(color);
            } else {
                formattedPrefix = Component.literal("A " + species.getTranslatedName().getString()).withStyle(color);
            }
        }
        return formattedPrefix;
    }
    
    public static MutableComponent getPrefix(Species species, ChatFormatting color) {
        MutableComponent formattedPrefix = null;
        char firstChar = species.getTranslatedName().getString().toLowerCase().charAt(0);
        if (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u' || firstChar == 'y') {
            formattedPrefix = Component.literal("An " + species.getTranslatedName().getString()).withStyle(color);
        } else {
            formattedPrefix = Component.literal("A " + species.getTranslatedName().getString()).withStyle(color);
        }
        return formattedPrefix;
    }

    public static String formatCoordinates(MutableBlockPos position) {
        return position.getX() + ", " + position.getY() + ", " + position.getZ();
    }

    public static String formatTeleportCommand(MutableBlockPos position) {
        return "/spawnalerttp " + position.getX() + " " + position.getY() + " " + position.getZ();
    }
    
    public static Style createCoordinatesStyle(ChatFormatting color, String coordinatesMessage, String coordinatesCommand) {
        return Style.EMPTY
                .withColor(color)
                .withUnderlined(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, coordinatesCommand))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Teleport to " + coordinatesMessage + "!")));
    }
    
    public static void sendSound(ServerPlayer player, Pokemon pokemon) {
        Species species = pokemon.getSpecies();
        SoundEvent sound = SoundEvents.FIREWORK_ROCKET_LAUNCH;
        if (species.isLegendary()) {
            sound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
        } else if (species.isMythical()) {
            sound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
        } else if (species.isUltraBeast()) {
            sound = SoundEvents.ENDER_DRAGON_GROWL;
        } else if (pokemon.isShiny()) {
            sound = SoundEvents.PLAYER_LEVELUP;
        }
        player.playSound(sound, 2.0F, 1.0F);
    }
    
    public static void sendSound(ServerPlayer player, Species species) {
        SoundEvent sound = SoundEvents.FIREWORK_ROCKET_LAUNCH;
        if (species.isLegendary()) {
            sound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
        } else if (species.isMythical()) {
            sound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
        } else if (species.isUltraBeast()) {
            sound = SoundEvents.ENDER_DRAGON_GROWL;
        }
        player.playSound(sound, 2.0F, 1.0F);
    }
}