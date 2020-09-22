package test_mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Test.MOD_ID, name="Test", version="1.0",dependencies="required-after:Forge@[10.13.2.1236,)")
public class Test {
	public static final String MOD_ID = "Test";

//	@SidedProxy(clientSide = "ClientProxyのパス", serverSide = "CommonProxyのパス")
//	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}
	@EventHandler
	 public void init(FMLInitializationEvent event) {
	}
	@EventHandler
	 public void postInit(FMLPostInitializationEvent event) {
	}
}
