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

public class TagCommand extends CommandBase {
    private static final List<String> aliases = new ArrayList<String>() {{
        add("ac_tag");
    }};

    @Override
    public String getName() {
        return "autocommand_tag";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/ac_tag <set|remove> <player name> <tag (only for 'set')>";
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if (!TagsMod.overrideChat) {
            ChatComponentText text = new ChatComponentText("Chat overriding is disabled in the config");
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        if (args == null || args.length < 2) {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        String option = args[0].toLowerCase();
        String nick = args[1];
        if (option.equals("remove")) {
            TagsMod.tags.remove(nick);
            TagsMod.tags.save();
            ChatComponentText text = new ChatComponentText("Removido com sucesso");
            text.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(text);
            return;
        }
        if (!option.equals("set")) {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        if (args.length < 3) {
            ChatComponentText text = new ChatComponentText("Invalid usage\nUsage: " + getCommandUsage(sender));
            text.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            sender.addChatMessage(text);
            return;
        }
        switch (nick) {
            case "natan12_":
            case "natan12_IntelliJ":
            case "roneyq123":
            case "BrubsCraft":
            case "lucaszainko":
            case "IlyBr":
            case "spootnd":
            case "pedrojamur":
            case "RumpeSan":
                if (sender.getName().equals(nick)) {
                    break;
                }
                ChatComponentText text = new ChatComponentText("A tag desse player n" + '\u00e3' + "o pode ser mudada");
                text.getChatStyle().setBold(true).setItalic(true).setColor(EnumChatFormatting.DARK_RED);
                sender.addChatMessage(text);
                return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) sb.append(" ");
        }
        String tag = sb.toString();
        TagsMod.tags.set(nick, tag);
        TagsMod.tags.save();
        ChatComponentText text = new ChatComponentText("Adicionado com sucesso");
        System.out.println("[TagsMod/Tags] " + nick + " -> " + tag);
        text.getChatStyle().setColor(EnumChatFormatting.GREEN);
        sender.addChatMessage(text);
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender iCommandSender) {
        boolean use;
        try {
            use = iCommandSender.getEntityWorld().isRemote;
        } catch (Exception e) {
            use = false;
        }
        return use;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos blockPos) {
        if (args == null) return null;
        if (args.length == 1) {
            final String typed = args[0].toLowerCase();
            return new ArrayList<String>() {
                {
                    if ("set".startsWith(typed)) add("set");
                    if ("remove".startsWith(typed)) add("remove");
                }
            };
        }
        if (args.length == 2) {
            List<String> ret = new ArrayList<>();
            String subcommand = args[0];
            String typed = args[1];
            if (subcommand.equals("remove")) {
                for (String s : TagsMod.tags.getKeys()) {
                    if (s.toLowerCase().startsWith(typed.toLowerCase())) ret.add(s);
                }
                return ret;
            }
            for (Object o : Minecraft.getMinecraft().theWorld.playerEntities) {
                if (!(o instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer) o;
                String playername = player.getName();
                if (typed.length() > 0 && !playername.substring(0, typed.length()).equalsIgnoreCase(typed)) continue;
                ret.add(playername);
            }
            return ret;
        }
        return null;
    }
}
