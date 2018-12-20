/*package services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientService implements Runnable {
    private Socket s = null;

    public ClientService(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            serverSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void serverSocket() throws IOException, ClassNotFoundException {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        String pass = null;
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        String req = dis.readUTF();
        String menuOrder = null;
        int aw = 0;
        int sw = 0;
        if (req.equals("login")) {
            pass = dis.readUTF();
            if (auth().equals(pass)) {
                System.out.println("remote client connected");
                dos.write(1);
            } else {
                dos.write(0);
            }

            if (MotionDetector.isMotionDetectorStopped()) {
                dos.write(0);
            } else {
                dos.write(1);
            }
            dos.close();
            dis.close();
            s.close();
        }
        if (req.equals("menu")) {
            menuOrder = dis.readUTF();
            if (menuOrder.equals("start")) {
                if (PirSensor.isPirStop()) {
                    try {
                        PirSensor.setPirStop(false);
                        Thread ps = new Thread(new PirSensor());
                        ps.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (menuOrder.equals("stop")) {
                PirSensor.setPirStop(true);
                MotionDetector.setMotionDetectorStopped(true);
            }
            if (menuOrder.equals("set weight")) {
                aw = LoadCell.getWeight();
                try {
                    WeightStore.setOrigoWeight(aw);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    WeightStore.setActualWeight(aw);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (menuOrder.equals("screen")) {
                ImageIO.write(MotionDetector.getPicture(), "jpg", new File("/home/pi/apache-tomcat-8.5.34/webapps/WebClient/screen.jpg"));
            }
            if (menuOrder.equals("actual weight")) {
            }
            try {
                sw = WeightStore.readOrigoWeight();
            } catch (IOException e) {
                e.printStackTrace();
            }
            aw = LoadCell.getWeight();


            if (MotionDetector.isMotionDetectorStopped()) {
                dos.write(0);
            } else {
                dos.write(1);
            }
            dos.writeInt(sw);
            dos.writeInt(aw);
            dos.close();
            dis.close();
            s.close();
        }
    }


    private String auth() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("mkjashdgf");
        Properties props = new org.jasypt.properties.EncryptableProperties(encryptor);
        try {
            props.load(new java.io.FileInputStream("conf.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty("password");
    }
}
*/