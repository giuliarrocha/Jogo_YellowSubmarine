import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ServindoCliente extends Thread {
    Socket clientSocket;
    StreamsDeSaida saida;
    Logica logica;
    int numJogador, numAdversario;
    int x, y, m, b, caso, colisaoMoeda, colisaoBomba, colisaoBau;
    boolean clienteVivo[] = {true, true}; //verifica se algum jogador saiu do jogo (fechou a janela)
    boolean jogadorVivo[] = {true, true}; //verifica se algum jogador perdeu todas as vidas no jogo
    Timer t;

    public ServindoCliente (Socket clientSocket, StreamsDeSaida saida, Logica logica) {
        this.clientSocket = clientSocket;
        this.saida = saida;
        this.logica = logica;
        numJogador = saida.cont++;
        numAdversario = 1 - numJogador; 
        int i;
        logica.geraCoordenadas();
    }

    public void run() {
        try {
          DataInputStream is = new DataInputStream(clientSocket.getInputStream());
          saida.os[numJogador] = new DataOutputStream(clientSocket.getOutputStream());
          String tipo;
    
          if (numJogador == 1) {
            enviaSinal();
            logica.jogando = true;
            enviaPosicoesObjetos();
            enviaPosicoesObjetosAdversario();
            preparaTemporizador();
            descarregaEnvio();
          }

          do {
            if (!logica.existenciaMoedas()) {
                if (logica.mudaFase()!=0) {
                    logica.geraCoordenadas();
                    logica.tornaVisivel(numJogador,numAdversario);
                    synchronized (logica) {
                        enviaTipo("TROCA FASE");
                        enviaTipoAdversario("TROCA FASE");
                        enviaPosicoesObjetos();
                        enviaPosicoesObjetosAdversario();
                        descarregaEnvio();
                    }
                    try {
                        sleep(20);
                    } catch (InterruptedException e) {}
                }
                else {
                    if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                        synchronized (logica) {
                            enviaTipo("GANHOU");
                            enviaTipoAdversario("PERDEU");
                            enviaPlacar();
                            descarregaEnvio();
                        }
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {}
                    }
                    else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                        synchronized (logica) {
                            enviaTipo("PERDEU");
                            enviaTipoAdversario("GANHOU");
                            enviaPlacar();
                            descarregaEnvio();
                        }
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {}
                    }
                    else {
                        synchronized (logica) {
                            enviaTipo("EMPATE");
                            enviaTipoAdversario("EMPATE");
                            enviaPlacar();
                            descarregaEnvio();
                        }
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {}    
                    }
                }
            }
            tipo = is.readUTF();
            x = is.readInt();
            y = is.readInt();
            logica.moveSubmarino(numJogador, tipo, x, y);
            switch (tipo) {
                case "ATIRAR":
                    logica.posMissel[numJogador].x = x+50;
                    logica.posMissel[numJogador].y = y+30;
                    logica.apareceMissel(numJogador);
                    break;
                default:
                    colisaoMoeda = logica.verificaColisaoMoeda(numJogador);
                    colisaoBomba = logica.verificaColisaoBomba(numJogador,1);
                    colisaoBau = logica.verificaColisaoBau(numJogador);
                    if (colisaoMoeda!=-1){
                        synchronized (logica) {
                            enviaTipo("ACERTOU MOEDA");
                            enviaTipoAdversario("ADV ACERTOU MOEDA");
                            envia(colisaoMoeda);
                            enviaAdversario(colisaoMoeda);
                            enviaPlacar();
                            enviaPlacarAdversario();
                            descarregaEnvio();
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException erro) {}
                    }
                    if (colisaoBau!=-1){
                        synchronized (logica) {
                            enviaTipo("ACERTOU BAU");
                            enviaTipoAdversario("ADV ACERTOU BAU");
                            envia(colisaoBau);
                            enviaAdversario(colisaoBau);
                            enviaPlacar();
                            enviaPlacarAdversario();
                            descarregaEnvio();
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException erro) {}
                    }
                    if (colisaoBomba!=-1) {
                        synchronized(logica) {
                            enviaTipo("ACERTOU BOMBA");
                            enviaTipoAdversario("ADV ACERTOU BOMBA");
                            envia(colisaoBomba);
                            enviaAdversario(colisaoBomba);
                            enviaPlacar();
                            enviaPlacarAdversario();
                            descarregaEnvio();
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException erro) {}
                    }
                    if (logica.vidas[numJogador]==0) {
                        synchronized (logica) {
                            enviaTipo("MORREU");
                            enviaTipoAdversario("ADVERSARIO MORREU");
                            enviaPlacar();
                            enviaPlacarAdversario();
                            jogadorVivo[numJogador] = false;
                            descarregaEnvio();
                        }
                        try {
                            sleep(20);
                        } catch (InterruptedException erro) {}
                    }
            }
            
          } while (clienteVivo[numJogador] && clienteVivo[numAdversario] && jogadorVivo[numJogador] && jogadorVivo[numAdversario]);
          saida.os[numJogador].close();
          is.close();
          clientSocket.close();
    
        }catch(IOException e){
        try {
          clientSocket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        }catch(NoSuchElementException e){
        }
    }

    public void preparaTemporizador() {
        new Thread() {
          public void run() {
            while (clienteVivo[numJogador] && clienteVivo[numAdversario]) {
              try {
                    temporizadorLogica();
                    sleep(20);
              } catch (InterruptedException e) {
              }
            }
          }
        }.start();
      }
    
      public void temporizadorLogica() {
        int b;
        if (logica.visivelMissel[numJogador]) {
            b = logica.moveMissel(numJogador);
            if (b!=-1) {
                synchronized (logica) {
                    enviaTipo("ATIROU BOMBA");
                    enviaTipoAdversario("ADV ATIROU BOMBA");
                    envia(b);
                    enviaAdversario(b);
                    descarregaEnvio();
                }
                try {
                    sleep(20);
                } catch (InterruptedException erro) {}
            }
        }
        if (logica.visivelMissel[numAdversario]) {
            b = logica.moveMissel(numAdversario);
            if (b!=-1) {
                synchronized (logica) {
                    enviaTipoAdversario("ATIROU BOMBA");
                    enviaTipo("ADV ATIROU BOMBA");
                    enviaAdversario(b);
                    envia(b);
                    descarregaEnvio();
                }
                try {
                    sleep(20);
                } catch (InterruptedException erro) {}
            }
        }
        synchronized (logica) {
          enviaTipo("MOVIMENTO");
          enviaTipoAdversario("MOVIMENTO");
          descarregaEnvio();
        }
      }

    public void enviaSinal() {
        try {
          envia("PRONTO");
          enviaAdversario("PRONTO");
          sleep(500);
        } catch (InterruptedException erro) {}
    }


    public void descarregaEnvio() {
        try{
            saida.os[numJogador].flush();
            saida.os[numAdversario].flush();
        } catch (IOException erro){

        }
    }

    public void envia(String s){
        if(clienteVivo[numJogador]){
            try{
                saida.os[numJogador].writeUTF(s);
            } catch (IOException erro){
                clienteVivo[numJogador] = false;
                if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                    synchronized (logica) {
                        enviaTipoAdversario("PERDEU");
                        enviaPlacarAdversario();
                        descarregaEnvio();
                    } 
                }
                else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                    synchronized (logica) {
                        enviaTipoAdversario("GANHOU");
                        enviaPlacarAdversario();
                        descarregaEnvio();
                    }
                }
                else {
                    synchronized (logica) {
                        enviaTipoAdversario("EMPATE");
                        enviaPlacarAdversario();
                        descarregaEnvio();
                    }
                }  
            }
        }
    }

    private void enviaAdversario(String s) {
        if (clienteVivo[numAdversario]) {
            try {
                saida.os[numAdversario].writeUTF(s);
            } catch (IOException erro) {
                clienteVivo[numAdversario] = false;
                if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                    enviaTipo("GANHOU");
                    enviaPlacar();
                }
                else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                    enviaTipo("PERDEU");
                    enviaPlacar();
                }
                else {
                    enviaTipo("EMPATE");
                    enviaPlacar();
                }
            }
        }
    }

    public void envia(boolean b){
        if(clienteVivo[numJogador]){
            try{
                saida.os[numJogador].writeBoolean(b);
            } catch (IOException erro){
                clienteVivo[numJogador] = false;
                if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                    enviaTipoAdversario("PERDEU");
                    enviaPlacarAdversario();
                }
                else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                    enviaTipoAdversario("GANHOU");
                    enviaPlacarAdversario();
                }
                else {
                    enviaTipoAdversario("EMPATE");
                    enviaPlacarAdversario();
                }  
            }
        }
    }

    private void enviaAdversario(boolean b) {
        if (clienteVivo[numAdversario]) {
            try {
                saida.os[numAdversario].writeBoolean(b);
            } catch (IOException erro) {
                clienteVivo[numAdversario] = false;
                if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                    enviaTipo("GANHOU");
                    enviaPlacar();
                }
                else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                    enviaTipo("PERDEU");
                    enviaPlacar();
                }
                else {
                    enviaTipo("EMPATE");
                    enviaPlacar();
                }
            }
        }
    }

    private void envia(int i) {
        if (clienteVivo[numJogador]) {
          try {
            saida.os[numJogador].writeInt(i);
          } catch (IOException erro) {
            clienteVivo[numJogador] = false;
            if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                enviaTipoAdversario("PERDEU");
                enviaPlacarAdversario();
            }
            else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                enviaTipoAdversario("GANHOU");
                enviaPlacarAdversario();
            }
            else {
                enviaTipoAdversario("EMPATE");
                enviaPlacarAdversario();
            }
          }
        }
    }
    
    private void enviaAdversario(int i) {
        if (clienteVivo[numAdversario]) {
          try {
            saida.os[numAdversario].writeInt(i);
          } catch (IOException erro) {
            clienteVivo[numAdversario] = false;
            if (logica.ptos[numJogador]>logica.ptos[numAdversario]) {
                enviaTipo("GANHOU");
                enviaPlacar();
            }
            else if (logica.ptos[numJogador]<logica.ptos[numAdversario]) {
                enviaTipo("PERDEU");
                enviaPlacar();
            }
            else {
                enviaTipo("EMPATE");
                enviaPlacar();
            }
          }
        }
    }
    
    private void enviaTipo(String tipo) {
        envia(tipo);
        enviaPosicoes();
    }

    private void enviaTipoAdversario(String tipo) {
        enviaAdversario(tipo);
        enviaPosicoesAdversario();
    }
    
    private void enviaPlacar() {
        envia(logica.ptos[numJogador]);
        envia(logica.ptos[numAdversario]);
        envia(logica.vidas[numJogador]);
        envia(logica.vidas[numAdversario]);
    }
    
    private void enviaPlacarAdversario() {
        enviaAdversario(logica.ptos[numAdversario]);
        enviaAdversario(logica.ptos[numJogador]);
        enviaAdversario(logica.vidas[numAdversario]);
        enviaAdversario(logica.vidas[numJogador]);
    }
    
    private void enviaPosicao(Posicao p) {
        envia(p.x);
        envia(p.y);
    }
    
    private void enviaPosicaoAdversario(Posicao p) {
        enviaAdversario(p.x);
        enviaAdversario(p.y);
    }
    private void enviaPosicoesAdversario() {
    //    enviaPosicaoAdversario(logica.posicaoBarata());
        enviaPosicaoAdversario(logica.posicaoSubmarino(numAdversario));
        enviaPosicaoAdversario(logica.posicaoSubmarino(numJogador));
        enviaPosicaoAdversario(logica.posicaoMissel(numAdversario));
        enviaPosicaoAdversario(logica.posicaoMissel(numJogador));
        enviaAdversario(logica.visivelMissel[numAdversario]);
        enviaAdversario(logica.visivelMissel[numJogador]);
    }
    
    private void enviaPosicoes() {
    //    enviaPosicao(logica.posicaoBarata());
        enviaPosicao(logica.posicaoSubmarino(numJogador));
        enviaPosicao(logica.posicaoSubmarino(numAdversario));
        enviaPosicao(logica.posicaoMissel(numJogador));
        enviaPosicao(logica.posicaoMissel(numAdversario));
        envia(logica.visivelMissel[numJogador]);
        envia(logica.visivelMissel[numAdversario]);
    }

    private void enviaPosicoesObjetos () {
        int i;
        for (i=0; i<10; i++) {
            enviaPosicao(logica.posicaoMoeda(i));
            enviaPosicao(logica.posicaoBomba(i));
        }
        enviaPosicao(logica.posicaoBau(0));
        enviaPosicao(logica.posicaoBau(1));
        
    }

    private void enviaPosicoesObjetosAdversario () {
        int i;
        for (i=0; i<10; i++) {
            enviaPosicaoAdversario(logica.posicaoMoeda(i));
            enviaPosicaoAdversario(logica.posicaoBomba(i));
        }
        enviaPosicaoAdversario(logica.posicaoBau(0));
        enviaPosicaoAdversario(logica.posicaoBau(1));
        
    }
};