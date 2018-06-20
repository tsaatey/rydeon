/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class ImageService {

    public BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }
}
