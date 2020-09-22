package transformation;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import transformation.event.TickHandlerClient;
import transformation.render.RenderPlayerHand;

public class ClientProxy extends CommonProxy {

	@Override
    public void preInit()
    {
        super.preInit();
        Accessor.client();
		RenderPlayerHand.detectObfuscation();
    }
	@Override
	public void initTickHandlers(){
		super.initTickHandlers();
		tickHandlerClient = new TickHandlerClient();
        FMLCommonHandler.instance().bus().register(tickHandlerClient);

	}
	@Override
	public EntityPlayer getEntityPlayerInstance() {
		return Minecraft.getMinecraft().thePlayer;
	}
    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }
}
