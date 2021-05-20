package com.company.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author lekeping
 */
public class UserWorker extends Thread {
    Socket userSocket;
    InputStream inputStream;
    OutputStream outputStream;
    Zone zone;


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
        try {
            outputStream.write("Connected\nType \"quit\" to exit".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!"quit".equalsIgnoreCase(line)) {
            try {

                String[] cmd = line.split(" ");
                if ("broadcast".equalsIgnoreCase(cmd[0])) {
                    this.broadcast();
                } else if ("echo".equalsIgnoreCase(cmd[0])) {
                    this.selfEcho();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        this.close();
    }

    private void broadcast() throws IOException {
        String s;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream.write("Input broadcast msg below\n".getBytes(StandardCharsets.UTF_8));
        s = bufferedReader.readLine();
        for (UserWorker worker : zone.getUserWorkers()) {
            worker.write(s);
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

    private void write(String message) throws IOException {
        outputStream.write((message + "\n").getBytes(StandardCharsets.UTF_8));
    }

}
