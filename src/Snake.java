import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class Snake {
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	public static boolean flag = false;
	
	private Node n = new Node(20, 30, Dir.L); //��ʼ������λ��
	private Yard y;
	
	public Snake(Yard y) { //��ʼ������ͷ��βָ��
		head = n;
		tail = n;
		size = 1;
		this.y = y;
	}
	
	//β�����ӽ��
	/*
	 * ����Dirֻ���������ж�����һ�С������ӽ�� ���ж��¸�ͼ���ó��ֵ���λ��(�����ƶ�)����û�������ķ���
	 */
	public void addToTail() {
		Node node = null; //�����ӵĽ��
		switch(tail.dir) {
		case L:
			node = new Node(tail.row, tail.col + 1, tail.dir);
			break;
		case U:
			node = new Node(tail.row + 1, tail.col, tail.dir);
			break;
		case R:
			node = new Node(tail.row, tail.col - 1, tail.dir);
			break;
		case D:
			node = new Node(tail.row - 1, tail.col, tail.dir);
			break;
		}
		tail.next = node;
		node.prev = tail;
		tail = node;
		size++;
	}
	
	//�ײ����ӽ��
	public void addToHead() {
		Node node = null;
		switch(head.dir) {
		case L:
			node = new Node(head.row, head.col - 1, head.dir);
			break;
		case U:
			node = new Node(head.row - 1, head.col, head.dir);
			break;
		case R:
			node = new Node(head.row, head.col + 1, head.dir);
			break;
		case D:
			node = new Node(head.row + 1, head.col, head.dir);
			break;
		}
		node.next = head;
		head.prev = node;
		head = node;
		size++;
	}
	
	//����������
	public void draw(Graphics g) {
		if(size <= 0) return;
		move(); //��move()��draw()�����̲����������
		for(Node n = head; n != null; n = n.next) {
			n.draw(g);
		}
	}
	
	//ͨ���߳�ʹ�����ƶ�
	private void move() {
		addToHead();
		deleteFromTail();
		checkDead();
	}

	private void deleteFromTail() {
		if(size == 0) return;
		tail = tail.prev;
		tail.next = null;
	}
	
	private void checkDead() {
		//�����ͷ�Ƿ����������ײ
		if(head.row < 2 || head.col < 0 || head.row * Yard.BLOCK_SIZE + Yard.BLOCK_SIZE / 2 > Yard.ROWS * Yard.BLOCK_SIZE || head.col * Yard.BLOCK_SIZE > (Yard.COLS - 1) * Yard.BLOCK_SIZE) {
			y.stop();
		}
		//�����ͷ�Ƿ��������ײ
		for(Node n = head.next; n != null; n = n.next) {
			if(head.row == n.row && head.col == n.col) {
				y.stop();
			}
		}
	}
	
	//�Ե�     ��ײ���
	public void eat(Egg e) {
		if(this.getRect().intersects(e.getRect())) {
			e.reAppear();
			addToTail();
			y.setScore(y.getScore() + 5);
			flag = true;
		}
	}
	
	//��ͷ�ľ���
	public Rectangle getRect() {
		return new Rectangle(Yard.BLOCK_SIZE * head.col, Yard.BLOCK_SIZE * head.row, head.w, head.h);
	}

	//�ڲ���
	private class Node {
		int w = Yard.BLOCK_SIZE;
		int h = Yard.BLOCK_SIZE;
		int row, col;
		Dir dir = Dir.L; //��㷽��
		Node next = null;
		Node prev = null;
		
		Node(int row, int col, Dir dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		//�����������
		void draw(Graphics g) { 
			Color c = g.getColor();
			g.setColor(Color.BLACK);
			g.fillRect(Yard.BLOCK_SIZE * col + Yard.BLOCK_SIZE / 2, Yard.BLOCK_SIZE * row, w, h);
			g.setColor(c);
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			if(head.dir != Dir.R)
				head.dir = Dir.L;
			break;
		case KeyEvent.VK_UP:
			if(head.dir != Dir.D)
				head.dir = Dir.U;
			break;
		case KeyEvent.VK_RIGHT:
			if(head.dir != Dir.L)
				head.dir = Dir.R;
			break;
		case KeyEvent.VK_DOWN:
			if(head.dir != Dir.U)
				head.dir = Dir.D;
			break;
		}
	}
}
