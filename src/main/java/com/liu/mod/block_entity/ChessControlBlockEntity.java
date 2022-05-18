package com.liu.mod.block_entity;

import com.liu.mod.EverythingLoader;
import com.liu.mod.blocks.ChessControlBlock;
import com.liu.mod.blocks.CombustibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class ChessControlBlockEntity extends BlockEntity{

    private boolean man_to_man = true;
    private boolean init = false;
    private int stage = 0;
    public int dex = 0;
    private int ticks = 0;
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

    public void setInit(boolean init) {
        this.init = init;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    private int[] mapToArray(){
        int[] a = new int[64];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(map[i], 0, a, i * 8, 8);
        }
        return a;
    }

    private void arrayToMap(int[] a){
        for (int i = 0; i < 8; i++) {
            System.arraycopy(a, i * 8, map[i], 0, 8);
        }
    }

    private void tick(Level level, BlockPos pos){
        ticks++;
        if (ticks >= 8) {
            ticks = 0;
            if (init) {
                int[][] map2 = new int[8][8];
                boolean change = false;
                switch (stage) {
                    case 0 -> {
                        for (int dx = d[dex][0]; dx < d[dex][1] && !change; dx++)
                            for (int dz = d[dex][2]; dz < d[dex][3] && !change; dz++)
                                if (level.getBlockState(pos.offset(dx, -2, dz)).getBlock() == EverythingLoader.COMBUSTIBLE_BLOCK_RED.get())
                                    if (map[dx - d[dex][0]][dz - d[dex][2]] == 0) {
                                        map[dx - d[dex][0]][dz - d[dex][2]] = 1;
                                        change = true;
                                    }
                        if (change) {
                            System.out.println("change1");
                            stage = 1;
                            for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                                for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                                    Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                    if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                        level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_BLUE, true), 35);
                                    }
                                }
                        }
                    }
                    case 1 -> {
                        for (int dx = d[dex][0]; dx < d[dex][1] && !change; dx++)
                            for (int dz = d[dex][2]; dz < d[dex][3] && !change; dz++)
                                if (level.getBlockState(pos.offset(dx, -2, dz)).getBlock() == EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get())
                                    if (map[dx - d[dex][0]][dz - d[dex][2]] == 0) {
                                        map[dx - d[dex][0]][dz - d[dex][2]] = 2;
                                        change = true;
                                    }
                        if (change) {
                            System.out.println("change2");
                            stage = 2;
                            for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                                for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                                    Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                    if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                        level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState(), 35);
                                    }
                                }
                        }
                    }
                    default -> {
                        stage = 0;
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                int r = 0, b = 0;
                                if (i > 0) {
                                    if (map[i - 1][j] == 1) r++;
                                    else if (map[i - 1][j] == 2) b++;
                                }
                                if (i < 7) {
                                    if (map[i + 1][j] == 1) r++;
                                    else if (map[i + 1][j] == 2) b++;
                                }
                                if (j > 0) {
                                    if (map[i][j - 1] == 1) r++;
                                    else if (map[i][j - 1] == 2) b++;
                                }
                                if (j < 7) {
                                    if (map[i][j + 1] == 1) r++;
                                    else if (map[i][j + 1] == 2) b++;
                                }
                                map2[i][j] = map[i][j];
                                if (map[i][j] == 0) {
                                    if (r > b && r >= 2) {
                                        level.setBlock(pos.offset(i + d[dex][0], -2, j + d[dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState(), 35);
                                        map2[i][j] = 1;
                                    }
                                    if (b > r && b >= 2) {
                                        level.setBlock(pos.offset(i + d[dex][0], -2, j + d[dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState(), 35);
                                        map2[i][j] = 2;
                                    }
                                } else if (map[i][j] == 1 && b == 3) {
                                    level.setBlock(pos.offset(i + d[dex][0], -2, j + d[dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_BLUE.get().defaultBlockState(), 35);
                                    map2[i][j] = 2;
                                } else if (map[i][j] == 2 && r == 3) {
                                    level.setBlock(pos.offset(i + d[dex][0], -2, j + d[dex][2]), EverythingLoader.COMBUSTIBLE_BLOCK_RED.get().defaultBlockState(), 35);
                                    map2[i][j] = 1;
                                }
                            }
                        }
                        IntStream.range(0, 8).forEach(i -> System.arraycopy(map2[i], 0, map[i], 0, 8));
                        for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                            for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                                Block block = level.getBlockState(pos.offset(dx, -2, dz)).getBlock();
                                if (block == EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get()) {
                                    level.setBlock(pos.offset(dx, -2, dz), EverythingLoader.COMBUSTIBLE_BLOCK_WHITE.get().defaultBlockState().setValue(CombustibleBlock.CAN_RED, true), 35);
                                }
                            }
                    }
                }
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        ((ChessControlBlockEntity)blockEntity).tick(level, pos);
    }

    public void setDex(Direction dir){
        switch (dir) {
            case EAST -> dex = 0;
            case WEST -> dex = 1;
            case NORTH -> dex = 2;
            case SOUTH -> dex = 3;
        }
    }

    public void reset(BlockPos pos) {
        init = true;
        stage = 0;
        ticks = 0;
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
            }
        for (int dy = 1; dy < 4; dy++)
            for (int dx = d[dex][0]; dx < d[dex][1]; dx++)
                for (int dz = d[dex][2]; dz < d[dex][3]; dz++) {
                    this.getLevel().setBlock(pos2.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), 35);
                }
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("man_to_man")) {this.man_to_man = compound.getBoolean("man_to_man");} else {this.man_to_man = true;}
        if (compound.contains("init")) {this.init = compound.getBoolean("init");} else {this.init = false;}
        if (compound.contains("stage")) {this.stage = compound.getInt("stage");} else {this.stage = 0;}
        if (compound.contains("dex")) {this.dex = compound.getInt("dex");} else {this.dex = 0;}
        if (compound.contains("map")) {arrayToMap(compound.getIntArray("map"));}
    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("man_to_man", this.man_to_man);
        compound.putBoolean("init", this.init);
        compound.putInt("stage", this.stage);
        compound.putInt("dex", this.dex);
        compound.putIntArray("map", mapToArray());
    }

}
