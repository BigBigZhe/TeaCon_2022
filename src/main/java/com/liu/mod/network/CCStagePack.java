package com.liu.mod.network;

import com.liu.mod.block_entity.ChessControlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CCStagePack {
    private final BlockPos pos;
    private final int stage;

    public CCStagePack(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        stage = buffer.readInt();
    }

    public CCStagePack(BlockPos pos, int stage) {
        this.pos = pos;
        this.stage = stage;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.stage);
    }

    public static void handler(CCStagePack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BlockEntity entity = sender.level.getBlockEntity(msg.pos);
            if (entity instanceof ChessControlBlockEntity){
                ((ChessControlBlockEntity)entity).setStage(msg.stage);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
