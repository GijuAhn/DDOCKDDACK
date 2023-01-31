//package com.ddockddack.domain.similarity.service;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//
//import org.opencv.core.*;
//import org.opencv.features2d.DescriptorMatcher;
//import org.opencv.features2d.FastFeatureDetector;
//import org.opencv.imgcodecs.Imgcodecs;
//
//// measure the similarity between the two images using speed-up robust features (SURF).
//public class SURF {
//
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
//
//    public static void main(String[] args) throws IOException {
//        String imagePath1 = "path/to/image1.jpg";
//        String imagePath2 = "path/to/image2.jpg";
//
//        // Load the images
//        Mat image1 = Imgcodecs.imread(imagePath1);
//        Mat image2 = Imgcodecs.imread(imagePath2);
//
//        FastFeatureDetector detector = FastFeatureDetector.create();
//        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//        detector.detect(image1, keypoints1);
//        detector.detect(image2, keypoints2);
//
//        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
//        Mat descriptors1 = new Mat();
//        Mat descriptors2 = new Mat();
//        extractor.compute(image1, keypoints1, descriptors1);
//        extractor.compute(image2, keypoints2, descriptors2);
//
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
//        Mat match = new Mat();
//        matcher.match(descriptors1, descriptors2, match);
//
//        double max_dist = Double.MIN_VALUE;
//        double min_dist = Double.MAX_VALUE;
//
//        for (int i = 0; i < descriptors1.rows(); i++) {
//            double dist = match.get(i, 0)[0];
//            if (dist < min_dist) {
//                min_dist = dist;
//            }
//            if (dist > max_dist) {
//                max_dist = dist;
//            }
//        }
//
//        System.out.println("max_dist: " + max_dist);
//        System.out.println("min_dist: " + min_dist);
//
//        Mat goodMatch = new Mat();
//        for (int i = 0; i < descriptors1.rows(); i++) {
//            if (match.get(i, 0)[0] < 3 * min_dist) {
//                goodMatch.push_back(match.row(i));
//            }
//        }
//
//        System.out.println("goodMatch: " + goodMatch.rows());
//
//        Mat imgMatch = new Mat();
//        MatOfByte drawnMatches = new MatOfByte();
//        Features2d.drawMatches(image1, keypoints1, image2, keypoints2, (MatOfDMatch) goodMatch, imgMatch);
//        Imgcodecs.imwrite("path/to/match.jpg", imgMatch);
//    }
//}
