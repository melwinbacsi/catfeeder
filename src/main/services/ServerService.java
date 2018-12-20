/*package services;

import gui.Menu;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ServerService implements Runnable {

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(55555, 0, InetAddress.getLoopbackAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                assert ss != null;
                new Thread(new ClientService(ss.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
*/