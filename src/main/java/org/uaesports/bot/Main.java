package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.commands.*;
import org.uaesports.bot.components.TestComponent;
import org.uaesports.bot.managers.cmds.CommandManager;
import org.uaesports.bot.managers.components.ComponentManager;

public class Main {
    public static void main(String[] args) {
        // Get token from environment variables and log in with it.
        var token = System.getenv("UADiscordBotToken");
        if (token == null) {
            System.err.println("TOKEN ENVIRONMENT VARIABLE NOT FOUND");
            return;
        }
        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        // Currently registered slash commands
        // The command info is stored on Discord's end, meaning the usage and description,
        // and then the bot created will act as a callback. This allows us to see what discord has registered
        // for commands currently on a global level. There are also server level slash commands.
        var commands = api.getGlobalSlashCommands().join();
        for (SlashCommand command : commands) {
            System.out.println(command.getName());
        }
        
        var manager = new CommandManager();
        
        manager.add(new Debug());
        manager.add(new Ping(api));
        manager.add(new Test());
        manager.add(new ExtraRoles());
        var custom = new Custom();
        manager.add(custom);
        
        var components = new ComponentManager();
        
        var testComponents = new TestComponent();
        custom.setComponents(testComponents.getComponents().toArray(new HighLevelComponent[0]));
        components.add(testComponents);
    
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction sci = event.getSlashCommandInteraction();
            manager.dispatch(sci);
        });
        api.addMessageComponentCreateListener(event -> {
            var interaction = event.getMessageComponentInteraction();
            components.handle(interaction);
        });
    }
}
