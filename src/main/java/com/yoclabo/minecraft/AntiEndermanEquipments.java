package com.yoclabo.minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = AntiEndermanEquipments.MOD_ID, name = AntiEndermanEquipments.MOD_ID, version = AntiEndermanEquipments.VERSION)
public class AntiEndermanEquipments {

    protected static final String MOD_ID = "AntiEndermanEquipments";
    protected static final String VERSION = "0.1";

    protected static AntiEndermanEquipments thisMod;

    protected static String LABEL_TAGNAME_CREATIVETAB = "AntiEndermanEquipments_CreativeTabs_TAB";
    public static final AntiEndermanEquipments_CreativeTabs t = new AntiEndermanEquipments_CreativeTabs(
            LABEL_TAGNAME_CREATIVETAB);

    protected static Item_PredatorBow m_PBOW;

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        this.SetPredatorBow(e);
    }

    private void SetPredatorBow(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PBOW = new Item_PredatorBow();
        m_PBOW.setUnlocalizedName("PredatorBow");
        m_PBOW.setCreativeTab(t);
        GameRegistry.register(
                m_PBOW,
                new ResourceLocation(
                        MOD_ID,
                        "predatorbow"));
        if (e.getSide().isClient()) {
            ModelLoader.setCustomModelResourceLocation(
                    m_PBOW,
                    0,
                    new ModelResourceLocation(m_PBOW.getRegistryName(), "inventory"));
        }
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent e) {
        this.SetRecipePredatorBow();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void SetRecipePredatorBow() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(m_PBOW),
                "si ",
                "s i",
                "si ",
                's', Items.STRING,
                'i', Items.IRON_INGOT);
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void ChatSubmitted(ServerChatEvent e) {
        EntityPlayerMP player = e.getPlayer();
        if (e.getUsername() != player.getDisplayNameString()) {
            return;
        }
        String patternStr;
        Pattern p;
        Matcher m;
        patternStr = "(Command|Comm|comm|com)( +)((Process|process|p)( +)(.*))";
        p = Pattern.compile(patternStr);
        m = p.matcher(e.getMessage());
        if (m.find()) {
            Command_PredatorBow.processMessage(m.group(3), player);
        }
    }

}
