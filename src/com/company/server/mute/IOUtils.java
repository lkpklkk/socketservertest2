package com.company.server.mute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lekeping
 */
public class IOUtils {


    public static int getMuteUser() {
        String line;
        int input;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("enter userId of the user u want to mute, \"quit\" for abort");
            try {
                line = bufferedReader.readLine();
                if ("quit".equalsIgnoreCase(line)) {
                    return -1;
                }
                input = Integer.parseInt(line);
                return input;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static MuteDuration getMuteDuration() {
        String line;
        int input;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("enter a duration of the mute\n" +
                    "1 : One day, 2 : Seven Day, 3 : Infinite, 4 : abort\n");
            try {
                line = bufferedReader.readLine();
                input = Integer.parseInt(line);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("something went wrong");
                continue;
            }
            switch (input) {
                case 1:
                    return MuteDuration.ONEDAY;
                case 2:
                    return MuteDuration.SEVENDAY;
                case 3:
                    return MuteDuration.INFINITE;
                default:
                    return null;
            }

        }
    }
}
