package com.yoclabo.minecraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AntiEndermanEquipments_CreativeTabs extends CreativeTabs {
    public AntiEndermanEquipments_CreativeTabs(String label) {
        super(label);
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public Item getTabIconItem() {
        return (Item) com.yoclabo.minecraft.AntiEndermanEquipments.m_PBOW;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public String getTranslatedTabLabel() {
        return com.yoclabo.minecraft.AntiEndermanEquipments.MOD_ID;
    }
}
