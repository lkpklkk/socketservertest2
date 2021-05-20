package com.company.broadcast_server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author lekeping
 */
public class UserWorker extends Thread {
    Socket userSocket;
    InputStream inputStream;
    OutputStream outputStream;
    Zone zone;
    String name;


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
                outputStream.write("your name plz:\n".getBytes(StandardCharsets.UTF_8));
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
                outputStream.write(("Connected\nType \"broadcast\" for sending a server broadcast, \"history\" for " +
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
        ArrayList<Message> messages = zone.getHistory();
        if (messages != null) {
            for (Message message : messages) {
                this.write(message);
            }
        } else {
            write(new Message("System", "history not init yet"));
        }
    }

    private void broadcast() throws IOException {
        String s;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream.write("Input broadcast msg below\n".getBytes(StandardCharsets.UTF_8));
        s = bufferedReader.readLine();
        for (UserWorker worker : zone.getUserWorkers()) {
            worker.write(new Message(name, s));
        }
        zone.addHistory(new Message(name,s));
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

    private void write(Message message) throws IOException {
        outputStream.write(message.toString().getBytes(StandardCharsets.UTF_8));
    }

}
