package com.liu.mod.network;

import com.liu.mod.block_entity.ChessControlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class CCArrayPack {
    private final BlockPos pos;
    private final int[] arr;

    public CCArrayPack(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        arr = buffer.readVarIntArray();
    }

    public CCArrayPack(BlockPos pos, int[] arr) {
        this.pos = pos;
        this.arr = arr;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeVarIntArray(this.arr);
    }

    public static void handler(CCArrayPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BlockEntity entity = sender.level.getBlockEntity(msg.pos);
            if (entity instanceof ChessControlBlockEntity){
                for (int i = 0; i < 64; i++) {
                    ((ChessControlBlockEntity)entity).map[(i >> 3)][i % 8] = msg.arr[i];
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
