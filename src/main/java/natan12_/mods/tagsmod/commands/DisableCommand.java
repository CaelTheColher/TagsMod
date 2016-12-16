package natan12_.mods.tagsmod.commands;

import natan12_.mods.tagsmod.TagsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class DisableCommand extends CommandBase
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_disable");}};

    @Override
    public String getName() {
        return "autocommand_disable";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_disable";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender iCommandSender, String[] strings) throws CommandException
    {
        TagsMod.overrideChat = false;
        TagsMod.allowParticles = false;
        TagsMod.useFormatting = false;

        for(Object o : Minecraft.getMinecraft().theWorld.playerEntities)
        {
            if(o == null) return;
            ((EntityPlayer) o).refreshDisplayName();
        }
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
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return null;
    }
}
