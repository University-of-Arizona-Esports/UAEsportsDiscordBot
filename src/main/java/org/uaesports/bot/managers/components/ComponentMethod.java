package org.uaesports.bot.managers.components;

import org.javacord.api.interaction.MessageComponentInteraction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Holds the method that gets executed when a component interaction is triggered.
 * Also contains the methods that can obtain custom parameters from the interaction.
 */
public class ComponentMethod {
    
    private ComponentHandler instance;
    private Method method;
    private Method[] paramConversions;
    
    public ComponentMethod(ComponentHandler instance, Method method, Method[] paramConversions) {
        this.instance = instance;
        this.method = method;
        this.paramConversions = paramConversions;
    }
    
    public void handle(MessageComponentInteraction interaction, String id) {
        var args = new Object[paramConversions.length + 1];
        args[0] = interaction;
        try {
            for (var i = 1; i < args.length; i++) {
                var conversion = paramConversions[i - 1];
                // id parameter is optional
                if (conversion.getParameterCount() == 2) {
                    args[i] = conversion.invoke(instance, interaction, id);
                }
                else {
                    args[i] = conversion.invoke(instance, interaction);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            // Forward exception to handler
            Throwable cause = e;
            if (e instanceof InvocationTargetException) cause = e.getCause();
            instance.onException(interaction, id, cause);
        }
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO We messed up, log it somewhere
        }
    }

}
