package com.ddockddack.domain.similarity.service;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;

import java.io.*;
import java.util.List;


class ImageHistogramTest {

    @Test
    void getHistogram() throws IOException {

        final ImageHistogram imageHistogram = new ImageHistogram();

//        calculate histogram similarity
//        File targetHandImage1 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand1.png");
        File targetHandImage1 = new File("C:/Users/wn725/Downloads/kaggle_friendship_image_dataset/Test/Img7345.jpg");
        List<Mat> hist1;
        try (InputStream targetHandImageStream1 = new FileInputStream(targetHandImage1)) {
            hist1 = imageHistogram.getHistogram(targetHandImageStream1);
            System.out.println("hist1 = " + hist1);
        }

//        File myHandImage1 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand1/1.jpg");
        File myHandImage1 = new File("C:/Users/wn725/Downloads/kaggle_friendship_image_dataset/Test/Img7434.jpg");
        List<Mat> hist2;
        try (InputStream myHandImageStream1 = new FileInputStream(myHandImage1)) {
            hist2 = imageHistogram.getHistogram(myHandImageStream1);
            System.out.println("hist2 = " + hist2);
        }

        File myHandImage2 = new File("C:/Users/wn725/Downloads/kaggle_friendship_image_dataset/Test/Img1137.jpg");
        List<Mat> hist3;
        try (InputStream myHandImageStream2 = new FileInputStream(myHandImage2)) {
            hist3 = imageHistogram.getHistogram(myHandImageStream2);
            System.out.println("hist3 = " + hist3);
        }

//        print comparison result
        System.out.println("hist1 = " + imageHistogram.compareHistograms(hist1, hist2));
        System.out.println("hist2 = " + imageHistogram.compareHistograms(hist1, hist3));

    }
}