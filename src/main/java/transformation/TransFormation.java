package transformation;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import transformation.Item.TestItem;
import transformation.Item.TestItem_2;
import transformation.packet.PacketHandler;

@Mod(modid=TransFormation.MOD_ID, name="TransFormation", version="1.0",dependencies="required-after:Forge@[10.13.2.1236,)")
public class TransFormation{
	public static final String MOD_ID = "TransFormation";

	@SidedProxy(clientSide = "transformation.ClientProxy", serverSide = "transformation.CommonProxy")
	public static CommonProxy proxy;
	public static Item testItem;
	public static Item testItem_2;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// テスト用のアイテム追加
		testItem = new TestItem()
		.setCreativeTab(CreativeTabs.tabMaterials)
		.setUnlocalizedName("TestItem")
		.setTextureName("transformation:test_item")
		.setMaxStackSize(1);
		GameRegistry.registerItem(testItem, "testItem");
		// テスト用のアイテム追加2
		testItem_2 = new TestItem_2()
		.setCreativeTab(CreativeTabs.tabMaterials)
		.setUnlocalizedName("TestItem2")
		.setTextureName("transformation:test_item_2")
		.setMaxStackSize(1);
		GameRegistry.registerItem(testItem_2, "testItem_2");


        //二箇所に登録するので、先にインスタンスを生成しておく。
		transformation.event.EventHandler eventHandler = new transformation.event.EventHandler();
        //Forge Eventの登録。
		MinecraftForge.EVENT_BUS.register(eventHandler);
		//FML Eventの登録。
		FMLCommonHandler.instance().bus().register(eventHandler);

		proxy.preInit();
		PacketHandler.init();
	}
	@EventHandler
	 public void init(FMLInitializationEvent event) {
		proxy.initTickHandlers();
	}
	@EventHandler
	 public void postInit(FMLPostInitializationEvent event) {
	}
    /* Minecraft.getMinecraft().getRenderPartialTicks();で現在のTickを取得できる(1.7.10不可)
     * ほかにも
     *
     * Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"field_71428_T","timer");
     * float partialTicks = timer.renderPartialTicks;
     * を経由するほか
     *
     * TickEvent.RenderTickEvent
     * でイベント経由などがある
     */
}