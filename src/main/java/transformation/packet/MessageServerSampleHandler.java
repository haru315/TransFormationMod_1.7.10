package transformation.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import transformation.TransFormation;

public class MessageServerSampleHandler implements IMessageHandler<MessageServerSample, IMessage> {

    @Override//IMessageHandlerのメソッド
    public IMessage onMessage(MessageServerSample message, MessageContext ctx) {

        // サーバー側で実行されることになる
        try {
                World worldIn = ctx.getServerHandler().playerEntity.worldObj;
                if (worldIn == null) return null;

                Entity maid = worldIn.getEntityByID(message.getEntityID());
            	//getEntityDataでに特定のキーがあるEntity調べる
                if (maid.getEntityData().hasKey(TransFormation.MOD_ID+"_trans")) {
                    return new MessageSample(maid);
                    //PacketHandler.INSTANCE.sendTo(new MessageSample(maid), ctx.getServerHandler().playerEntity);
                }
    	}catch (NullPointerException e) {
            //開始直後にgetEntityByID()内でぬるぽが出ることがあるのでもみ消しとく
    	}
        return null;//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
    }
}
