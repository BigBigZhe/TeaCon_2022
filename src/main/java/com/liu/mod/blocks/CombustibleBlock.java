package com.liu.mod.blocks;

import com.liu.mod.EverythingLoader;
import com.liu.mod.network.CCArrayPack;
import com.liu.mod.network.CCPack;
import com.liu.mod.network.FCHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class CombustibleBlock extends Block {

    public static final BooleanProperty CAN_RED = BooleanProperty.create("can_red");
    public static final BooleanProperty CAN_BLUE = BooleanProperty.create("can_blue");

    public CombustibleBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE));
        this.registerDefaultState(this.defaultBlockState().setValue(CAN_RED, false).setValue(CAN_BLUE, false));
    }

    public InteractionResult use(BlockState p_51357_, Level p_51358_, BlockPos p_51359_, Player p_51360_, InteractionHand p_51361_, BlockHitResult p_51362_) {
        if (p_51358_.isClientSide) {
            Item item = p_51360_.getMainHandItem().getItem();
            if (item == EverythingLoader.FIRE_ROD_RED.get()){
                BlockState state = p_51358_.getBlockState(p_51359_);
                if (state.getValue(CAN_RED)){
                    p_51358_.setBlock(p_51359_, EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState(), 35);
                    FCHandler.INSTANCE.sendToServer(new CCPack(p_51359_, 4));
                }
            }
            else if (item == EverythingLoader.FIRE_ROD_BLUE.get()){
                BlockState state = p_51358_.getBlockState(p_51359_);
                if (state.getValue(CAN_BLUE)){
                    p_51358_.setBlock(p_51359_, EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState(), 35);
                    FCHandler.INSTANCE.sendToServer(new CCPack(p_51359_, 5));
                }
            }
            else if (item == Items.POTATO){
                System.out.println("woooooooooooooooooooooooooooooooooooo");
            }
        }
        return InteractionResult.CONSUME;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49532_) {
        p_49532_.add(CAN_RED, CAN_BLUE);
    }

}
