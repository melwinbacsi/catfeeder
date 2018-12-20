package services;


import db.DB;
import db.Measurement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PictureSaver {
    private String path;
    private String measurementTime;

    public PictureSaver(Measurement measurement) {
        this(MotionDetector.getPicture(), measurement);
    }

    public PictureSaver(BufferedImage picture, Measurement measurement) {
        try {
            DB db = new DB();
            Measurement previousMeasurement = db.getMeasurement(measurement.getId()-1);
            if (LoadCell.getWeight() < (db.getMeasurement(-1).getActualWeight() - 1)) {
                measurementTime = measurement.getMeasurementTime();
                path = "/home/pi/camera/" + measurementTime.substring(0, 5) + "/" + measurementTime.substring(6) + ".jpg";
                File directory = new File("/home/pi/camera/" + measurementTime.substring(0, 5));
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
}