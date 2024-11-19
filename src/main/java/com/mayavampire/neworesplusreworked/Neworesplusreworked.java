package com.mayavampire.neworesplusreworked;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.rmi.registry.Registry;

import static net.minecraft.world.item.ToolMaterial.DIAMOND;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Neworesplusreworked.MODID)
public class Neworesplusreworked {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "neworesplusreworked";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger ();
    // Create a Deferred Register to hold Blocks which will all be registered under the "neworesplusreworked" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks ( MODID );
    // Create a Deferred Register to hold Items which will all be registered under the "neworesplusreworked" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems ( MODID );
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "neworesplusreworked" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create ( Registries.CREATIVE_MODE_TAB , MODID );

    // Creates a new Block with the id "neworesplusreworked:example_block", combining the namespace and path
    public static final DeferredBlock<Block> SAPPHIRE_BLOCK = BLOCKS.registerSimpleBlock ( "sapphire_block", BlockBehaviour.Properties.of ().sound ( SoundType.AMETHYST ).strength ( 3.0f, 10f).requiresCorrectToolForDrops () );
    public static final DeferredItem<BlockItem> SAPPHIRE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem ( "sapphire_block_item", SAPPHIRE_BLOCK );
    public static final DeferredItem<Item> SAPPHIRE_ITEM = ITEMS.registerSimpleItem ( "sapphire_item");
    public static final DeferredBlock<Block> SAPPHIRE_ORE = BLOCKS.registerSimpleBlock ( "sapphire_ore", BlockBehaviour.Properties.of ().sound ( SoundType.AMETHYST ).strength ( 3.0f, 10f).requiresCorrectToolForDrops () );
    public static final DeferredItem<BlockItem> SAPPHIRE_ORE_ITEM = ITEMS.registerSimpleBlockItem ( "sapphire_ore_item", SAPPHIRE_ORE );
    public static final DeferredItem<Item> SAPPHIRE_AXE = ITEMS.registerSimpleItem ( "sapphire_axe" , new Item.Properties ().durability ( 19902 ).rarity ( Rarity.EPIC ));
    public static final DeferredItem<Item> RAW_SAPPHIRE = ITEMS.registerSimpleItem ( "raw_sapphire" );
    public static final DeferredBlock<Block> DEEPSLATE_SAPPHIRE_ORE = BLOCKS.registerSimpleBlock ( "deepslate_sapphire_ore", BlockBehaviour.Properties.of ().sound ( SoundType.AMETHYST ).strength ( 5.3f, 10f ).requiresCorrectToolForDrops () );
    public static final DeferredItem<BlockItem> DEEPSLATE_SAPPHIRE_ORE_ITEM = ITEMS.registerSimpleBlockItem ( "deepslate_sapphire_ore_item", DEEPSLATE_SAPPHIRE_ORE );
    public static final DeferredBlock<Block> NETHER_SAPPHIRE_ORE = BLOCKS.registerSimpleBlock ( "nether_sapphire_ore", BlockBehaviour.Properties.of ().sound ( SoundType.NETHERRACK ).strength ( 1f, 0.4f  ).requiresCorrectToolForDrops () );
    public static final DeferredItem<BlockItem> NETHER_SAPPHIRE_ORE_ITEM = ITEMS.registerSimpleBlockItem ( "nether_sapphire_ore_item", NETHER_SAPPHIRE_ORE );
    public static final DeferredItem<Item> SAPPHIRE_SWORD;

    static {
        SAPPHIRE_SWORD = ITEMS.registerSimpleItem ( "sapphire_sword" );
    }

    // Creates a creative tab with the id "neworesplusreworked:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register ( "example_tab" , () -> CreativeModeTab.builder ().title ( Component.translatable ( "itemGroup.neworesplusreworked" ) ).withTabsBefore ( CreativeModeTabs.COMBAT ).icon ( () -> SAPPHIRE_ITEM.get ().getDefaultInstance () ).displayItems ( (parameters , output) -> {
        output.accept ( SAPPHIRE_ITEM.get () );
        output.accept ( SAPPHIRE_BLOCK_ITEM );
        output.accept ( SAPPHIRE_ORE_ITEM );
        output.accept (SAPPHIRE_AXE );
        output.accept ( RAW_SAPPHIRE );
        output.accept ( DEEPSLATE_SAPPHIRE_ORE_ITEM );
        output.accept (NETHER_SAPPHIRE_ORE_ITEM);
    } ).build () );

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Neworesplusreworked(IEventBus modEventBus , ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener ( this::commonSetup );

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register ( modEventBus );
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register ( modEventBus );
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register ( modEventBus );

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (neworesplusreworked) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register ( this );

        // Register the item to a creative tab
        modEventBus.addListener ( this::addCreative );

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig ( ModConfig.Type.COMMON , Config.SPEC );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info ( "HELLO FROM COMMON SETUP" );

        if (Config.logDirtBlock) LOGGER.info ( "DIRT BLOCK >> {}" , BuiltInRegistries.BLOCK.getKey ( Blocks.DIRT ) );

        LOGGER.info ( "{}{}" , Config.magicNumberIntroduction , Config.magicNumber );

    }

    // Add the example blocks item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey ()== CreativeModeTabs.COMBAT) event.accept ( SAPPHIRE_BLOCK_ITEM );
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info ( "HELLO from server starting" );
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info ( "HELLO FROM CLIENT SETUP" );
            LOGGER.info ( "MINECRAFT NAME >> {}" , Minecraft.getInstance ().getUser ().getName () );
        }
    }
}
