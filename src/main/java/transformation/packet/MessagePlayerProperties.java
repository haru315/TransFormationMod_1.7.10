package transformation.packet;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import transformation.TransFormation;

public class MessagePlayerProperties implements IMessage {

	private boolean data;
	private NBTTagCompound transID;

    public MessagePlayerProperties(){}

    public MessagePlayerProperties(Entity par1) {
        this.data= par1.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans");
        this.transID = (NBTTagCompound)par1.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    }

    @Override//IMessageのメソッド。ByteBufからデータを読み取る。
    public void fromBytes(ByteBuf buf) {
    	PacketBuffer pbuf = new PacketBuffer(buf);
        this.data= pbuf.readBoolean();
        try {
			this.transID = pbuf.readNBTTagCompoundFromBuffer();
		} catch (IOException e) {}
    }

    @Override//IMessageのメソッド。ByteBufにデータを書き込む。
    public void toBytes(ByteBuf buf) {
    	PacketBuffer pbuf = new PacketBuffer(buf);
    	pbuf.writeBoolean(this.data);
    	try {
			pbuf.writeNBTTagCompoundToBuffer(this.transID);
		} catch (IOException e) {}
    }
    public boolean getBoolean() {
        return this.data;
    }
    public NBTTagCompound getEntityTag() {
        return this.transID;
    }
}