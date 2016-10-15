package com.yoclabo.minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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

    private static String LABEL_TAGNAME_CREATIVETAB = "AntiEndermanEquipments_CreativeTabs_TAB";

    private AntiEndermanEquipments_CreativeTabs creativetab;

    public AntiEndermanEquipments_CreativeTabs GetCreativeTab() {
        return creativetab;
    }

    private static Item_PredatorBow m_PBOW;

    public Item_PredatorBow GetPBOW() {
        return m_PBOW;
    }

    private static Item_PlasticArtifact m_PA;

    private static Item_PlasticArtifact_Droplets m_PA_D;

    private static Item_PlasticArtifact_Jellyfish m_PA_J;

    private static Item_PlasticArtifact_Stoneflower m_PA_S;

    private static Command_PredatorBow m_CPBOW;

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        creativetab = new AntiEndermanEquipments_CreativeTabs(LABEL_TAGNAME_CREATIVETAB);
        this.SetPredatorBow(e);
        this.SetPA(e);
        this.SetPA_D(e);
        this.SetPA_J(e);
        this.SetPA_S(e);
        this.SetCPBOW();
    }

    private void SetPredatorBow(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PBOW = new Item_PredatorBow();
        m_PBOW.setUnlocalizedName("PredatorBow");
        m_PBOW.setCreativeTab(creativetab);
        ResourceLocation rl = new ResourceLocation(MOD_ID, "predatorbow");
        GameRegistry.register(m_PBOW, rl);
        if (e.getSide().isClient()) {
            ModelResourceLocation mrl = new ModelResourceLocation(m_PBOW.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(m_PBOW, 0, mrl);
        }
    }

    private void SetPA(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PA = new Item_PlasticArtifact();
        m_PA.setUnlocalizedName("PlasticArtifact");
        m_PA.setCreativeTab(creativetab);
        ResourceLocation rl = new ResourceLocation(MOD_ID, "plasticartifact");
        GameRegistry.register(m_PA, rl);
        if (e.getSide().isClient()) {
            ModelResourceLocation mrl = new ModelResourceLocation(m_PA.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(m_PA, 0, mrl);
        }
    }

    private void SetPA_D(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PA_D = new Item_PlasticArtifact_Droplets();
        m_PA_D.setUnlocalizedName("PlasticArtifact_Droplets");
        m_PA_D.setCreativeTab(creativetab);
        ResourceLocation rl = new ResourceLocation(MOD_ID, "plasticartifact_droplets");
        GameRegistry.register(m_PA_D, rl);
        if (e.getSide().isClient()) {
            ModelResourceLocation mrl = new ModelResourceLocation(m_PA_D.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(m_PA_D, 0, mrl);
        }
    }

    private void SetPA_J(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PA_J = new Item_PlasticArtifact_Jellyfish();
        m_PA_J.setUnlocalizedName("PlasticArtifact_Jellyfish");
        m_PA_J.setCreativeTab(creativetab);
        ResourceLocation rl = new ResourceLocation(MOD_ID, "plasticartifact_jellyfish");
        GameRegistry.register(m_PA_J, rl);
        if (e.getSide().isClient()) {
            ModelResourceLocation mrl = new ModelResourceLocation(m_PA_J.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(m_PA_J, 0, mrl);
        }
    }

    private void SetPA_S(net.minecraftforge.fml.common.event.FMLPreInitializationEvent e) {
        m_PA_S = new Item_PlasticArtifact_Stoneflower();
        m_PA_S.setUnlocalizedName("PlasticArtifact_Stoneflower");
        m_PA_S.setCreativeTab(creativetab);
        ResourceLocation rl = new ResourceLocation(MOD_ID, "plasticartifact_stoneflower");
        GameRegistry.register(m_PA_S, rl);
        if (e.getSide().isClient()) {
            ModelResourceLocation mrl = new ModelResourceLocation(m_PA_S.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(m_PA_S, 0, mrl);
        }
    }

    private void SetCPBOW() {
        m_CPBOW = new Command_PredatorBow();
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent e) {
        this.SetRecipePredatorBow();
        this.SetRecipePA();
        this.SetRecipePA_D();
        this.SetRecipePA_J();
        this.SetRecipePA_S();
        this.SetRecipeIron();
        this.SetRecipeGunPowder();
        this.SetRecipeETC();
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

    private void SetRecipePA() {
        net.minecraftforge.fml.common.registry.GameRegistry.addSmelting(
                Blocks.DIRT,
                new ItemStack(m_PA, 3),
                0.1F);
    }

    private void SetRecipePA_D() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(m_PA_D),
                "pp",
                'p', m_PA);
    }

    private void SetRecipePA_J() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(m_PA_J),
                "pp",
                'p', m_PA_D);
    }

    private void SetRecipePA_S() {
        net.minecraftforge.fml.common.registry.GameRegistry.addSmelting(
                m_PA_D,
                new ItemStack(m_PA_S),
                0.1F);
    }

    private void SetRecipeIron() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.IRON_INGOT, 16),
                "d d",
                " j ",
                "d d",
                'd', m_PA_D,
                'j', m_PA_J);
    }

    private void SetRecipeGunPowder() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.GUNPOWDER, 24),
                "p p",
                " d ",
                "p p",
                'd', m_PA_D,
                'p', m_PA);
    }

    private void SetRecipeETC() {
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.GOLD_INGOT, 2),
                "sss",
                "sss",
                "sss",
                's', m_PA_S);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.GLOWSTONE_DUST, 2),
                "p  ",
                " d ",
                "  p",
                'd', m_PA_D,
                'p', m_PA);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.GOLDEN_APPLE, 1),
                "sss",
                "sas",
                "sss",
                's', m_PA_S,
                'a', Items.APPLE);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.DIAMOND, 2),
                "jcj",
                "c c",
                "jcj",
                'j', m_PA_J,
                'c', Items.COAL);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.LAVA_BUCKET, 1),
                "sss",
                "jdj",
                "sss",
                's', m_PA_S,
                'j', m_PA_J,
                'd', m_PA_D);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.PAPER, 4),
                "wpw",
                "p p",
                "wpw",
                'p', m_PA,
                'w', Items.STICK);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.STRING, 4),
                "pwp",
                "pwp",
                "pwp",
                'p', m_PA,
                'w', Items.STICK);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.BONE, 12),
                "pdp",
                "p p",
                "pdp",
                'p', m_PA,
                'd', Blocks.DIRT);
        net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(
                new ItemStack(Items.ARROW, 24),
                "p",
                "p",
                "i",
                'p', m_PA,
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
            m_CPBOW.processMessage(m.group(3), player);
        }
        patternStr = "(Command|Comm|comm|com)";
        p = Pattern.compile(patternStr);
        m = p.matcher(e.getMessage());
        if (m.find()) {

        }
    }

}
