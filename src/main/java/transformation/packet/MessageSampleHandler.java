package transformation.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import transformation.TransFormation;

public class MessageSampleHandler implements IMessageHandler<MessageSample, IMessage> {

    @Override//IMessageHandlerのメソッド
    public IMessage onMessage(MessageSample message, MessageContext ctx) {

    	try {
            World worldIn = TransFormation.proxy.getWorld();
            if (worldIn == null) return null;
            Entity maid = worldIn.getEntityByID(message.getEntityID());
            maid.getEntityData().setBoolean(TransFormation.MOD_ID+"_trans",message.getBoolean());
            maid.getEntityData().setTag(TransFormation.MOD_ID+"_transID",message.getEntityTag());

    	}catch (NullPointerException e) {
            //開始直後にgetEntityByID()内でぬるぽが出ることがあるのでもみ消しとく
    	}
        return null;//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
    }
}
