package com.ddockddack.domain.similarity.service;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class ImageHistogram {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Imgcodecs.imread("image.jpg");

        Mat histogram = new Mat();
        int channels[] = {0};
        MatOfInt histSize = new MatOfInt(256);
//        MatOfInt histRange = new MatOfInt(0, 256);
        MatOfFloat histRange = new MatOfFloat(0, 256);

        Imgproc.calcHist(Arrays.asList(image), new MatOfInt(0), new Mat(), histogram, histSize, histRange, false);
    }

}

