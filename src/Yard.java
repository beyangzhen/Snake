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
	
	private int score = 0; //��Ϸ����
	 
	private boolean gameOver = false; //��Ϸ�Ƿ����	
	
	private Font fontGameOver = new Font("����", Font.BOLD, 50);
	private Font again = new Font("����", Font.BOLD, 30);
	
	Snake s = new Snake(this);
	Egg e = new Egg();
	
	//˫����
	Image offScreenImage = null;
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		//��ʾ������ɫ
		g.setColor(Color.gray); //������ɫ
		g.fillRect(0, 0, BLOCK_SIZE * COLS, BLOCK_SIZE * ROWS + BLOCK_SIZE / 2);
		g.setColor(Color.DARK_GRAY); //���ߵ���ɫ
		
		//��������
		for(int i=1; i<ROWS; i++){
			g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * COLS, BLOCK_SIZE * i);
		}
		for(int i=1; i<COLS; i++){
			g.drawLine(BLOCK_SIZE * i + BLOCK_SIZE / 2, 0, BLOCK_SIZE * i + BLOCK_SIZE / 2, BLOCK_SIZE * ROWS);
		}
		
		//��ʾ����
		g.setColor(Color.YELLOW);
		g.drawString("���� :" + score, 10, 60);
		
		//��ʾ��Ϸ����
		if(gameOver) {
			g.setFont(fontGameOver);
			g.drawString("��Ϸ����", 120, 180);
			g.setFont(again);
			g.drawString("������Ϸ ��F2", 120, 230);
			
			new PaintThread().gameOver();
		}
		
		g.setColor(c);
		
		s.draw(g); //����
		e.draw(g); //����
		s.eat(e); //û�������߳̾ͼ���Ƿ�Ե���
	}
	
	//˫����
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(BLOCK_SIZE * COLS, BLOCK_SIZE * ROWS + BLOCK_SIZE / 2);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	//��������ʾ��Ϸ����
	public void stop() { 
		gameOver = true;
	}
	
	//���д���
	public void launch() {
		this.setLocation(200, 200);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE + BLOCK_SIZE / 2);
		this.addWindowListener(new WindowAdapter() { //���ô��ڼ���

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor()); //���ü��̼���
		
		//�����߳�
		new Thread(new PaintThread()).start();
	}
	
	//�����߳�
	/*
	 * ��ͨ���̲߳����ػ�ͼ��ÿ��ͼ�����λ�ò�ͬ�������߿��������ƶ�
	 */
	private class PaintThread implements Runnable {	
		private boolean running = true;
		private boolean pause = false;
		public void run() {
			while(running) {
				if(pause) continue; //�ȴ��û�ѡ���Ƿ�Ҫ������Ϸ
				else repaint(); //ÿ�����߳̾ͻ����»�ͼ
				try {
					Thread.sleep(100); //ÿ100ms������run()����
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//��Ϸ����
		public void gameOver() {
			running = false;
		}
		
		//��space��  ��ͣ
		public void pause() {
			if(pause == false) pause = true;
			else pause = false;
		}
		
		//��F2��  ���¿�ʼ
		public void reStart() {
			s = new Snake(Yard.this);
			score = 0;
			gameOver = false;
			running = true;
		}
	}
	
	//���̼���
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode(); 
			if(key == KeyEvent.VK_F2) { //����F2�����¿�ʼ��
				new PaintThread().reStart();
			}
			if(key == KeyEvent.VK_SPACE) { //����space����ͣ��
				new PaintThread().pause();
			}

			s.keyPressed(e); //������,��,��,�Ҽ������Ʒ���
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
