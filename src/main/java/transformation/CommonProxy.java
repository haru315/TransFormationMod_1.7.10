package transformation;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import transformation.event.TickHandlerClient;
import transformation.event.TickHandlerServer;

public class CommonProxy {

	public void preInit() {

	}
	public void initTickHandlers() {
        tickHandlerServer = new TickHandlerServer();
        FMLCommonHandler.instance().bus().register(tickHandlerServer);
	}
	public EntityPlayer getEntityPlayerInstance(){
		return null;
	}
	public World getWorld() {
		return null;
	}

    // クライアント側のtick処理
    public TickHandlerClient tickHandlerClient;
    // サーバー側のtick処理
    public TickHandlerServer tickHandlerServer;
}
