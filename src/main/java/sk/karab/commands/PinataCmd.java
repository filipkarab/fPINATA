package sk.karab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import sk.karab.commands.subcmds.DebugSubCMD;
import sk.karab.commands.subcmds.HelpSubCMD;
import sk.karab.commands.subcmds.LocationSubCMD;
import sk.karab.commands.subcmds.SpawnPinataSubCMD;

import java.util.ArrayList;
import java.util.List;

public class PinataCmd implements TabExecutor {


    private final ArrayList<ISubCommand> subCommands;


    public PinataCmd() {
        subCommands = new ArrayList<>();

        subCommands.add(new HelpSubCMD());
        subCommands.add(new SpawnPinataSubCMD());
        subCommands.add(new DebugSubCMD());
        subCommands.add(new LocationSubCMD());
    }


    private ISubCommand getSubCommand(String arg) {

        for (ISubCommand subCommand : subCommands)
            if (subCommand.getSubArgument().equalsIgnoreCase(arg))
                return subCommand;

        return null;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        ISubCommand subCommand;

        if (args.length < 1) subCommand = getSubCommand("help");
        else subCommand = getSubCommand(args[0]);

        if (subCommand == null) subCommand = getSubCommand("help");
        assert subCommand != null;

        subCommand.execute(sender, args);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> suggestions;

        if (args.length < 1) suggestions = getAllSubCommandArgs();
        else {
            ISubCommand subCommand = getSubCommand(args[0]);
            if (subCommand == null) suggestions = getAllSubCommandArgs();
            else suggestions = subCommand.complete(sender, args);
        }

        ArrayList<String> approvedSuggestions = new ArrayList<>();
        suggestions.forEach((suggestion) -> {
            if (suggestion.startsWith(args[args.length - 1])) approvedSuggestions.add(suggestion);
        });

        return approvedSuggestions;
    }


    private ArrayList<String> getAllSubCommandArgs() {
        ArrayList<String> result = new ArrayList<>();
        subCommands.forEach((subCommand) -> result.add(subCommand.getSubArgument()));
        return result;
    }


}
