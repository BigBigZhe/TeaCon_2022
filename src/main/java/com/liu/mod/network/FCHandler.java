package com.liu.mod.network;

import com.liu.mod.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class FCHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Utils.modid, "cc"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init(){
        int id = 1;
        INSTANCE.registerMessage(id++, CCPack.class, CCPack::toBytes, CCPack::new, CCPack::handler);
        INSTANCE.registerMessage(id++, CCReloadPack.class, CCReloadPack::toBytes, CCReloadPack::new, CCReloadPack::handler);
        INSTANCE.registerMessage(id++, CCArrayPack.class, CCArrayPack::toBytes, CCArrayPack::new, CCArrayPack::handler);
        INSTANCE.registerMessage(id++, CCInitPack.class, CCInitPack::toBytes, CCInitPack::new, CCInitPack::handler);
        INSTANCE.registerMessage(id++, CCStagePack.class, CCStagePack::toBytes, CCStagePack::new, CCStagePack::handler);
    }
}
