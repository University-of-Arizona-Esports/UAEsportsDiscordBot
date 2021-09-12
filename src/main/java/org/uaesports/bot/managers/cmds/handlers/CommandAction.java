package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.ParamInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

/**
 * The part of a command handler that executes the command itself.
 * Converts the slash command to parameters and executes the method.
 */
public class CommandAction implements InteractionHandler {
    
    private Method method;
    private ParamInfo[] params;
    
    /**
     * @param method Method that gets execute to handle this command.
     * @param params Array that holds information of how to fetch parameters
     * from an interaction.
     */
    public CommandAction(Method method, ParamInfo[] params) {
        this.method = method;
        this.params = params;
    }
    
    @Override
    public void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Command instance) {
        var args = new Object[params.length + 1];
        args[0] = sci;
        var i = 0;
        try {
            for (i = 0; i < params.length; i++) {
                args[i + 1] = params[i].getValue(provider, instance);
            }
        } catch (ExecutionException | IllegalAccessException | InterruptedException e) {
            // Failed to request data from discord, let the interaction fail
        } catch (InvocationTargetException e) {
            instance.onInvalidParameter(sci, provider, params[i].name(), e.getCause());
        }
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO this should only occur if our method throws an exception
        }
    }
    
}
