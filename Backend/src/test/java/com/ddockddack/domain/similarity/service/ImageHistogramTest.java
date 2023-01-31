package com.ddockddack.domain.similarity.service;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;

import java.awt.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageHistogramTest {

    @Test
    void getHistogram() throws IOException {

        final ImageHistogram imageHistogram = new ImageHistogram();

//        File image1 = new File("C:/Users/SSAFY/Downloads/lenna.png");
//        InputStream stream1 = new FileInputStream(image1);
//
//        imageHistogram.getHistogram(stream1);


        File targetHandImage1 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand1.png");
//        File targetHandImage2 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand2.png");
//        File targetHandImage3 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand3.png");
//        File targetHandImage4 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand4.png");
//        File targetHandImage5 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand5.png");
//        File targetHandImage6 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand6.png");

        InputStream targetHandImageStream1 = new FileInputStream(targetHandImage1);
//        InputStream targetHandImageStream2 = new FileInputStream(targetHandImage2);
//        InputStream targetHandImageStream3 = new FileInputStream(targetHandImage3);
//        InputStream targetHandImageStream4 = new FileInputStream(targetHandImage4);
//        InputStream targetHandImageStream5 = new FileInputStream(targetHandImage5);
//        InputStream targetHandImageStream6 = new FileInputStream(targetHandImage6);


        File myHandImage1 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand1/1.jpg");
        File myHandImage2 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand2/1.jpg");
//        File myHandImage3 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand3/1.jpg");
//        File myHandImage4 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand4/1.jpg");
//        File myHandImage5 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand5/1.jpg");
//        File myHandImage6 = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_input/hand6/1.jpg");

        InputStream myHandImageStream1 = new FileInputStream(myHandImage1);
        InputStream myHandImageStream2 = new FileInputStream(myHandImage2);
//        InputStream myHandImageStream3 = new FileInputStream(myHandImage3);
//        InputStream myHandImageStream4 = new FileInputStream(myHandImage4);
//        InputStream myHandImageStream5 = new FileInputStream(myHandImage5);
//        InputStream myHandImageStream6 = new FileInputStream(myHandImage6);

        List<Mat> hist1 = imageHistogram.getHistogram(myHandImageStream1);
        List<Mat> hist2 = imageHistogram.getHistogram(myHandImageStream2);
//        System.out.println(imageHistogram.compareHistograms(hist1, hist2));
    }
}