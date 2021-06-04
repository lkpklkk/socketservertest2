package com.company.server.mute;

/**
 * duration of which mute lasts
 *
 * @author lekeping
 */

public enum MuteDuration {
    /**
     * last one day
     */
    ONE_DAY,
    /**
     * last seven day
     */
    SEVEN_DAY,
    /**
     * last until server shutdown
     */
    INFINITE
}
