package org.uaesports.bot.managers.cmds;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.*;
import org.uaesports.bot.managers.cmds.annotations.*;
import org.uaesports.bot.managers.cmds.handlers.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Stores command data from the annotations in object form.
 */
public class CommandData {
    
    private Class<?> type;
    private String name;
    private String description;
    private List<Option> options;
    
    private Map<Type, ParamReader> customParams;
    
    private CommandData() {
        options = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<Option> getOptions() {
        return options;
    }
    
    private Map<Type, ParamReader> getCustomParams() {
        if (customParams != null) return customParams;
        return customParams = new HashMap<>();
    }
    
    public boolean isGlobal() {
        return !type.isAnnotationPresent(ForServer.class);
    }
    
    // Get the server for this command
    public Optional<Server> getServer(DiscordApi api) {
        var serverInfo = type.getAnnotation(ForServer.class);
        return Optional.ofNullable(serverInfo).flatMap(info -> api.getServerById(info.value()));
    }
    
    // Get the actual server object
    private Server requireServer(DiscordApi api) {
        return getServer(api).orElseThrow(() -> new IllegalArgumentException("Could not get server for command."));
    }
    
    // Get the command already registered in discord
    // Gets by matching name
    public Optional<SlashCommand> getCommand(DiscordApi api) {
        var commands = isGlobal() ? api.getGlobalSlashCommands().join() : api.getServerSlashCommands(requireServer(api)).join();
        return commands.stream()
                       .filter(slashCommand -> slashCommand.getName().equals(name))
                       .findFirst();
    }
    
    // Add command to discord and update its permissions
    public CompletableFuture<ServerSlashCommandPermissions> overwriteAndUpdatePermissions(DiscordApi api) {
        return addToDiscord(api).thenCompose(slashCommand -> updatePermissions(api, slashCommand));
    }
    
    // Add a command to discord
    public CompletableFuture<SlashCommand> addToDiscord(DiscordApi api) {
        var cmd = buildSlashCommand();
        if (isGlobal()) return cmd.createGlobal(api);
        else return cmd.createForServer(requireServer(api));
    }
    
    // Delete this command
    // Deletes by name
    public CompletableFuture<Void> deleteCommand(DiscordApi api) {
        var slashCommand = getCommand(api).orElseThrow(() -> new IllegalArgumentException("Registered command not found in discord."));
        if (isGlobal()) return slashCommand.deleteGlobal();
        else return slashCommand.deleteForServer(requireServer(api));
    }
    
    public CompletableFuture<ServerSlashCommandPermissions> updatePermissions(DiscordApi api) {
        var cmd = getCommand(api).orElseThrow(() -> new IllegalArgumentException("Command is not registered."));
        return updatePermissions(api, cmd);
    }
    
    public CompletableFuture<ServerSlashCommandPermissions> updatePermissions(DiscordApi api, SlashCommand command) {
        var server = getServer(api).orElseThrow(() -> new IllegalArgumentException("Updating permissions requires @ForServer attribute."));
        var updater = new SlashCommandPermissionsUpdater(server);
        var hasPermissions = false;
        for (EnableRole enableRole : type.getAnnotationsByType(EnableRole.class)) {
            updater.addPermission(enableRole.value(), SlashCommandPermissionType.ROLE, true);
            hasPermissions = true;
        }
        for (DisableRole disableRole : type.getAnnotationsByType(DisableRole.class)) {
            updater.addPermission(disableRole.value(), SlashCommandPermissionType.ROLE, false);
            hasPermissions = true;
        }
        if (hasPermissions) return updater.update(command.getId());
        else return CompletableFuture.completedFuture(null);
    }
    
    // Convert the command data into a slash command for discord.
    public SlashCommandBuilder buildSlashCommand() {
        var builder = new SlashCommandBuilder()
                .setName(name)
                .setDescription(description);
        var defaultPermission = type.getAnnotation(DefaultPermission.class);
        if (defaultPermission != null) {
            builder.setDefaultPermission(defaultPermission.value());
        }
        // Special case: Executing the base command
        if (options.size() == 1 && options.get(0).name() == null) {
            for (Option option : ((SubcommandOption) options.get(0)).options()) {
                builder.addOption(option.build());
            }
        }
        else {
            for (Option option : options) {
                builder.addOption(option.build());
            }
        }
        return builder;
    }
    
    // Convert the command data into a handler for execution.
    public InteractionHandler buildHandler() {
        if (options.size() == 1 && options.get(0).name() == null) {
            var sub = (SubcommandOption) options.get(0);
            return new CommandAction(sub.method(), getParamInfo(sub));
        }
        else {
            var handlers = options.stream()
                                  .map(this::getHandler)
                                  .map(NamedInteractionHandler.class::cast)
                                  .toList();
            return new GroupHandler(null, handlers);
        }
    }
    
    public GroupOption getGroup(String name, String description) {
        return options.stream()
                      .filter(option -> option instanceof GroupOption g && g.name().equals(name))
                      .findFirst()
                      .map(GroupOption.class::cast)
                      .orElseGet(() -> new GroupOption(name, description, new ArrayList<>()));
    }
    
    private InteractionHandler getHandler(Option option) {
        if (option instanceof GroupOption group) {
            var subcommands = group.options()
                                   .stream()
                                   .map(this::getHandler)
                                   .map(NamedInteractionHandler.class::cast)
                                   .toList();
            return new GroupHandler(group.name(), subcommands);
        }
        else if (option instanceof SubcommandOption sub) {
            var params = getParamInfo(sub);
            var action = new CommandAction(sub.method(), params);
            return new SubcommandAction(sub.name(), action);
        }
        return null;
    }
    
    private ParamInfo[] getParamInfo(SubcommandOption sub) {
        return sub.options()
                  .stream()
                  .map(Parameter.class::cast)
                  .map(parameter -> {
                      if (parameter.reader() != null) {
                          return new CustomParamInfo(parameter.name(), parameter.paramType(), parameter.required(), parameter.reader());
                      }
                      else {
                          return new ParamInfo(parameter.name(), parameter.paramType(), parameter.required());
                      }
                  })
                  .toArray(ParamInfo[]::new);
    }
    
    // Read the annotations from a command class into an object
    public static CommandData read(Class<? extends CommandHandler> type) {
        var data = new CommandData();
        data.type = type;
        readCustomParameters(data, type);
        Name name = type.getAnnotation(Name.class);
        if (name == null) throw new IllegalArgumentException("Command is missing @Name attribute.");
        Description description = type.getAnnotation(Description.class);
        if (description == null) throw new IllegalArgumentException("Command is missing @Description attribute.");
        data.name = name.value();
        data.description = description.value();
        Arrays.stream(type.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(Execute.class))
              .forEach(method -> readMethod(data, method));
        if (data.options.size() == 0) throw new IllegalArgumentException("Command has no methods marked @Execute.");
        return data;
    }
    
    // Read the annotations on a method and add it to the command data
    private static void readMethod(CommandData data, Method method) {
        var group = method.getAnnotation(Group.class);
        var subcommand = method.getAnnotation(Subcommand.class);
        var params = readParameters(data, method);
        // Group -> Subcommand
        if (group != null) {
            if (subcommand == null)
                throw new IllegalArgumentException("Method has @Group but missing @Subcommand. Groups are like folders for subcommands.");
            if (data.options.stream().anyMatch(option -> option instanceof SubcommandOption s && s.name == null)) {
                throw new IllegalArgumentException("Cannot have both groups/subcommands and execute the base command.");
            }
            var sub = new SubcommandOption(subcommand.name(), subcommand.description(), params, method);
            var g = data.getGroup(group.name(), group.description());
            if (g.options().stream().anyMatch(option -> option.name().equals(sub.name()))) {
                throw new IllegalArgumentException("Duplicate subcommand in group.");
            }
            g.options().add(sub);
        }
        // Subcommand
        else if (subcommand != null) {
            if (data.options.stream().anyMatch(option -> option instanceof SubcommandOption s && s.name == null)) {
                throw new IllegalArgumentException("Cannot have both groups/subcommands and execute the base command.");
            }
            var sub = new SubcommandOption(subcommand.name(), subcommand.description(), params, method);
            if (data.options.stream().anyMatch(option -> option.name().equals(sub.name()))) {
                throw new IllegalArgumentException("Duplicate group/subcommand name.");
            }
            data.options.add(sub);
        }
        // Base command
        else {
            if (data.options.size() > 0) {
                throw new IllegalArgumentException("Cannot have both groups/subcommands and execute the base command.");
            }
            var cmd = new SubcommandOption(null, null, params, method);
            data.options.add(cmd);
        }
    }
    
    // Read the method's parameter list into an option list
    private static List<Option> readParameters(CommandData data, Method method) {
        var types = method.getParameterTypes();
        if (types.length < 1 || types[0] != SlashCommandInteraction.class) {
            throw new IllegalArgumentException("First parameter of method must be SlashCommandInteraction.");
        }
        var paramInfo = Arrays.stream(method.getAnnotationsByType(Param.class))
                              .sorted(Comparator.comparingInt(Param::index))
                              .toArray(Param[]::new);
        var paramTypes = method.getGenericParameterTypes();
        if (paramTypes.length - 1 != paramInfo.length) {
            throw new IllegalArgumentException("Method should have one @Param for each command parameter.");
        }
        var result = new ArrayList<Option>();
        for (var i = 1; i < paramTypes.length; i++) {
            var info = paramInfo[i - 1];
            if (info.index() != i - 1) {
                throw new IllegalArgumentException("@Param index mismatch. Expecting " + (i - 1) + ", found " + info.index() + ".");
            }
            // Check if param is required
            var optional = paramTypes[i] instanceof ParameterizedType pt && pt.getRawType() == Optional.class;
            if (!optional && result.stream().anyMatch(option -> option instanceof Parameter p && !p.required())) {
                throw new IllegalArgumentException("Required parameters must come before optional parameters.");
            }
            // Check if the type is valid
            var paramClass = optional ? ((ParameterizedType) paramTypes[i]).getActualTypeArguments()[0] : paramTypes[i];
            if (!(paramClass instanceof Class c) || Parameter.getType(c) == null) {
                var customs = data.getCustomParams();
                if (!customs.containsKey(paramClass)) throw new IllegalArgumentException("Custom parameter type not defined: " + paramClass.getTypeName());
                var customParam = customs.get(paramClass);
                result.add(new Parameter(info.name(), info.description(), !optional, customParam.getDiscordType(), customParam.getMethod()));
            }
            else if (c.isEnum() && c.getEnumConstants().length > 25) {
                throw new IllegalArgumentException("Choices may have up to 25 options.");
            }
            else {
                result.add(new Parameter(info.name(), info.description(), !optional, c, null));
            }
        }
        return result;
    }
    
    private static void readCustomParameters(CommandData data, Class<?> type) {
        Arrays.stream(type.getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(CustomParam.class))
              .forEach(method -> addCustomParam(data, method));
    }
    
    private static void addCustomParam(CommandData data, Method method) {
        var customs = data.getCustomParams();
        var params = method.getGenericParameterTypes();
        if (params.length != 1) throw new IllegalArgumentException("Custom parameter parser should have a single argument.");
        var input = params[0];
        if (!(input instanceof Class<?> c) || Parameter.getType(c) == null) {
            throw new IllegalArgumentException("Custom parameter input should be an existing Discord parameter type.");
        }
        var output = method.getGenericReturnType();
        if (output == Void.class) throw new IllegalArgumentException("Custom parameter parser cannot return void");
        if (customs.containsKey(output)) throw new IllegalArgumentException("Duplicate custom parameter type " + output.getTypeName());
        customs.put(output, new ParamReader(c, output, method));
    }
    
    /**
     * Object form of a slash command option
     */
    public interface Option {
        
        String name();
        
        String description();
        
        SlashCommandOptionType type();
        
        SlashCommandOption build();
        
    }
    
    /**
     * An option that contains other options, such as subcommands or groups.
     */
    public interface UpperOption extends Option {
        
        List<Option> options();
        
        default List<SlashCommandOption> buildOptions() {
            return options().stream().map(Option::build).toList();
        }
        
        default SlashCommandOption build() {
            return SlashCommandOption.createWithOptions(type(), name(), description(), buildOptions());
        }
        
    }
    
    /**
     * A subcommand group or "folder" as discord says.
     */
    public record GroupOption(String name, String description, List<Option> options) implements UpperOption {
        @Override
        public SlashCommandOptionType type() {
            return SlashCommandOptionType.SUB_COMMAND_GROUP;
        }
        
    }
    
    /**
     * A subcommand in the command
     */
    public record SubcommandOption(String name, String description, List<Option> options,
                                   Method method) implements UpperOption {
        @Override
        public SlashCommandOptionType type() {
            return SlashCommandOptionType.SUB_COMMAND;
        }
        
    }
    
    /**
     * Any parameter for a command.
     *
     * @param paramType The real type of the parameter. If the parameter is Optional&lt;String&gt;,
     * this value should be String. For a choice parameter, this is the enum type.
     */
    public record Parameter(String name, String description, boolean required, Class<?> paramType, Method reader) implements Option {
        
        /**
         * Assuming the type is a choice (enum), get all the values.
         */
        public List<SlashCommandOptionChoice> getChoices() {
            return Arrays.stream(paramType.getEnumConstants())
                         .map(Choice.class::cast)
                         .map(choice -> SlashCommandOptionChoice.create(choice.getName(), choice.getValue()))
                         .toList();
        }
        
        public static boolean isChoice(Class<?> paramType) {
            return paramType.isEnum() && Choice.class.isAssignableFrom(paramType);
        }
        
        public static SlashCommandOptionType getType(Class<?> paramType) {
            if (paramType == String.class) return SlashCommandOptionType.STRING;
            else if (paramType == int.class || paramType == Integer.class) return SlashCommandOptionType.INTEGER;
            else if (paramType == boolean.class || paramType == Boolean.class) return SlashCommandOptionType.BOOLEAN;
            else if (paramType == User.class) return SlashCommandOptionType.USER;
            else if (paramType == ServerChannel.class) return SlashCommandOptionType.CHANNEL;
            else if (paramType == Role.class) return SlashCommandOptionType.ROLE;
            else if (paramType == Mentionable.class) return SlashCommandOptionType.MENTIONABLE;
            else if (isChoice(paramType)) return SlashCommandOptionType.INTEGER;
            else return null;
        }
        
        @Override
        public SlashCommandOptionType type() {
            var type = getType(paramType);
            if (type == null) throw new IllegalArgumentException();
            return type;
        }
        
        @Override
        public SlashCommandOption build() {
            if (isChoice(paramType)) {
                return SlashCommandOption.createWithChoices(SlashCommandOptionType.INTEGER, name, description, required, getChoices());
            }
            return SlashCommandOption.create(type(), name, description, required);
        }
        
    }
    
}
