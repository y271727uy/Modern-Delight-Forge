package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.block_util.power_util.ACGenerateAble;
import com.y271727uy.moderndelight.screen.custom.FaradayGeneratorScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class FaradayGeneratorBlockEntity extends BlockEntity implements MenuProvider, ACGenerateAble {
    public FaradayGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FARADAY_GENERATOR_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return FaradayGeneratorBlockEntity.this.isWorking;
            }

            @Override
            public void set(int index, int value) {
                FaradayGeneratorBlockEntity.this.isWorking = value;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }
    
    protected final ContainerData propertyDelegate;
    private int isWorking = 0;
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide){
            return;
        }
        if (world.getGameTime() % 20L == 0L){
            BlockPos blockPos = pos;
            Direction thisDir = state.getValue(FaradayGeneratorBlock.FACING);
            switch (thisDir){
                case WEST -> blockPos = pos.south(3).above().east();
                case SOUTH -> blockPos = pos.east(3).above().north();
                case EAST -> blockPos = pos.north(3).above().west();
                case NORTH -> blockPos = pos.west(3).above().south();
            }
            if (world.getBlockEntity(blockPos) instanceof SterlingEngineBlockEntity engineBlockEntity){
                Direction engineDir = world.getBlockState(blockPos).getValue(SterlingEngineBlock.FACING);
                switch (thisDir){
                    case WEST -> {if (engineDir != Direction.NORTH) {
                        this.isWorking = 0;
                        return;
                    }}
                    case SOUTH -> {if (engineDir != Direction.WEST) {
                        this.isWorking = 0;
                        return;
                    }}
                    case EAST -> {if (engineDir != Direction.SOUTH) {
                        this.isWorking = 0;
                        return;
                    }}
                    case NORTH -> {if (engineDir != Direction.EAST) {
                        this.isWorking = 0;
                        return;
                    }}
                }
                if (engineBlockEntity.getBlockState().getValue(SterlingEngineBlock.IS_WORKING)){
                    this.isWorking = 1;
                } else {
                    this.isWorking = 0;
                }
            } else {
                this.isWorking = 0;
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.FARADAY_GENERATOR.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FaradayGeneratorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    
    public static long getEnergyEfficiency(){
        try {
            int val = ModConfig.getEnergyGeneratedByFaradayGenerator();
            if (val >= 1){
                return val;
            } else return 200;
        } catch (Throwable e){
            return 200;
        }
    }
    
    @Override
    public long getEfficiency() {
        if (isWorking != 0){
            return getEnergyEfficiency();
        } else return 0;
    }
}
