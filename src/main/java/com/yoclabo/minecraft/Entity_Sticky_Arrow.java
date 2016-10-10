package com.yoclabo.minecraft;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class Entity_Sticky_Arrow extends EntityTippedArrow {

    public enum ArrowType {
        NORMAL, SPLIT, HIIMPACT, AIRBURST, NAPALM, LOCKIN,
    }

    private ArrowType type;

    public void SetArrowType(ArrowType arg) {
        type = arg;
    }

    public ArrowType GetArrowType() {
        return type;
    }

    private int knockBackWeight;

    public void SetKnockBackWeight(int arg) {
        knockBackWeight = arg;
    }

    private int explosiveRange;

    public void SetExplosiveRange(int arg) {
        explosiveRange = arg;
    }

    private int ignitionRange;

    public void SetIgnitionRange(int arg) {
        ignitionRange = arg;
    }

    private Entity entityForLockin;

    private int lockRange;

    public void SetLockRange(int arg) {
        lockRange = arg;
    }

    private int wallThickness;

    public void SetWallThickness(int arg) {
        wallThickness = arg;
    }

    public Entity_Sticky_Arrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public Entity_Sticky_Arrow(World world, ItemStack arrow, EntityLivingBase shooter) {
        super(world, shooter);
        super.setPotionEffect(arrow);
        knockBackWeight = 4;
        explosiveRange = 4;
        ignitionRange = 3;
        entityForLockin = null;
        lockRange = 3;
        wallThickness = 1;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity hit = raytraceResultIn.entityHit;
        switch (type) {
        case HIIMPACT:
            this.BlowAway(hit);
            break;
        case AIRBURST:
            this.Explode();
            this.setDead();
            break;
        case NAPALM:
            this.NapalmImpact();
            break;
        case LOCKIN:
            entityForLockin = hit;
            this.LockIn();
            this.setDead();
            break;
        default:
            break;
        }
        super.onHit(raytraceResultIn);
    }

    private void BlowAway(Entity hit) {
        double eMX = hit.motionX;
        double eMY = hit.motionY;
        double eMZ = hit.motionZ;
        double aMX = this.motionX * knockBackWeight;
        double aMY = this.motionY * knockBackWeight;
        double aMZ = this.motionZ * knockBackWeight;
        hit.setVelocity(eMX + (aMX / 3), eMY + (aMY / 3), eMZ + (aMZ / 3));
    }

    private void Explode() {
        this.worldObj.createExplosion(
                this,
                this.posX,
                this.posY,
                this.posZ,
                explosiveRange,
                true);
    }

    private void LockIn() {
        CreateWall(WallDirection.ABOVE);
        CreateWall(WallDirection.FRONT);
        CreateWall(WallDirection.REAR);
        CreateWall(WallDirection.LEFT);
        CreateWall(WallDirection.RIGHT);
        CreateWall(WallDirection.BELOW);
    }

    private enum WallDirection {
        ABOVE, FRONT, REAR, LEFT, RIGHT, BELOW,
    }

    private void CreateWall(WallDirection d) {
        int acLockRange;
        int ePosX = (int) Math.round(entityForLockin.posX);
        int ePosY = (int) Math.round(entityForLockin.posY);
        int ePosZ = (int) Math.round(entityForLockin.posZ);
        for (acLockRange = lockRange + wallThickness; acLockRange >= lockRange; acLockRange--) {
            for (int i = 0 - acLockRange; i <= acLockRange; i++) {
                for (int j = 0 - acLockRange; j <= acLockRange; j++) {
                    switch (d) {
                    case ABOVE:
                        BlockPos posUpside = new BlockPos(ePosX + i, ePosY + acLockRange, ePosZ + j);
                        if (this.worldObj.isAirBlock(posUpside)) {
                            this.worldObj.setBlockState(posUpside, Blocks.GLASS.getDefaultState());
                        }
                        break;
                    case FRONT:
                        BlockPos posFront = new BlockPos(ePosX + acLockRange, ePosY + i, ePosZ + j);
                        if (this.worldObj.isAirBlock(posFront)) {
                            this.worldObj.setBlockState(posFront, Blocks.GLASS.getDefaultState());
                        }
                        break;
                    case REAR:
                        BlockPos posBack = new BlockPos(ePosX - acLockRange, ePosY + i, ePosZ + j);
                        if (this.worldObj.isAirBlock(posBack)) {
                            this.worldObj.setBlockState(posBack, Blocks.GLASS.getDefaultState());
                        }
                        break;
                    case LEFT:
                        BlockPos posLeft = new BlockPos(ePosX + i, ePosY + j, ePosZ + acLockRange);
                        if (this.worldObj.isAirBlock(posLeft)) {
                            this.worldObj.setBlockState(posLeft, Blocks.GLASS.getDefaultState());
                        }
                        break;
                    case RIGHT:
                        BlockPos posRight = new BlockPos(ePosX + i, ePosY + j, ePosZ - acLockRange);
                        if (this.worldObj.isAirBlock(posRight)) {
                            this.worldObj.setBlockState(posRight, Blocks.GLASS.getDefaultState());
                        }
                        break;
                    case BELOW:
                        BlockPos posBelow = new BlockPos(ePosX + i, ePosY - acLockRange, ePosZ + j);
                        if (this.worldObj.isAirBlock(posBelow)) {
                            this.worldObj.setBlockState(posBelow, Blocks.GLASS.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private void NapalmImpact() {
        int aPosX = (int) Math.round(this.posX);
        int aPosY = (int) Math.round(this.posY);
        int aPosZ = (int) Math.round(this.posZ);
        for (int x = aPosX - ignitionRange; x < aPosX + ignitionRange; x++) {
            for (int y = aPosY - ignitionRange; y < aPosY + ignitionRange; y++) {
                for (int z = aPosZ - ignitionRange; z < aPosZ + ignitionRange; z++) {
                    this.TryToIgnition(x, y, z);
                }
            }
        }
    }

    private void TryToIgnition(int posX, int posY, int posZ) {
        BlockPos pos = new BlockPos(posX, posY, posZ);
        if (this.worldObj.isAirBlock(pos)) {
            this.worldObj.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
        }
    }

}
