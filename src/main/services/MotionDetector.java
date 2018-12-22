package services;

import db.DB;
import db.Measurement;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;


import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MotionDetector
        implements Runnable {

    private static boolean motionDetectorStopped = false;
    private static BufferedImage picture;
    private static BufferedImage capturedPic = null;

    static BufferedImage getCapturedPic() {
        return capturedPic;
    }

    static void setCapturedPic(BufferedImage capturedPic) {
        MotionDetector.capturedPic = capturedPic;
    }

    static BufferedImage getPicture() {
        return picture;
    }

    public static void setPicture(BufferedImage picture) {
        MotionDetector.picture = picture;
    }

    public static boolean isMotionDetectorStopped() {
        return motionDetectorStopped;
    }

    public static void setMotionDetectorStopped(boolean motionDetectorStopped) {
        MotionDetector.motionDetectorStopped = motionDetectorStopped;
    }

    public void run() {
        try {
            detectMotion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detectMotion() throws Exception {
        long time = 0;

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(-1);
        grabber.start();
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat frame = converter.convert(grabber.grab());
        Mat image = null;
        Mat previousImage = null;
        Mat difference = null;
        MatVector contour = new MatVector();
        int detectionCounter = 0;
        boolean captured = false;
        BufferedImage cachedPicture = null;
        while (!isMotionDetectorStopped()) {
            setPicture(new Java2DFrameConverter().getBufferedImage(grabber.grab()));
            GaussianBlur(frame, frame, new Size(3, 3), 0);
            if (image == null) {
                image = new Mat(frame.rows(), frame.cols(), CV_8UC1);
                cvtColor(frame, image, CV_BGR2GRAY);
            } else {
                previousImage = image;
                image = new Mat(frame.rows(), frame.cols(), CV_8UC1);
                cvtColor(frame, image, CV_BGR2GRAY);
            }
            if (difference == null) {
                difference = new Mat(frame.rows(), frame.cols(), CV_8UC1);
            }
            if (previousImage != null) {
                if ((System.currentTimeMillis() / 1000) - time > 60 && System.currentTimeMillis() / 1000 - time < 100 && captured) {
                    System.out.println("alarm");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    Measurement measurement = new Measurement(LocalDateTime.now().format(dtf), DB.getMeasurement(-1).getOrigoTime(), LoadCell.getWeight(), DB.getMeasurement(-1).getOrigoWeight());
                    new PictureSaver(getCapturedPic(), measurement, false);
                    captured = false;
                    cachedPicture = null;
                    PirSensor.setPirDetected(false);
                }

                absdiff(image, previousImage, difference);
                threshold(difference, difference, 45, 255, CV_THRESH_BINARY);
                findContours(difference, contour, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
                if (contour.size() > 0 && (System.currentTimeMillis() / 1000) - time > 7) {
                    if ((System.currentTimeMillis() / 1000) - time < 15 && PirSensor.isPirDetected()) {
                        detectionCounter++;
                    } else {
                        detectionCounter = 0;
                    }
                    time = System.currentTimeMillis() / 1000;
                    if (detectionCounter >= 3) {
                        if (cachedPicture == null) {
                            cachedPicture = getPicture();
                        }
                        setCapturedPic(cachedPicture);
                        cachedPicture = getPicture();
                        captured = true;
                    }
                }
            }
        }
        grabber.stop();
    }
}

