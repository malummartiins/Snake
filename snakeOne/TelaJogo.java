import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class TelaJogo extends JPanel implements ActionListener {

    private static final int LARGURA_TELA = 750;
    private static final int ALTURA_TELA = 750;
    private static final int TAMANHO_BLOCO = 50;
    private static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private static final int INTERVALO = 200;
    private static final String NOME_FONTE = "Ink Free";
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoY = new int[UNIDADES];
    private int corpoCobra = 6;
    private int blocosComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';
    private boolean estaRodando = false;
    Timer timer;
    Random random;

    TelaJogo() {
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new LeitorDeTeclasAdapter());
        iniciarJogo();
    }

    public void iniciarJogo() {
        criarBloco();
        estaRodando = true;
        timer = new Timer(INTERVALO, this);
        timer.start();
    }

    private void criarBloco() {
        blocoX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        blocoY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    public void desenharTela(Graphics g) {
        if (estaRodando) {
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            for (int i = 0; i < corpoCobra; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(eixoX[0], eixoY[0], TAMANHO_BLOCO, TAMANHO_BLOCO);

                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(
                    "Pontos: " + blocosComidos,(LARGURA_TELA - metrics.stringWidth("Pontuação " + blocosComidos)) / 2,
                    getFont().getSize());
        } else {
            fimDeJogo(g);
        }
    }

    public void fimDeJogo(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics fontePontuacao = getFontMetrics(g.getFont());
        g.drawString(
                "Pontos: " + blocosComidos,(LARGURA_TELA - fontePontuacao.stringWidth("Pontuação " + blocosComidos)) / 2,
                getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("FIM DE JOGO", (LARGURA_TELA - fonteFinal.stringWidth("FIM DE JOGO")) / 2,
                getFont().getSize());

    }

    public void actionPerformed(ActionEvent e) {
        if (estaRodando) {
            andar();
            alcancarBloco();
            validarLimites();
        }
        repaint();
    }

    private void andar() {
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }
        switch (direcao) {
            case 'C':
                eixoY[0] = eixoY[0] - TAMANHO_BLOCO;
                break;
            case 'B':
                eixoY[0] = eixoY[0] + TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] = eixoX[0] - TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_BLOCO;
                break;
            default:
                break;
        }
    }

    private void alcancarBloco() {
        if (eixoX[0] == blocoX && eixoY[0] == blocoY){
            corpoCobra++;
            blocosComidos++;
            criarBloco();
        }
    }

     private void validarLimites(){
        //a cabeça bateu no corpo?
        for (int i = corpoCobra; i > 0; i--){
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]){
                estaRodando = false;
                break;
            }
        }

        //cabeça tocou uma das boras
        if (eixoX[0] < 0 || eixoX[0] > LARGURA_TELA){
            estaRodando = false;
        }
        // cabeça tocou piso ou teto?
        if (eixoY[0] < 0 || eixoY[0] > ALTURA_TELA){
            estaRodando = false;
        }
        if(!estaRodando){
            timer.stop();
        }
     } 
     
     public class LeitorDeTeclasAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direcao != 'D'){
                        direcao = 'E';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direcao != 'E'){
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direcao != 'B'){
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direcao != 'C'){
                        direcao = 'B';
                    }
                    break;
                    default:
                    break;
            }
        }
     }
}
