package com.company.server;

import com.company.server.mute.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * a simple terminal to display current connected clients, mute users and remove mute from users.
 *
 * @author lekeping
 */
public class Main {

    public static void main(String[] args) {

        int port = 5000;
        Zone zone = new Zone(port);
        zone.start();
        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("1 : display current users, 2 : mute user, 3 : disable guanLiYuan mode,");
            String line;
            int result;
            try {
                line = bufferedReader.readLine();
                result = Integer.parseInt(line);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("oops");
                continue;
            }
            if (result == 1) {
                zone.getUsers().forEach((User user) -> {
                    System.out.println("name " + user.getUserName());
                    System.out.println("userId " + user.getUserId());
                });
            } else if (result == 2) {
                System.out.println("mute" + zone.muteUser(IOUtils.getMuteDuration(), IOUtils.getMuteUser()));
            } else if (result == 3) {
                System.out.println("disabled");
                break;
            } else {
                break;
            }

        }


    }
}
