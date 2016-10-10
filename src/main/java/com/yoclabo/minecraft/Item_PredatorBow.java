package com.yoclabo.minecraft;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Item_PredatorBow extends Item {

    public Item_PredatorBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(AntiEndermanEquipments.t);
        this.addPropertyOverride(
                new ResourceLocation(
                        "predatorbow",
                        "pull"),
                new IItemPropertyGetter() {
                    @SideOnly(Side.CLIENT)
                    public float apply(
                            ItemStack stack,
                            @Nullable World worldIn,
                            @Nullable EntityLivingBase entityIn) {
                        if (entityIn == null) {
                            return 0.0F;
                        } else {
                            ItemStack itemstack = entityIn.getActiveItemStack();
                            if (itemstack != null && itemstack.getItem() == Items.BOW) {
                                return (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
                            } else {
                                return 0.0F;
                            }
                        }
                    }
                });
        this.addPropertyOverride(
                new ResourceLocation(
                        "predatorbow",
                        "pulling"),
                new IItemPropertyGetter() {
                    @SideOnly(Side.CLIENT)
                    public float apply(
                            ItemStack stack,
                            @Nullable World worldIn,
                            @Nullable EntityLivingBase entityIn) {
                        if (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) {
                            return 1.0F;
                        } else {
                            return 0.0F;
                        }
                    }
                });

        arrows = new ArrayList<ReleasedArrow>();
        lastArrowID = 0;
        ticksForMe = 0;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse
     * button).
     */
    public void onPlayerStoppedUsing(ItemStack bowItem, World world, EntityLivingBase shooter, int timeLeft) {

        if (shooter instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) shooter;

            boolean isInfinity = this.IsArrowInfinity(player, bowItem);

            ItemStack arrowItem = this.findAmmo(player);

            int drawWeight = this.GetDrawWeight(
                    world,
                    player,
                    bowItem,
                    timeLeft,
                    arrowItem,
                    isInfinity);
            if (drawWeight < 0) {
                return;
            }

            if (arrowItem != null || isInfinity) {
                if (arrowItem == null) {
                    arrowItem = new ItemStack(Items.ARROW);
                }

                float arrowVelocity = getArrowVelocity(drawWeight);

                if ((double) arrowVelocity >= 0.1D) {
                    boolean canRecycle = this.EvaluateArrowRecycle(
                            player,
                            bowItem,
                            arrowItem);

                    if (!world.isRemote) {

                        EntityArrow arrowEntity = this.PrepareArrow(
                                world,
                                player,
                                bowItem,
                                arrowItem,
                                arrowVelocity,
                                canRecycle);

                        bowItem.damageItem(1, player);

                        if (canRecycle) {
                            arrowEntity.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        this.CastArrow(world, arrowEntity);
                    }

                    world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
                            1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

                    if (!canRecycle) {
                        --arrowItem.stackSize;

                        if (arrowItem.stackSize == 0) {
                            player.inventory.deleteStack(arrowItem);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    private boolean IsArrowInfinity(EntityPlayer player, ItemStack bowItem) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowItem) > 0) {
            return true;
        }
        if (player.capabilities.isCreativeMode) {
            return true;
        }
        return false;
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }
            return null;
        }
    }

    protected boolean isArrow(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemArrow;
    }

    private int GetDrawWeight(World world, EntityPlayer player, ItemStack bowItem, int timeLeft, ItemStack arrowItem,
            boolean isInfinity) {
        int drawWeight = this.getMaxItemUseDuration(bowItem) - timeLeft;
        if (drawWeight < 0) {
            return 0;
        }
        drawWeight = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(
                bowItem,
                world,
                player,
                drawWeight,
                arrowItem != null || isInfinity);
        return drawWeight;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * Gets the velocity of the arrow entity from the bow's charge
     */
    public static float getArrowVelocity(int charge) {
        float f = (float) charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private boolean EvaluateArrowRecycle(EntityPlayer player, ItemStack bowItem, ItemStack arrowItem) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }
        if (arrowItem.getItem() instanceof ItemArrow) {
            return ((ItemArrow) arrowItem.getItem()).isInfinite(arrowItem, bowItem, player);
        } else {
            return false;
        }
    }

    private EntityArrow PrepareArrow(
            World world,
            EntityPlayer player,
            ItemStack bowItem,
            ItemStack arrowItem,
            float arrowVelocity,
            boolean canRecycle) {
        ItemArrow arrowCreator = null;
        if (arrowItem.getItem() instanceof ItemArrow) {
            arrowCreator = (ItemArrow) arrowItem.getItem();
        } else {
            arrowCreator = (ItemArrow) Items.ARROW;
        }
        EntityArrow arrowEntity = arrowCreator.createArrow(world, arrowItem, player);
        arrowEntity.setAim(
                player,
                player.rotationPitch,
                player.rotationYaw,
                0.0F,
                arrowVelocity * 7.0F,
                1.0F);
        if (arrowVelocity >= 1.0F) {
            arrowEntity.setIsCritical(true);
        }
        int powerBoost = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bowItem);
        if (powerBoost > 0) {
            arrowEntity.setDamage(arrowEntity.getDamage() + (double) powerBoost * 0.5D + 0.5D);
        }
        int knockBackBoost = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowItem);
        if (knockBackBoost > 0) {
            arrowEntity.setKnockbackStrength(knockBackBoost);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowItem) > 0) {
            arrowEntity.setFire(100);
        }
        return arrowEntity;
    }

    private void CastArrow(World world, EntityArrow arrowEntity) {
        world.spawnEntityInWorld(arrowEntity);

        ReleasedArrow addArrow = new ReleasedArrow();
        addArrow.SetArrow(arrowEntity);
        addArrow.SetID(lastArrowID);
        addArrow.SetTicksReleased(ticksForMe);

        lastArrowID += 1;
    }

    /**
     * returns the action that specifies what animation to play when the items
     * is being used
     */
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
            EnumHand hand) {
        boolean flag = this.findAmmo(playerIn) != null;

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn,
                playerIn, hand, flag);
        if (ret != null)
            return ret;

        if (!playerIn.capabilities.isCreativeMode && !flag) {
            return !flag ? new ActionResult(EnumActionResult.FAIL, itemStackIn)
                    : new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else {
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
    }

    /**
     * Return the enchantability factor of the item, most of the time is based
     * on material.
     */
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public void onUpdate(ItemStack bowItem, World world, Entity entityIn, int itemSlot, boolean isSelected) {
        ticksForMe++;
        super.onUpdate(bowItem, world, entityIn, itemSlot, isSelected);
        for (ReleasedArrow item : arrows) {
            if (item.ArrowActive()) {
                if (item.GetTicksReleased() + 100 < ticksForMe) {
                    this.ArrowExplode(world, item);
                }
            }
        }
    }

    private void ArrowExplode(World world, ReleasedArrow item) {
        world.createExplosion(
                item.GetArrow(),
                item.GetArrow().posX,
                item.GetArrow().posY,
                item.GetArrow().posZ,
                3,
                true);
        item.GetArrow().setDead();
    }

    private List<ReleasedArrow> arrows;

    private int lastArrowID;

    private long ticksForMe;

    private class ReleasedArrow {

        private EntityArrow arrow;

        public void SetArrow(EntityArrow arg) {
            arrow = arg;
        }

        public EntityArrow GetArrow() {
            return arrow;
        }

        private int id;

        public void SetID(int arg) {
            id = arg;
        }

        public int GetID() {
            return id;
        }

        private long ticksReleased;

        public void SetTicksReleased(long arg) {
            ticksReleased = arg;
        }

        public long GetTicksReleased() {
            return ticksReleased;
        }

        public boolean ArrowActive() {
            if (arrow != null && !arrow.isDead) {
                return true;
            } else {
                return false;
            }
        }

    }

}
