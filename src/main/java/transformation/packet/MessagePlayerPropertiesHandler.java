package transformation.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import transformation.TransFormation;

public class MessagePlayerPropertiesHandler implements IMessageHandler<MessagePlayerProperties, IMessage> {

    @Override
    public IMessage onMessage(MessagePlayerProperties message, MessageContext ctx) {
        //Client側にIExtendedEntityPropertiesを渡す。
    	EntityPlayer player = TransFormation.proxy.getEntityPlayerInstance();
    	player.getEntityData().setBoolean(TransFormation.MOD_ID+"_trans",message.getBoolean());
    	player.getEntityData().setTag(TransFormation.MOD_ID+"_transID",message.getEntityTag());
        //REPLYは送らないので、nullを返す。
        return null;
    }
}

//public class MessagePlayerJoinInAnnouncementHandler implements IMessageHandler<MessagePlayerJoinInAnnouncement, MessagePlayerProperties> {
//    @Override
//    public MessagePlayerProperties onMessage(MessagePlayerJoinInAnnouncement message, MessageContext ctx) {