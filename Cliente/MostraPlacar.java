import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class MostraPlacar {
    JPanel arena;
    Image imgSub1, imgSub2, imgSub3, imgCoin;
    Submarino sub;
    int n;

    MostraPlacar(JPanel arena, int n, Submarino sub) {
        this.n = n;
        try {
            this.arena = arena;
            imgSub1 = ImageIO.read(new File("Submarino" + (n == 1 ? "A" : "B") + ".png"));
            imgSub3 = ImageIO.read(new File("cinza.png"));
            imgCoin = ImageIO.read(new File("coin.png"));
            this.sub = sub;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(arena, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void desenha (Graphics g) {
        if (n==1) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Jogador 1: ", 50, arena.getSize().height-90);

            //escreve o numero de pontos e desenha a pilha de moedas
            g.setColor(Color.ORANGE);
            sub.pontos = Integer.toString(sub.p); 
            g.setFont(new Font("Serif", Font.BOLD, 35));
            g.drawString(sub.pontos, 230, arena.getSize().height - 87);
            g.drawImage(imgCoin, 190, arena.getSize().height - 115, 30, 30, arena);
        
            //condicao para determinar quantos submarinos ficam pintados e quantos nao de acordo com o numero de vidas do sub1
            if (sub.vida == 1) {
                g.drawImage(imgSub1, 50, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, 130, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, 210, arena.getSize().height - 70, 70, 50, arena);
            }

            else if (sub.vida == 2) {
                g.drawImage(imgSub1, 50,  arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, 130, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, 210, arena.getSize().height - 70, 70, 50, arena);
            }

            else if (sub.vida == 3) {
                g.drawImage(imgSub1, 50, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, 130, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, 210, arena.getSize().height - 70, 70, 50, arena);
            }
        }
        else {
            //escreve "Jogador 2:"
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.BOLD, 30)); 
            g.drawString("Jogador 2: ", arena.getSize().width - 260, arena.getSize().height-90);

            //escreve o numero de pontos e desenha a pilha de moedas
            g.setColor(Color.ORANGE);
            sub.pontos = Integer.toString(sub.p); //converte inteiro para string
            g.setFont(new Font("Serif", Font.BOLD, 35));
            g.drawString(sub.pontos, arena.getSize().width - 80, arena.getSize().height - 87);
            g.drawImage(imgCoin, arena.getSize().width - 120, arena.getSize().height - 115, 30, 30, arena);

            //condicao para determinar quantos submarinos ficam pintados e quantos nao de acordo com o numero de vidas do sub2
            if (sub.vida == 1) {
                g.drawImage(imgSub1, arena.getSize().width - 280, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, arena.getSize().width - 200, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, arena.getSize().width - 120, arena.getSize().height - 70, 70, 50, arena);
            }

            else if (sub.vida == 2) {
                g.drawImage(imgSub1, arena.getSize().width - 280, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, arena.getSize().width - 200, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub3, arena.getSize().width - 120, arena.getSize().height - 70, 70, 50, arena);
            }

            else if (sub.vida == 3) {
                g.drawImage(imgSub1, arena.getSize().width - 280, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, arena.getSize().width - 200, arena.getSize().height - 70, 70, 50, arena);
                g.drawImage(imgSub1, arena.getSize().width - 120, arena.getSize().height - 70, 70, 50, arena);
            }
        }
    }
}