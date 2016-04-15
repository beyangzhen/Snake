import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Yard extends Frame {

	public static final int ROWS = 30;
	public static final int COLS = 30;
	public static final int BLOCK_SIZE = 15;
	
	private int score = 0; //游戏分数
	 
	private boolean gameOver = false; //游戏是否结束	
	
	private Font fontGameOver = new Font("宋体", Font.BOLD, 50);
	private Font again = new Font("宋体", Font.BOLD, 30);
	
	Snake s = new Snake(this);
	Egg e = new Egg();
	
	//双缓冲
	Image offScreenImage = null;
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		//显示背景颜色
		g.setColor(Color.gray); //背景颜色
		g.fillRect(0, 0, BLOCK_SIZE * COLS, BLOCK_SIZE * ROWS + BLOCK_SIZE / 2);
		g.setColor(Color.DARK_GRAY); //横线的颜色
		
		//画出横线
		for(int i=1; i<ROWS; i++){
			g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * COLS, BLOCK_SIZE * i);
		}
		for(int i=1; i<COLS; i++){
			g.drawLine(BLOCK_SIZE * i + BLOCK_SIZE / 2, 0, BLOCK_SIZE * i + BLOCK_SIZE / 2, BLOCK_SIZE * ROWS);
		}
		
		//显示分数
		g.setColor(Color.YELLOW);
		g.drawString("分数 :" + score, 10, 60);
		
		//显示游戏结束
		if(gameOver) {
			g.setFont(fontGameOver);
			g.drawString("游戏结束", 120, 180);
			g.setFont(again);
			g.drawString("继续游戏 按F2", 120, 230);
			
			new PaintThread().gameOver();
		}
		
		g.setColor(c);
		
		s.draw(g); //画蛇
		e.draw(g); //画蛋
		s.eat(e); //没次启动线程就检测是否吃到蛋
	}
	
	//双缓冲
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(BLOCK_SIZE * COLS, BLOCK_SIZE * ROWS + BLOCK_SIZE / 2);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	//蛇死后显示游戏结束
	public void stop() { 
		gameOver = true;
	}
	
	//运行窗口
	public void launch() {
		this.setLocation(200, 200);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE + BLOCK_SIZE / 2);
		this.addWindowListener(new WindowAdapter() { //调用窗口监听

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor()); //调用键盘监听
		
		//开启线程
		new Thread(new PaintThread()).start();
	}
	
	//定义线程
	/*
	 * 是通过线程不断重画图像，每个图像结点的位置不同，来让蛇看起来在移动
	 */
	private class PaintThread implements Runnable {	
		private boolean running = true;
		private boolean pause = false;
		public void run() {
			while(running) {
				if(pause) continue; //等待用户选择是否要继续游戏
				else repaint(); //每启动线程就会重新画图
				try {
					Thread.sleep(100); //每100ms运行下run()方法
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//游戏结束
		public void gameOver() {
			running = false;
		}
		
		//按space后  暂停
		public void pause() {
			if(pause == false) pause = true;
			else pause = false;
		}
		
		//按F2后  重新开始
		public void reStart() {
			s = new Snake(Yard.this);
			score = 0;
			gameOver = false;
			running = true;
		}
	}
	
	//键盘监听
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode(); 
			if(key == KeyEvent.VK_F2) { //监听F2（重新开始）
				new PaintThread().reStart();
			}
			if(key == KeyEvent.VK_SPACE) { //监听space（暂停）
				new PaintThread().pause();
			}

			s.keyPressed(e); //监听上,下,左,右键（控制方向）
		}
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public static void main(String[] args) {
		new Yard().launch();
	}

}
