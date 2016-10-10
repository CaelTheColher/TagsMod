package natan12_.mods.tagsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintCommand implements ICommand
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
        TagsMod.notIgnoredPlayers.addAll(Arrays.asList(args));
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

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
