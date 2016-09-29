package natan12_.mods.tagsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Natan on 29/09/2016.
 */
public class ParticlesCommand implements ICommand
{
    static final ArrayList<String> PARTICLES = new ArrayList<String>()
    {
        {
            for(EnumParticleTypes type : EnumParticleTypes.values())
                add(type.toString());
        }
    };

    final List aliases = new ArrayList<>();

    public ParticlesCommand()
    {
        aliases.add("ac_particles");
    }

    @Override
    public String getName() {
        return "autocommand_particles";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_particles <set|remove> <nick> <vanilla particle effect (only for 'set')>";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length < 2)
        {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        String subcommand = args[0];
        String playername = args[1];

        if(subcommand.equals("remove"))
        {
            TagsMod.particles.remove(playername);
            TagsMod.particles.save();
            ChatComponentText text = new ChatComponentText("Removido com sucesso");
            text.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(text);
            return;
        }
        else if(!subcommand.equals("set"))
        {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }

        if(args.length < 3)
        {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }

        String particlename = args[2];

        boolean valid;
        try
        {
            EnumParticleTypes.valueOf(particlename);
            valid = true;
        }
        catch(Exception e) {valid = false;}

        if(!valid)
        {
            ChatComponentText text = new ChatComponentText("Invalid particle");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }

        TagsMod.particles.set(playername, particlename);
        TagsMod.particles.save();
        ChatComponentText text = new ChatComponentText("Adicionado com sucesso");
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
    public List addTabCompletionOptions(ICommandSender sender, final String[] args, BlockPos blockPos) {
        if(args == null) return null;
        if(args.length == 1)
        {
            return new ArrayList()
            {
                {
                    if("set".startsWith(args[0]))add("set");
                    if("remove".startsWith(args[0]))add("remove");
                }
            };
        }
        if(args.length == 2)
        {
            List ret = new ArrayList();
            for(Object o : Minecraft.getMinecraft().theWorld.playerEntities)
            {
                if(!(o instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer) o;
                String playername = player.getName();
                String typed = args[1];
                if(typed.length() > 0 && !playername.substring(0, typed.length()).equalsIgnoreCase(typed)) continue;
                ret.add(playername);
            }
            return ret;
        }
        if(args.length == 3)
        {
            String typed = args[2];
            List<String> ret = new ArrayList<>();
            Iterator<String> it = PARTICLES.iterator();
            while(it.hasNext())
            {
                String next = it.next();
                if(next.startsWith(typed)) ret.add(next);
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
