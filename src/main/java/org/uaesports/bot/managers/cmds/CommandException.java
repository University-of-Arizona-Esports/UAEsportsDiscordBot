package org.uaesports.bot.managers.cmds;

/**
 * Exception when there was a problem executing a command.
 */
public class CommandException extends Exception {
    
    public CommandException() { }
    
    public CommandException(String message) {
        super(message);
    }
    
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CommandException(Throwable cause) {
        super(cause);
    }
    
}
