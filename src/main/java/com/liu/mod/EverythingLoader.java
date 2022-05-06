package com.liu.mod;

import com.liu.mod.block_entity.*;
import com.liu.mod.blocks.*;
import com.liu.mod.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.*;

public class EverythingLoader {

    //Tab
    public static final CreativeModeTab MY_TAB = new CreativeModeTab("MyTab") {public ItemStack makeIcon() {return new ItemStack(COMBUSTIBLE_WHITE_ITEM.get());}};

    //Items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.modid);
    public static final RegistryObject<Item> FIRE_ROD_RED = ITEMS.register("fire_rod_red", FireRodItem::new);
    public static final RegistryObject<Item> FIRE_ROD_BLUE = ITEMS.register("fire_rod_blue", FireRodItem::new);
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", WrenchItem::new);

    //Blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Utils.modid);
    public static final RegistryObject<Block> COMBUSTIBLE_BLOCK_WHITE = BLOCKS.register("combustible_block_white", CombustibleBlock::new);
    public static final RegistryObject<Block> COMBUSTIBLE_BLOCK_RED = BLOCKS.register("combustible_block_red", CombustibleRedBlock::new);
    public static final RegistryObject<Block> COMBUSTIBLE_BLOCK_BLUE = BLOCKS.register("combustible_block_blue", CombustibleBlueBlock::new);
    public static final RegistryObject<Block> CHESS_CONTROL_BLOCK = BLOCKS.register("chess_control_block", ChessControlBlock::new);

    //Block_Item
    public static final RegistryObject<Item> COMBUSTIBLE_WHITE_ITEM = ITEMS.register("combustible_item_white", ()->new BlockItem(COMBUSTIBLE_BLOCK_WHITE.get(), new Item.Properties().tab(MY_TAB)));
    public static final RegistryObject<Item> COMBUSTIBLE_RED_ITEM = ITEMS.register("combustible_item_red", ()->new BlockItem(COMBUSTIBLE_BLOCK_RED.get(), new Item.Properties().tab(MY_TAB)));
    public static final RegistryObject<Item> COMBUSTIBLE_BLUE_ITEM = ITEMS.register("combustible_item_blue", ()->new BlockItem(COMBUSTIBLE_BLOCK_BLUE.get(), new Item.Properties().tab(MY_TAB)));
    public static final RegistryObject<Item> CHESS_CONTROL_ITEM = ITEMS.register("chess_control_item", ()->new BlockItem(CHESS_CONTROL_BLOCK.get(), new Item.Properties().tab(MY_TAB)));

    //Block_Entity_Type
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Utils.modid);
    public static final RegistryObject<BlockEntityType<ChessControlBlockEntity>> CHESS_CONTROL_BLOCK_ENTITY = BLOCK_ENTITY.register("chess_control_block_entity", ()->BlockEntityType.Builder.of(ChessControlBlockEntity::new, CHESS_CONTROL_BLOCK.get()).build(null));

}
