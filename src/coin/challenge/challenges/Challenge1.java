package coin.challenge.challenges;

import static coin.challenge.utils.ImageConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.awt.Color;

import coin.challenge.bean.Image;
import coin.challenge.utils.ImageUtils;
import javax.swing.JOptionPane;

/**
 * Classe para a solução do desafio de contagem de moedas.
 * 
 * Requisitos para o funcionamento do algoritmo criado:
 * - As moedas não podem estar muito próximas/coladas
 * - Tem que seguir o mesmo tamanho das moedas contidos na imagem Moedas1.png 
 */
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
        // Aplica dilatação na imagem
        //
        for (int i = 2; i < image.getWidth() - 2; i++) {
            for (int j = 2; j < image.getHeight() - 2; j++) {
                if (new Color(matrix[i][j]).getRed() == 255 &&
                    new Color(matrix[i + 1][j]).getRed() == 255 &&
                    new Color(matrix[i][j + 1]).getRed() == 255 &&
                    new Color(matrix[i + 1][j + 1]).getRed() == 255) {
                    for (int x = i - 2; x < i + 2; x++) {
                        for (int y = j - 2; y < j + 2; y++) {
                            matrix[x][y] = new Color(255, 255, 255).getRGB();
                        }
                    }
                }
            }
        }
        //
        // Conta o valor em reais das moedas medindo seus diâmetros
        //
        int diametro = 0;
        int raio = 0;
        List<Integer> array = new ArrayList<>();
        for (int i = 2; i < image.getHeight() - 2; i++) {
            for (int j = 2; j < image.getWidth() - 2; j++) {
                if (new Color(matrix[j][i]).getRed() == 255) {
                    int altura = i;
                    //
                    // Calcula a altura da moeda
                    //
                    while (new Color(matrix[j][altura]).getRed() != 0) {
                        altura++;
                    }
                    diametro = altura - i;
                    raio = diametro / 2;
                    int margem = (int) Math.round(raio * 1.3 + j);
                    if (margem > image.getWidth()) {
                        margem = image.getWidth();
                    }
                    //
                    // Apaga da imagem a moeda processada 
                    //
                    for (int y = i; y < altura * 1.05; y++) {
                        for (int x = j - raio; x < margem; x++) {
                            if (x < 0) {
                                x = 0;
                            }
                            matrix[x][y] = new Color(0, 0, 0).getRGB();
                        }
                    }
                    //
                    // Adiciona no array de moedas a moeda processada
                    //
                    array.add(diametro);
                    //
                    // Reposiciona os índicas para iniciar a leitura da imagem do ínício
                    //
                    i = 2;
                    j = 2;
                }
            }
        }
        //
        // Calcula o valor da imagem a partir dos diâmetros das moedas
        //
        double valor = 0;
        for (int i : array) {
            if (i >= 70) {
                valor += 1;
                continue;
            }
            if (i >= 65) {
                valor += 0.25;
                continue;
            }
            if (i >= 61) {
                valor += 0.50;
                continue;
            }
            if (i >= 58) {
                valor += 0.05;
                continue;
            }
            if (i >= 53) {
                valor += 0.10;
                continue;
            }
        }
        JOptionPane.showMessageDialog(null, "A imagem tem: " + String.format("%,.2f", valor) + " reais");
    }

}
