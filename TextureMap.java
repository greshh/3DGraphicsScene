/*
    Class to implement a texture mapping procedure
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureMap {
    BufferedImage textureMap;
      

    public TextureMap(String fileName) {
        try {
            textureMap = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color pickColour(double u, double v) { 
        int ximg = (int)(u*(textureMap.getWidth()-1));
        int yimg = (int)(v*(textureMap.getHeight()-1));
        Color colour = new Color(textureMap.getRGB(ximg, yimg), false);
        return colour; 
    }

}
