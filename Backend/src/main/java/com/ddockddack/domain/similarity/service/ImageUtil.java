package com.ddockddack.domain.similarity.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgcodecs.Imgcodecs.imdecode;
import org.opencv.imgcodecs.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.core.CvType.CV_8UC;

public class ImageUtil {
//    image format must be a jpg

    //    BufferedImage to Mat
    public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
    }

    //    Mat to BufferedImage
    public static BufferedImage Mat2BufferedImage(Mat matrix) throws IOException {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }


    //  converting byte[] image data (from frontend) to InputStream, and vice versa.
    //  byte array to inputStream
    public static InputStream ByteArray2InputStream(byte[] byteArray){
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        return inputStream;
    }

    //    inputStream to byte array
    public static byte[] InputStream2ByteArray(InputStream inputStream) {
        final byte[] byteArray;
        try (inputStream) {
            byteArray = inputStream.readAllBytes();
            return byteArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}