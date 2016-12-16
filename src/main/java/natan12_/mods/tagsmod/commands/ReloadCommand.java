package natan12_.mods.tagsmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends CommandBase
{
    final List<String> aliases = new ArrayList<String>(){{add("ac_reload");}};

    @Override
    public String getName() {
        return "autocommand_reload";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_reload";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        for(Object o : Minecraft.getMinecraft().theWorld.playerEntities)
        {
            if(o == null || !(o instanceof EntityPlayer)) continue;
            ((EntityPlayer) o).refreshDisplayName();
        }
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
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return null;
    }
}
