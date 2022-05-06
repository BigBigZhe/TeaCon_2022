package com.liu.mod.block_entity;

import com.liu.mod.EverythingLoader;
import com.liu.mod.blocks.ChessControlBlock;
import com.liu.mod.blocks.CombustibleBlock;
import com.liu.mod.network.CCArrayPack;
import com.liu.mod.network.CCPack;
import com.liu.mod.network.CCReloadPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ticket.ITicketManager;
import net.minecraftforge.common.ticket.SimpleTicket;

import static com.liu.mod.network.FCHandler.INSTANCE;

public class ChessControlBlockEntity extends BlockEntity{

    private boolean man_to_man = true;
    private boolean init = false;
    private int stage = 0;
    public int dex = 0;
    public int[][] d = {{-9, -1, -3, 5}, {2, 10, -4, 4}, {-3, 5, 2, 10}, {-4, 4, -9, -1}};
    public int[][] map = {{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};

    public ChessControlBlockEntity(BlockPos pos, BlockState state) {
        super(EverythingLoader.CHESS_CONTROL_BLOCK_ENTITY.get(), pos, state);
        setDex(state.getValue(ChessControlBlock.FACING));
    }

    public void changeManToMan() {
        this.man_to_man = !man_to_man;
    }

    public boolean isMan_to_man() {
        return man_to_man;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (level.isClientSide) {
            ChessControlBlockEntity entity = (ChessControlBlockEntity) blockEntity;
            if (entity.init) {
                int[][] map2 = new int[8][8];
                boolean change = false;
                switch (entity.stage) {
                    case 0:
                        for (int dx = entity.d[entity.dex][0]; dx < entity.d[entity.dex][1] && !change; dx++)
                            for (int dz = entity.d[entity.dex][2]; dz < entity.d[entity.dex][3] && !change; dz++)
                                if (level.getBlockState(pos.offset(dx, -2, dz)).getBlock() == EverythingLoader.COMBUSTIBLE_BLOCK_RED.get())
                                    if (entity.map[dx - entity.d[entity.dex][0]][dz - entity.d[entity.dex][2]] == 0) {
                                        entity.map[dx - entity.d[entity.dex][0]][dz - entity.d[entity.dex][2]] = 1;
                                        INSTANCE.sendToServer(new CCReloadPack(pos, dx - entity.d[entity.dex][0], dz - entity.d[entity.dex][2], 1));
                                        change = true;
                                    }
                        if (change) {
                            System.out.println("change1");
                            entity.stage = 1;
                            for (int dx = entity.d[entity.dex][0]; dx < entity.d[entity.dex][1]; dx++)
                                for (int dz = entity.d[entity.dex][2]; dz < entity.d[entity.dex][3]; dz++) {
                                    Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                    if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                        level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_BLUE, true), 35);
                                        INSTANCE.sendToServer(new CCPack(pos.offset(dx, -2, dz), 2));
                                    }
                                }
                        }
                        break;
                    case 1:
                        for (int dx = entity.d[entity.dex][0]; dx < entity.d[entity.dex][1] && !change; dx++)
                            for (int dz = entity.d[entity.dex][2]; dz < entity.d[entity.dex][3] && !change; dz++)
                                if (level.getBlockState(pos.offset(dx, -2, dz)).getBlock() == EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get())
                                    if (entity.map[dx - entity.d[entity.dex][0]][dz - entity.d[entity.dex][2]] == 0) {
                                        entity.map[dx - entity.d[entity.dex][0]][dz - entity.d[entity.dex][2]] = 2;
                                        INSTANCE.sendToServer(new CCReloadPack(pos, dx - entity.d[entity.dex][0], dz - entity.d[entity.dex][2], 2));
                                        change = true;
                                    }
                        if (change) {
                            System.out.println("change2");
                            entity.stage = 2;
                            for (int dx = entity.d[entity.dex][0]; dx < entity.d[entity.dex][1]; dx++)
                                for (int dz = entity.d[entity.dex][2]; dz < entity.d[entity.dex][3]; dz++) {
                                    Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                    if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                        level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState(), 35);
                                        INSTANCE.sendToServer(new CCPack(pos.offset(dx, -2, dz), 3));
                                    }
                                }
                        }
                        break;
                    default:
                        entity.stage = 0;
                        for (int i = 0; i < 8; i++){
                            for (int j = 0; j < 8; j++) {
                                int r = 0, b = 0;
                                if (i > 0){if (entity.map[i - 1][j] == 1) r++;else if (entity.map[i - 1][j] == 2) b++;}
                                if (i < 7){if (entity.map[i + 1][j] == 1) r++;else if (entity.map[i + 1][j] == 2) b++;}
                                if (j > 0){if (entity.map[i][j - 1] == 1) r++;else if (entity.map[i][j - 1] == 2) b++;}
                                if (j < 7){if (entity.map[i][j + 1] == 1) r++;else if (entity.map[i][j + 1] == 2) b++;}
                                map2[i][j] = entity.map[i][j];
                                if (entity.map[i][j] == 0){
                                    if (r > b && r >= 2){
                                        level.setBlock(pos.offset(i + entity.d[entity.dex][0],-2,j + entity.d[entity.dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState(), 35);
                                        INSTANCE.sendToServer(new CCPack(pos.offset(i + entity.d[entity.dex][0],-2,j + entity.d[entity.dex][2]), 4));
                                        map2[i][j] = 1;
                                    }
                                    if (b > r && b >= 2){
                                        level.setBlock(pos.offset(i + entity.d[entity.dex][0],-2,j + entity.d[entity.dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState(), 35);
                                        INSTANCE.sendToServer(new CCPack(pos.offset(i + entity.d[entity.dex][0],-2,j + entity.d[entity.dex][2]), 5));
                                        map2[i][j] = 2;
                                    }
                                }
                                else if (entity.map[i][j] == 1 && b == 3) {
                                    level.setBlock(pos.offset(i + entity.d[entity.dex][0], -2, j + entity.d[entity.dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState(), 35);
                                    INSTANCE.sendToServer(new CCPack(pos.offset(i + entity.d[entity.dex][0], -2, j + entity.d[entity.dex][2]), 5));
                                    map2[i][j] = 2;
                                }
                                else if (entity.map[i][j] == 2 && r == 3) {
                                    level.setBlock(pos.offset(i + entity.d[entity.dex][0], -2, j + entity.d[entity.dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState(), 35);
                                    INSTANCE.sendToServer(new CCPack(pos.offset(i + entity.d[entity.dex][0], -2, j + entity.d[entity.dex][2]), 4));
                                    map2[i][j] = 1;
                                }
                            }
                        }
                        int[] temp = new int[64];
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++){
                                entity.map[i][j] = map2[i][j];
                                temp[i * 8 + j] = map2[i][j];
                            }
                        INSTANCE.sendToServer(new CCArrayPack(pos, temp));
                        for (int dx = entity.d[entity.dex][0]; dx < entity.d[entity.dex][1]; dx++)
                            for (int dz = entity.d[entity.dex][2]; dz < entity.d[entity.dex][3]; dz++) {
                                Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                    level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_RED, true), 35);
                                    INSTANCE.sendToServer(new CCPack(pos.offset(dx, -2, dz), 1));
                                }
                            }
                        break;
                }
            }
        }
    }

    public void setDex(Direction dir){
        switch (dir) {
            case EAST -> dex = 0;
            case WEST -> dex = 1;
            case NORTH -> dex = 2;
            case SOUTH -> dex = 3;
        }
    }

    public void reset(Direction dir, BlockPos pos) {
        init = true;
        stage = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                map[i][j] = 0;
        BlockPos pos2 = pos.offset(0,-2,0);
        for (int dy = 0; dy < 4; dy++)
            for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                for (int dz = d[dex][2]; dz < d[dex][3]; dz++)
                    if (this.getLevel().getBlockEntity(pos2.offset(dx, dy, dz)) != null)
                        return;
        for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
            for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                this.getLevel().setBlock(pos2.offset(dx, 0, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_RED, true).setValue(CombustibleBlock.CAN_BLUE, false), 35);
                INSTANCE.sendToServer(new CCPack(pos2.offset(dx, 0, dz), 1));
            }
        for (int dy = 1; dy < 4; dy++)
            for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                    this.getLevel().setBlock(pos2.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), 35);
                    INSTANCE.sendToServer(new CCPack(pos2.offset(dx, dy, dz), 0));
                }
    }

}
