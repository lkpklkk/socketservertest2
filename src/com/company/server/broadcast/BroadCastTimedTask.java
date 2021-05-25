package com.company.server.broadcast;

import com.company.server.Zone;

import java.util.ArrayList;

/**
 * @author lekeping
 */
public class BroadCastTimedTask implements Runnable {
    final Zone zone;

    public BroadCastTimedTask(Zone zone) {
        this.zone = zone;
    }

    @Override
    public void run() {
        ArrayList<Message> curBroadCasting = zone.getMsgsQueAndClear();
        if (curBroadCasting.size() != 0) {
            zone.broadcast(curBroadCasting);
        }
    }


}
