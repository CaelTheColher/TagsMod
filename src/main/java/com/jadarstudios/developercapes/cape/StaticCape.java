/**
 * DeveloperCapes by Jadar
 * License: MIT License
 * (https://raw.github.com/jadar/DeveloperCapes/master/LICENSE)
 * version 4.0.0.x
 */
package com.jadarstudios.developercapes.cape;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;

import com.jadarstudios.developercapes.DevCapes;
import com.jadarstudios.developercapes.HDImageBuffer;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Default Cape implementation
 *
 * @author jadar
 */
public class StaticCape extends AbstractCape {

	public StaticCape (String name, URL url) {
		setName(name);
		setURL(url);
	}

	public StaticCape (String name) {
		this(name, null);
	}
	@Override
	public void loadTexture (AbstractClientPlayer player) {
		ResourceLocation location = getLocation();

		// mmdanggg2: using reflection to modify the private locationCape, hacky
		// but it works.
		// Wehavecookies56: Added obfuscated field names for reflection and fixed for 1.9
		try {
			Field playerInfoF = ReflectionHelper.findField(AbstractClientPlayer.class, "playerInfo", "field_175157_a");
			playerInfoF.setAccessible(true);
			NetworkPlayerInfo nci = (NetworkPlayerInfo) playerInfoF.get(player);

			ReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, nci, location, "locationCape", "field_178862_f");

			playerInfoF.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
			DevCapes.getInstance();
		}

		Minecraft.getMinecraft().renderEngine.loadTexture(location, getTexture());
	}

	@Override
	public boolean isTextureLoaded (AbstractClientPlayer player) {
		ResourceLocation cape = player.getLocationCape();
		return cape != null;
	}

	public void setURL (URL url) {
		if (url == null) {
			this.texture = null;
			return;
		}
		this.texture = new ThreadDownloadImageData(null, url.toString(), null, new HDImageBuffer());
	}

	public void setName (String name) {
		this.name = name;
		this.location = new ResourceLocation("DevCapes/" + name);
	}
}



/*


net.minecraftforge.fml.relauncher.ReflectionHelper$UnableToFindFieldException: java.lang.NoSuchFieldException: field_187107_a
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraftforge.fml.relauncher.ReflectionHelper.findField(ReflectionHelper.java:90)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at com.jadarstudios.developercapes.cape.StaticCape.loadTexture(StaticCape.java:52)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at com.jadarstudios.developercapes.RenderEventHandler.renderPlayer(RenderEventHandler.java:40)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraftforge.fml.common.eventhandler.ASMEventHandler_28_RenderEventHandler_renderPlayer_Pre.invoke(.dynamic)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraftforge.fml.common.eventhandler.ASMEventHandler.invoke(ASMEventHandler.java:55)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraftforge.fml.common.eventhandler.EventBus.post(EventBus.java:140)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.entity.RenderPlayer.func_180596_a(RenderPlayer.java:56)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.entity.RenderPlayer.func_76986_a(RenderPlayer.java:242)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.entity.RenderManager.func_147939_a(RenderManager.java:398)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.entity.RenderManager.func_147936_a(RenderManager.java:355)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.entity.RenderManager.func_147937_a(RenderManager.java:322)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.RenderGlobal.func_180446_a(RenderGlobal.java:818)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.EntityRenderer.func_175068_a(EntityRenderer.java:1734)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.EntityRenderer.func_78471_a(EntityRenderer.java:1559)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.renderer.EntityRenderer.func_78480_b(EntityRenderer.java:1348)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.Minecraft.func_71411_J(Minecraft.java:1055)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.Minecraft.func_99999_d(Minecraft.java:345)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.client.main.Main.main(SourceFile:120)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at java.lang.reflect.Method.invoke(Unknown Source)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.launchwrapper.Launch.launch(Launch.java:135)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraft.launchwrapper.Launch.main(Launch.java:28)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: Caused by: java.lang.NoSuchFieldException: field_187107_a
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at java.lang.Class.getDeclaredField(Unknown Source)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	at net.minecraftforge.fml.relauncher.ReflectionHelper.findField(ReflectionHelper.java:81)
[18:10:39] [Client thread/INFO] [STDERR]: [java.lang.Throwable$WrappedPrintStream:println:-1]: 	... 23 more



 */