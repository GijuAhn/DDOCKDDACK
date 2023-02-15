package com.ddockddack.domain.similarity.service;

import org.opencv.core.Mat;

import java.util.List;

public class EnsembleModel {

//    Hashing(Grayscale) + FeatureExtraction(KAZE) + StructuralSimilarity(SSIM, Grayscale) + log{Histogram(RGB)}
//    InputStream once ended, cannot reuse.
//    Image data input format from frontend: ByteArray
//    public static double CalculateSimilarity(InputStream inputStream1, InputStream inputStream2) throws Exception {
    public static Integer CalculateSimilarity(byte[] byteArray1, byte[] byteArray2) throws Exception {

        final PerceptualHash perceptualHash = new PerceptualHash();
        final FeatureDetectorDescriptor featureDetectorDescriptor = new FeatureDetectorDescriptor();
        final StructuralSimilarity structuralSimilarity = new StructuralSimilarity();
        final ImageHistogram imageHistogram = new ImageHistogram();

//        byte[] byteArray1 = ImageUtil.InputStream2ByteArray(inputStream1);
//        byte[] byteArray2 = ImageUtil.InputStream2ByteArray(inputStream2);

        List<Mat> hist1 = imageHistogram.getHistogram(ImageUtil.ByteArray2InputStream(byteArray1));
        List<Mat> hist2 = imageHistogram.getHistogram(ImageUtil.ByteArray2InputStream(byteArray2));

        int result;

//        result = (double) (
//                8192 * (featureDetectorDescriptor.compareFeatures(
//                        ImageUtil.ByteArray2InputStream(byteArray1),
//                        ImageUtil.ByteArray2InputStream(byteArray2)))
//
//                + 512 * (structuralSimilarity.compareImages(
//                ImageUtil.ByteArray2InputStream(byteArray1),
//                ImageUtil.ByteArray2InputStream(byteArray2)))
//
//                - 2 * (perceptualHash.distance(
//                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray1)),
//                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray2))))
//
//                - 2 * baseLog(imageHistogram.compareHistograms(hist1, hist2),2)
//        );

//      [+] *8192
        double featureScore = (featureDetectorDescriptor.compareFeatures(
                        ImageUtil.ByteArray2InputStream(byteArray1),
                        ImageUtil.ByteArray2InputStream(byteArray2)));

//      [+] *512
        double structureScore = (structuralSimilarity.compareImages(
                ImageUtil.ByteArray2InputStream(byteArray1),
                ImageUtil.ByteArray2InputStream(byteArray2)));

//      [-] *2
        double hashDistance = (perceptualHash.distance(
                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray1)),
                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray2))));

//      [-] *2, if NaN batch -32.0
        double histogramDiff = (double) baseLog(imageHistogram.compareHistograms(hist1, hist2),2);
        if (Double.isNaN(histogramDiff)){
            histogramDiff = 1.0;
        }

//        System.out.println("[+] featureScore * 8192: " + (8192 * featureScore));
//        System.out.println("[+] structureScore * 512: " + (512 * structureScore));
//        System.out.println("[-] hashDistance * 2: " + (2 * hashDistance));
//        System.out.println("[-] histogramDiff * 2: " + (2 * histogramDiff));

//        result = (int) Math.round((8192 * featureScore) + (512 * structureScore) - (2 * hashDistance) - (2 * histogramDiff));
//        ease weight cause of relative evaluation
        result = (int) Math.round((1000 * featureScore) + (100 * structureScore) - (hashDistance) - (histogramDiff));

//        final similarity score
//        estimated 500±500
        return result;
    }

    private static double baseLog(double x, double base) {
        return Math.log10(x) / Math.log10(base);
    }

}
