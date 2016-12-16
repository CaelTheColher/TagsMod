package natan12_.mods.tagsmod.commands;

import natan12_.mods.tagsmod.TagsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends CommandBase
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_remove");}};

    @Override
    public String getName()
    {
        return "autocommand_remove";
    }

    @Override
    public List getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        try
        {
            if(!sender.getEntityWorld().isRemote)
            {
                sender.addChatMessage(new ChatComponentText("Client-side only"));
                return;
            }
            if(args == null || args.length < 1)
            {
                ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
                text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
                sender.addChatMessage(text);
                return;
            }
            String IP = args[0];
            TagsMod.servers.remove(IP);
            TagsMod.servers.save();
            ChatComponentText text = new ChatComponentText("[Command Sender] Removido com sucesso");
            text.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(text);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ChatComponentText text = new ChatComponentText("Erro detectado, por favor poste seu log aqui");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            text.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forum.gamemods.com.br/resources/autocommand.142/"));
            sender.addChatMessage(text);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender)
    {
        return "autocommand_remove <IP>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] args, BlockPos blockPos)
    {
        if(args.length > 1) return null;
        ArrayList<String> list = new ArrayList<>();
        if(args.length == 1)
        {
            String typed = args[0].toLowerCase();
            for(String s : TagsMod.servers.getKeys())
            {
                if(s.toLowerCase().startsWith(typed)) list.add(s);
            }
        }
        else
            if(Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP != null)
                list.add(Minecraft.getMinecraft().getCurrentServerData().serverIP);
        return list;
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
