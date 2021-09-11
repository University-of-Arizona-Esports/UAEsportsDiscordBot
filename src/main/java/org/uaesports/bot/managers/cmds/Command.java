package org.uaesports.bot.managers.cmds;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.handlers.InteractionHandler;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class Command {
    
    private String name;
    private InteractionHandler handler;
    
    public Command() {
        var data = CommandData.read(getClass());
        name = data.getName();
        handler = data.buildHandler();
    }
    
    public final String getName() {
        return name;
    }
    
    public final void handleInteraction(SlashCommandInteraction sci) {
        handler.handle(sci, sci, this);
    }
    
    /**
     * Called when an exception occurs while parsing a parameter.
     * @param sci Interaction object.
     * @param provider Option for the current command (use one of the getByName methods with paramName on this object to get parameter values)
     * @param paramName Name of the parameter that failed.
     * @param cause The exception that was thrown while parsing the parameter.
     */
    public void onInvalidParameter(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, String paramName, Throwable cause) { }
    
    // Get the specific param from the command assuming that it exists
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static Object getParam(SlashCommandInteractionOptionsProvider provider, ParamInfo info) throws ExecutionException, InterruptedException {
        if (provider == null) return Optional.empty();
        var option = provider.getOptionByName(info.name());
        // If empty then the param is not required and was left out
        if (option.isEmpty()) return Optional.empty();
        var value = option.get();
        var type = info.type();
        Object o = null;
        // Try to get one of the valid param types
        if (type == String.class) o = value.getStringValue().get();
        else if (type == int.class || type == Integer.class) o = value.getIntValue().get();
        else if (type == boolean.class || type == Boolean.class) o = value.getBooleanValue().get();
        else if (type == User.class) o = value.requestUserValue().get().get();
        else if (type == ServerChannel.class) o = value.getChannelValue().get();
        else if (type == Role.class) o = value.getRoleValue().get();
        else if (type == Mentionable.class) o = value.requestMentionableValue().get().get();
        else if (Choice.class.isAssignableFrom(type)) {
            // Get corresponding enum
            var index = value.getIntValue().get();
            o = type.getEnumConstants()[index];
        }
        // Convert to optional if the parameter is optional
        if (!info.required()) o = Optional.ofNullable(o);
        return o;
    }
    
}
