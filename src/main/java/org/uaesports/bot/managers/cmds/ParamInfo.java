package org.uaesports.bot.managers.cmds;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

/**
 * Holds the information needed to get a parameter from an interaction.
 */
public class ParamInfo {
    
    private final String name;
    private final Class<?> type;
    private final boolean required;
    
    public ParamInfo(String name, Class<?> type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }
    
    public String name() {
        return name;
    }
    
    public Class<?> type() {
        return type;
    }
    
    public boolean required() {
        return required;
    }
    
    /**
     * Get the value of this parameter from an interaction. The instance is the {@link Command}
     * object that handles the current command.
     * The value this object should return is exactly the parameter value of the method
     * that will handle the command. That means if the parameter is an {@link java.util.Optional},
     * then the value obtained from the interaction needs to be wrapped in an Optional before
     * being returned.
     * @param provider The option that holds the parameters for the command.
     * @param instance Corresponding command instance.
     * @return Value of this parameter.
     */
    public Object getValue(SlashCommandInteractionOptionsProvider provider, Object instance)
            throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return Command.getParam(provider, this);
    }
    
}
