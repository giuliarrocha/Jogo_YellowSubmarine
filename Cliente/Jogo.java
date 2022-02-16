import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

/*
* JOGO DESENVOLVIDO POR:
* GIULIA ROSSATTO ROCHA - RA: 191025372
* LARISSA DE CASTRO BONADIO - RA: 191020222
*
* NOME DO JOGO: "YELLOW SUBMARINE"
*/

class AreaJogo extends JPanel {
    Jogo jogo;
    Missel m1 = new Missel(this,1);
    Missel m2 = new Missel(this,2);
    Bau bau[] = {new Bau(this), new Bau(this)};
    Bomb b[] = {new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this), new Bomb(this)};
    Coin c[] = { new Coin(this), new Coin(this), new Coin(this), new Coin(this), new Coin(this), new Coin(this),
            new Coin(this), new Coin(this), new Coin(this), new Coin(this) };
    Submarino sub1 = new Submarino (this,1);
    Submarino sub2 = new Submarino (this,2);
    Rede rede;
    boolean jogoIniciado = false, continuaJogo = true;
    Image fundo1, fundo2, fundo3, fundo4, fundo5, fundo6, fundo7;
    MostraPlacar placarDesenho1 = new MostraPlacar(this,1,sub1);
    MostraPlacar placarDesenho2 = new MostraPlacar(this,2,sub2);
    int fase=1;
    int ganhador, perdedor, situacao;

    AreaJogo(Jogo jogo, Rede rede) {
        this.jogo = jogo;
        this.rede = rede;
        setPreferredSize(new Dimension(700,600));

        try{
            fundo1 = ImageIO.read(new File("fundo1.jpg"));
            fundo2 = ImageIO.read(new File("fundo2.jpg"));
            fundo3 = ImageIO.read(new File("fundo3.jpg"));
            fundo4 = ImageIO.read(new File("fundo1_cinza.jpg"));
            fundo5 = ImageIO.read(new File("fundo2_cinza.jpg"));
            fundo6 = ImageIO.read(new File("fundo3_cinza.jpg"));
            fundo7 = ImageIO.read(new File("fundo.png"));
        } catch (IOException erro){
            System.out.println("Não foi possivel carregar a imagem");
        }

        // tratamento das teclas 
        jogo.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e){
                if(jogoIniciado){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                        rede.enviaPosicao("DIREITA", sub1.x, sub1.y);
                        rede.descarregaEnvio();
                    } 
                    if(e.getKeyCode() == KeyEvent.VK_LEFT){
                        rede.enviaPosicao("ESQUERDA", sub1.x, sub1.y);
                        rede.descarregaEnvio();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_UP){
                        rede.enviaPosicao("SOBE", sub1.x, sub1.y);
                        rede.descarregaEnvio();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_DOWN){
                        rede.enviaPosicao("DESCE", sub1.x, sub1.y);
                        rede.descarregaEnvio();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_D){
                        rede.enviaPosicao("ATIRAR", sub1.x, sub1.y);
                        rede.descarregaEnvio();
                        Music.playMusic("tiro.wav", false);
                    }
                }
                else{
                    System.out.println("Esperando outro jogador");
                }
            } 
        });

        new Thread() {
            Posicao posSubmarino1 = new Posicao(100, 150);
            Posicao posSubmarino2 = new Posicao(100, 300);
            Visivel v[] = {new Visivel(false), new Visivel(false)};
            Posicao posMoeda[] = {new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0)};
            Posicao posBomba[] = {new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0), new Posicao(0, 0)};
            Posicao posBau[] = {new Posicao(0,0), new Posicao(0,0)};
            Placar placar = jogo.placar();
            Posicao posM1 = new Posicao(0, 0);
            Posicao posM2 = new Posicao(0, 0);
            
            public void run() {
                recebeSinal();
                rede.recebePosicoesObjetos(posMoeda, posBomba, posBau);
                posicionaObjetos(posMoeda, posBomba, posBau);
                jogoIniciado = true;
                if (fase==1) {
                    bau[0].esconde();
                    bau[1].esconde();
                }

                while (rede.continua() && continuaJogo) {
                    String tipo = rede.recebeTipoMsg();
                    rede.recebePosicoes(posSubmarino1, posSubmarino2, posM1, posM2, v[0], v[1]);
                   
                    switch(tipo){
                        case "ATIROU BOMBA":
                            b[rede.recebeObjeto()].esconde();
                            Music.playMusic("tiro.wav", false);
                            break;
                        case "ADV ATIROU BOMBA":
                            b[rede.recebeObjeto()].esconde();
                            Music.playMusic("tiro.wav", false);
                            break;
                        case "ACERTOU BOMBA":
                            b[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            Music.playMusic("tiro.wav", false);
                            alteraPlacar(placar);
                            break;
                        case "ADV ACERTOU BOMBA":
                            b[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            Music.playMusic("tiro.wav", false);
                            break;
                        case "ACERTOU MOEDA":
                            c[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            Music.playMusic("coin.wav", false);
                            alteraPlacar(placar);
                            break;
                        case "ADV ACERTOU MOEDA":
                            c[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            Music.playMusic("coin.wav", false);
                            break;
                        case "ACERTOU BAU":
                            bau[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            Music.playMusic("bau.wav", false);
                            alteraPlacar(placar);
                            break;
                        case "ADV ACERTOU BAU":
                            bau[rede.recebeObjeto()].esconde();
                            rede.recebePlacar(placar);
                            Music.playMusic("bau.wav", false);
                            alteraPlacar(placar);
                            break;
                        case "MOVIMENTO":
                            sub1.x = posSubmarino1.x;
                            sub1.y = posSubmarino1.y;
                            sub2.x = posSubmarino2.x;
                            sub2.y = posSubmarino2.y;
                            m1.x = posM1.x;
                            m1.y = posM1.y;
                            m2.x = posM2.x;
                            m2.y = posM2.y;
                            m1.visivel = v[0].visivel;
                            m2.visivel = v[1].visivel;
                            break;
                        case "MORREU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 0;
                            ganhador = 2;
                            perdedor = 1;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "ADVERSARIO MORREU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 1;
                            ganhador = 1;
                            perdedor = 2;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "GANHOU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 1;
                            ganhador = 1;
                            perdedor = 2; 
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "ADVERSARIO GANHOU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 0;
                            ganhador = 2;
                            perdedor = 1;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "PERDEU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 0;
                            ganhador = 2;
                            perdedor = 1;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "ADVERSARIO PERDEU":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 1;
                            ganhador = 1;
                            perdedor = 2;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "EMPATE":
                            rede.recebePlacar(placar);
                            alteraPlacar(placar);
                            situacao = 2;
                            continuaJogo = false;
                            Music.playMusic("fim.wav", false);
                            break;
                        case "TROCA FASE":
                            fase++;
                            rede.recebePosicoesObjetos(posMoeda, posBomba, posBau);
                            posicionaObjetos(posMoeda, posBomba, posBau);
                            tornaVisivel();
                            break;
                    }
                    repaint();
                }
            }
        }.start();
    }

    public void paintComponent (Graphics g) { 
        super.paintComponent(g);
        int i;
        if (continuaJogo) {
            if (fase==1)
                g.drawImage(fundo1, 0, 0, getSize().width, getSize().height, this);
            else if (fase==2)
                g.drawImage(fundo2, 0, 0, getSize().width, getSize().height, this);
            else
                g.drawImage(fundo3, 0, 0, getSize().width, getSize().height, this);
            if (jogoIniciado) {
                for(i = 0; i < 10; i++){
                    c[i].desenha(g);
                    b[i].desenha(g);
                }
                for (i=0; i<2; i++) {
                    bau[i].desenha(g);
                }
                m2.desenha(g);
                sub2.desenha(g);
            }
            m1.desenha(g);
            sub1.desenha(g);
            placarDesenho1.desenha(g);
            if (jogoIniciado)
                placarDesenho2.desenha(g);
        }
        else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 45));
            switch(situacao) {
                case 0:
                    if (fase==1)
                        g.drawImage(fundo4, 0, 0, getSize().width, getSize().height, this);
                    else if (fase==2)
                        g.drawImage(fundo5, 0, 0, getSize().width, getSize().height, this);
                    else
                        g.drawImage(fundo6, 0, 0, getSize().width, getSize().height, this);
                        g.drawString("VOCE PERDEU!", 250, getSize().height-400);
                    break;
                case 1:
                    if (fase==1)
                        g.drawImage(fundo1, 0, 0, getSize().width, getSize().height, this);
                    else if (fase==2)
                        g.drawImage(fundo2, 0, 0, getSize().width, getSize().height, this);
                    else
                        g.drawImage(fundo3, 0, 0, getSize().width, getSize().height, this);
                        g.drawString("VOCE GANHOU!", 250, getSize().height-400);
                        break;
                    case 2:
                        g.drawImage(fundo7, 0, 0, getSize().width, getSize().height, this);
                        g.drawString("EMPATE!", 250, getSize().height-400);
                    break;
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void alteraPlacar (Placar placar) {
        sub1.p = placar.ptosA;
        sub2.p = placar.ptosB;
        sub1.vida = placar.vidasA;
        sub2.vida = placar.vidasB;
    }

    public void recebeSinal() {
        rede.recebeTipoMsg();
    }

    public void tornaVisivel () {
        int i;
        for (i=0; i<10; i++) {
            c[i].visivel = true;
            b[i].visivel = true;
        }
        if (fase>=2) {
            bau[0].visivel = true;
            bau[1].visivel = true;                     
        }
    }

    public void posicionaObjetos (Posicao posMoeda[], Posicao posBomba[], Posicao posBau[]) {
        int i;
        for (i=0; i<10; i++) {
            c[i].x = posMoeda[i].x;
            c[i].y = posMoeda[i].y;
            b[i].x = posBomba[i].x;
            b[i].y = posBomba[i].y;
        }
        for (i=0; i<2; i++) {
            bau[i].x = posBau[i].x;
            bau[i].y = posBau[i].y;
        }
    }

}

    public class Jogo extends JFrame {
        Placar placar = new Placar(0, 0, 3, 3);
        Rede rede = new Rede(this, "127.0.0.1", 12345);
        AreaJogo area = new AreaJogo(this, rede);

        public Jogo() {
            super("Yellow Submarine");
            setBounds (0,0,700,600);
            add(area);
            pack();
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        public Placar placar() {
            return placar;
        }

        public static void main (String[] args){
            new Jogo();
        }
    }