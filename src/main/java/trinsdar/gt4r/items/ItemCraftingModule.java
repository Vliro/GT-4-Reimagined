package trinsdar.gt4r.items;

import muramasa.antimatter.capability.IGuiHandler;
import muramasa.antimatter.cover.CoverStack;
import muramasa.antimatter.item.ItemCover;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.system.CallbackI;
import trinsdar.gt4r.Ref;
import trinsdar.gt4r.data.GT4RData;
import trinsdar.gt4r.data.Guis;
import trinsdar.gt4r.gui.ContainerCraftingCover;
import trinsdar.gt4r.gui.ContainerCraftingItem;

import javax.annotation.Nullable;

public class ItemCraftingModule extends ItemCover implements INamedContainerProvider {
    public ItemCraftingModule() {
        super(Ref.ID, GT4RData.COVER_CRAFTING.getId());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (openGui(playerIn)) return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public boolean openGui(PlayerEntity player) {
        if (player.world.isRemote) return false;
        NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> {
            packetBuffer.writeBlockPos(player.getPosition());
        });
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getTranslationKey());
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        //TODO
        return Guis.ITEM_CRAFTING_HANDLER.getMenu((IGuiHandler) player, playerInv, windowId);
    }
}
