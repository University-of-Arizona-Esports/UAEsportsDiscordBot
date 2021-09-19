package org.uaesports.bot.managers.cmds;

import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    
    private Map<String, Command> commands = new HashMap<>();
    
    public void add(Command command) {
        commands.put(command.getName(), command);
    }
    
    public void add(CommandHandler handler) {
        add(new ExternalCommand(handler));
    }
    
    public void dispatch(SlashCommandInteraction sci) {
        var command = commands.getOrDefault(sci.getCommandName(), null);
        if (command == null) return; // Executed command we don't have a handler for
        command.handleInteraction(sci);
    }
    
}
