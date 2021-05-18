package com.company.Client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    Socket socket;

    {
        try {
            socket = new Socket("127.0.0.1",5000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
