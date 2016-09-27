package natan12_.mods.tagsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = "tagsmods", name = "Tags Mod", version = "1.8", guiFactory = "natan12_.mods.tagsmod.ConfigGUI")
public class TagsMod
{
    @Mod.Instance("tagsmod")
    public static TagsMod instance;
    public static Configuration config;
    public static ConfigLua servers;
    public static ConfigLua tags;
    public static ConfigLua ignored;
    public static boolean useFormatting = false;
    public static boolean overrideChat = false;
    public static boolean showPlayerInfo = false;
    public static boolean overrideClicks = false;
    public static int range;

    public static final HashMap<String, String> DEFAULT_TAGS = new HashMap<String, String>()
    {
        {
            put("natan12_", "&6&l&kHHH&r&6&lDesenvolvedor do Mod&kHHH&r&6&l&o");
            put("natan12_IntelliJ", "&6&l&kHHH&r&6&lDesenvolvedor do Mod&kHHH&r&6&l&o");
            put("IlyBr", "&8&kHH&r&8Undertale&kHH&r&8");
            put("lucaszainko", "&c&kHHH&r&aVIP&c&kHHH&r");
            put("spootnd", "&2&kaoa&a&6D&a&3o&a&5i&a&1d&a&9a&7&ao&2&kaoa&a&3");
            put("pedrojamur", "&4UmPigmeuAmigo&r");
        }
    };
    public static final Random random = new Random(System.currentTimeMillis());
    public static final EnumChatFormatting[] roneyBrubs =
            {EnumChatFormatting.DARK_BLUE, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_AQUA,
             EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GOLD,
             EnumChatFormatting.BLUE, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA,
             EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.YELLOW};
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static int currentColor = random.nextInt(roneyBrubs.length);
    public static int msgs = 0;

    public static final int defaultRange = 32;

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        File cfgFile = event.getSuggestedConfigurationFile();
        config = new Configuration(cfgFile);
        config.load();
        servers = new ConfigLua(cfgFile, "config.luatable");
        tags = new ConfigLua(cfgFile, "tags.luatable");
        ignored = new ConfigLua(cfgFile, "ignored.luatable");

