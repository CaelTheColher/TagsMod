package natan12_.mods.tagsmod;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natan on 23/09/2016.
 */
public class UnignoreCommand implements ICommand
{
    final List<String> aliases = new ArrayList<String>(){{add("ac_unignore");}};

    @Override
    public String getName() {
        return "autocommand_unignore";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_unignore <player>";
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
        if(!TagsMod.ignored.containsKey(playername))
        {
            ChatComponentText text = new ChatComponentText("Player is not ignored");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        TagsMod.ignored.remove(playername);
        TagsMod.ignored.save();
        ChatComponentText text = new ChatComponentText("Player '" + playername + "' unblocked");
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
            List ret = new ArrayList();
            for(String s : TagsMod.ignored.getKeys())
            {
                String typed = args[0];
                if(typed.length() > 0 && !s.substring(0, typed.length()).equalsIgnoreCase(typed)) continue;
                ret.add(s);
            }
            return ret;
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
