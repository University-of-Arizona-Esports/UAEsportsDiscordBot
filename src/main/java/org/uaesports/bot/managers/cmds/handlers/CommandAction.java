package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.ParamInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The part of a command handler that executes the command itself.
 * Converts the slash command to parameters and executes the method.
 */
public class CommandAction implements InteractionHandler {
    
    private Method method;
    private ParamInfo[] params;
    
    public CommandAction(Method method, ParamInfo[] params) {
        this.method = method;
        this.params = params;
    }
    
    @Override
    public void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Object instance) {
        var args = new Object[params.length + 1];
        args[0] = sci;
        for (var i = 0; i < params.length; i++) {
            args[i + 1] = Command.getParam(provider, params[i]);
        }
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO log/notify that there was an error
        }
    }
    
}
