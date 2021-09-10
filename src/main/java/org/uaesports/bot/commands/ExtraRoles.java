package org.uaesports.bot.commands;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOptionBuilder;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.util.DiscordRegexPattern;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.*;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

@Name("roles")
@Description("Commands for getting roles for yourself.")
public class ExtraRoles extends Command {
    @Execute
    @Subcommand(name = "get", description = "Display a menu for selecting roles to assign to yourself.")
    public void roles(SlashCommandInteraction sci) {
        var option = new SelectMenuBuilder()
                .setCustomId("roleMenu")
                .setPlaceholder("Select roles to add or remove.");

        // Get server information from the command interface
        var server = sci.getServer();

        // Keeping track of the maximum number of options (roles) that a user will select at a time
        // This will allow users to select multiple roles without limiting their selection numbers
        AtomicInteger maxOptions = new AtomicInteger(0);

        // Checking if the server exists
        server.ifPresent(guild -> {
            // Getting all roles in the server, and iterating through them
            var roles = guild.getRoles();
            for (var role : roles) {
                // If the role is not the @everyone role, then add it as an option to the response\
                if (!role.getName().equals("@everyone")) {
                    option.addOption(new SelectMenuOptionBuilder()
                            .setLabel(role.getName())
                            .setValue(role.getIdAsString())
                            .build()
                    );
                    maxOptions.getAndIncrement();
                }
            }
        });

        // Set the selection limit for items, and build the menu object
        var menu = option
                .setMaximumValues(maxOptions.get())
                .build();

        // Create a response message to the original command
        sci.createImmediateResponder()
                // Make the response only visible to the user who used the command
                .setFlags(MessageFlag.EPHEMERAL)
                // Add the role selection menu
                .addComponents(ActionRow.of(menu))
                // Add a small descriptor message
                .addEmbed(new EmbedBuilder()
                        .setTitle("Select a Role to Add")
                        .setDescription("Select the role that you wish to get from the dropdown below.")
                        .setColor(Color.MAGENTA)
                )
                .respond();
    }

    @Execute
    @Subcommand(name = "add", description = "Add roles to the list of self-assignable roles.")
    @Param(index = 0, name = "role", description = "The role to add.")
    @Param(index = 1, name = "emoji", description = "The emoji to connect the role with.")
    public void addRoles(SlashCommandInteraction sci, Role role, String emoji) {
        System.out.println(emoji);
        var menuBuilder = new SelectMenuBuilder()
                .setCustomId("roleAddMenu")
                .setPlaceholder("Select roles to add to be self assignable.");

        var m = DiscordRegexPattern.CUSTOM_EMOJI.matcher(emoji);
        if (m.find()) {
            for (var i = 1; i <= m.groupCount(); i++) {
                System.out.printf("%d - %s\n", i, m.group(i));
            }

        }


        menuBuilder.addOption(new SelectMenuOptionBuilder()
                .setLabel(role.getName())
                .setValue(role.getIdAsString())
                .build()
        );

        var menu = menuBuilder.build();

        sci.createImmediateResponder()
                .setFlags(MessageFlag.EPHEMERAL)
                .addEmbed(new EmbedBuilder()
                        .setTitle("Role Example")
                        .setDescription("Just to show an example to make sure that the arguments worked.")
                        .setColor(new Color(0, 51, 102, 1))
                )
                .addComponents(ActionRow.of(menu))
                .respond();
    }

    @Execute
    @Subcommand(name = "remove", description = "Remove roles from the list of self-assignable roles.")
    public void removeRoles(SlashCommandInteraction sci) {
        var isServer = sci.getServer().isPresent();
        if (isServer) {
            var menuBuilder = new SelectMenuBuilder()
                    .setCustomId("roleAddMenu")
                    .setPlaceholder("Select roles to add to be self assignable.");

            var server = sci.getServer().get();
            AtomicInteger maxOptions = new AtomicInteger(0);

            var roles = server.getRoles();
            for (var role : roles) {
                // If the role is not the @everyone role, then add it as an option to the response\
                if (!role.getName().equals("@everyone")) {
                    menuBuilder.addOption(new SelectMenuOptionBuilder()
                            .setLabel(role.getName())
                            .setValue(role.getIdAsString())
                            .build()
                    );
                    maxOptions.getAndIncrement();
                }
            }

            var menu = menuBuilder
                    .setMaximumValues(maxOptions.get())
                    .build();

            sci.createImmediateResponder()
                    .setFlags(MessageFlag.EPHEMERAL)
                    .addComponents(ActionRow.of(menu))
                    .respond();
        } else {
            sci.createImmediateResponder()
                    .setFlags(MessageFlag.EPHEMERAL)
                    .addEmbed(new EmbedBuilder()
                            .setTitle("Server Only")
                            .setDescription("This command can only be used in a server.")
                            .setColor(new Color(0, 51, 102, 1))
                    )
                    .respond();
        }
    }
}
