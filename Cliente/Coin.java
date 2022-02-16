import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Coin {
    Image img;
    boolean visivel = true;
    final int ALTURA = 40;
    final int LARGURA = 40;
    int x;
    int y;
    JPanel arena;

    Coin (JPanel arena) {
        try {
            arena = this.arena;
            img = ImageIO.read(new File("coin.png"));
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(arena, "A imagem não pode ser carregada!\n" + e, "Erro",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void esconde() {
        visivel = false;
    }

    public void desenha(final Graphics g) {
        if(visivel){
            g.drawImage(img, x, y, LARGURA, ALTURA, arena);
        }
    }
}

