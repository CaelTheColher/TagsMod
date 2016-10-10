package natan12_.mods.tagsmod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class AddCommand implements ICommand
{
    private static final List<String> aliases = new ArrayList<String>(){{add("ac_add");}};

    @Override
    public String getName()
    {
        return "autocommand_add";
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
            if(args == null || args.length < 2)
            {
                ChatComponentText text = new ChatComponentText("Invalid usage\n Usage: " + getCommandUsage(sender));
                text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
                sender.addChatMessage(text);
                return;
            }
            String IP = args[0];
            String cmd = "";
            for(int i = 1; i < args.length; i++)
                cmd += args[i] + " ";
            TagsMod.servers.set(IP, cmd);
            TagsMod.servers.save();
            ChatComponentText text = new ChatComponentText("[Command Sender] Adicionado com sucesso");
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
    public boolean isUsernameIndex(String[] strings, int i)
    {
        return false;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender)
    {
        return "autocommand_add <IP> <';' separated commands>";
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos)
    {
        if(strings.length > 1) return null;
        ArrayList<String> list = new ArrayList<>();
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
