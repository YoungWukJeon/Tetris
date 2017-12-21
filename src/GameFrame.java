import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

public class GameFrame extends JFrame
{
	GamePanel fgp;
	static int gameMode = 1;
	static JFrame jf;

	GameFrame()
	{
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(null);
		setSize(360, 640);
		setResizable(false);

		jf = this;
		Container c = this.getContentPane();

		c.setBackground(new Color(30, 160, 255));

		switch (gameMode)
		{
			case 1:
				fgp = new GamePanel();
				fgp.setBounds(0, 0, 360, 640);
				add(fgp);
				MyKey1(fgp);
				break;
		}

		setLocationRelativeTo(getParent());

		setVisible(true);
	}

	public void remakePanel()
	{
		fgp.highestScore.setBounds(255, 200, 205, 30);
		fgp.time.setBounds(255, 240, 205, 30);
	}

	public void MyKey1(JPanel gp)
	{
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (((GamePanel) gp).gameRun && ((GamePanel) gp).Gaming)
				{
//					if (e.getKeyText(e.getKeyCode()).equals(FirstPlayerKeySetting.FKeyType[0]))
					if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[0])
					{
						((GamePanel) gp).move_down();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[1])
					{
						((GamePanel) gp).move_left();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[2])
					{
						((GamePanel) gp).move_right();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[3])
					{
						// 내게 쓰기
						((GamePanel) gp).ogp = fgp;

						JLabel labelNum = ((GamePanel) gp).itemLabel.get(0);
//						if (labelNum.getName().equals("6"))
//							((GamePanel) gp).ogp = sgp;

						((GamePanel) gp).useItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[4])
					{
						// 적에게 쓰기
//						((GamePanel) gp).ogp = sgp;
						((GamePanel) gp).attackItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[5])
					{
						// 아이템 지우기
						((GamePanel) gp).deleteItem();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[6])
					{
						((GamePanel) gp).move_drop();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
					else if (e.getKeyCode() == FirstPlayerKeySetting.FKeyType[7])
					{
						((GamePanel) gp).move_turn();
						((GamePanel) gp).checkArray();
						((GamePanel) gp).drawTetris();
					}
				}
			}
		});
	}

	public static void main(String[] args)
	{
		new ImageSource();
		new GameFrame();
	}
}

class GamePanel extends JPanel implements Runnable
{
	JPanel tetrisArea = new JPanel();
	JLabel nextBlock = new JLabel();
	JLabel score = new JLabel();
	JLabel highestScore = new JLabel();
	JPanel item = new JPanel();
	JPanel item_using = new JPanel();
	JLabel time = new JLabel();
	AbstractBorder WhiteLineBorder;

	JLabel[][] fieldLabel = new JLabel[20][10];

	Thread th;
	TimeThread timeTh;
	int gameSpeed = 1000;
	int speedCount = 0;
	int preSpeed;

	JLabel tube;
	JLabel ground;
	JLabel textLabel = new JLabel();
	JPanel blackPanel = new JPanel();
	boolean Gaming = false;

	GamePanel ogp; // OtherGamePanel

