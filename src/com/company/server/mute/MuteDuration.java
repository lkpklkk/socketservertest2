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
    ONEDAY,
    /**
     * last seven day
     */
    SEVENDAY,
    /**
     * last until server shutdown
     */
    INFINITE
}
