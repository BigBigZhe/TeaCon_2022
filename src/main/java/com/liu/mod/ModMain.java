package com.liu.mod;

import com.liu.mod.network.FCHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Utils.modid)
public class ModMain
{
    public ModMain()
    {
        EverythingLoader.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EverythingLoader.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EverythingLoader.BLOCK_ENTITY.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        FCHandler.init();
    }
}