	GamePanel()
	{
		setLayout(null);

		makeComponent(0);
		makeTetrisArea(0);
		makeBackground();

		setArray(); // 데이터 배열 테두리 초기화

		addBlock(); // 블럭추가

		Thread textTh = new Thread()
		{
			public void run()
			{
				int textCount = 3;

				while (textCount > -1)
				{
					if (textCount > 0)
						textLabel.setText(Integer.toString(textCount));
					else
						textLabel.setText("START!!");
					try
					{
						sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					blackPanel.repaint();
					textCount--;
				}
				tetrisArea.setVisible(true);
				blackPanel.setVisible(false);

				synchronized (timeTh)
				{
					timeTh.notify();
				}
				synchronized (th)
				{
					th.notify();
				}
				Gaming = true;
				// repaint();
			}
		};
		textTh.start();
	}

	GamePanel(GamePanel ogp)
	{
		this();
		this.ogp = ogp;
	}

	public void makeBackground()
	{
		setOpaque(false);
		setBackground(new Color(30, 160, 255));

		tube = new JLabel(ImageSource.kakao_tube);
		int tubeWidth = ImageSource.kakao_tube.getIconWidth();
		int tubeHeight = ImageSource.kakao_tube.getIconHeight();
		tube.setBounds(240, 360, tubeWidth, tubeHeight);
		add(tube);

		ground = new JLabel(ImageSource.bg_ground);
		int groundWidth = ImageSource.bg_ground.getIconWidth();
		int groundHeight = ImageSource.bg_ground.getIconHeight();
		ground.setBounds(0, 640 - groundHeight, groundWidth, groundHeight);
		add(ground);
	}

	public void makeComponent(int n)
	{
		WhiteLineBorder = new LineBorder(Color.WHITE);

		tetrisArea = new JPanel();
		tetrisArea.setBounds(10, 60, 240, 480);
		tetrisArea.setBackground(Color.BLACK);
		blackPanel.setBounds(10, 60, 240, 480);
		blackPanel.setBackground(Color.BLACK);
		add(blackPanel);
		add(tetrisArea);
		tetrisArea.setVisible(false);

		nextBlock = new JLabel(ImageSource.block_L);
		nextBlock.setFont(new Font("verdana", 0, 12));
		nextBlock.setForeground(Color.WHITE);
		nextBlock.setBackground(Color.BLACK);
		nextBlock.setOpaque(true);
		nextBlock.setBorder(WhiteLineBorder);
		nextBlock.setBounds(255, 60, 90, 90);
		add(nextBlock);

		score = new JLabel("0", SwingConstants.RIGHT);
		score.setText("0");
		score.setFont(new Font("verdana", 0, 20));
		score.setForeground(Color.WHITE);
		score.setBackground(Color.BLACK);
		score.setOpaque(true);
		score.setBorder(WhiteLineBorder);
		score.setBounds(255, 160, 90, 30);
		add(score);

		highestScore = new JLabel("0", SwingConstants.RIGHT);
		highestScore.setText("999999");
		highestScore.setFont(new Font("verdana", 0, 20));
		highestScore.setForeground(Color.WHITE);
		highestScore.setBackground(Color.BLACK);
		highestScore.setOpaque(true);
		highestScore.setBorder(WhiteLineBorder);
		highestScore.setBounds(255, 200, 90, 30);
		add(highestScore);

		time = new JLabel("", SwingConstants.CENTER);
		time.setText("time");
		time.setFont(new Font("verdana", 0, 12));
		time.setForeground(Color.WHITE);
		time.setBackground(Color.BLACK);
		time.setOpaque(true);
		time.setBorder(WhiteLineBorder);
		time.setBounds(255, 240, 90, 30);
		add(time);
		timeTh = new TimeThread(this, n);
		timeTh.start();

		item_using = new JPanel(null);
		item_using.setBackground(Color.BLACK);
		item_using.setBorder(WhiteLineBorder);
		item_using.setBounds(10, 25, 240, 30);
		add(item_using);

		item = new JPanel(null);
		item.setBackground(Color.BLACK);
		item.setBorder(WhiteLineBorder);
		item.setBounds(10, 545, 240, 30);
		add(item);

		blackPanel.setLayout(null);
		blackPanel.add(textLabel);
		textLabel.setBounds(20, 50, 200, 50);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setFont(new Font("Verdana", 1, 30));
		textLabel.setOpaque(true);
		textLabel.setBackground(new Color(255, 255, 255, 0));
		textLabel.setForeground(new Color(-1));
		textLabel.setBorder(null);

	}

	public void makeTetrisArea(int n)
	{
		tetrisArea.setLayout(new GridLayout(20, 10));

		for (int i = 0; i < 20; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				fieldLabel[i][j] = new JLabel();
				tetrisArea.add(fieldLabel[i][j]);
				fieldLabel[i][j].setBackground(Color.BLACK);
				fieldLabel[i][j].setOpaque(true);

				WhiteLineBorder = new LineBorder(new Color(15, 15, 15));
				fieldLabel[i][j].setBorder(WhiteLineBorder);
			}
		}
	}

	/******************** 테트리스 영역 메소드 ********************/

	/****************************************
	 * 표시 레이블, 데이터 배열, 배열 복사본, 아이템 객체
	 ****************************************/
	int[][] array = new int[21][12];
	int[][] field = new int[21][12];
	int[][] copy;

	Vector<JLabel> itemLabel = new Vector<JLabel>();
	Vector<UsingItemLabel> usingItemLabel = new Vector<UsingItemLabel>();

	UseItem uItem = new UseItem();

	/******************** 테트리스 동작 그리기 ********************/

	int preBlock = (int) (Math.random() * 7 + 1);

	int[][] block;
	int x; // 블럭 가로 위치 (점 0,0)
	int y; // 블럭 세로 위치 (점 0,0)
	int blockNum;
	int blockWSize; // 블럭의 너비, 용도 : 목표 범위 초과하여 데이터 손실 막기
	int blockHSize; // 블럭의 높이, 용도 : 목표 범위 초과하여 데이터 손실 막기
	boolean gameRun = true;

	public void setArray()
	{
		/****************************************
		 * 안내 : 데이터 배열내에 테두리를 표현 단순 테두리 표현으로 실제 데이터에 영향을 안줌. 출력시에만 영향을 주므로 실제 초기
		 * 테두리 데이터는 0임.
		 ****************************************/
		for (int row = 0; row < array.length - 1; row++)
			for (int col = 1; col < array[0].length - 1; col++)
				if (array[row][col] == 9)
					array[row][col] = 0;

		for (int row = 0; row < array.length; row++)
		{
			array[row][0] = 9;
			array[row][array[0].length - 1] = 9;
		}

		for (int col = 0; col < array[0].length; col++)
			array[array.length - 1][col] = 9;
	}

