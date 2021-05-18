package com.company.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author lekeping
 */
public class ClientHandler extends Thread {
    Socket clientSocket;
    InputStream inputStream;
    OutputStream outputStream;
    @Override
    public void run(){
        try {
            outputStream.write("Connected\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.echo();

        this.close();
    }
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.outputStream = clientSocket.getOutputStream();
            this.inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void close(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void echo(){

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                if (line.equalsIgnoreCase("quit")){
                    break;
                }
                outputStream.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
