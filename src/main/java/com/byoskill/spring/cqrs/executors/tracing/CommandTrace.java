package com.byoskill.spring.cqrs.executors.tracing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.byoskill.spring.cqrs.gate.impl.TraceCommandExecution;

/**
 * The Class CommandTrace.
 */
public class CommandTrace implements Serializable {

    /** The commands. */
    List<TraceCommandExecution> commands = new ArrayList<>();

    /**
     * Adds the command.
     *
     * @param _command the command
     */
    public void addCommand(final TraceCommandExecution _command) {
	commands.add(_command);
    }


    /**
     * Gets the commands.
     *
     * @return the commands
     */
    public List<TraceCommandExecution> getCommands() {
	return commands;
    }

    /**
     * Sets the commands.
     *
     * @param _commands the new commands
     */
    public void setCommands(final List<TraceCommandExecution> _commands) {
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