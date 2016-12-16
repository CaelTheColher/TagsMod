/**
 * DeveloperCapes by Jadar
 * License: MIT License
 * (https://raw.github.com/jadar/DeveloperCapes/master/LICENSE)
 * version 4.0.0.x
 */
package com.jadarstudios.developercapes.cape;

import com.jadarstudios.developercapes.DevCapes;
import com.jadarstudios.developercapes.HDImageBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.net.URL;

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

		try {
			Field playerInfoF = ReflectionHelper.findField(AbstractClientPlayer.class, "playerInfo", "field_175157_a");
			playerInfoF.setAccessible(true);
			NetworkPlayerInfo nci = (NetworkPlayerInfo) playerInfoF.get(player);

			ReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, nci, location, "locationCape", "field_178862_f");

			playerInfoF.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
			DevCapes.getInstance();
			System.err.println("Setting cape ResourceLocation failed!");
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