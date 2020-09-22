package transformation.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import transformation.TransFormation;

public class TickHandlerClient {

    private double ySize;
	private float eyeHeight;

	@SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if(mc.theWorld != null)
        {
            if(event.phase == TickEvent.Phase.START)
            {
            	EntityClientPlayerMP player = mc.thePlayer;
            	if (player.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans") && player.getEntityData().getTag(TransFormation.MOD_ID+"_transID") != null)
            	{
            		NBTTagCompound tag =(NBTTagCompound)player.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
            		EntityLivingBase entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, player.worldObj);

                    ySize = mc.thePlayer.yOffset - entity.getEyeHeight() + mc.thePlayer.getDefaultEyeHeight();
                    eyeHeight = mc.thePlayer.eyeHeight;
                    mc.thePlayer.lastTickPosY -= ySize;
                    mc.thePlayer.prevPosY -= ySize;
                    mc.thePlayer.posY -= ySize;
                    mc.thePlayer.eyeHeight = mc.thePlayer.getDefaultEyeHeight();
            	}
            }
            else
            {
                if(mc.thePlayer.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans"))
                {
                    mc.thePlayer.lastTickPosY += ySize;
                    mc.thePlayer.prevPosY += ySize;
                    mc.thePlayer.posY += ySize;
                    mc.thePlayer.eyeHeight = eyeHeight;
                }
            }
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.ClientTickEvent event)
    {

    }
}
