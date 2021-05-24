package com.company.server;

import com.company.server.broadcast_modules.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lekeping
 */
public class UserWorker extends Thread {
    final int MAX_USER_INPUT_LENGTH = 200;
    final long BROADCAST_INTERVAL_MILI = 10000;
    Socket userSocket;
    InputStream inputStream;
    OutputStream outputStream;
    Zone zone;
    String name;
    long lastMsgTime = 0;


    public UserWorker(Socket userSocket, Zone zone) {
        this.userSocket = userSocket;
        this.zone = zone;
        try {
            this.outputStream = userSocket.getOutputStream();
            this.inputStream = userSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while (true) {

            try {
                outputStream.write("Connected\nYour name plz:\n".getBytes(StandardCharsets.UTF_8));
                line = reader.readLine();
                if (line.length() != 0 && line.length() < 10) {
                    name = line.trim();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {

            while (!"quit".equalsIgnoreCase(line)) {
                outputStream.write(("Type \"broadcast\" for sending a server broadcast, \"history\" for " +
                        "last 50 broadcasthistory\n \"echo\" for echo mode and \"quit\" to disconnect " +
                        "server\n").getBytes(StandardCharsets.UTF_8));
                line = reader.readLine();
                switch (line) {
                    case "broadcast":
                        broadcast();
                        break;
                    case "history":
                        getHistory();
                        break;
                    case "echo":
                        selfEcho();
                        break;
                    default:
                        break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        zone.removeWorker(this);
        this.close();
    }

    private void getHistory() throws IOException {
        List<Message> messages = zone.getHistory();
        if (messages != null) {
            for (Message message : messages) {
                this.write(message.toString());
            }
        } else {
            write(new Message("System", "history not init yet").toString());
        }
    }

    private void broadcast() throws IOException {
        if (lastMsgTime != 0) {
            if (System.currentTimeMillis() - lastMsgTime < BROADCAST_INTERVAL_MILI) {
                outputStream.write("plz wait for 10 sec until next broadcast".getBytes(StandardCharsets.UTF_8));
                return;
            }
        }
        String s;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream.write("Input broadcast msg below\n".getBytes(StandardCharsets.UTF_8));
        s = bufferedReader.readLine();
        if (s.length() > 0 && s.length() < MAX_USER_INPUT_LENGTH) {
            //msg successfully sent out from user

            lastMsgTime = System.currentTimeMillis();
            Message msg = new Message(name, s);
            zone.addMsgsQue(msg);
            zone.addHistory(msg);
        } else {
            outputStream.write((s.length() > 0) ? "msg too long\n".getBytes(StandardCharsets.UTF_8)
                    : "msg cant be empty\n".getBytes(StandardCharsets.UTF_8));
        }


    }

    private void close() {
        try {
            userSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selfEcho() {

        try {
            outputStream.write("echo mode started\n".getBytes(StandardCharsets.UTF_8));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if ("quit".equalsIgnoreCase(line)) {
                    break;
                }
                outputStream.write(("you entered " + line + "\n").getBytes(StandardCharsets.UTF_8));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes(StandardCharsets.UTF_8));
    }

}
