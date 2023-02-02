package com.ddockddack.domain.similarity.service;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.KAZE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class FeatureDetectorDescriptor {

    static {
        // Load the OpenCV library
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OpenCV.loadLocally();

    }


    public float compareFeatures(InputStream input1, InputStream input2) throws IOException {

        BufferedImage img1 = ImageIO.read(input1);
        Mat image1 = ImageUtil.BufferedImage2Mat(img1);

        BufferedImage img2 = ImageIO.read(input2);
        Mat image2 = ImageUtil.BufferedImage2Mat(img2);

        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        // Detect keypoints and compute descriptors using KAZE
        KAZE detector = KAZE.create();
        detector.detectAndCompute(image1, new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(image2, new Mat(), keypoints2, descriptors2);

        // Match descriptors using Brute-Force matcher
        List<MatOfDMatch> matches = new LinkedList<>();
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
        matcher.knnMatch(descriptors1, descriptors2, matches, 2);

        // Filter matches using the Lowe's ratio test = 0.75f
//        float ratioThreshold = 0.75f;
//        float ratioThreshold = 0.70f;
        float ratioThreshold = 0.80f;
//        float ratioThreshold = 0.90f;
//        float ratioThreshold = 0.999f;
        List<DMatch> goodMatches = new LinkedList<>();
        for (MatOfDMatch matOfDMatch : matches) {
            DMatch[] dmatcharray = matOfDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * ratioThreshold) {
                goodMatches.add(m1);
            }
        }

        // Calculate similarity score as the ratio of good matches to total matches
        float score = (float) goodMatches.size() / (float) keypoints1.rows();

        return score;
    }
}
