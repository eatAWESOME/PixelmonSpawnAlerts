package eatAWESOME.pixelmonspawnalerts.data;

import java.util.function.Supplier;

import eatAWESOME.pixelmonspawnalerts.PixelmonSpawnAlerts;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class AttachmentHandler {
    private AttachmentHandler() {
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    PixelmonSpawnAlerts.MOD_ID
            );

    public static final Supplier<AttachmentType<SpawnAlertData>>
            SPAWN_ALERT_DATA =
                    ATTACHMENT_TYPES.register(
                            "spawn_alert_data",
                            () -> AttachmentType.serializable(
                                            SpawnAlertData::new
                                    )
                                    .copyOnDeath()
                                    .build()
                    );
}