        saveConfigs();
    }

    public static MovingObjectPosition getMouseOverExtended(float dist)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        Entity theRenderViewEntity = mc.getRenderViewEntity();
        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
                theRenderViewEntity.posX-0.5D,
                theRenderViewEntity.posY-0.0D,
                theRenderViewEntity.posZ-0.5D,
                theRenderViewEntity.posX+0.5D,
                theRenderViewEntity.posY+1.5D,
                theRenderViewEntity.posZ+0.5D
        );
        MovingObjectPosition returnMOP = null;
        if (mc.theWorld != null)
        {
            double var2 = dist;
            returnMOP = theRenderViewEntity.rayTrace(var2, 1F);
            double calcdist = var2;
            Vec3 pos = theRenderViewEntity.getPositionEyes(0);
            var2 = calcdist;
            if (returnMOP != null)
            {
                calcdist = returnMOP.hitVec.distanceTo(pos);
            }

            Vec3 lookvec = theRenderViewEntity.getLook(0);
            Vec3 var8 = pos.addVector(lookvec.xCoord * var2,
                    lookvec.yCoord * var2,
                    lookvec.zCoord * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;
            @SuppressWarnings("unchecked")
            List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                    theRenderViewEntity,
                    theViewBoundingBox.addCoord(
                            lookvec.xCoord * var2,
                            lookvec.yCoord * var2,
                            lookvec.zCoord * var2).expand(var9, var9, var9));
            double d = calcdist;

            for (Entity entity : list)
            {
                if (entity.canBeCollidedWith())
                {
                    float bordersize = entity.getCollisionBorderSize();
                    AxisAlignedBB aabb = new AxisAlignedBB(
                            entity.posX-entity.width/2,
                            entity.posY,
                            entity.posZ-entity.width/2,
                            entity.posX+entity.width/2,
                            entity.posY+entity.height,
                            entity.posZ+entity.width/2);
                    aabb.expand(bordersize, bordersize, bordersize);
                    MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);

                    if (aabb.isVecInside(pos))
                    {
                        if (0.0D < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = 0.0D;
                        }
                    } else if (mop0 != null)
                    {
                        double d1 = pos.distanceTo(mop0.hitVec);

                        if (d1 < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = d1;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d < calcdist || returnMOP == null))
            {
                returnMOP = new MovingObjectPosition(pointedEntity);
            }
        }
        return returnMOP;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderOverlay(RenderGameOverlayEvent event)
    {
        if(!showPlayerInfo) return;
        EntityLooked entity = getPlayerLook();
        if(event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if(!entity.isPlayer) return;
        if(entity.player == null) return;
        String name = entity.name;
        EntityPlayer player = entity.player;
        ItemStack helditem = player.getHeldItem();
        String item;
        if(helditem == null) item = "None";
        else item = StatCollector.translateToLocal(helditem.getUnlocalizedName());
        Minecraft.getMinecraft().fontRendererObj.drawString("Name: " + name + ", Health: " + player.getHealth() + "/" + player.getMaxHealth() +
                ", Held Item: " + item,
                10, 3, 0xFFFFFF);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onLogin(PlayerEvent.NameFormat event)
    {
        System.out.println(event.username);
        if(event.username.equals("roneyq123") || event.username.equals("BrubsCraft"))
        {
            switch(event.username)
            {
                case "roneyq123":
                    event.displayname = parseTag(roneyBrubs[currentColor] + "&l&kHH&r" + roneyBrubs[currentColor] + "&lNamorado da Bruna&kHH&r" + roneyBrubs[currentColor] + "&o&l") + " " + event.username;
                    break;
                case "BrubsCraft":
                    event.displayname = parseTag(roneyBrubs[currentColor] + "&l&kHH&r" + roneyBrubs[currentColor] + "&lNamorada do Roney&kHH&r" + roneyBrubs[currentColor] + "&o&l" + " " + event.username);
                    break;
            }
            return;
        }
        if(DEFAULT_TAGS.containsKey(event.username))
        {
            event.displayname = parseTag(DEFAULT_TAGS.get(event.username)) + " " + event.username;
            return;
        }
        if(tags.get(event.username, null) != null)
        {
            event.displayname = parseTag(tags.get(event.username, null)[0]) + " " + event.username;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void chatReceived(ClientChatReceivedEvent event)
    {
        if(!overrideChat) return;
        IChatComponent message = event.message;
        String formatted = message.getFormattedText();
        String unformatted = message.getUnformattedText();
        System.out.println("[TagsMod/Chat] Formatted: " + formatted);
        System.out.println("[TagsMod/Chat] Unformatted: " + unformatted);
        int first = formatted.indexOf(":");
        int unformattedFirst = unformatted.indexOf(":");
        event.setCanceled(true);
        if(unformattedFirst > 0 && unformattedFirst < 30 && !unformatted.subSequence(0, unformatted.length() < 20 ? unformatted.length() : 20).toString().contains(">"))
        {
            ChatComponentText toSend = new ChatComponentText("");
            String playernamewithtag = formatted.substring(0, first);
            String playername = unformatted.substring(0,unformattedFirst).split(" ")[playernamewithtag.split(" ").length-1];
            if(ignored.get(playername, null) != null && !playername.equals(Minecraft.getMinecraft().thePlayer.getName()))
            {
                if(ignored.get(playername, null)[0].equals("true"))
                {
                    System.out.println("[TagsMod/Ignore] Blocked message from '" + playername + "': " + unformatted);
                    return;
                }
            }
            ChatComponentText name;
            if(tags.get(playername, null) != null && playername.equals(Minecraft.getMinecraft().thePlayer.getName()))
            {
                String tag = parseTag(tags.get(playername, null)[0]);
                name = new ChatComponentText(tag + " " + playername);
            }
            else if(playername.equals("roneyq123") || playername.equals("BrubsCraft"))
            {
                switch(playername)
                {
                    case "roneyq123":
                        name = new ChatComponentText(parseTag(roneyBrubs[currentColor] + "&l&kHH&r" + roneyBrubs[currentColor] + "&lNamorado da Bruna&kHH&r" + roneyBrubs[currentColor] + "&o&l"));
                        name.appendText(" ").appendText(playername);
                        msgs++;
                        if (msgs > 5) {
                            msgs = 0;
                            currentColor = random.nextInt(roneyBrubs.length);
                            Object o = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("roneyq123");
                            if(o != null) ((EntityPlayer) o).refreshDisplayName();
                        }
                        break;
                    case "BrubsCraft":
                        name = new ChatComponentText(parseTag(roneyBrubs[currentColor] + "&l&kHH&r" + roneyBrubs[currentColor] + "&lNamorada do Roney&kHH&r" + roneyBrubs[currentColor] + "&o&l"));
                        name.appendText(" ").appendText(playername);
                        msgs++;
                        if (msgs > 5) {
                            msgs = 0;
                            currentColor = random.nextInt(roneyBrubs.length);
                            Object o = Minecraft.getMinecraft().theWorld.getPlayerEntityByName("BrubsCraft");
                            if(o != null) ((EntityPlayer) o).refreshDisplayName();
                        }
                        break;
                    default:
                        name = new ChatComponentText("ERROR");
                }
            }
            else if(DEFAULT_TAGS.containsKey(playername))
            {
                name = new ChatComponentText(parseTag(DEFAULT_TAGS.get(playername)));
                name.appendText(" ").appendText(playername);
            }
            else
            {
                if(tags.get(playername, null) != null)
                {
                    String tag = parseTag(tags.get(playername, null)[0]);
                    name = new ChatComponentText(tag + " " + playername);
                }
                else
                {
                    name = new ChatComponentText(playernamewithtag);
                    ChatStyle styleName = name.getChatStyle();
                    styleName.setBold(true);
                    styleName.setItalic(true);
                }
            }
            ChatComponentText text = new ChatComponentText("");
            String msg = formatted.substring(first+1);
            String[] parts = msg.contains(" ") ? msg.split(" ") : new String[]{msg};
            for(int i = 0; i < parts.length; i++)
            {
                String s = parts[i];
                if(s.equals("")) continue;
                String url = s.replaceAll('\u00a7' + "[0123456789aAbBcCdDeEfFkKlLmMoOrR]", "");
                Matcher matcher = urlPattern.matcher(url);
                if(matcher.matches())
                {
                    ChatComponentText t = new ChatComponentText(s);
                    t.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).setItalic(true);
                    text.appendSibling(t);
                }
                else
                    text.appendText(s);
                if(i < parts.length - 1) text.appendText(" ");
            }

            if(!overrideClicks)
            {
                name.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + playername.replaceAll("(§.)*", "") + " "))
                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Clique aqui para mandar um /tell para essa pessoa")));
            }
            toSend.appendSibling(name);
            toSend.appendText(": ");
            toSend.appendSibling(text);
            ChatStyle style = toSend.getChatStyle();
            if(overrideClicks)
            {
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + playername.replaceAll("(§.)*", "") + " "));
                style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Clique aqui para mandar um /tell para essa pessoa")));
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(toSend);
            return;
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(message);
    }

    public static String parseTag(String tag)
    {
        char[] b = tag.toCharArray();
        for (int i = 0; i < b.length - 1; i++)
        {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1)
            {
                b[i] = '\u00a7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public static EntityLooked getPlayerLook()
    {
        if(Minecraft.getMinecraft().objectMouseOver != null)
        {
            MovingObjectPosition object = getMouseOverExtended(range);
            if(object == null) return new EntityLooked();
            if(object.entityHit != null)
            {
                Entity entity = object.entityHit;
                return new EntityLooked(entity.getName(), entity.getCustomNameTag(), entity.getDisplayName().getUnformattedText(), entity instanceof EntityPlayer, entity instanceof  EntityPlayer ? (EntityPlayer) entity : null);
            }
        }

        return new EntityLooked();
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(instance);
        FMLCommonHandler.instance().bus().register(instance);
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientCommandHandler handler = ClientCommandHandler.instance;
        handler.registerCommand(new AddCommand());
        handler.registerCommand(new RemoveCommand());
        handler.registerCommand(new ListCommand());
        handler.registerCommand(new TagCommand());
        handler.registerCommand(new IgnoreCommand());
        handler.registerCommand(new UnignoreCommand());
        handler.registerCommand(new ReloadCommand());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        try
        {
            if(Minecraft.getMinecraft().getCurrentServerData().serverIP == null) return;
            String currIp = Minecraft.getMinecraft().getCurrentServerData().serverIP;
            sendCommand.setIP(currIp);
            System.out.println("Connected to server '" + currIp + "'");
            if(servers.get(currIp, ";") != null)
                MinecraftForge.EVENT_BUS.register(EntityEventHandler.INSTANCE);
        }
        catch(Throwable th)
        {
            System.err.println("[TagsMod/onConnectedToServerEvent] Error detected, aborting execution");
            System.err.println("[TagsMod/onConnectedToServerEvent] " + th.getMessage());
            th.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        System.out.println(eventArgs.modID);
        if(eventArgs.modID.equals("tagsmod"))
            saveConfigs();
    }

    public void saveConfigs()
    {
        overrideChat = config.getBoolean("override_chat", Configuration.CATEGORY_GENERAL, false, "Whether or not to override chat messages\n(Disable if buggy)");
        useFormatting = config.getBoolean("use_formatting", Configuration.CATEGORY_GENERAL, false, "Use formatting codes in messages (use & instead of §)");
        showPlayerInfo = config.getBoolean("show_player_info", Configuration.CATEGORY_GENERAL, false, "Show player info when you look at them");
        overrideClicks = config.getBoolean("override_chat_clicks", Configuration.CATEGORY_GENERAL, false, "Send /tell by clicking player message in chat\n(Off makes only the name be clickable)");
        range = config.getInt("player_info_range", Configuration.CATEGORY_GENERAL, defaultRange, 0, 256, "Range where player info can be seen");

        if(config.hasChanged()) config.save();
    }

    public static final CommandSender sendCommand = new CommandSender();

    public static final class CommandSender implements Runnable
    {
        private String IP;

        public void setIP(String ip)
        {
            this.IP = ip;
        }

        public void run()
        {
            try
            {
                System.out.println("Sending commands...");
                for(String c : servers.get(IP, ";"))
                {
                    System.out.println("Executing command '" + c + "'");
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(c);
                }
            }
            catch(Throwable th)
            {
                System.err.println("[TagsMod/CommandSender] Error detected, aborting execution");
                System.err.println("[TagsMod/CommandSender] " + th.getMessage());
                th.printStackTrace();
            }
        }
    };

    public static class EntityEventHandler {
        public static final EntityEventHandler INSTANCE = new EntityEventHandler();

        @SubscribeEvent
        public void entityJoinWorld(EntityJoinWorldEvent event) {
            try
            {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                if(player == null) return;
                Minecraft.getMinecraft().addScheduledTask(sendCommand);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
            catch(Throwable th)
            {
                System.err.println("[TagsMod/EntityEventHandler] Error detected, aborting execution");
                System.err.println("[TagsMod/EntityEventHandler] " + th.getMessage());
                th.printStackTrace();
            }
        }
    }

    public static class EntityLooked
    {
        public final String name;
        public final String customNametag;
        public final String displayName;
        public final boolean isPlayer;
        public final EntityPlayer player;

        public EntityLooked(String name, String customNameTag, String displayName, boolean isPlayer, EntityPlayer player)
        {
            this.name = name;
            this.customNametag = customNameTag;
            this.displayName = displayName;
            this.isPlayer = isPlayer;
            this.player = player;
        }

        public EntityLooked()
        {
            this.name = null;
            this.customNametag = null;
            this.displayName = null;
            this.isPlayer = false;
            this.player = null;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("{name = ");
            sb.append(name);
            sb.append(", ");
            sb.append("customNametag = ");
            sb.append(customNametag);
            sb.append(", ");
            sb.append("displayName = ");
            sb.append(displayName);
            sb.append(", ");
            sb.append("isPlayer = ");
            sb.append(isPlayer);
            sb.append("}");
            return sb.toString();
        }
    }
}
