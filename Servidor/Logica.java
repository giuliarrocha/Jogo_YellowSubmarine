import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Random;

public class Logica extends Thread {
    public Posicao[] posMoeda = {new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0)};
    public Posicao[] posBomba = {new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0), new Posicao(0,0)};
    public Posicao[] posBau = {new Posicao(0,0), new Posicao(0,0)};
    public boolean[] visivelMoeda = { true, true, true, true, true, true, true, true, true, true };
    public boolean[] visivelBomba = { true, true, true, true, true, true, true, true, true, true };
    public boolean[] visivelBau = { false, false };
    final Dimension tamAreaJogo = new Dimension(700, 600);
    public Posicao posSubmarino[] = {new Posicao(20, 150), new Posicao(20, 300)};
    public Posicao posMissel[] = {new Posicao(-55,-55), new Posicao(-55, -55)};
    public boolean[] visivelMissel= {false, false};
    public boolean paradoSubmarino[] = {true, true};
    public int[] ptos = { 0, 0 };
    public int[] vidas = { 3, 3 };
    public int fase = 1;
    public boolean jogando = false;
    Random random = new Random();

    public void moveSubmarino(int numJogador, String comando, int x, int y) {
        switch (comando) {
            case "DIREITA":
                if (posSubmarino[numJogador].x<=700)
                    posSubmarino[numJogador].x += 5;
                break;
            case "ESQUERDA":
                if (posSubmarino[numJogador].x>=0)
                    posSubmarino[numJogador].x -= 5;
                break;
            case "SOBE":
                if (posSubmarino[numJogador].y>=0)
                    posSubmarino[numJogador].y -= 5;
                break;
            case "DESCE":
                if (posSubmarino[numJogador].y<=500)
                    posSubmarino[numJogador].y += 5;
                break;
        }
    }

    public int moveMissel (int numJogador) {
        if (posMissel[numJogador].x<700) {
            (posMissel[numJogador].x) += 5;
            return verificaColisaoBomba(numJogador, 2);
        }
        else {
            visivelMissel[numJogador] = false;
            return -1;
        }     
    }

    public int verificaColisaoBomba(int numJogador, int n) {
        //se n=1 o objeto eh o submarino, se nao eh o missel
        int i;
        if (n==1) {
            for (i = 0; i < 10; i++) {
                if (visivelBomba[i] && (posSubmarino[numJogador].x < (posBomba[i].x + 30))
                        && ((posSubmarino[numJogador].x + 120) > (posBomba[i].x + 5))
                        && (posSubmarino[numJogador].y < (posBomba[i].y + 30))
                        && ((posSubmarino[numJogador].y + 70) > (posBomba[i].y + 5))) {
                    visivelBomba[i] = false;
                    ptos[numJogador] -= 10;
                    vidas[numJogador]--;
                    if (numJogador==0) {
                        posSubmarino[numJogador].x = 20;
                        posSubmarino[numJogador].y = 150;
                    }
                    else {
                        posSubmarino[numJogador].x = 20;
                        posSubmarino[numJogador].y = 300;
                    }
                    return i;
                }
            }
        }
        else {
            for (i = 0; i < 10; i++) {
                if (visivelBomba[i] && (posMissel[numJogador].x < (posBomba[i].x + 30))
                        && ((posMissel[numJogador].x + 35) > (posBomba[i].x + 5))
                        && (posMissel[numJogador].y < (posBomba[i].y + 30))
                        && ((posMissel[numJogador].y + 35) > (posBomba[i].y + 5))) {
                    visivelMissel[numJogador] = false;
                    visivelBomba[i] = false;
                    return i;
                }
            }
        }
        return -1;
    }

    public int verificaColisaoMoeda(int numJogador) {
        int i;
        for (i = 0; i < 10; i++) {
            if (visivelMoeda[i] && (posSubmarino[numJogador].x < (posMoeda[i].x + 30))
                    && ((posSubmarino[numJogador].x + 120) > (posMoeda[i].x + 5))
                    && (posSubmarino[numJogador].y < (posMoeda[i].y + 30))
                    && ((posSubmarino[numJogador].y + 70) > (posMoeda[i].y + 5))) {
                visivelMoeda[i] = false;
                ptos[numJogador] += 10;
                return i;
            }
        }
        return -1;
    }

    public int verificaColisaoBau(int numJogador) {
        int i;
        for (i = 0; i < 2; i++) {
            if (visivelBau[i] && (posSubmarino[numJogador].x < (posBau[i].x + 60))
                    && ((posSubmarino[numJogador].x + 120) > (posBau[i].x + 5))
                    && (posSubmarino[numJogador].y < (posBau[i].y + 50))
                    && ((posSubmarino[numJogador].y + 70) > (posBau[i].y + 5))) {
                visivelBau[i] = false;
                ptos[numJogador] += 50;
                return i;
            }
        }
        return -1;
    }

    public boolean existenciaMoedas() {
        int i;
        for (i = 0; i < 10; i++) {
            if (visivelMoeda[i])
                return true;
        }
        return false;
    }

    public void tornaVisivel(int numJogador, int numAdversario) {
        int i;
        for (i = 0; i < 10; i++) {
            visivelBomba[i] = true;
            visivelMoeda[i] = true;
        }
        for (i=0; i<2; i++) {
            visivelBau[i] = true;
        }
        if (numJogador==0) {
            posSubmarino[numJogador].x = 20;
            posSubmarino[numJogador].y = 150;
            posSubmarino[numAdversario].x = 20;
            posSubmarino[numAdversario].y = 300;
        }
        else {
            posSubmarino[numJogador].x = 20;
            posSubmarino[numJogador].y = 300;
            posSubmarino[numAdversario].x = 20;
            posSubmarino[numAdversario].y = 150;
        }
    }

    public void geraCoordenadas () {
        int i;
        for (i=0; i<10; i++) {
            posMoeda[i].x = random.nextInt(500) + 150;
            posBomba[i].x = random.nextInt(500) + 150;
        }
        for (i=0; i<2; i++) {
            posBau[i].x = random.nextInt(500) + 150;
            posBau[i].y = random.nextInt(400) + 10;
        }
        posMoeda[0].y = 70;
        posMoeda[1].y = 70;
        posMoeda[2].y = 150;
        posMoeda[3].y = 200;
        posMoeda[4].y = 250;
        posMoeda[5].y = 350;
        posMoeda[6].y = 400;
        posMoeda[7].y = 400;
        posMoeda[8].y = 450;
        posMoeda[9].y = 500;
        posBomba[0].y = 70;
        posBomba[1].y = 70;
        posBomba[2].y = 150;
        posBomba[3].y = 200;
        posBomba[4].y = 250;
        posBomba[5].y = 350;
        posBomba[6].y = 400;
        posBomba[7].y = 400;
        posBomba[8].y = 450;
        posBomba[9].y = 500;
    }

    public void apareceMissel (int numJogador) {
        visivelMissel[numJogador] = true;
    }

    public void escondeMissel (int numJogador) {
        visivelMissel[numJogador] = false;
    }

    public Posicao posicaoMoeda(int i) {
        return new Posicao(posMoeda[i].x, posMoeda[i].y);
    }

    public Posicao posicaoBomba(int i) {
        return new Posicao(posBomba[i].x, posBomba[i].y);
    }

    public Posicao posicaoBau(int i) {
        return new Posicao(posBau[i].x, posBau[i].y);
    }

    public Posicao posicaoSubmarino (int numJogador){
        return posSubmarino[numJogador];
    }

    public Posicao posicaoMissel (int numJogador) {
        return posMissel[numJogador];
    }

    public boolean estadoSubmarino (int numJogador) {
        return paradoSubmarino[numJogador];
    }

    public int mudaFase () {
        if (fase<3) {
            fase++;
            return 1;
        }
        else
            return 0;
    }
}