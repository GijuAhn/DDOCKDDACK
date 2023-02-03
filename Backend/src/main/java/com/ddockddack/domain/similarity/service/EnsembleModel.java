package com.ddockddack.domain.similarity.service;

import org.opencv.core.Mat;

import java.util.List;

public class EnsembleModel {

    //  Hashing(Grayscale) + FeatureExtraction(KAZE) + StructuralSimilarity(SSIM, Grayscale) + log{Histogram(RGB)}
//    InputStream once ended, cannot reuse.
//    Image data input format from frontend: ByteArray
//    public static double CalculateSimilarity(InputStream inputStream1, InputStream inputStream2) throws Exception {
    public static double CalculateSimilarity(byte[] byteArray1, byte[] byteArray2) throws Exception {

        final PerceptualHash perceptualHash = new PerceptualHash();
        final FeatureDetectorDescriptor featureDetectorDescriptor = new FeatureDetectorDescriptor();
        final StructuralSimilarity structuralSimilarity = new StructuralSimilarity();
        final ImageHistogram imageHistogram = new ImageHistogram();

//        byte[] byteArray1 = ImageUtil.InputStream2ByteArray(inputStream1);
//        byte[] byteArray2 = ImageUtil.InputStream2ByteArray(inputStream2);

        List<Mat> hist1 = imageHistogram.getHistogram(ImageUtil.ByteArray2InputStream(byteArray1));
        List<Mat> hist2 = imageHistogram.getHistogram(ImageUtil.ByteArray2InputStream(byteArray2));

        Double result;

        result = (double) (
                8000 * (featureDetectorDescriptor.compareFeatures(
                        ImageUtil.ByteArray2InputStream(byteArray1),
                        ImageUtil.ByteArray2InputStream(byteArray2)))

                + 1000 * (structuralSimilarity.compareImages(
                ImageUtil.ByteArray2InputStream(byteArray1),
                ImageUtil.ByteArray2InputStream(byteArray2)))

                - 2 * (perceptualHash.distance(
                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray1)),
                perceptualHash.getHash(ImageUtil.ByteArray2InputStream(byteArray2))))

                - 2 * baseLog(imageHistogram.compareHistograms(hist1, hist2),2)
        );

//        final similarity score
//        estimated 1000 ± 500
        return result;
    }

    private static double baseLog(double x, double base) {
        return Math.log10(x) / Math.log10(base);
    }

}
