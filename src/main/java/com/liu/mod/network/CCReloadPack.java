package com.liu.mod.network;

import com.liu.mod.block_entity.ChessControlBlockEntity;
import com.liu.mod.blocks.CombustibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CCReloadPack {
    private final BlockPos pos;
    private final int dx;
    private final int dz;
    private final int num;

    public CCReloadPack(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        dx = buffer.readInt();
        dz = buffer.readInt();
        num = buffer.readInt();
    }

    public CCReloadPack(BlockPos pos, int dx, int dz, int num) {
        this.pos = pos;
        this.dx = dx;
        this.dz = dz;
        this.num = num;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.dx);
        buf.writeInt(this.dz);
        buf.writeInt(this.num);
    }

    public static void handler(CCReloadPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BlockEntity entity = sender.level.getBlockEntity(msg.pos);
            if (entity instanceof ChessControlBlockEntity) {
                ((ChessControlBlockEntity)entity).map[msg.dx][msg.dz] = msg.num;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