	public void addBlock()
	{
		int[][] block;
		int start_x;
		int start_y;
		int blockNum;

		Blocks b = new Blocks();
		nextBlock(b);

		block = b.getBlock();
		blockNum = b.getBlockNum();
		block = b.getBlock();
		start_x = b.getStart_x();
		start_y = b.getStart_y();
		// System.out.println(start_x + "  " + start_y);

		this.block = block;
		this.blockNum = blockNum;
		x = start_x;
		y = start_y;
		blockWSize = block[0].length;
		blockHSize = block.length;

		/****************************************
		 * start_y 의 존재 : 시작위치 0값을 받아옴
		 ****************************************/

		for (int row = 0, h = 0 + start_y; row < blockHSize - start_y; row++, h++)
			for (int col = start_x, w = 0; col < blockWSize + start_x; col++, w++)
				if (block[h][w] > 0)
					array[row][col] = block[h][w];

		int count = 0;
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x; col < x + blockWSize; col++)
				if (array[row][col] > 0 && field[row][col] > 0)
					count++;
		if (count > 0)
		{
			gameRun = false;
			th.interrupt();
			timeTh.interrupt();
			// th = null;
			// timeTh = null;
			// System.gc();
			// drawEndTetris();
			if (GameFrame.gameMode == 2)
			{
				textLabel.setText("LOSE");
			}
			else if (GameFrame.gameMode == 1)
			{
				textLabel.setText("FAIL");
			}
			textLabel.setVisible(true);
			blackPanel.setVisible(true);
			tetrisArea.setVisible(false);
			
			System.exit(0);
		}
		else
		{
			drawTetris();
			checkArray();
			th = new Thread(this);
			th.start();
		}

