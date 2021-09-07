# UA Esports Discord Bot

The official repository for the Java rewrite of the original UA Esports Discord bot, written in Python. This project is created and maintaing by University of Arizona students, for the Wildcat Gaming and UA Esports Discord servers.

---

# Setup
Setup instructions to run the project yourself.

1) Install [Java 16+](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)
1) Clone the repository
1) In IntelliJ, open the cloned folder. This should automatically detect the Gradle sources and build wrapper
1) Create a Configuration for the [UpdateCommands](src/main/java/org/uaesports/bot/UpdateCommands.java) and [Main](src/main/java/org/uaesports/bot/Main.java) classes
1) Make sure that the configurations have the `UADiscordBotToken` environment variables set to the bot's token value (DM `Heroicos_HM#0310` to get this if you need)
1) You can also use your own bot token and replace the esports bot token in environment variables
1) If you replaced the token with your own, you need to run the `UpdateCommands` configuration you made in order to register the global commands with Discord (this may take up to an hour to properly register/update)
1) Stop the `UpdateCommands` configuration if it is running, then run the `Main` configuration to turn the bot on

---

# Contributing
Guidelines/notes for making contributions to the project.

- All changes must be submitted through a pull request on a new branch
- Try to make sure that all new updates are commented and follow similar structure to the rest of the project
- Whenever you change the commands/add new ones, you must run the `UpdateCommands` configuration *once* in order for Discord to register the updates. This is purely for command names and descriptions, the callback functions are handled as part of the `Main` application and do not require the `UpdateCommands` configuration to be run

---

# TODO

 The list of planned features for the bot, listed in order of relative priority.
 
 - [ ] Role Reactions
 - [ ] Simple moderation commands (Kick, Ban, Mute)
 - [ ] Will add more to this list I just want to get this posted and am too lazy to think of things off of the top of my head rn. Feel free to suggest things.
