package org.uaesports.bot.commands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.Description;
import org.uaesports.bot.managers.cmds.annotations.Execute;
import org.uaesports.bot.managers.cmds.annotations.Name;
import org.uaesports.bot.managers.cmds.annotations.Param;

import java.util.Optional;

@Name("test")
@Description("A command used for testing, has no real purpose.")
public class Test extends Command {
    @Execute
    @Param(index = 0, name="input", description = "A random input string.")
    public void test(SlashCommandInteraction sci, Optional<String> input) {
        System.out.println(input);
        sci.createImmediateResponder()
                .setContent("Testing response, only the command user should see this.")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
    }
}