		b = null;
	}

	public void nextBlock(Blocks b)
	{
		int currentBlock;
		currentBlock = preBlock;
		b.blockNum = currentBlock;
		preBlock = (int) (Math.random() * 7 + 1);

		switch (preBlock)
		{
		/**********************
		 * I : red, J : lime, L : orange, O : purple S : cyan T : blue Z : green
		 **********************/
		case 1:
			nextBlock.setIcon(ImageSource.block_I);
			break;
		case 2:
			nextBlock.setIcon(ImageSource.block_J);
			break;
		case 3:
			nextBlock.setIcon(ImageSource.block_L);
			break;
		case 4:
			nextBlock.setIcon(ImageSource.block_O);
			break;
		case 5:
			nextBlock.setIcon(ImageSource.block_S);
			break;
		case 6:
			nextBlock.setIcon(ImageSource.block_T);
			break;
		case 7:
			nextBlock.setIcon(ImageSource.block_Z);
			break;
		}
	}

	public void drawEndTetris()
	{
		for (int row = 0; row < fieldLabel.length; row++)
			for (int col = 0; col < fieldLabel[0].length; col++)
			{
				//
				// field[row][col] += 10;
				fieldLabel[row][col].setIcon(ImageSource.block_gray);
			}
		// try{Thread.sleep(1000);}
		// catch (InterruptedException e){e.printStackTrace();}
	}

	public void drawTetris()
	{
		for (int row = 0; row < fieldLabel.length; row++)
			for (int col = 0; col < fieldLabel[0].length; col++)
			{
				if (array[row][col + 1] > 0 && array[row][col + 1] < 8)
					switch (array[row][col + 1])
					{
					/**********************
					 * I : red, J : lime, L : orange, O : purple S : cyan T :
					 * blue Z : green
					 **********************/
					case 1:
						fieldLabel[row][col].setIcon(ImageSource.block_red);
						break;
					case 2:
						fieldLabel[row][col].setIcon(ImageSource.block_lime);
						break;
					case 3:
						fieldLabel[row][col].setIcon(ImageSource.block_orange);
						break;
					case 4:
						fieldLabel[row][col].setIcon(ImageSource.block_puple);
						break;
					case 5:
						fieldLabel[row][col].setIcon(ImageSource.block_cyan);
						break;
					case 6:
						fieldLabel[row][col].setIcon(ImageSource.block_blue);
						break;
					case 7:
						fieldLabel[row][col].setIcon(ImageSource.block_green);
						break;
					}
				else if (field[row][col + 1] >= 80 && field[row][col + 1] < 90)
				{
					switch (field[row][col + 1] - 80)
					{
					/**********************
					 * I : red, J : lime, L : orange, O : purple S : cyan T :
					 * blue Z : green
					 **********************/
					case 0:
						fieldLabel[row][col]
								.setIcon(ImageSource.block_blackout);
						break;
					case 1:
						fieldLabel[row][col].setIcon(ImageSource.block_fast);
						break;
					case 2:
						fieldLabel[row][col]
								.setIcon(ImageSource.block_lineup_1);
						break;
					case 3:
						fieldLabel[row][col]
								.setIcon(ImageSource.block_lineup_3);
						break;
					case 4:
						fieldLabel[row][col].setIcon(ImageSource.block_zigzag);
						break;
					case 5:
						fieldLabel[row][col].setIcon(ImageSource.block_bomb);
						break;
					case 6:
						fieldLabel[row][col].setIcon(ImageSource.block_change);
						break;
					case 7:
						fieldLabel[row][col]
								.setIcon(ImageSource.block_linedown_1);
						break;
					case 8:
						fieldLabel[row][col]
								.setIcon(ImageSource.block_linedown_3);
						break;
					case 9:
						fieldLabel[row][col].setIcon(ImageSource.block_slow);
						break;
					}
				}
				else if (field[row][col + 1] >= 90 && field[row][col + 1] < 100)
				{
					switch (field[row][col + 1] - 90)
					{
					/**********************
					 * I : red, J : lime, L : orange, O : purple S : cyan T :
					 * blue Z : green
					 **********************/
					case 1:
						fieldLabel[row][col].setIcon(ImageSource.block_red);
						break;
					case 2:
						fieldLabel[row][col].setIcon(ImageSource.block_lime);
						break;
					case 3:
						fieldLabel[row][col].setIcon(ImageSource.block_orange);
						break;
					case 4:
						fieldLabel[row][col].setIcon(ImageSource.block_puple);
						break;
					case 5:
						fieldLabel[row][col].setIcon(ImageSource.block_cyan);
						break;
					case 6:
						fieldLabel[row][col].setIcon(ImageSource.block_blue);
						break;
					case 7:
						fieldLabel[row][col].setIcon(ImageSource.block_green);
						break;
					}
				}
				else if (field[row][col + 1] >= 100)
					fieldLabel[row][col].setIcon(ImageSource.block_gray);
				else
					fieldLabel[row][col].setIcon(null);
			}
	}

	public void drawTurn()
	{
		for (int row = y, h = 0; h < blockWSize; row++, h++)
			for (int col = x, w = 0; w < blockHSize; col++, w++)
				array[row][col] = 0;

		for (int row = y, h = 0; h < blockHSize; row++, h++)
			for (int col = x, w = 0; w < blockWSize; col++, w++)
				if (block[h][w] > 0)
					array[row][col] = block[h][w];

		/****************************************
		 * 조건 : 우측 벽에서 모형 손실에 대한 예방대책 세울것
		 ****************************************/
		if (x + blockWSize > array[0].length - 1)
			move_left();
	}

	public void fixBlock()
	{
		/****************************************
		 * 조건 : 1. move_drop, move_down의 인터럽트 발생시 바닥에 닿았다는 조건이므로 마지막 데이터 값을 변경해줘
		 * 고정됨을 표시한다. 2. 데이터 값 >> I:91 J:92 L:93 O:94 S:95 T:96 Z:97 3.
		 * checkArray()에서 고정된 블럭은 n으로 표시되게끔 코드를 수정한다.
		 *****************************************/

		for (int row = 0; row < array.length - 1; row++)
			for (int col = 1; col < array[0].length - 1; col++)
			{
				if (array[row][col] != 0 && array[row][col] != 8)
					field[row][col] = array[row][col] + 90; // 90: 고정을 의미
				array[row][col] = 0;
			}
		lineCheck();
	}

	public void lineCheck()
	{
		int deleteCount = 0;
		// 정지된 y값의 줄부터 블럭의 length까지 계산한다.
		// System.out.println("마지막 y의 위치 : " + y);
		
		for (int i = y; i < y + block.length; i++)
		{
			int count = 0;
			for (int j = 1; j < field[0].length - 1; j++)
				if (field[i][j] != 0)
					count++;
			// System.out.println(i + "번 라인의 1의 개수 : " + count);
			
			if (count == 10)
			{
				for (int j = 1; j < field[0].length; j++)
				{
					if (field[i][j] >= 80 && field[i][j] < 90 && itemLabel.size() < 7)
					{
						JLabel itemL = new JLabel();

						switch (field[i][j] - 80)
						{
						case 0:
							itemL.setIcon(ImageSource.item_blackout);
							itemL.setName("0");
							break;
						case 1:
							itemL.setIcon(ImageSource.item_fast);
							itemL.setName("1");
							break;
						case 2:
							itemL.setIcon(ImageSource.item_lineup_1);
							itemL.setName("2");
							break;
						case 3:
							itemL.setIcon(ImageSource.item_lineup_3);
							itemL.setName("3");
							break;
						case 4:
							itemL.setIcon(ImageSource.item_zigzag);
							itemL.setName("4");
							break;
						case 5:
							itemL.setIcon(ImageSource.item_bomb);
							itemL.setName("5");
							break;
						case 6:
							itemL.setIcon(ImageSource.item_change);
							itemL.setName("6");
							break;
						case 7:
							itemL.setIcon(ImageSource.item_linedown_1);
							itemL.setName("7");
							break;
						case 8:
							itemL.setIcon(ImageSource.item_linedown_3);
							itemL.setName("8");
							break;
						case 9:
							itemL.setIcon(ImageSource.item_slow);
							itemL.setName("9");
							break;
						}

						itemLabel.add(itemL);
						item.removeAll();

						for (int k = 0; k < itemLabel.size(); k++)
						{
							item.add(itemLabel.get(k));
							itemLabel.get(k).setBounds(30 * k, 0, 30, 30);
						}
					}
				}

				lineDelete(i);
				// System.out.println("삭제한 줄" + i);

				deleteCount++;
			}
		}
		if (deleteCount > 0)
		{
			addItem(deleteCount);
			addScore(deleteCount);
		}

	}

	public void lineDelete(int num)
	{
		/* 전역: int[][] */
		copy = new int[field.length][field[0].length];
		
		for (int row = 0; row < field.length - 1; row++)
			for (int col = 0; col < field[0].length; col++)
				if (row < num)
					copy[row + 1][col] = field[row][col];
				else
					copy[row + 1][col] = field[row + 1][col];

		backArray(field, copy);

		copy = null;
	}

	public void addItem(int num)
	{
		int itemCount = 0;

		for (int i = 0; i < field.length; i++)
		{
			for (int j = 1; j < field[0].length - 1; j++)
			{
				if (field[i][j] > 90)
				{
					for (int k = 1; k <= num; k++)
					{
						// 전체 아이템 개수 7개로 제한하기 필요.(나중에 추가할것)
						int chance = (int) (Math.random() * 20);

						if (chance == 1)
						{
							field[i][j] = (int) (Math.random() * 10) + 80;
//							 field[i][j] = 80;
							itemCount++;
						}
						if (itemCount == 2)
						{
							return;
						}
					}
				}
			}
		}
	}

	public void addScore(int num)
	{
		int addScore = Integer.parseInt(score.getText());

		if (num == 4)
			addScore += (120 * 1000 / gameSpeed);
		else if (num == 3)
			addScore += (70 * 1000 / gameSpeed);
		else if (num == 2)
			addScore += (30 * 1000 / gameSpeed);
		else
			addScore += (10 * 1000 / gameSpeed);

		score.setText(Integer.toString(addScore));
	}

	public void useItem()
	{
		String numStr = itemLabel.get(0).getName();

		switch (numStr)
		{
		case "0":
			uItem.blackout();
			break;
		case "1":
			uItem.fast();
			break;
		case "2":
			uItem.oneLineUp();
			break;
		case "3":
			uItem.threeLineUp();
			break;
		case "4":
			uItem.zigzag();
			break;
		case "5":
			uItem.bomb();
			break;
		case "6":
			uItem.change();
			break;
		case "7":
			uItem.oneLineDown();
			break;
		case "8":
			uItem.threeLineDown();
			break;
		case "9":
			uItem.slow();
			break;
		}
		deleteItem();
	}

	public void attackItem()
	{
		String numStr = itemLabel.get(0).getName();
		// System.out.println("공격공격");
		switch (numStr)
		{
		case "0":
			uItem.blackout();
			break;
		case "1":
			uItem.fast();
			break;
		case "2":
			uItem.oneLineUp();
			break;
		case "3":
			uItem.threeLineUp();
			break;
		case "4":
			uItem.zigzag();
			break;
		case "5":
			uItem.bomb();
			break;
		case "6":
			uItem.change();
			break;
		case "7":
			uItem.oneLineDown();
			break;
		case "8":
			uItem.threeLineDown();
			break;
		case "9":
			uItem.slow();
			break;
		}
		deleteItem();
	}

	public void deleteItem()
	{
		if (itemLabel.size() > 0)
		{
			item.removeAll();
			item.repaint();
			itemLabel.remove(0);

			for (int i = 0; i < itemLabel.size(); i++)
			{
				item.add(itemLabel.get(i));
				itemLabel.get(i).setBounds(i * 30, 0, 30, 30);
			}
		}
	}

	/******************** 테트리스 키 이벤트 ********************/

	public void move_left()
	{
		/* 전역: int[][] */copy = new int[array.length][array[0].length];
		for (int row = 0; row < array.length; row++)
			for (int col = 1; col < array[0].length; col++)
				copy[row][col - 1] = array[row][col];

		int count = 0;
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x - 1; col < x + blockWSize; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 좌측 데이터의 벽<9> 넘지 않기
		 ****************************************/
		if (!(count > 0))
			if (x > 1)
			{
				x--;
				backArray(array, copy);
			}

		copy = null;
	}

	public void move_right()
	{
		/* 전역: int[][] */
		copy = new int[array.length][array[0].length];
		
		for (int row = 0; row < array.length; row++)
			for (int col = 0; col < array[0].length - 1; col++)
				copy[row][col + 1] = array[row][col];

		int count = 0;
		
		for (int row = y; row < y + blockHSize; row++)
			for (int col = x; col < x + blockWSize + 1; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 우측 데이터의 벽<9> 넘지 않기
		 ****************************************/
		
		if (!(count > 0))
			if (x < array[0].length - blockWSize - 1)
			{
				x++;
				backArray(array, copy);
			}

		copy = null;
	}

	public void move_down()
	{
		/* 전역: int[][] */copy = new int[array.length][array[0].length];
		for (int row = 0; row < array.length - 1; row++)
			for (int col = 0; col < array[0].length; col++)
				copy[row + 1][col] = array[row][col];

		int count = 0;
		for (int row = y; row < y + blockHSize + 1; row++)
			for (int col = x; col < x + blockWSize; col++)
				if (copy[row][col] > 0 && field[row][col] > 0)
					count++;

		/****************************************
		 * 조건 : 하단 데이터의 벽<9> 넘지 않기
		 ****************************************/
		if (count > 0)
		{
			th.interrupt();
			fixBlock();
			addBlock();
		}
		else if (y < (array.length - 1) - blockHSize)
		{
			y++;
			backArray(array, copy);
		}
		else
		{
			th.interrupt();
			fixBlock();
			addBlock();
		}

		copy = null;
	}

	public void move_up()
	{
		/* 전역: int[][] */
		copy = new int[array.length][array[0].length];
		
		for (int row = 1; row < array.length - 1; row++)
			for (int col = 0; col < array[0].length; col++)
				copy[row - 1][col] = array[row][col];

		y--;
		backArray(array, copy);

		copy = null;
	}

	public void move_drop()
	{
		while (true)
		{
			/* 전역: int[][] */copy = new int[array.length][array[0].length];
			for (int row = 0; row < array.length - 1; row++)
				for (int col = 0; col < array[0].length; col++)
					copy[row + 1][col] = array[row][col];

			int count = 0;
			for (int row = y; row < y + blockHSize + 1; row++)
				for (int col = x; col < x + blockWSize + 1; col++)
					if (copy[row][col] > 0 && field[row][col] > 0)
						count++;

			/****************************************
			 * 조건 : 하단 데이터의 벽<9> 넘지 않기 (move_down() 메소드와 동일함)
			 ****************************************/
			if (count > 0)
			{
				th.interrupt();
				fixBlock();
				addBlock();
				break;
			}
			else if (y < (array.length - 1) - blockHSize)
			{
				y++;
				backArray(array, copy);
			}
			else
			{
				/* 무한루프로 끝까지 내린후 바닥에 닿으면 break를 호출하여 루프 종료 */
				th.interrupt();
				fixBlock();
				addBlock();
				break;
			}
			copy = null;
		}
	}

	public void move_turn()
	{
		int[][] turn = new int[blockWSize][blockHSize];
		for (int i = 0; i < blockWSize; i++)
			for (int j = 0; j < blockHSize; j++)
				turn[i][j] = block[j][(blockWSize - 1) - i];

		int count = 0;
		if (blockNum == 1 && x >= 9)
			count++;
		if (!(count > 0))
			for (int row = y, i = 0; i < blockWSize; row++, i++)
				for (int col = x, j = 0; j < blockHSize; col++, j++)
					if (turn[i][j] > 0 && field[row][col] > 0)
						count++;

		if (!(count > 0))
		{
			int temp = blockHSize;
			blockHSize = blockWSize;
			blockWSize = temp;

			block = null;
			block = new int[blockHSize][blockWSize];
			block = turn;

			drawTurn();
		}

		turn = null;
	}

	public void backArray(int[][] array, int[][] copy)
	{
		/****************************************
		 * 안내 : backArray(원래 데이터, 복사 데이터) 복사 데이터를 원래 데이터로 재복사
		 ****************************************/

		for (int row = 0; row < array.length; row++)
			for (int col = 0; col < array[0].length; col++)
				array[row][col] = copy[row][col];
	}

	/******************** 테트리스 검사용 메소드 ********************/

	public void checkArray() // 테트리스 배열데이터 출력
	{
		setArray();
		// System.out.println("블럭좌표 : x " + x + " ---- y " + y);
		
		/*
		for (int row = 0; row < field.length; row++)
		{
			// System.out.printf("%2d -- ", row);
			for (int col = 0; col < field[0].length; col++)
				// if (field[row][col] >= 1)
					// System.out.print('n' + " ");
				// else
					// System.out.print(field[row][col] + " ");
			// System.out.println();
		}
		*/
	}

	/******************** 테트리스 쓰레드 메소드 ********************/

	public void run()
	{
		if (!Gaming)
		{
			synchronized (th)
			{
				try
				{
					th.wait();
					while (gameRun)
					{
						try
						{
							Thread.sleep(gameSpeed);
						}
						catch (InterruptedException e)
						{
							return;
						}
						move_down();
						// checkArray();
						drawTetris();
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			while (gameRun)
			{
				try
				{
					Thread.sleep(gameSpeed);
				}
				catch (InterruptedException e)
				{
					return;
				}
				move_down();
				// checkArray();
				drawTetris();
			}
		}
	}

	/******************** 테트리스 아이템 메소드 ********************/

	class UseItem
	{
		int width;
		int height;

		UseItem()
		{
			width = field[0].length;
			height = field.length;
		}

		public void bomb()
		{
			for (int row = 0; row < height; row++)
				for (int col = 0; col < width; col++)
				{
					ogp.array[row][col] = 0;
					ogp.field[row][col] = 0;
				}
			ogp.th.interrupt();
			ogp.addBlock();
		}

		public void change()
		{
			int temp[][] = array;
			array = ogp.array;
			ogp.array = temp;

			temp = field;
			field = ogp.field;
			ogp.field = temp;

			temp = block;
			block = ogp.block;
			ogp.block = temp;

			int tempVar = x;
			x = ogp.x;
			ogp.x = tempVar;

			tempVar = y;
			y = ogp.y;
			ogp.y = tempVar;

			tempVar = blockHSize;
			blockHSize = ogp.blockHSize;
			ogp.blockHSize = tempVar;

			tempVar = blockWSize;
			blockWSize = ogp.blockWSize;
			ogp.blockWSize = tempVar;

			tempVar = blockNum;
			blockNum = ogp.blockNum;
			ogp.blockNum = tempVar;

			drawTetris();
			ogp.drawTetris();

			temp = null;
		}

		public void oneLineDown()
		{
			ogp.lineDelete((ogp.field.length - 1) - 1);
			ogp.drawTetris();
		}

		public void threeLineDown()
		{
			for (int i = 3; i >= 1; i--)
				ogp.lineDelete((ogp.field.length - 1) - i);
			ogp.drawTetris();
		}

		public void slow()
		{
			ogp.gameSpeed += 200;
			coolDown(ImageSource.item_slow);
		}

		public void blackout()
		{
			Thread blackTh = new Thread()
			{
				public void run()
				{
					int alpha = 255;
					
					blackPanel.setVisible(true);
					blackPanel.remove(textLabel);
					
					while(alpha > 0)
					{
						blackPanel.setBackground(new Color(0,0,0,alpha));
						alpha -= 25;
						try { sleep(1000); }
						catch(InterruptedException e) { e.printStackTrace(); }
					}
					blackPanel.setVisible(false);
				}
			};
			blackTh.start();
			coolDown(ImageSource.item_blackout);
		}

		public void zigzag()
		{
			int row = 0;

			for (int i = 0; i < ogp.field.length; i++)
			{
				for (int j = 1; j < ogp.field[0].length - 1; j++)
				{
					if (ogp.field[i][j] > 0)
					{
						row = i;
						break;
					}
				}
				if (row > 0)
					break;
			}

			if (row > 0)
			{
				for (int i = row; i < ogp.field.length; i++)
				{
					Vector<Integer> v = new Vector<Integer>();

					for (int j = 1; j < ogp.field[0].length - 1; j++)
						v.add(ogp.field[i][j]);

					for (int j = 1; j < ogp.field[0].length - 1; j++)
					{
						int index = (int) (Math.random() * v.size());
						ogp.field[i][j] = v.get(index);
						v.remove(index);
					}
				}
			}

			ogp.drawTetris();
		}

		public void oneLineUp()
		{
			/* 전역: int[][] */ogp.copy = new int[ogp.field.length][ogp.field[0].length];
			for (int row = 1; row < ogp.field.length - 1; row++)
				for (int col = 0; col < ogp.field[0].length; col++)
					ogp.copy[row - 1][col] = ogp.field[row][col];

			backArray(ogp.field, ogp.copy);

			int num = (int) (Math.random() * 10 + 1);
			for (int col = 1; col < ogp.field[0].length - 1; col++)
				if (col != num)
					ogp.field[ogp.field.length - 2][col] = 100;

			ogp.drawTetris();

			ogp.copy = null;
		}

		public void threeLineUp()
		{
			/* 전역: int[][] */ogp.copy = new int[ogp.field.length][ogp.field[0].length];
			for (int row = 3; row < ogp.field.length - 1; row++)
				for (int col = 0; col < ogp.field[0].length; col++)
					ogp.copy[row - 3][col] = ogp.field[row][col];

			backArray(ogp.field, ogp.copy);

			int num = (int) (Math.random() * 10 + 1);
			for (int row = ogp.field.length - 4; row < ogp.field.length - 1; row++)
				for (int col = 0; col < ogp.field[0].length - 1; col++)
					if (col != num)
						ogp.field[row][col] = 100;

			ogp.drawTetris();

			ogp.copy = null;
		}

		public void fast()
		{
			ogp.gameSpeed -= 200;
			coolDown(ImageSource.item_fast);
		}

		public void coolDown(ImageIcon icon)
		{
			ogp.item_using.removeAll();
			ogp.usingItemLabel.add(new UsingItemLabel(icon));

			for (int i = 0; i < ogp.usingItemLabel.size(); i++)
			{
				ogp.item_using.add(ogp.usingItemLabel.get(i));
				ogp.usingItemLabel.get(i).setLocation(i * 30, 0);
			}
			ogp.item_using.repaint();
		}
	}

	class UsingItemLabel extends JLabel implements Runnable
	{
		int w, h;
		int colSpeed = 1;
		int rowSpeed = 1;
		int x1, x2, x3, x4, x5, x6, x7;
		int y1, y2, y3, y4, y5, y6, y7;
		int Switch = 1;
		int times = 10;
		boolean End = true;
		ImageIcon icon;

		UsingItemLabel(ImageIcon icon)
		{
			w = 30;
			h = 30;
			setSize(w, h);

			this.icon = icon;
			Thread coolTimeTh = new Thread(this);

			x1 = w / 2;
			x2 = w / 2;
			x3 = w;
			x4 = w;
			x5 = 0;
			x6 = w / 2;
			x7 = 0;

			y1 = 0;
			y2 = 0;
			y3 = 0;
			y4 = h;
			y5 = h;
			y6 = h / 2;
			y7 = 0;

			coolTimeTh.start();
		}

		public void run()
		{
			int i = 0;

			while (End)
			{
				if (x2 < w)
					x2 += colSpeed;
				else
				{
					Switch = 2;

					if (y3 < h)
						y3 += rowSpeed;
					else
					{
						Switch = 3;

						if (x4 > 0)
							x4 -= colSpeed;
						else
						{
							Switch = 4;

							if (y5 > 0)
								y5 -= rowSpeed;
							else
							{
								Switch = 5;

								if (x7 < w / 2)
									x7 += colSpeed;
								else
									End = false;
							}
						}
					}
				}
				i++;
				repaint();

				if (i % 12 == 0)
					times--;

				try
				{
					Thread.sleep(80);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			// 쿨타임 후 원래 스피드로 복구
			String iconName = icon.toString().substring(14);
			switch (iconName)
			{
			case "fast.png":
				ogp.gameSpeed += 200;
				break;
			case "slow.png":
				ogp.gameSpeed -= 200;
				break;
			}

			ogp.item_using.removeAll();
			ogp.usingItemLabel.remove(this);

			for (int j = 0; j < ogp.usingItemLabel.size(); j++)
			{
				ogp.item_using.add(ogp.usingItemLabel.get(j));
				ogp.usingItemLabel.get(j).setLocation(j * 30, 0);
			}
			ogp.item_using.repaint();
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(icon.getImage(), 0, 0, this);
			g.setColor(Color.BLACK);
			if (Switch == 1)
				g.fillPolygon(new int[] { x1, x2, x6 },
						new int[] { y1, y2, y6 }, 3);
			else if (Switch == 2)
				g.fillPolygon(new int[] { x1, x2, x3, x6 }, new int[] { y1, y2,
						y3, y6 }, 4);
			else if (Switch == 3)
				g.fillPolygon(new int[] { x1, x2, x3, x4, x6 }, new int[] { y1,
						y2, y3, y4, y6 }, 5);
			else if (Switch == 4)
				g.fillPolygon(new int[] { x1, x2, x3, x4, x5, x6 }, new int[] {
						y1, y2, y3, y4, y5, y6 }, 6);
			else
				g.fillPolygon(new int[] { x1, x2, x3, x4, x5, x7, x6 },
						new int[] { y1, y2, y3, y4, y5, y7, y6 }, 7);

			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", 1, 20));
			g.drawString(Integer.toString(times), 7, 22);
		}
	}

}

/******************** 테트리스 시간 클래스 ********************/
class TimeThread extends Thread
{
	GamePanel gp;
	static boolean gameState = true;
	int n;

	TimeThread(GamePanel gp, int n)
	{
		this.gp = gp;
		this.n = n;
	}

	public void run()
	{
		int time = 0;
		int second = 0;
		int minute = 0;
		int count = 1;

		synchronized (this)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		while (gameState)
		{
			if (time % 1000 == 0)
			{
				minute = (time / 1000) / 60;
				second = (time / 1000) % 60;
				String str = String.format("%2d m %02d s", minute, second);
				gp.time.setText(str);
			}
			try
			{
				sleep(1);
			}
			catch (InterruptedException e)
			{
				return;
			}

			time++;

			// 10초마다 gameSpeed 20씩 줄이기
			if (time / count >= 10000 && gp.gameSpeed > 400)
			{
				gp.gameSpeed -= 20;
				count++;
			}
		}
	}
}

class Blocks
{
	int[][] block;
	int start_x;
	int start_y;
	int blockNum;

	Blocks()
	{
		// blockNum = (int)(Math.random() * 7 + 1);
		// block = getBlock();
	}

	public int getBlockNum()
	{
		return blockNum;
	}

	public int getStart_x()
	{
		return start_x;
	}

	public int getStart_y()
	{
		return start_y;
	}

	public int[][] getBlock()
	{
		switch (blockNum)
		{
		case 1: // block - I
			block = new int[][] { { 1, 1, 1, 1 } };
			break;
		case 2: // block - J
			block = new int[][] { { 2, 2, 2 }, { 0, 0, 2 } };
			break;
		case 3: // block - L
			block = new int[][] { { 3, 3, 3 }, { 3, 0, 0 } };
			break;
		case 4: // block - O
			block = new int[][] { { 4, 4 }, { 4, 4 } };
			break;
		case 5: // block - S
			block = new int[][] { { 0, 5, 5 }, { 5, 5, 0 } };
			break;
		case 6: // block - T
			block = new int[][] { { 0, 6, 0 }, { 6, 6, 6 } };
			break;
		case 7: // block - Z
			block = new int[][] { { 7, 7, 0 }, { 0, 7, 7 } };
			break;
		}

		start_x = (12 / 2) - (block[0].length / 2);
		start_y = 0;
		return block;
	}
}