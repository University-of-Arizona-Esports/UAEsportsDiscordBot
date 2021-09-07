package org.uaesports.bot;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles all commands related to managing, assigning, and removing roles.
 * This makes use of the MenuSelect component feature to allow users to select multiple roles from
 * a drop down list at a time, adding or removing them as necessary.
 */
public class Roles {
    /**
     * The entrypoint for the Roles handler class.
     */
    public Roles() {
        System.out.println("Loaded \"test\" class.");
    }

    /**
     * Handles the callback of the '/roles' command, which shows a list of available roles for a user to assign
     * to themselves. Roles selected from the list are added to the user if they do not have them, or removed
     * if they already have the role.
     *
     * TODO: Update this to use only the roles which are enabled for role reactions, rather than all roles.
     *
     * @param interaction An interaction interface for the message context that was used to trigger the slash command.
     */
    public void callbackRoles(SlashCommandInteraction interaction) {
        // Create the basic Select Menu dropdown for seeing roles
        var option = new SelectMenuBuilder()
                .setCustomId("roleMenu")
                .setPlaceholder("Select roles to add or remove.");

        // Get server information from the command interface
        var server = interaction.getServer();

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
        interaction.createImmediateResponder()
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

    /**
     * The callback handler for when roles are selected from the dropdown menu in the '/roles' command.
     * This is where roles are actually given or taken from the users.
     *
     * TODO: Actually give or take the roles from users. Kekw.
     *
     * @param interaction An interaction interface for the message context that was used to trigger the slash command.
     */
    public void callbackRoleMenu(MessageComponentInteraction interaction) {
        // Check that the interaction is a select menu (should always be true)
        interaction.asSelectMenuInteraction().ifPresent(context -> {
            // Make sure the menu was used in a server
            interaction.getServer().ifPresent(server -> {
                // Get the options selected by the user
                var chosen = context.getChosenOptions();
                String[] roles = new String[chosen.size()];
                AtomicInteger i = new AtomicInteger();
                // Get an array of roles for each role that the bot can get from the server that was selected.
                for (var option : chosen) {
                    System.out.println(option.getValue());
                    server.getRoleById(option.getValue()).ifPresent(role -> {
                        roles[i.get()] = role.getMentionTag();
                        i.getAndIncrement();
                    });
                }

                // Create a response to the user.
                interaction.createImmediateResponder()
                        // Only visible to user
                        .setFlags(MessageFlag.EPHEMERAL)
                        // Information about the roles selected.
                        .addEmbed(new EmbedBuilder()
                                .setTitle("Roles Added")
                                .setColor(Color.GREEN)
                                .setDescription("Lmao u thought. The roles weren't actually added, " +
                                        "but this at least confirms some sort of successful interaction.\n" +
                                        String.join("\n", roles))
                        )
                        .respond();
            });
        });
    }
}
