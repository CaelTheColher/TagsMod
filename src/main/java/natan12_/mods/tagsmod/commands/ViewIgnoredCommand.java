package natan12_.mods.tagsmod.commands;

import natan12_.mods.tagsmod.TagsMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class ViewIgnoredCommand extends CommandBase
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_viewignored");}};

    @Override
    public String getName() {
        return "autocommand_viewignored";
    }

    @Override
    public List<String> getAliases() {
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
        String index = args[0];
        String[] parts = TagsMod.ignoredMsgs.get(index, null);
        if(parts == null)
        {
            ChatComponentText text = new ChatComponentText("Invalid index");
            text.getChatStyle().setColor(EnumChatFormatting.YELLOW);
            sender.addChatMessage(text);
            return;
        }
        ChatComponentText text = new ChatComponentText("");
        ChatComponentText msg = new ChatComponentText(parts[0]);
        ChatComponentText prefix = new ChatComponentText("[Ignored Message #" + index + "]: ");
        prefix.getChatStyle().setColor(EnumChatFormatting.GREEN);
        text.appendSibling(prefix).appendSibling(msg);
        sender.addChatMessage(text);
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender sender) {
        boolean use;
        try
        {
            use = sender.getEntityWorld().isRemote;
        }catch(Exception e){use = false;}
        return use;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_viewignored <number>";
    }
}
