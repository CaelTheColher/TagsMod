package natan12_.mods.tagsmod.commands;

import natan12_.mods.tagsmod.TagsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCommand extends CommandBase
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_ignore");}};

    @Override
    public String getName() {
        return "autocommand_ignore";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_ignore <player> (the messages the player sents are still printed to the log)";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if(args == null || args.length < 1)
        {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        String playername = args[0];
        if(playername.equals(Minecraft.getMinecraft().thePlayer.getName()))
        {
            ChatComponentText text = new ChatComponentText("You can't ignore yourself!");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        TagsMod.ignored.set(playername, "true");
        TagsMod.ignored.save();
        ChatComponentText text = new ChatComponentText("Player '" + playername + "' blocked");
        text.getChatStyle().setColor(EnumChatFormatting.GREEN);
        sender.addChatMessage(text);
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender iCommandSender) {
        boolean use;
        try
        {
            use = iCommandSender.getEntityWorld().isRemote;
        }catch(Exception e){use = false;}
        return use;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] args, BlockPos blockPos) {
        if(args == null) return null;
        if(args.length == 1)
        {
            List<String> ret = new ArrayList<>();
            String typed = args[0].toLowerCase();
            for(Object o : Minecraft.getMinecraft().theWorld.playerEntities)
            {
                if(!(o instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer) o;
                String playername = player.getName();
                if(!playername.toLowerCase().startsWith(typed)) continue;
                ret.add(playername);
            }
            return ret;
        }
        return null;
    }
}
