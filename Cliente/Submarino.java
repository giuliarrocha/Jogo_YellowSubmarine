import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Submarino {
    Image img;
    int vida = 3;
    int x;
    int y;
    int p = 0;
    final int ALTURA = 80;
    final int LARGURA = 130;
    String pontos = "0";
    JPanel arena;

    Submarino (JPanel arena, int n) {
        try {
            arena = this.arena;
            img = ImageIO.read(new File("Submarino" + (n == 1 ? "A" : "B") + ".png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(arena, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void posiciona (int x, int y) {
        this.x = x;
        this.y = y;
        arena.repaint();
    }

    public void desenha(Graphics g) {
        g.drawImage(img, x, y, LARGURA, ALTURA, arena);
    }
}