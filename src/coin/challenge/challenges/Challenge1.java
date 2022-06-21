package coin.challenge.challenges;

import static coin.challenge.utils.ImageConstants.*;

import java.util.Optional;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.Color;

import coin.challenge.bean.Image;
import coin.challenge.utils.ImageUtils;
import javax.swing.JOptionPane;

public final class Challenge1 implements Challenge {

    /** Construtor do desafio 1 */
    public Challenge1() {}

    @Override
    public void solveChallenge() {
        Optional<Image> optImage = ImageUtils.getImageFromFile(CHALLENGE_1_PATH);
        if (optImage.isEmpty()) {
            return;
        }
        //
        // Transforma em escala de cinza
        //
        Image image = optImage.get();
        int[][] matrix = image.getMatrix();
        BufferedImage processed = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_BYTE_GRAY);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(matrix[i][j]);
                matrix[i][j] = (byte) new Color(((c.getRed() + c.getGreen() + c.getBlue()) / 3)).getRGB(); 
            }
        }
        //
        // Inverte as cores
        //
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(matrix[i][j]);
                if (c.getBlue() < 220) {
                    matrix[i][j] = new Color(255, 255, 255).getRGB();
                } else {
                    matrix[i][j] = new Color(0, 0, 0).getRGB();
                }
            }
        }
        
        //
        // Aplica erosão na imagem
        // 
        for(int z = 0; z < 0; z++){
            for (int i = 2; i < image.getWidth() - 2; i++) {
                for (int j = 2; j < image.getHeight() - 2; j++) {
                    if (new Color(matrix[i][j]).getRed() != 255 ||
                        new Color(matrix[i+1][j]).getRed() != 255 || 
                        new Color(matrix[i][j+1]).getRed() != 255 ||
                        new Color(matrix[i+1][j+1]).getRed() != 255) {
                        for (int x = i - 2; x < i + 1; x++) {
                            for (int y = j - 2; y < j + 1; y++) {
                                matrix[x][y] = new Color(0, 0, 0).getRGB();
                            }
                        }
                    }
                }
            }
        }
        
        //
        // Aplica dilatação na imagem
        // 
        for(int z = 0; z < 1; z++){
            for (int i = 2; i < image.getWidth() - 2; i++) {
                for (int j = 2; j < image.getHeight() - 2; j++) {
                    if (new Color(matrix[i][j]).getRed()     == 255 &&
                        new Color(matrix[i+1][j]).getRed()   == 255 && 
                        new Color(matrix[i][j+1]).getRed()   == 255 &&
                        new Color(matrix[i+1][j+1]).getRed() == 255) {
                        for (int x = i - 2; x < i + 2; x++) {
                            for (int y = j - 2; y < j + 2; y++) {
                                matrix[x][y] = new Color(255, 255, 255).getRGB();
                            }
                        }
                    }
                }
            }
        } 
        
        //
        // Conta e mede as moedas
        //
        int actualColor = 0;
        int coinLength = 0;
        int actualCoinLength = 0;
        for (int i = 2; i < image.getWidth() - 2; i++) {
                 for (int j = 2; j < image.getHeight() - 2; j++) {
                     if (new Color(matrix[i][j]).getRed() == 255) {
                         actualColor = 255;
                         actualCoinLength++;
                     }else if(new Color(matrix[i][j]).getRed() == 0 &&
                             actualColor == 255) {
                             actualColor = 0;
                             if(actualCoinLength > coinLength)
                                 coinLength = actualCoinLength;
                             actualCoinLength = 0;
                 }
                     
            }
         }
        
        JOptionPane.showMessageDialog(null, "Tamanho Maior Moeda: " + coinLength);
        
        // Imprime
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                processed.setRGB(i, j, new Color(matrix[i][j]).getRGB());
            }
        }
        ImageUtils.writeImage(processed, PROCESSED_IMAGE_PATH);
        ImageUtils.showProcessedImage();
    }

}
