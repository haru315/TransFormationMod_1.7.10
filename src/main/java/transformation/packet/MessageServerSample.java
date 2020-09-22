package transformation.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;

public class MessageServerSample implements IMessage {

	private int entityId;

    public MessageServerSample(){}

    public MessageServerSample(Entity par1) {
        this.entityId = par1.getEntityId();
    }

    @Override//IMessageのメソッド。ByteBufからデータを読み取る。
    public void fromBytes(ByteBuf buf) {
    	PacketBuffer pbuf = new PacketBuffer(buf);
        this.entityId = pbuf.readInt();
    }

    @Override//IMessageのメソッド。ByteBufにデータを書き込む。
    public void toBytes(ByteBuf buf) {
    	PacketBuffer pbuf = new PacketBuffer(buf);
    	pbuf.writeInt(this.entityId);
    }
    public int getEntityID() {
        return this.entityId;
    }
}
