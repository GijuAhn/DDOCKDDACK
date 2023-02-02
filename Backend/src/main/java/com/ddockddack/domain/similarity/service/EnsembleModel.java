package com.ddockddack.domain.similarity.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.ddockddack.domain.similarity.service.PerceptualHash;
import com.ddockddack.domain.similarity.service.ImageHistogram;
import com.ddockddack.domain.similarity.service.ImageUtil;
import com.ddockddack.domain.similarity.service.StructuralSimilarity;
import com.ddockddack.domain.similarity.service.FeatureDetectorDescriptor;

public class EnsembleModel {

    //      Histogram(RGB) 은 result range 가 너무 넓고 (1,000,000 ~ 20,000,000),
//      예측불가능하다고 판단해 Similarity Metrics 제외.

    //  Hashing(Grayscale) + FeatureExtraction(KAZE) + StructuralSimilarity(SSIM, Grayscale)
//    InputStream once ended, cannot reuse.
    public static double CalculateSimilarity(InputStream inputStream1, InputStream inputStream2) throws Exception {

        final PerceptualHash perceptualHash = new PerceptualHash();
        final FeatureDetectorDescriptor featureDetectorDescriptor = new FeatureDetectorDescriptor();
        final StructuralSimilarity structuralSimilarity = new StructuralSimilarity();

        byte[] byteArray1 = ImageUtil.InputStream2ByteArray(inputStream1);
        byte[] byteArray2 = ImageUtil.InputStream2ByteArray(inputStream2);

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
        );

//        final similarity score
//        estimated 1000 ± 500
        return result;
    }
}
