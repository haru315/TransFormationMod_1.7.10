package transformation.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import transformation.TransFormation;

public class PacketHandler {

    //このMOD用のSimpleNetworkWrapperを生成。チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
    //チャンネル名は20文字以内の文字数制限があるので注意。
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(TransFormation.MOD_ID);


    public static void init() {

        /*IMesssageHandlerクラスとMessageクラスの登録。
        *第三引数：MessageクラスのMOD内での登録ID。256個登録できる
        *第四引数：送り先指定。クライアントかサーバーか、Side.CLIENT Side.SERVER*/
        INSTANCE.registerMessage(MessageSampleHandler.class, MessageSample.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessageServerSampleHandler.class, MessageServerSample.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessagePlayerPropertiesHandler.class, MessagePlayerProperties.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(MessagePlayerJoinInAnnouncementHandler.class, MessagePlayerJoinInAnnouncement.class, 3, Side.SERVER);
    }
}