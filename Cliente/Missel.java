import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Missel {
    Image img;
    int x;
    int y;
    boolean visivel = false;
    final int ALTURA = 40;
    final int LARGURA = 40;
    JPanel arena;

    Missel (JPanel arena, int n) {
        try {
            arena = this.arena;
            img = ImageIO.read(new File("missel" + (n == 1 ? "2" : "1") + ".png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(arena, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void desenha (Graphics g) {
        if (visivel)
            g.drawImage(img, x, y, ALTURA, LARGURA, arena);
    }
}