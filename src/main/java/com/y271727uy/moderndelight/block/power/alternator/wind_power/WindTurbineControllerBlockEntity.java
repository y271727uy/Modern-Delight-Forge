package com.y271727uy.moderndelight.block.power.alternator.wind_power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.block_util.power_util.ACGenerateAble;
import com.y271727uy.moderndelight.screen.custom.WindTurbineControllerScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

public class WindTurbineControllerBlockEntity extends BlockEntity implements MenuProvider, ACGenerateAble {
    public WindTurbineControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WIND_TURBINE_CONTROLLER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> WindTurbineControllerBlockEntity.this.isWorking;
                    case 1 -> WindTurbineControllerBlockEntity.this.getBlockPos().getY();
                    case 2 -> WindTurbineControllerBlockEntity.this.efficiency;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> WindTurbineControllerBlockEntity.this.isWorking = value;
                    case 2 -> WindTurbineControllerBlockEntity.this.efficiency = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
    
    protected final ContainerData propertyDelegate;
    private int isWorking = 0;
    private int efficiency = 0;
    private BlockPos facingBlock = getBlockPos();
    
    @Override
    public long getEfficiency() {
        return efficiency;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide){
            return;
        }
        switch (state.getValue(WindTurbineControllerBlock.FACING)){
            case EAST -> facingBlock = pos.east();
            case SOUTH -> facingBlock = pos.south();
            case WEST -> facingBlock = pos.west();
            case NORTH -> facingBlock = pos.north();
        }
        if (world.getBlockEntity(facingBlock) instanceof FanBladeBlockEntity){
            if (world.getBlockState(facingBlock).getValue(FanBladeBlock.FACING) == state.getValue(WindTurbineControllerBlock.FACING)){
                this.isWorking = 1;
                if (world.isThundering()){
                    this.efficiency = (int) Math.max(getMultiplier()*pos.getY() / 3, 3);
                } else if (world.isRaining()){
                    this.efficiency = (int) Math.max(getMultiplier()*pos.getY() / 4, 2);
                } else {
                    this.efficiency = (int) Math.max(getMultiplier()*pos.getY() / 5, 1);
                }
                return;
            }
        }
        this.isWorking = 0;
        this.efficiency = 0;
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("wind_turbine_controller.isWorking", this.isWorking);
        nbt.putInt("wind_turbine_controller.efficiency", this.efficiency);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.isWorking = nbt.getInt("wind_turbine_controller.isWorking");
        this.efficiency = nbt.getInt("wind_turbine_controller.efficiency");
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.WIND_TURBINE_CONTROLLER.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new WindTurbineControllerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    
    public static float getMultiplier(){
        try {
            float value = (float) ModConfig.getWindTurbineMultiplier();
            if (value > 0){
                return value;
            } else return 3.0f;
        } catch (Throwable e){
            return 3.0f;
        }
    }
}
