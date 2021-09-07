package org.uaesports.bot.commands;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.uaesports.bot.managers.cmds.*;

import java.util.Optional;

@Name("debug")
@Description("The debug command")
public class Debug extends Command {
    
    public Debug() { }
    
    @Execute
    @Subcommand(name = "get", description = "Gets something")
    @Param(index = 0, name = "a", description = "A")
    @Param(index = 1, name = "b", description = "B")
    public void callback(SlashCommandInteraction sci, int a, Optional<Integer> b) {
        var msg = sci.createImmediateResponder()
                     .append("You typed ").append(a);
        b.ifPresent(i -> msg.append(" and " + b));
        msg.respond();
    }
    
}
