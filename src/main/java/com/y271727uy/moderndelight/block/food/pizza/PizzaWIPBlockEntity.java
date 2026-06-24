package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PizzaWIPBlockEntity extends AbstractPizzaBlockEntity {
    public static final String NEED_INGREDIENT = "moderndelight.pizza_message.need_ingredient";
    public static final String NEED_CHEESE = "moderndelight.pizza_message.need_cheese";

    public PizzaWIPBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIZZA_WIP_ENTITY.get(), pos, state);
    }

    public void use(Player player, BlockState state, Level level, InteractionHand hand) {
        if (level.isClientSide) {
            return;
        }

        ItemStack heldStack = player.getItemInHand(hand);
        Item item = heldStack.getItem();
        int currentState = state.getValue(PizzaWIPBlock.CRAFT_STATE);

        if (currentState < 5) {
            if (isPizzaIngredients(item)) {
                setItem(currentState, heldStack.split(1));
                level.setBlock(worldPosition, state.setValue(PizzaWIPBlock.CRAFT_STATE, currentState + 1), 3);
                playSound(SoundEvents.ITEM_PICKUP, 1.0f, level.random.nextFloat() + 0.1f);
            } else {
                player.displayClientMessage(Component.translatable(NEED_INGREDIENT), true);
            }
            setChanged();
            return;
        }

        if (item == ModItems.CHEESE.get()) {
            heldStack.shrink(1);
            ItemStack rawPizza = new ItemStack(ModBlocks.RAW_PIZZA_ITEM.get());
            CompoundTag nbt = saveWithoutMetadata();
            BlockItem.setBlockEntityData(rawPizza, ModBlockEntities.RAW_PIZZA_BLOCK_ENTITY.get(), nbt);
            playSound(SoundEvents.HONEY_BLOCK_PLACE, 1.0f, level.random.nextFloat() + 0.1f);
            level.setBlock(worldPosition, ModBlocks.RAW_PIZZA.get().defaultBlockState(), 3);
            if (level.getBlockEntity(worldPosition) instanceof RawPizzaBlockEntity rawPizzaBlockEntity) {
                rawPizzaBlockEntity.load(nbt);
                rawPizzaBlockEntity.setChanged();
            } else {
                Block.popResource(level, worldPosition, rawPizza);
            }
        } else {
            player.displayClientMessage(Component.translatable(NEED_CHEESE), true);
        }

        setChanged();
    }

    private boolean isPizzaIngredients(Item item) {
        return new ItemStack(item).is(TagKeys.PIZZA_INGREDIENTS);
    }
}

