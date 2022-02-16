import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;


public class Rede {
    Socket socket = null;
    DataInputStream is = null;
    DataOutputStream os = null;
    Jogo jogo;
    boolean temDados = true;

    public Rede (Jogo jogo, String IP, int porto){
        this.jogo = jogo;
        try{
            socket = new Socket(IP, porto);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException erro){
            JOptionPane.showMessageDialog(jogo, "Servidor não encontrado!\n   " + erro, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException erro){
            JOptionPane.showMessageDialog(jogo, "Não pode trocar dados com o servidor!\n   " + erro, "Erro", JOptionPane.ERROR_MESSAGE);            
            System.exit(0);
        }
    }

        public void enviaPosicao(String tipo, int x, int y){
            try{
                os.writeUTF(tipo);
                os.writeInt(x);
                os.writeInt(y);
            } catch (IOException erro){
                temDados = false;
            }
        }

        public void recebePlacar(Placar placar) {
            try {
              placar.ptosA = is.readInt();
              placar.ptosB = is.readInt();
              placar.vidasA = is.readInt();
              placar.vidasB = is.readInt();
            } catch (IOException e) {
              temDados = false;
            }
        }

        public int recebeObjeto () {
            try {
                return is.readInt();
              } catch (IOException e) {
                temDados = false;
                return -1;
              }
        }
        
        public boolean continua() {
            return temDados;
        }
        
        public String recebeTipoMsg() {
            try {
              return is.readUTF();
            } catch (IOException e) {
              temDados = false;
              return "";
            }
        }
        
        public void recebePosicoes (Posicao posSubA, Posicao posSubB, Posicao posM1, Posicao posM2, Visivel visivelM1, Visivel visivelM2) {
            try{
                posSubA.x   = is.readInt();
                posSubA.y   = is.readInt();
                posSubB.x   = is.readInt();
                posSubB.y   = is.readInt();
                posM1.x     = is.readInt();
                posM1.y     = is.readInt();
                posM2.x     = is.readInt();
                posM2.y     = is.readInt();
                visivelM1.visivel = is.readBoolean();
                visivelM2.visivel = is.readBoolean();
            } catch (IOException erro) {
                temDados = false;
            }
        }

        public void recebePosicoesObjetos (Posicao posMoeda[], Posicao posBomba[], Posicao posBaus[]) {
            try {
                int i;
                for (i=0; i<10; i++) {
                    posMoeda[i].x = is.readInt();
                    posMoeda[i].y = is.readInt();
                    posBomba[i].x = is.readInt();
                    posBomba[i].y = is.readInt();
                }
                posBaus[0].x = is.readInt();
                posBaus[0].y = is.readInt();
                posBaus[1].x = is.readInt();
                posBaus[1].y = is.readInt();
            } catch (IOException e) {
                temDados = false;
            }
        }
        public void descarregaEnvio() {
            try{
                os.flush();
            } catch (IOException erro) {
                temDados = false;
            }
        }
}