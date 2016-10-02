package natan12_.mods.tagsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Set;

public class ConfigGUI implements IModGuiFactory
{
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement runtimeOptionCategoryElement)
    {
        return null;
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return GUI.class;
    }

    @Override
    public void initialize(Minecraft minecraft) {}

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    public static class GUI extends GuiConfig
    {
        public GUI(GuiScreen parent)
        {
            super(parent,
                    new ConfigElement(TagsMod.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                    TagsMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(TagsMod.config.toString()));
        }
    }
}
