package tonius.simplyjetpacks;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "simplyjetpacks", name = "SimplyJetpacks", dependencies = "required-after:CoFHCore;required-after:ThermalExpansion")
@NetworkMod(channels = { "SmpJet" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class SimplyJetpacks {

    @Instance("simplyjetpacks")
    public static SimplyJetpacks instance;

    @SidedProxy(clientSide = "tonius.simplyjetpacks.client.ClientProxy", serverSide = "tonius.simplyjetpacks.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config;
    public static Logger logger;
    public static KeyboardTracker keyboard;

    public static CreativeTabs creativeTab = new CreativeTabs("tabSimplyJetpacks") {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(SJItems.jetpackTier4, 1, 0);
        }
    };

    @EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting Simply Jetpacks");

        logger.info("Loading configuration file");
        config = new Configuration(new File(evt.getModConfigurationDirectory(), "simplyjetpacks.cfg"));
        ConfigReader.loadConfig(config);

        SJItems.preInit();

        logger.info("Registering handlers");
        proxy.registerHandlers();
    }

    @EventHandler
    public static void init(FMLInitializationEvent evt) {
        SJItems.init();
    }

}
