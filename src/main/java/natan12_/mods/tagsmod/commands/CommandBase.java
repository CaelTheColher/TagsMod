package natan12_.mods.tagsmod.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public abstract class CommandBase implements ICommand
{
    @Override
    public String getCommandName() {
        return getName();
    }

    public abstract String getName();

    @Override
    public List<String> getCommandAliases() {
        return getAliases();
    }

    public abstract List<String> getAliases();

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        execute(sender, args);
    }

    public abstract void execute(ICommandSender sender, String[] args) throws CommandException;

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return canCommandSenderUse(iCommandSender) ;
    }

    public abstract boolean canCommandSenderUse(ICommandSender sender);

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
