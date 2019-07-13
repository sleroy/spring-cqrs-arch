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
package com.byoskill.spring.cqrs.api;

public interface EventThrower<C> {

    /**
     * Event triggred in cqse of failure.
     *
     * @param failure the failure
     * @return the event that should be thrown (null does not send event)
     */
    Object eventOnFailure(Throwable failure);

    /**
     * Event triggered on success.
     *
     * @param result the result
     * @return the event that should be thrown (null does not send event)
     */
    Object eventOnSuccess(C result);
}
