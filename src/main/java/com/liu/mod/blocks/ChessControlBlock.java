package com.liu.mod.blocks;

import com.liu.mod.EverythingLoader;
import com.liu.mod.block_entity.ChessControlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChessControlBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<ControlPart> PART = EnumProperty.create("part", ControlPart.class);

    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D),
            Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D)
    };

    public ChessControlBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE));
        this.registerDefaultState(this.defaultBlockState().setValue(PART, ControlPart.LEFT).setValue(FACING, Direction.NORTH));
    }

    public InteractionResult use(BlockState p_51357_, Level p_51358_, BlockPos p_51359_, Player p_51360_, InteractionHand p_51361_, BlockHitResult p_51362_) {
        if (p_51358_.isClientSide) {
            Item item = p_51360_.getMainHandItem().getItem();
            BlockEntity entity = p_51358_.getBlockEntity(p_51359_);
            if (entity instanceof ChessControlBlockEntity) {
                if (item == EverythingLoader.WRENCH.get()) {
                    ((ChessControlBlockEntity) entity).reset(p_51357_.getValue(FACING), p_51357_.getValue(PART) == ControlPart.RIGHT ? p_51359_.relative(getDir(p_51357_.getValue(FACING)).getOpposite()) : p_51359_);
                }
                if (item instanceof AirItem) {
                    ((ChessControlBlockEntity) entity).changeManToMan();
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void playerWillDestroy(Level p_49505_, BlockPos p_49506_, BlockState p_49507_, Player p_49508_) {
        if (!p_49505_.isClientSide && p_49508_.isCreative()) {
            ControlPart controlpart = p_49507_.getValue(PART);
            BlockPos blockpos = p_49506_.relative(controlpart == ControlPart.LEFT ? getDir(p_49507_.getValue(FACING)) : getDir(p_49507_.getValue(FACING)).getOpposite());
            BlockState blockstate = p_49505_.getBlockState(blockpos);
            if (blockstate.is(this)) {
                p_49505_.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                p_49505_.levelEvent(p_49508_, 2001, blockpos, Block.getId(blockstate));
            }
        }
        super.playerWillDestroy(p_49505_, p_49506_, p_49507_, p_49508_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_49479_) {
        Direction direction = p_49479_.getHorizontalDirection();
        BlockPos blockpos = p_49479_.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = p_49479_.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(p_49479_) && level.getWorldBorder().isWithinBounds(blockpos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == EverythingLoader.CHESS_CONTROL_BLOCK_ENTITY.get() && !level.isClientSide ? ChessControlBlockEntity::serverTick : null;
    }

    private Direction getDir(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case EAST -> Direction.SOUTH;
            case WEST -> Direction.NORTH;
            default -> direction;
        };
    }

    public void setPlacedBy(Level p_49499_, BlockPos p_49500_, BlockState p_49501_, @javax.annotation.Nullable LivingEntity p_49502_, ItemStack p_49503_) {
        super.setPlacedBy(p_49499_, p_49500_, p_49501_, p_49502_, p_49503_);
        if (!p_49499_.isClientSide) {
            BlockPos blockpos = p_49500_.relative(getDir(p_49501_.getValue(FACING)));
            p_49499_.setBlock(blockpos, p_49501_.setValue(PART, ControlPart.RIGHT).setValue(FACING, p_49501_.getValue(FACING)), 3);
            p_49499_.blockUpdated(p_49500_, Blocks.AIR);
            p_49501_.updateNeighbourShapes(p_49499_, p_49500_, 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ChessControlBlockEntity(p_153215_, p_153216_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49532_) {
        p_49532_.add(FACING, PART);
    }

    public VoxelShape getShape(BlockState p_51222_, BlockGetter p_51223_, BlockPos p_51224_, CollisionContext p_51225_) {
        return SHAPE[p_51222_.getValue(FACING) == Direction.WEST || p_51222_.getValue(FACING) == Direction.EAST ? 1 : 0];
    }

    public RenderShape getRenderShape(BlockState p_49545_) {
        return RenderShape.MODEL;
    }
}
