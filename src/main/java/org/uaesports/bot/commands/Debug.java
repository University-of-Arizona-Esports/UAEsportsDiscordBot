package org.uaesports.bot.commands;

import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.*;

import java.util.Optional;

@Name("debug")
@Description("The debug command")
@ForServer(737843314136449145L)
@DefaultPermission(false)
@EnableRole(737860544236748901L)
public class Debug extends Command {
    
    @Execute
    @Subcommand(name = "ping", description = "Debug ping command")
    @Param(index = 0, name = "user", description = "Specific user to pong")
    public void ping(SlashCommandInteraction sci, Optional<User> user) {
        var response = sci.createImmediateResponder()
                      .append("Pong");
        user.ifPresent(u -> response.append(" ").append(u));
        response.respond();
    }
    
    @Execute
    @Subcommand(name = "private", description = "Send a message visible only the command user sees.")
    public void privateMessage(SlashCommandInteraction sci) {
        sci.createImmediateResponder()
                .setFlags(MessageFlag.EPHEMERAL)
                .append("Private message uwu")
                .respond();
    }
    
    @Execute
    @Subcommand(name = "poke", description = "Poke someone")
    @Param(index = 0, name = "user", description = "Who to poke")
    @Param(index = 1, name = "times", description = "How many times to poke?")
    public void poke(SlashCommandInteraction sci, User user, Optional<Integer> times) {
        if (times.isPresent()) {
            if (times.get() == 0) {
                sci.createImmediateResponder()
                        .setFlags(MessageFlag.EPHEMERAL)
                        .append("Nothing happened...", MessageDecoration.ITALICS)
                        .respond();
                return;
            }
            else if (times.get() < 0) {
                sci.createImmediateResponder()
                   .setFlags(MessageFlag.EPHEMERAL)
                   .append("You can't poke that many times.")
                   .respond();
                return;
            }
        }
        var response = sci.createImmediateResponder()
                .append("Poke ").append(user);
        times.ifPresent(i -> {
            response.append(" " + i + " time");
            if (i != 1) response.append("s");
        });
        response.respond();
    }
    
}
