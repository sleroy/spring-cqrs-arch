package com.byoskill.spring.cqrs.gate.impl;

import java.util.Optional;

public interface SpringGateFilters {

    /**
     * Filter a command. The command may be returned modified or not returnd
     * (optional empty) in the case the command should be excluded.
     *
     * @param <T>     the generic type
     * @param command the command
     * @return the optional result of the filtering
     */
    <T> Optional<T> filterCommand(T command);

}
