package com.y271727uy.moderndelight.block.power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;
import com.y271727uy.moderndelight.util.block_util.power_util.ACGenerateAble;
import com.y271727uy.moderndelight.screen.custom.TeslaCoilScreenHandler;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.registry_util.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeslaCoilBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData propertyDelegate;
    private static final Direction[] DIRECTIONS = {Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
    
    public TeslaCoilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TESLA_COIL_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> TeslaCoilBlockEntity.this.efficiency;
                    case 1 -> TeslaCoilBlockEntity.this.showPractical;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> TeslaCoilBlockEntity.this.efficiency = value;
                    case 1 -> TeslaCoilBlockEntity.this.showPractical = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
    
    @Override
    public Component getDisplayName() {
        return ModBlocks.TESLA_COIL.get().getName();
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new TeslaCoilScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }
    
    private int showPractical = 0;
    private int efficiency = 0;
    private final List<BlockEntity> confinedBlockEntities = new ArrayList<>();
    private BlockEntity powerSupply = null;
    private int ticker = 60;
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide){
            return;
        }
        this.ticker--;
        if (this.ticker <= 0){
            this.ticker = 60;
        }
        if (this.showPractical != 0){
            world.setBlock(pos, state.setValue(TeslaCoilBlock.SHOW_PARTICLE, true), 3);
        } else {
            world.setBlock(pos, state.setValue(TeslaCoilBlock.SHOW_PARTICLE, false), 3);
        }
        
        // Attack
        if (this.ticker % 20 == 0){
            if (this.efficiency > 0){
                AABB box = new AABB(pos).inflate(1.2);
                List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, box);
                for (LivingEntity entity : entities) {
                    if (entity != null){
                        entity.hurt(ModDamageTypes.of(world, ModDamageTypes.ELECTROSHOCK),
                                (float) ((float) this.efficiency / 100.0) * 2);
                        for (int j = 0; j < 4; j++){
                            world.addParticle(net.minecraft.core.particles.ParticleTypes.WAX_OFF,
                                    pos.getX() + 0.3 + world.random.nextFloat()/3,
                                    pos.getY() + 0.5, pos.getZ() + 0.3 + world.random.nextFloat()/3,
                                    0, 0, 0);
                        }
                        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                                ModSounds.BLOCK_TESLA_COIL.get(), net.minecraft.sounds.SoundSource.BLOCKS,
                                1.0f, 0.8f + world.random.nextFloat(), false);
                    }
                }
            }
        }
        
        // 每秒更新一次
        if (this.ticker % 60 == 0){
            // 电源判定
            switch (state.getValue(TeslaCoilBlock.FACING)){
                case UP -> this.powerSupply = this.getPowerSupply(world, pos.above());
                case DOWN -> this.powerSupply = this.getPowerSupply(world, pos.below());
                case EAST -> this.powerSupply = this.getPowerSupply(world, pos.west());
                case SOUTH -> this.powerSupply = this.getPowerSupply(world, pos.north());
                case WEST -> this.powerSupply = this.getPowerSupply(world, pos.east());
                case NORTH -> this.powerSupply = this.getPowerSupply(world, pos.south());
            }
            
            int max = 0;
            long cachedPower = 0;
            confinedBlockEntities.clear();
            
            // 遍历范围内所有的方块实体
            for (Direction direction : DIRECTIONS){
                for (int i = 1; i <= 8; i++){
                    switch (direction){
                        case NORTH -> confinedBlockEntities.add(world.getBlockEntity(pos.north(i)));
                        case SOUTH -> confinedBlockEntities.add(world.getBlockEntity(pos.south(i)));
                        case EAST -> confinedBlockEntities.add(world.getBlockEntity(pos.east(i)));
                        case WEST -> confinedBlockEntities.add(world.getBlockEntity(pos.west(i)));
                        case UP -> confinedBlockEntities.add(world.getBlockEntity(pos.above(i)));
                        case DOWN -> confinedBlockEntities.add(world.getBlockEntity(pos.below(i)));
                    }
                }
            }
            
            if (!isOnPowerSupply()){
                // 找自己并获取最大的电量
                for (BlockEntity entity : confinedBlockEntities){
                    if (entity instanceof TeslaCoilBlockEntity teslaCoilBlock){
                        if (max < teslaCoilBlock.getEfficiency()){
                            max = teslaCoilBlock.getEfficiency();
                        }
                    }
                }
                this.efficiency = (int) (max * getConversionEfficiency());
            }
            
            // 找要电的
            for (BlockEntity entity : confinedBlockEntities){
                if (entity instanceof ACConsumer consumer){
                    if (consumer.isWorking()){
                        cachedPower += consumer.getConsumedValue();
                    }
                }
            }
            
            this.efficiency -= (int) cachedPower;
            
            // 用电器工作
            if (this.efficiency > 0){
                for (BlockEntity entity : confinedBlockEntities){
                    if (entity instanceof ACConsumer consumer){
                        if (consumer.isWorking()){
                            consumer.energize();
                        }
                    }
                }
                world.setBlock(pos, state.setValue(TeslaCoilBlock.IS_OVERLOADED, false), 3);
            } else {
                world.setBlock(pos, state.setValue(TeslaCoilBlock.IS_OVERLOADED, true), 3);
            }
            setChanged();
        }
    }

    public static float getConversionEfficiency() {
        try {
            float value = (float) ModConfig.getTeslaCoilConversionEfficiency();
            if (value > 0 && value <= 1){
                return value;
            } else return 0.8f;
        } catch (Throwable e){
            return 0.8f;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("tesla_coil.showParticle", this.showPractical);
        nbt.putInt("tesla_coil.ticker", this.ticker);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.showPractical = nbt.getInt("tesla_coil.showParticle");
        this.ticker = nbt.getInt("tesla_coil.ticker");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    
    public void setShowParticle(boolean value){
        if (value){
            this.showPractical = 1;
        } else {
            this.showPractical = 0;
        }
        setChanged();
    }
    
    public int getEfficiency() {
        return this.efficiency;
    }
    
    private boolean isOnPowerSupply(){
        return this.powerSupply != null;
    }
    
    private BlockEntity getPowerSupply(Level world, BlockPos pos){
        if (world.getBlockEntity(pos) instanceof ACGenerateAble able && able.getEfficiency() > 0){
            this.efficiency = (int) able.getEfficiency();
            return world.getBlockEntity(pos);
        } else return null;
    }
}
