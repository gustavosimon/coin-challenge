package coin.challenge.utils;

import static coin.challenge.utils.ImageConstants.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import coin.challenge.bean.Image;
import coin.challenge.window.Processed;

public class ImageUtils {
    
    /** Construtor privado para garantir que não existam instâncias dessa classe */
    private ImageUtils() {}

    /**
     * Retorna matriz com valores RGB a partir da imagem original.
     * 
     * <p>
     * A imagem original se encontra em resources.
     * 
     * @return optional contendo objeto {@code Image} representando a imagem carregada
     * @throws IOException
     */
    public static Optional<Image> getImageFromFile(String imagePath) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            return Optional.empty();
        }
        int width = bufferedImage.getWidth(null);
        int height = bufferedImage.getHeight(null);
        int[][] pixels = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = bufferedImage.getRGB(i, j);
            }
        }
        return Optional.of(new Image(pixels, width, height));
    }

    /**
     * Grava a imagem processada com o nome padrão.
     * 
     * @param imageToWrite imagem bufferizada para gravação
     * @param imagePath localização para gravar a imagem
     */
    public static void writeImage(BufferedImage imageToWrite, String imagePath) {
        File outputfile = new File(imagePath);
        try {
            ImageIO.write(imageToWrite, IMAGE_EXTENSION, outputfile);
        } catch (IOException e) {
            System.out.println("Falha ao gravar a imagem processada" + e.toString());
        }
    }    

    /**
     * Exibe a imagem processada em um janela.
     */
    public static void showProcessedImage() {
        Processed p = new Processed(PROCESSED_IMAGE_PATH);
        p.setVisible(true);
    }
}
