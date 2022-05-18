package com.liu.mod.network;

import com.liu.mod.EverythingLoader;
import com.liu.mod.block_entity.ChessControlBlockEntity;
import com.liu.mod.blocks.CombustibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CCInitPack {
    private final BlockPos pos;
    private final boolean init;

    public CCInitPack(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        init = buffer.readBoolean();
    }

    public CCInitPack(BlockPos pos, boolean init) {
        this.pos = pos;
        this.init = init;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(this.init);
    }

    public static void handler(CCInitPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BlockEntity entity = sender.level.getBlockEntity(msg.pos);
            if (entity instanceof ChessControlBlockEntity){
                ((ChessControlBlockEntity)entity).setInit(msg.init);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
