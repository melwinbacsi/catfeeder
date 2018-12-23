package services;


import db.DB;
import db.Measurement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PictureSaver implements Runnable {
    private String path;
    private String measurementTime;
    private Measurement measurement;
    private Measurement previousMeasurement;
    private BufferedImage picture;
    private boolean testMailSend;

    @Override
    public void run() {
        try {
            if ((measurement.getActualWeight() < (previousMeasurement.getActualWeight() - 1)) || testMailSend) {
                if (!testMailSend) {
                    DB.addMeasurement(measurement);
                }
                measurementTime = measurement.getMeasurementTime();
                path = "/home/pi/camera/" + measurementTime.substring(0, 8) + "/" + measurementTime.substring(9) + ".jpg";
                File directory = new File("/home/pi/camera/" + measurementTime.substring(0, 8));
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                ImageIO.write(picture, "jpg", new File(path));
                new MailServices().sendMail(path, measurement, previousMeasurement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PictureSaver(Measurement measurement) {
        this(MotionDetector.getPicture(), measurement, measurement, true);
    }

    public PictureSaver(BufferedImage picture, Measurement measurement, Measurement previousMeasurement, boolean testMailSend) {
        this.picture = picture;
        this.measurement = measurement;
        this.testMailSend = testMailSend;
        this.previousMeasurement = previousMeasurement;
    }
}