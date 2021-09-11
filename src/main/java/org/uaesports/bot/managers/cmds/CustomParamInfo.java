package org.uaesports.bot.managers.cmds;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CustomParamInfo extends ParamInfo {
    
    private Method method;
    
    public CustomParamInfo(String name, Class<?> type, boolean required, Method method) {
        super(name, type, required);
        this.method = method;
    }
    
    public Method getMethod() {
        return method;
    }
    
    @Override
    public Object getValue(SlashCommandInteractionOptionsProvider provider, Object instance)
            throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException {
        var discordValue = super.getValue(provider, instance);
        if (discordValue instanceof Optional o) {
            if (o.isPresent()) return Optional.ofNullable(method.invoke(instance, o.get()));
            return o;
        }
        return method.invoke(instance, discordValue);
    }
    
}
