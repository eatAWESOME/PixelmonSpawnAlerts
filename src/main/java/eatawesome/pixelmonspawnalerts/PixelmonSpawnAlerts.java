package eatawesome.pixelmonspawnalerts;

import eatawesome.pixelmonspawnalerts.commands.SpawnAlert;
import eatawesome.pixelmonspawnalerts.commands.SpawnAlertTP;
import eatawesome.pixelmonspawnalerts.data.AttachmentHandler;
import eatawesome.pixelmonspawnalerts.events.PlayerListener;
import eatawesome.pixelmonspawnalerts.events.RaidListener;
import eatawesome.pixelmonspawnalerts.events.SpawnListener;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PixelmonSpawnAlerts.MOD_ID)
public class PixelmonSpawnAlerts {

    public static final String MOD_ID = "pixelmonspawnalerts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    private static PixelmonSpawnAlerts instance;
    
    public PixelmonSpawnAlerts(IEventBus modEventBus) {
        instance = this;
        
        AttachmentHandler.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(PixelmonSpawnAlerts::onModLoad);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new PlayerListener());
    }

    public static void onModLoad(FMLCommonSetupEvent event) {
        Pixelmon.EVENT_BUS.register(new SpawnListener());
        //Pixelmon.EVENT_BUS.register(new RaidListener());
    }
    
    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        SpawnAlert.register(event.getDispatcher());
        SpawnAlertTP.register(event.getDispatcher());
    }

    public static PixelmonSpawnAlerts getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
