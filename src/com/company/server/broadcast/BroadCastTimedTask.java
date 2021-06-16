package com.company.server.broadcast;

import com.company.server.Zone;

import java.util.ArrayList;

/**
 * Broadcast task scheduled with a fixed rate
 *
 * @author lekeping
 */
public class BroadCastTimedTask implements Runnable {
    final Zone zone;

    public BroadCastTimedTask(Zone zone) {
        this.zone = zone;
    }

    @Override
    public void run() {
        ArrayList<Message> curBroadCasting = zone.getMsgQueAndClear();
        if (curBroadCasting.size() != 0) {
            zone.broadcast(curBroadCasting);
        }
    }


}
