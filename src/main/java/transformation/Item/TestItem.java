package transformation.Item;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import transformation.Formation;
import transformation.TransFormation;
import transformation.packet.MessageSample;
import transformation.packet.PacketHandler;

public class TestItem  extends Item
{
	/**アイテムでブロックを右クリックしたのメソッド。ItemMonsterPlacer参照。*/
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ)
    {
            return false;
    }

    /**アイテムを使ったときのメソッド。ItemMonsterPlacer参照。*/
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
    	//サーバー側の場合は処理をスキップする
        //if (world.isRemote){return itemStack;}
		//player.addChatMessage( new ChatComponentText(""));

    	if(itemStack.stackTagCompound != null && itemStack.stackTagCompound.getString("EntityType") != null) {

    		//シフトクリック検知
        	if(!player.isSneaking()){

        		//EntityDataに書き込み
                NBTTagCompound entityData = player.getEntityData();
                entityData.setBoolean(TransFormation.MOD_ID+"_trans", true);//false true
                entityData.setTag(TransFormation.MOD_ID+"_transID", itemStack.stackTagCompound.getTag("EntityType"));

            	//同期メッセージ
                PacketHandler.INSTANCE.sendToDimension(new MessageSample(player), player.dimension);

                world.getClosestPlayer(0, 0, 0, 0);

        	}
    	}
		return itemStack;
    }
    /**
     * EntityLivingBaseを右クリックしたのメソッド。
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity)
    {
    	if(itemStack.stackTagCompound == null || itemStack.stackTagCompound.getString("EntityType") == null) {
    		//NBTタグを取得します。
    		if(itemStack.stackTagCompound == null)
    		{
    			itemStack.stackTagCompound = new NBTTagCompound();
    		}
    		NBTTagCompound tag = (NBTTagCompound)entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		if(tag != null) {
        		itemStack.stackTagCompound.setTag("EntityType", tag);
    		}else {
        		itemStack.stackTagCompound.setTag("EntityType", Formation.getEntityLivingBaseTag(entity));
    		}

    		//NBTの入ったアイテム取得
    		player.inventory.addItemStackToInventory(itemStack);
    		--itemStack.stackSize;
    		itemStack.stackTagCompound = null;


    	}else if(player.isSneaking()){//シフトクリック検知
    		//EntityDataに書き込み

            NBTTagCompound entityData = entity.getEntityData();
            entityData.setBoolean(TransFormation.MOD_ID+"_trans", true);//false true
            entityData.setTag(TransFormation.MOD_ID+"_transID", itemStack.stackTagCompound.getTag("EntityType"));

        	//同期メッセージ
            PacketHandler.INSTANCE.sendToDimension(new MessageSample(entity), player.dimension);
    	}
        return true;
    }
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
	{
		if(itemstack.getTagCompound() == null)
		{
			list.add("null");
		} else {
			list.add("EntityType="+((NBTTagCompound)itemstack.stackTagCompound.getTag("EntityType")).getString("id"));
		}
	}
}