package org.uaesports.bot.managers.cmds;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public class ParamInfo {
    
    private final String name;
    private final Class<?> type;
    private final boolean required;
    
    public ParamInfo(String name, Class<?> type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }
    
    public String name() { return name; }
    
    public Class<?> type() { return type; }
    
    public boolean required() { return required; }
    
    public Object getValue(SlashCommandInteractionOptionsProvider provider, Object instance) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return Command.getParam(provider, this);
    }
    
}
