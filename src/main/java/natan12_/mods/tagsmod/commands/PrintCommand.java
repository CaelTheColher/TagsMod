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

public class PrintCommand extends CommandBase
{
    private static final List<String> aliases = new ArrayList<String>() {{add("ac_print");}};


    @Override
    public String getName() {
        return "autocommand_print";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_print <name1> [<name2> <name3> ...]";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if(args == null || args.length < 1)
        {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        boolean valid = false;
        for(String s : args)
        {
            if(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(s) != null)
            {
                valid = true;
                TagsMod.notIgnoredPlayers.add(s);
            }
        }
        if(!valid)
        {
            ChatComponentText text = new ChatComponentText("At least one of the players selected must be online and near you");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        TagsMod.timeLeft = System.currentTimeMillis() + 10000;
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
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos blockPos) {
        if(args.length < 1) return null;
        List<String> ret = new ArrayList<>();
        String typed = args[args.length-1].toLowerCase();
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
}
