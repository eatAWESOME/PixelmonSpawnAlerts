package eatAWESOME.pixelmonspawnalerts.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;

import eatAWESOME.pixelmonspawnalerts.data.AttachmentHandler;
import eatAWESOME.pixelmonspawnalerts.data.SpawnAlertData;
import eatAWESOME.pixelmonspawnalerts.utils.SpawnAlertTracker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.ArrayList;
import java.util.List;

public class SpawnAlert {

    public SpawnAlert() {
    }
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnalert")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    SpawnAlertData data = player.getData(AttachmentHandler.SPAWN_ALERT_DATA);
                    String oldTargetName = data.getSpawnAlert();
                    if (oldTargetName.equals("disable")) {
                        context.getSource().sendSuccess(() -> Component.literal("No active spawn alerts."), false);
                    } else {
                        context.getSource().sendSuccess(() -> Component.literal("Current spawn alert: " + oldTargetName), false);
                    }
                    return Command.SINGLE_SUCCESS;
                })
            .then(Commands.argument("target", StringArgumentType.word())
                .suggests((context, builder) -> {
                    List<String> suggestions = new ArrayList<>();
                    suggestions.add("disable");
                    for (Species pokedexSpecies : PixelmonSpecies.getAll()) {
                        suggestions.add(pokedexSpecies.getStrippedName());
                    }
                    return SharedSuggestionProvider.suggest(suggestions, builder);
                })
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    String targetName = reformatSpecies(StringArgumentType.getString(context, "target"));
                    
                    if (!targetName.equals("disable") && !validateSpecies(targetName)) {
                        context.getSource().sendFailure(Component.literal("Invalid target. Entry must be 'disable' or a pok�mon species."));
                    } else {
                        SpawnAlertData data = player.getData(AttachmentHandler.SPAWN_ALERT_DATA);
                        String oldTargetName = data.getSpawnAlert();
                        data.setSpawnAlert(targetName);
                        SpawnAlertTracker.updateSpawnAlert(player.getUUID(), targetName);
                        
                        if (targetName.equals("disable")) {
                            if (oldTargetName.equals("disable")) {
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts already disabled."), false);
                            } else {
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts disabled."), false);
                            }
                        } else {
                            if (oldTargetName.equals(targetName)) {
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts already enabled for " + targetName + "."), false);
                            } else if (oldTargetName.equals("disable")) {
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts enabled for " + targetName + "."), false);
                            } else {
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts disabled for " + oldTargetName + "."), false);
                                context.getSource().sendSuccess(() -> Component.literal("Spawn alerts enabled for " + targetName + "."), false);
                            }
                        }
                    }
                    return Command.SINGLE_SUCCESS;
                })));
    }
    
    public static boolean validateSpecies(String targetName) {
        for (Species pokedexSpecies : PixelmonSpecies.getAll()) {
            if (pokedexSpecies.getStrippedName().toLowerCase().equals(targetName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    public static String reformatSpecies(String targetName) {
        if (targetName.toLowerCase().equals("disable")) {
            return "disable";
        }
        for (Species pokedexSpecies : PixelmonSpecies.getAll()) {
            if (pokedexSpecies.getStrippedName().toLowerCase().equals(targetName.toLowerCase())) {
                return pokedexSpecies.getStrippedName();
            }
        }
        return targetName;
    }
}