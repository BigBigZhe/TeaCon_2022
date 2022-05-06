package com.liu.mod.network;

import com.liu.mod.EverythingLoader;
import com.liu.mod.blocks.CombustibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class CCPack {
    private final BlockPos pos;
    private final int type;

    public CCPack(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        type = buffer.readInt();
    }

    public CCPack(BlockPos pos, int type) {
        this.pos = pos;
        this.type = type;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.type);
    }

    public static void handler(CCPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BlockState state = Blocks.AIR.defaultBlockState();
            switch (msg.type){
                case 1 -> state = EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_RED, true);
                case 2 -> state = EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_BLUE, true);
                case 3 -> state = EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState();
                case 4 -> state = EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState();
                case 5 -> state = EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState();
            }
            sender.level.setBlock(msg.pos, state, 35);
        });
        ctx.get().setPacketHandled(true);
    }
}
