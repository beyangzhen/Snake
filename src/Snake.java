import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class Snake {
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	public static boolean flag = false;
	
	private Node n = new Node(20, 30, Dir.L); //初始化结点的位置
	private Yard y;
	
	public Snake(Yard y) { //初始化结点的头和尾指针
		head = n;
		tail = n;
		size = 1;
		this.y = y;
	}
	
	//尾部增加结点
	/*
	 * 方向Dir只是用来①判定在哪一行、列来加结点 ②判定下个图结点该出现的新位置(即：移动)，并没有真正的方向
	 */
	public void addToTail() {
		Node node = null; //新增加的结点
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
	
	//首部增加结点
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
	
	//画出整条蛇
	public void draw(Graphics g) {
		if(size <= 0) return;
		move(); //先move()再draw()，键盘操作会更灵敏
		for(Node n = head; n != null; n = n.next) {
			n.draw(g);
		}
	}
	
	//通过线程使蛇能移动
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
		//检测蛇头是否和四条边碰撞
		if(head.row < 2 || head.col < 0 || head.row * Yard.BLOCK_SIZE + Yard.BLOCK_SIZE / 2 > Yard.ROWS * Yard.BLOCK_SIZE || head.col * Yard.BLOCK_SIZE > (Yard.COLS - 1) * Yard.BLOCK_SIZE) {
			y.stop();
		}
		//检测舌头是否和蛇身碰撞
		for(Node n = head.next; n != null; n = n.next) {
			if(head.row == n.row && head.col == n.col) {
				y.stop();
			}
		}
	}
	
	//吃蛋     碰撞检测
	public void eat(Egg e) {
		if(this.getRect().intersects(e.getRect())) {
			e.reAppear();
			addToTail();
			y.setScore(y.getScore() + 5);
			flag = true;
		}
	}
	
	//蛇头的矩形
	public Rectangle getRect() {
		return new Rectangle(Yard.BLOCK_SIZE * head.col, Yard.BLOCK_SIZE * head.row, head.w, head.h);
	}

	//内部类
	private class Node {
		int w = Yard.BLOCK_SIZE;
		int h = Yard.BLOCK_SIZE;
		int row, col;
		Dir dir = Dir.L; //结点方向
		Node next = null;
		Node prev = null;
		
		Node(int row, int col, Dir dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
		}
		
		//画出单个结点
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
