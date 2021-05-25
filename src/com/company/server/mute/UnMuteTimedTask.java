package com.company.server.mute;

import java.util.Map;

/**
 * @author lekeping
 */
public class UnMuteTimedTask implements Runnable {
    private final Map<Integer, Boolean> muteMap;
    private final int userId;

    public UnMuteTimedTask(Map<Integer, Boolean> muteMap, int userId) {
        this.muteMap = muteMap;
        this.userId = userId;
    }

    @Override
    public void run() {
        if (muteMap.containsKey(userId)) {
            muteMap.put(userId, false);
        }

    }
}
