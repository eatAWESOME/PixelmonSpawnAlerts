package eatawesome.pixelmonspawnalerts.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SpawnAlertData implements INBTSerializable<CompoundTag> {

    private static final String SPAWN_ALERT = "spawnAlert";
    
    private String spawnAlert = "disable";
    
    public String getSpawnAlert() {
        return spawnAlert;
    }
    
    public void setSpawnAlert(String name) {
        spawnAlert = name;
    }
    
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString(SPAWN_ALERT, spawnAlert);
        return tag;
    }
    
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        spawnAlert = tag.getString(SPAWN_ALERT);
    }
}