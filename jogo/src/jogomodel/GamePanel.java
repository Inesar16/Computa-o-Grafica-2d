package jogomodel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.GradientPaint;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
	
    // Tamanho da janela
    public static final int WIDTH = 1090, HEIGHT = 700;
	
	// Configuração da grelha (mundo lógico)
    public static final int COLS = 34;        // número de colunas
    public static final int ROWS = 22;        // número de linhas
    public static final int CELL_SIZE = 32;   // tamanho de cada célula em píxeis
    
    // Tipos de célula
    private static final int FLOOR = 0;
    private static final int WALL = 1;
    private static final int TOWER = 2;
    private static final int PORTAL = 3;
    private static final int HELPPOINT = 4;
    

    private final int[][] mapa = {
    	    /*0*/{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    	    /*1*/{1,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,1,1,0,0,1,1,1,0,1,0,0,0,0,1,0,0,0,1},
    	    /*2*/{1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1,0,0,1,0,1,0,1,1,0,1,0,1,0,1},
    	    /*3*/{1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,1,0,1,0,1,0,0,1,0,1,0,1},
    	    /*4*/{1,0,1,0,1,0,0,0,0,0,1,0,1,0,1,0,1,0,1,0,0,1,0,0,1,0,1,0,1,0,0,1,0,1},
    	    /*5*/{1,0,1,0,1,1,1,1,1,0,1,0,0,0,1,0,1,0,0,0,1,1,0,1,1,0,1,0,1,1,1,1,0,1},
    	    /*6*/{1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1},
    	    /*7*/{1,1,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0,0,1},
    	    /*8*/{1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,1,1},
    	    /*9*/{1,0,1,1,1,0,1,0,0,0,1,0,1,1,1,0,1,1,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1},
    	    /*10*/{1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,1},
    	    /*11*/{1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,1,1,0,1,0,1,0,1,1},
    	    /*12*/{1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,1},
    	    /*13*/{1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1},
    	    /*14*/{1,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,1,1},
    	    /*15*/{1,0,1,0,1,1,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,1},
    	    /*16*/{1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,1},
    	    /*17*/{1,1,1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1},
    	    /*18*/{1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1},
    	    /*19*/{1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1},
    	    /*20*/{1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1},
    	    /*21*/{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    	};

    // Modelo do labirinto
    private int[][] lab = new int[ROWS][COLS];
    
    // Posição inicial do player
    public int playerRow = 1;
    public int playerCol = 1;
    
    // Posição da Torre 
    private final int towerRow = 7;
    private final int towerCol = 32;
    
    // Estado
    private boolean ganhou = false;
    
    // declarar variaveis de imagem
    private BufferedImage playerImg;
    private BufferedImage TowerImg;
    private BufferedImage[] walk = new BufferedImage[12]; // 12 frames para criar a animação
    
    //contador de frames para a animação
    private int frame = 0;
    
    // Animação
    private boolean movingAnim = false;
    private int animTicksLeft = 0;   // quantos "avanços" de frame faltam
    private Timer animTimer;         // avança o frame de X em X ms
    
    // botão imprimir
    private Rectangle printButton;
    
    //garante o foco no painel principal
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
    
    public GamePanel() {
        // Diz ao Swing qual o tamanho ideal deste painel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Cor de fundo padrão (vai aparecer se não desenharmos por cima)
        setBackground(Color.BLACK);
       
        gerarLabirinto();
        colocarPortais();
        colocarHelpPoint();
        carregarTorre();
        
        carregarTowerImagem();
        carregarPlayerImagem();     
        
        // Input teclado
        
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (ganhou) {
                	if(e.getKeyCode()== KeyEvent.VK_R) reiniciarJogo();
                	//if (e.getKeyCode() == KeyEvent.VK_ENTER) saveScreenshot();
                	return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:    moverJogador(-1, 0); break;
                    case KeyEvent.VK_DOWN:  moverJogador( 1, 0); break;
                    case KeyEvent.VK_LEFT:  moverJogador( 0,-1); break;
                    case KeyEvent.VK_RIGHT: moverJogador( 0, 1); break;
                }
            }
        });
        // Clique do botão “Imprimir” só quando houve vitória
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!ganhou) return;                         // só no Game Over/Vitória
                if (printButton != null && printButton.contains(e.getPoint())) {
               	 saveScreenshot();
                }
            }
        });
    }
    
    
    // --- LABIRINTO ---

    // Função que constroi o labirinto
    private void gerarLabirinto() {

    	for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r < mapa.length && c < mapa[r].length) {
                    lab[r][c] = mapa[r][c];
                } else {
                    lab[r][c] = WALL; // segurança se faltar algo
                }
            }
        }
        // garantir que posição inicial do player é caminho
        lab[playerRow][playerCol] = FLOOR;
    }
    
    // --- PORTAIS ---
    //Função que define o local dos Portais 
    private void colocarPortais() {
    	// Portal 1: linha 9, colunas 7 a 9
    	objetoLinha(8, 6, 8);

        // Portal 2: linha 14, colunas 15 a 17
    	objetoLinha(13, 14, 16);

        // Portal 3: linha 4, colunas 25 a 27
    	objetoLinha(4, 23, 25);
    }
    
    //horientação por linha para Portais
    private void objetoLinha(int row, int cStart, int cEnd) {
        for (int c = cStart; c <= cEnd; c++) {
            lab[row][c] = PORTAL;
        }
    }
    
    // --- HELPPOINTS ---
    //Função que define o local dos HelpPoints  
    private void colocarHelpPoint() {
    	// Help 1: coluna 11, linhas 18 a 20
    	objetoColuna(10, 17, 19);

        // Help 2: coluna 19, linhas 16 a 18
    	objetoColuna(21, 15, 17);

        // Help 3: coluna 29, linhas 8 a 10
    	objetoColuna(28, 7, 9);
    }
    
    //horientação por coluna para HelpPoints
    private void objetoColuna(int col, int rowStart, int rowEnd) {
        for (int r = rowStart; r <= rowEnd; r++) {
            lab[r][col] = HELPPOINT;
        }
    }
    
    // --- TORRE --- 
    private void carregarTorre() {
    	lab[towerRow][towerCol] = TOWER;
    }

    // -- carregar imagem da torre --
    
    private void carregarTowerImagem() {
        try {
            // ler imagem png
        	TowerImg = ImageIO.read(
                getClass().getResource("/image/Water_ruins1.png"));
        } catch (Exception e) {
            System.out.println("Erro a carregar imagem do player: " + e.getMessage());
            TowerImg = null; 
        }
    }
    
    // --- PLAYER --- 
    // -- carregar imagem do player --
    
    private void carregarPlayerImagem() {
    	for (int i = 1; i <= 12; i++) {
            String path = "/Player/" + i + ".png"; // <-- CAMINHO CERTO
            try (InputStream in = getClass().getResourceAsStream(path)) {
                if (in == null) throw new IllegalStateException("Recurso não encontrado: " + path);
                walk[i-1] = ImageIO.read(in);
            } catch (Exception ex) {
                System.err.println("Erro a carregar " + path + ": " + ex.getMessage());
                walk[i-1] = null;
            }
        }
    	iniciarAnimTimer(80); 
    }
    
    // --- Animação do Player ---
    private void iniciarAnimTimer(int delayMs) {
    	animTimer = new Timer(delayMs, e -> {
    		if (!movingAnim) return;         // só avança quando animamos
    		frame = (frame + 1) % 12;        // próximo frame
    		if (--animTicksLeft <= 0) {      // terminou a “rajada” da animação
    			movingAnim = false;
    		}
    		repaint();
    	});
    	animTimer.setRepeats(true);
    	animTimer.start();
    }

    // --- Move Player --- 
    private void moverJogador(int dRow, int dCol) {
        int novoRow = playerRow + dRow;
        int novoCol = playerCol + dCol;

        // fora da grelha
        if (novoRow < 0 || novoRow >= ROWS || novoCol < 0 || novoCol >= COLS) return;

        int destino = lab[novoRow][novoCol];

        // não atravessa paredes
        if (destino == WALL) return;

        // move
        playerRow = novoRow;
        playerCol = novoCol;
        
        movingAnim = true;
        animTicksLeft = 16;   // ajusta a duração do “passo animado”
        frame = 0; 

        // verifica se chegou à torre
        if (destino == TOWER) {
            ganhou = true;
        }

        // (no futuro: se destino == PORTAL -> ativa visão reduzida)
        // (se destino == HELPPOINT -> remove penalização, etc.)

        repaint();
    }
    
    private void reiniciarJogo() {
    	//estado
    	ganhou = false;
    	
    	//respaw do player
    	playerRow = 1;
    	playerCol = 1;
    	
    	//reconstruir labirinto e objetos
    	gerarLabirinto();
        colocarPortais();
        colocarHelpPoint();
        carregarTorre();
    	
    	repaint();
    }
    
    // --- DESENHO DOS HELPPOINTS POR AREAS --- 
    private void drawHelpPointArea(Graphics2D g2, int x, int y) {
        // ocupa 1x3 células (vertical)
        int w = CELL_SIZE;
        int h = CELL_SIZE * 3;

        // Construção por boolean ops: “cápsula” arredondada
        Area a = new Area(new Ellipse2D.Double(x, y, w, w));                          // topo
        a.add(new Area(new Rectangle2D.Double(x, y + w/2.0, w, h - w)));              // corpo
        a.add(new Area(new Ellipse2D.Double(x, y + h - w, w, w)));                    // base

        // preenchimento semi-transparente (deixa ver o chão/parede por baixo)
        GradientPaint gpFill = new GradientPaint(x, y,new Color(150, 200, 240, 150), x + w , y ,new Color(4, 118, 208, 255));
        
        g2.setPaint(gpFill);
        g2.fill(a);

        // brilho interior suave (segunda passada)
        GradientPaint gpGlow = new GradientPaint(
            x, y + h/3f, new Color(180, 235, 255, 120),
            x, y + h,     new Color(180, 235, 255, 0), true
        );
        g2.setPaint(gpGlow);
        g2.fill(a);

        // contorno subtil para recorte visual
        g2.setPaint(new Color(255, 255, 255, 90));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(a);
    }

    // --- DESENHO DOS PORTAL POR PATH ---
    private void drawPortalPath(Graphics2D g2, int x, int y) {
        // ocupa 3x1 células (horizontal)
        int w = CELL_SIZE * 3;
        int h = CELL_SIZE;

        // “portal” com curvas (Path2D + Beziers)
        Path2D path = new Path2D.Double();
        double r = h * 0.45;               // raio aproximado nas bordas
        double cx = x + w / 2.0;
        double cy = y + h / 2.0;

        // forma alongada com “boca” ondulada
        path.moveTo(x + r, y);
        path.curveTo(x + r*0.4, y + h*0.05, cx - r, y + h*0.12, cx, y + h*0.28);
        path.curveTo(cx + r, y + h*0.42, x + w - r*0.4, y + h*0.05, x + w - r, y);
        path.quadTo(x + w - r*0.3, y + h*0.52, x + w - r*0.8, y + h - 1);
        path.curveTo(cx + r, y + h*0.92, cx - r, y + h*0.88, x + r*0.8, y + h - 1);
        path.quadTo(x + r*0.35, y + h*0.52, x + r, y);
        path.closePath();

        // preenchimento translúcido (deixa ver o mosaico por baixo)
        GradientPaint gp = new GradientPaint(x, y,new Color(230, 120, 255, 230),x,  y + h /0.8f, Color.BLACK);
        g2.setPaint(gp);
        g2.fill(path);

        // “aura”/contorno
        g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
        g2.setPaint(new Color(255, 200, 255, 140));
        g2.setStroke(new BasicStroke(3f));
        g2.draw(path);

        // reset do alpha
        g2.setComposite(AlphaComposite.SrcOver);
    }
    
    // --- PaintComponent ---
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Conversão para Graphics2D -> API completa de Computação Gráfica 2D
        Graphics2D g2 = (Graphics2D) g;
        
        // Desenhar cada célula do labirinto
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                int tipo = lab[row][col];
                
                if(tipo == WALL){
                	g2.setColor(new Color(140, 140, 170));
                }else {
                    g2.setColor(new Color(60, 60, 70));
                }
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
            	
            	 int x = col * CELL_SIZE;
                 int y = row * CELL_SIZE;
                 int tipo = lab[row][col];
            	
                switch (tipo) {
                case TOWER:
                    if (TowerImg != null) {
                        g2.drawImage(TowerImg, x, y, CELL_SIZE, CELL_SIZE, null);
                    } else {
                        g2.setColor(new Color(230, 220, 70));
                        g2.fillRect(x + 4, y + 4, CELL_SIZE - 8, CELL_SIZE - 8);
                    }
                    break;

                case PORTAL:
                	if (col + 2 < COLS && lab[row][col + 1] == PORTAL && lab[row][col + 2] == PORTAL ){
                		
                		drawPortalPath(g2, x, y);
                     }
                    break;

                case HELPPOINT:
                	if (row + 2 < ROWS && lab[row + 1][col] == HELPPOINT && lab[row + 2][col] == HELPPOINT)  {
                		
                		drawHelpPointArea(g2, x, y);
                		
                    }
                    break;
                }

            // linhas da grelha
            g2.setColor(new Color(80, 80, 100));
            g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // --- PLAYER ---
        
        // desenhar o player por cima do mapa
        int px = playerCol * CELL_SIZE;
        int py = playerRow * CELL_SIZE;
        
        BufferedImage current = null;
        if (walk[0] != null) {
            current = movingAnim ? walk[frame] : walk[0];  // parado mostra o primeiro frame
        }

        if (current != null) {
            g2.drawImage(current, px, py, CELL_SIZE, CELL_SIZE, null);
        } else if (playerImg != null) {
            // fallback se não houver frames
            g2.drawImage(playerImg, px, py, CELL_SIZE, CELL_SIZE, null);
        } else {
            // fallback final: rect verde
            g2.setColor(new Color(0, 200, 170));
            g2.fillRoundRect(px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8, 10, 10);
        }
        
        // --- TEXTO ---
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Amasis", Font.BOLD, 16));
        g2.drawString("Setas = mover | Amarelo = Torre | Roxo = Portal | Azul = Ajuda", 10, 20);
        
        if (ganhou) {
        	drawScreenOver(g2);
        }    
    }
    
    // --- Desenho do gameover scren ---
    private void drawScreenOver(Graphics2D g2) {
    	//escorecer o fundo
    	g2.setColor(new Color(0,0,0,150));
    	g2.fillRect(0, 0, WIDTH, HEIGHT);
    	
    	// -- titulo --
    	g2.setFont(new Font("Algerian", Font.BOLD, 48));
    	g2.setColor(new Color(255,230,120));
    	var fm = g2.getFontMetrics();
    	String titulo = "Ganhaste !!";
    	
    	//alterar tamanho e posição
    	int tx = (WIDTH - fm.stringWidth(titulo))/2;
    	int ty = (HEIGHT / 2 - fm.getHeight())/2;
    	g2.drawString(titulo, tx, ty);
    	
    	// -- subtitulo --
    	g2.setFont(new Font("Algerian", Font.PLAIN, 40));
    	g2.setColor(new Color(255,230,120));
    	var fmsub = g2.getFontMetrics();
    	String replay = "Pressiona R para voltar a jogar";
    	int dx = (WIDTH - fmsub.stringWidth(replay))/2;
    	int dy = ty + fmsub.getHeight() + 12;
    	g2.drawString(replay, dx, dy);
    	
    	// BOTÕES 
	    int buttonWidth = 200;
	    int buttonHeight = 50;

	    int centerX = (WIDTH - buttonWidth) / 2;
	    int buttonsY = (HEIGHT / 2) + 180; // por baixo do texto

	    // desenha e guarda bounds
	    printButton = drawButton(g2, centerX,   centerX, buttonWidth, buttonHeight, "Imprimir");
    }
    
    private Rectangle drawButton(Graphics2D g2, int x, int y, int w, int h, String label) {
  	  // sombra
  	  g2.setColor(new Color(0, 0, 0, 80));
  	  Shape shadow = new RoundRectangle2D.Float(x + 4, y + 4, w, h, 16, 16);
  	  g2.fill(shadow);
  	
  	  // gradiente
  	  GradientPaint gp = new GradientPaint(x, y,new Color(247, 231, 206),x, y + h,new Color(243, 198, 83));
  	  g2.setPaint(gp);
  	  Shape rect = new RoundRectangle2D.Float(x, y, w, h, 16, 16);
  	  g2.fill(rect);
  	
  	  // contorno
  	  g2.setColor(new Color(230, 230, 255));
  	  g2.setStroke(new BasicStroke(2f));
  	  g2.draw(rect);
  	
  	  // texto
  	  g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18f));
  	  FontMetrics fm = g2.getFontMetrics();
  	  int textWidth = fm.stringWidth(label);
  	  int textX = x + (w - textWidth) / 2;
  	  int textY = y + (h + fm.getAscent()) / 2;
  	  g2.setColor(new Color(58,64,90));
  	  g2.drawString(label, textX, textY);
  	
  	  return new Rectangle(x, y, w, h); // usado pelo mouseClicked
  	}
    
    // --- função para imprimir ---
    private void saveScreenshot() {
	    try {
	        // Cria imagem com o conteúdo atual do painel
	        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2 = img.createGraphics();
	        this.paint(g2); // desenha tudo (incluindo game over) na imagem
	        g2.dispose();

	        // Dialog para o jogador escolher onde guardar
	        JFileChooser chooser = new JFileChooser();
	        chooser.setDialogTitle("Guardar screenshot");
	        chooser.setSelectedFile(new File("gameover_screenshot.png")); // nome sugerido

	        int result = chooser.showSaveDialog(this);

	        if (result == JFileChooser.APPROVE_OPTION) {
	            File file = chooser.getSelectedFile();

	            // Garante extensão .png
	            String path = file.getAbsolutePath(); 
	            if (!path.toLowerCase().endsWith(".png")) {
	                file = new File(path + ".png");
	            }

	            ImageIO.write(img, "png", file);
	            System.out.println("Screenshot guardada em: " + file.getAbsolutePath());
	        } else {
	            System.out.println("Screenshot cancelada pelo jogador.");
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	} 
}
