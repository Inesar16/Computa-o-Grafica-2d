package jogomodel;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class jogo2D extends JFrame {
	
	public jogo2D() {
       
        setTitle("Shadow");
        //define que o programa termina assim que o jogador clica no X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //nesta class é desenhada toda a tela
        GamePanel panel = new GamePanel();
        getContentPane().add(panel);
        
        //Ajusta automaticamente o tamanho da janela (800x600)
        pack();
        //Coloca a janela no centro do ecrã (posição relativa a null)
        setLocationRelativeTo(null);
        //Torna a janela visível (por padrão, as janelas Swing são false)
        setVisible(true);
        
    }

	public static void main(String[] args) {
		
		new jogo2D();
	}
	
}
