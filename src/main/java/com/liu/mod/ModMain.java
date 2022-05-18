package com.liu.mod;

import com.liu.mod.network.FCHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Utils.modid)
public class ModMain
{
    public ModMain()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EverythingLoader.ITEMS.register(bus);
        EverythingLoader.BLOCKS.register(bus);
        EverythingLoader.BLOCK_ENTITY.register(bus);
        bus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        FCHandler.init();
    }
}
