package org.uaesports.bot.managers.cmds;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ParamReader {
    
    private Class<?> discordType;
    private Type outputType;
    private Method method;
    
    public ParamReader(Class<?> discordType, Type outputType, Method method) {
        this.discordType = discordType;
        this.outputType = outputType;
        this.method = method;
    }
    
    public Class<?> getDiscordType() {
        return discordType;
    }
    
    public Type getOutputType() {
        return outputType;
    }
    
    public Method getMethod() {
        return method;
    }
    
}
