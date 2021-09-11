package org.uaesports.bot.commands;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.*;

import java.util.Arrays;
import java.util.List;

@Name("custom")
@Description("Command that uses custom parameters")
public class Custom extends Command {
    
    @CustomParam
    public List<Integer> readList(String s) {
        return Arrays.stream(s.split(", *")).map(Integer::parseInt).toList();
    }
    
    @Override
    public void onInvalidParameter(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, String paramName, Throwable cause) {
        sci.createImmediateResponder()
           .append("Invalid format for parameter ").append(paramName)
           .respond();
    }
    
    @Execute
    @Param(index = 0, name = "numbers", description = "Comma separated numbers")
    public void call(SlashCommandInteraction sci, List<Integer> args) {
        sci.createImmediateResponder()
                .append("Sum: ").append(args.stream().mapToInt(value -> value).sum())
                .respond();
    }
    
}
