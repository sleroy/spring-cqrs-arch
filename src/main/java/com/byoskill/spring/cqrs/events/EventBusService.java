/*
 * Copyright (C) 2017 Sylvain Leroy - BYOSkill Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: sleroy at byoskill.com, or visit : www.byoskill.com
 *
 */
package com.byoskill.spring.cqrs.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;

/**
 * The Interface IEventBusService is a wrapper above the Spring Application
 * Event Publisher.
 */
@FunctionalInterface
public interface EventBusService {

    /**
     * Notify all <strong>matching</strong> listeners registered with this
     * application of an event.
     * <p>
     * If the specified {@code event} is not an {@link ApplicationEvent}, it is
     * wrapped in a {@link PayloadApplicationEvent}.
     *
     * @param event the event to publish
     * @see PayloadApplicationEvent
     * @since 4.2
     */
    void publishEvent(Object event);


}
