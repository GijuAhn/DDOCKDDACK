package com.ddockddack.domain.similarity.service;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PerceptualHashTest {

    @Test
    void distance() {
    }

    @Test
    void getHash() throws Exception {
//        given
        final PerceptualHash perceptualHash = new PerceptualHash();
        File myImage = new File("C:/Users/SSAFY/Downloads/image_similarity_test/hand_target/hand1.png");
        InputStream myImageStream = new FileInputStream(myImage);

//        when
        final String resultHash = perceptualHash.getHash(myImageStream);

//        then
        System.out.println(resultHash);
    }
}