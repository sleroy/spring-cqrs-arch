package com.byoskill.spring.cqrs.gate.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CommandTrace.
 */
public class CommandTrace implements Serializable {

    /** The commands. */
    List<CommandExecution> commands = new ArrayList<>();

    /**
     * Adds the command.
     *
     * @param _command the command
     */
    public void addCommand(final CommandExecution _command) {
	commands.add(_command);
    }


    /**
     * Gets the commands.
     *
     * @return the commands
     */
    public List<CommandExecution> getCommands() {
	return commands;
    }

    /**
     * Sets the commands.
     *
     * @param _commands the new commands
     */
    public void setCommands(final List<CommandExecution> _commands) {
	commands = _commands;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "CommandTrace [commands=" + commands + "]";
    }
}