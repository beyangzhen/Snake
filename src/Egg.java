import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Egg {
	int row, col;
	int w = Yard.BLOCK_SIZE;
	int h = Yard.BLOCK_SIZE;
	private static Random r = new Random(); //���������
	private Color color = Color.GREEN;
	
	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Egg() {
		this(r.nextInt(Yard.ROWS - 2) + 2, r.nextInt(Yard.COLS)); //this������ʾ������һ�����췽��
	}
	
	//�����Ժ��ٴ��������
	public void reAppear() {
		this.row = r.nextInt(Yard.ROWS - 2) + 2;
		this.col = r.nextInt(Yard.COLS);
	}
	
	//���ľ���
	public Rectangle getRect() {
		return new Rectangle(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(Yard.BLOCK_SIZE * col + Yard.BLOCK_SIZE / 2, Yard.BLOCK_SIZE * row, w, h);
		g.setColor(c);
		//ÿ��һ�Σ�������ɫ�仯
		if(Snake.flag == true) {
			if(color == Color.GREEN) {
				color = Color.RED;
				Snake.flag = false;
			}
			else {
				color = Color.GREEN;
				Snake.flag = false;
			}
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
