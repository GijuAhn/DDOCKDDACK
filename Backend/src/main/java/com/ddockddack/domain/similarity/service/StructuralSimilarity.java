package com.ddockddack.domain.similarity.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

// grayscale (single channel) SSIM
public class StructuralSimilarity {

    public static double compareImages(InputStream image1, InputStream image2) throws IOException {
        BufferedImage img1 = toGrayscale(ImageIO.read(image1));
        BufferedImage img2 = toGrayscale(ImageIO.read(image2));

        int width = img1.getWidth();
        int height = img1.getHeight();
        double meanX = 0;
        double meanY = 0;
        double standardDeviationX = 0;
        double standardDeviationY = 0;
        double covariance = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray1 = img1.getRGB(x, y) & 0xff;
                int gray2 = img2.getRGB(x, y) & 0xff;

                meanX += gray1;
                meanY += gray2;
            }
        }

        meanX /= (width * height);
        meanY /= (width * height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gray1 = img1.getRGB(x, y) & 0xff;
                int gray2 = img2.getRGB(x, y) & 0xff;

                standardDeviationX += Math.pow(gray1 - meanX, 2);
                standardDeviationY += Math.pow(gray2 - meanY, 2);
                covariance += (gray1 - meanX) * (gray2 - meanY);
            }
        }

        standardDeviationX = Math.sqrt(standardDeviationX / (width * height));
        standardDeviationY = Math.sqrt(standardDeviationY / (width * height));
        covariance /= (width * height);

        double SSIMScore = (2 * meanX * meanY + 0.0001) * (2 * covariance + 0.0001) /
                ((meanX * meanX + meanY * meanY + 0.0001) * (standardDeviationX * standardDeviationX + standardDeviationY * standardDeviationY + 0.0001));

//        HIGHER SSIM => HIGHER Similarity
        return SSIMScore;
    }

    private static BufferedImage toGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayImage.getGraphics().drawImage(image, 0, 0, null);
        return grayImage;
    }

}
