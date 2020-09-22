package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;

public class RendererLivingEntityAccessor {
	/** protectedなのでここで呼び出す */
	public static ModelBase getMainModel(RendererLivingEntity r) {
		return r.mainModel;
	}
}
