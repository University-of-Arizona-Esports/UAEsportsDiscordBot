package org.uaesports.bot.managers.cmds;

/**
 * Delegates command interactions to a separate command handler instance.
 */
public class ExternalCommand extends Command {
    
    private CommandHandler instance;
    
    public ExternalCommand(CommandHandler instance) {
        super(false);
        this.instance = instance;
        var data = CommandData.read(instance.getClass());
        setName(data.getName());
        setHandler(data.buildHandler());
    }
    
    @Override
    public CommandHandler getHandlerInstance() {
        return instance;
    }
    
}
