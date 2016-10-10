package natan12_.mods.tagsmod;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements ICommand
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_list");}};

    @Override
    public String getName()
    {
        return "autocommand_list";
    }

    @Override
    public List getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if(!sender.getEntityWorld().isRemote)
        {
            sender.addChatMessage(new ChatComponentText("Client-side only"));
            return;
        }
        sender.addChatMessage(new ChatComponentText(TagsMod.servers.list()));
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i)
    {
        return false;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender)
    {
        return "autocommand_list";
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos)
    {
        return null;
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender iCommandSender)
    {
        boolean use;
        try
        {
            use = iCommandSender.getEntityWorld().isRemote;
        }catch(Exception e){use = false;}
        return use;
    }
}

