import java.net.URL;
import javax.swing.ImageIcon;

public class ImageSource
{
	static ImageIcon img_field;
	static ImageIcon img_logo;				// 360x640
	static ImageIcon img_logo2;				// 320x480
	static ImageIcon img_button;         
	static ImageIcon img_buttonP;    
	static ImageIcon img_KNUT;
	
	static ImageIcon bg_ground;
	static ImageIcon kakao_tube;
	
	static ImageIcon block_I;
	static ImageIcon block_J;
	static ImageIcon block_L;
	static ImageIcon block_O;
	static ImageIcon block_S;
	static ImageIcon block_T;
	static ImageIcon block_Z;
	
	static ImageIcon block_blue;
	static ImageIcon block_cyan;
	static ImageIcon block_gray;
	static ImageIcon block_green;
	static ImageIcon block_lime;
	static ImageIcon block_orange;
	static ImageIcon block_puple;
	static ImageIcon block_red;
	static ImageIcon block_yellow;
	
	static ImageIcon block_blackout;
	static ImageIcon block_fast;
	static ImageIcon block_lineup_1;
	static ImageIcon block_lineup_3;
	static ImageIcon block_zigzag;
	static ImageIcon block_bomb;
	static ImageIcon block_change;
	static ImageIcon block_linedown_1;
	static ImageIcon block_linedown_3;
	static ImageIcon block_slow;
	
	static ImageIcon item_blackout;
	static ImageIcon item_fast;
	static ImageIcon item_lineup_1;
	static ImageIcon item_lineup_3;
	static ImageIcon item_zigzag;
	static ImageIcon item_bomb;
	static ImageIcon item_change;
	static ImageIcon item_linedown_1;
	static ImageIcon item_linedown_3;
	static ImageIcon item_slow;
	
	static ImageIcon info_i;
	static ImageIcon info_q;
	static ImageIcon btn_replay;
	static ImageIcon img_pencil;
	
	public ImageSource()
	{
		img_field = new ImageIcon(ImageSource.class.getClassLoader().getResource("field.png"));
		img_logo = new ImageIcon(ImageSource.class.getClassLoader().getResource("logo.png"));				// 360x640
		img_logo2 = new ImageIcon(ImageSource.class.getClassLoader().getResource("logo2.png"));			// 320x480
		img_button = new ImageIcon(ImageSource.class.getClassLoader().getResource("button.png"));         // 
		img_buttonP = new ImageIcon(ImageSource.class.getClassLoader().getResource("button_push.png"));    // 
		img_KNUT = new ImageIcon(ImageSource.class.getClassLoader().getResource("KNUT.png"));
		
		bg_ground = new ImageIcon(ImageSource.class.getClassLoader().getResource("bg_ground.png"));
		kakao_tube = new ImageIcon(ImageSource.class.getClassLoader().getResource("kakao_tube.png"));
		
		block_I = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_I.png"));
		block_J = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_J.png"));
		block_L = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_L.png"));
		block_O = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_O.png"));
		block_S = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_S.png"));
		block_T = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_T.png"));
		block_Z = new ImageIcon(ImageSource.class.getClassLoader().getResource("block_Z.png"));
		
		block_blue = new ImageIcon(ImageSource.class.getClassLoader().getResource("blue.png"));
		block_cyan = new ImageIcon(ImageSource.class.getClassLoader().getResource("cyan.png"));
		block_gray = new ImageIcon(ImageSource.class.getClassLoader().getResource("gray.png"));
		block_green = new ImageIcon(ImageSource.class.getClassLoader().getResource("green.png"));
		block_lime = new ImageIcon(ImageSource.class.getClassLoader().getResource("lime.png"));
		block_orange = new ImageIcon(ImageSource.class.getClassLoader().getResource("orange.png"));
		block_puple = new ImageIcon(ImageSource.class.getClassLoader().getResource("puple.png"));
		block_red = new ImageIcon(ImageSource.class.getClassLoader().getResource("red.png"));
		block_yellow = new ImageIcon(ImageSource.class.getClassLoader().getResource("yellow.png"));
		
		block_blackout = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_blackout.png"));
		block_fast = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_fast.png"));
		block_lineup_1 = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_lineup_1.png"));
		block_lineup_3 = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_lineup_3.png"));
		block_zigzag = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_zigzag.png"));
		block_bomb = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_bomb.png"));
		block_change = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_change.png"));
		block_linedown_1 = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_linedown_1.png"));
		block_linedown_3 = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_linedown_3.png"));
		block_slow = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_slow.png"));
		
		item_blackout = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_blackout.png"));
		item_fast = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_fast.png"));
		item_lineup_1 = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_lineup_1.png"));
		item_lineup_3 = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_lineup_3.png"));
		item_zigzag = new ImageIcon(ImageSource.class.getClassLoader().getResource("a_zigzag.png"));
		item_bomb = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_bomb.png"));
		item_change = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_change.png"));
		item_linedown_1 = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_linedown_1.png"));
		item_linedown_3 = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_linedown_3.png"));
		item_slow = new ImageIcon(ImageSource.class.getClassLoader().getResource("p_slow.png"));
		
		info_i = new ImageIcon(ImageSource.class.getClassLoader().getResource("info_i.png"));
		info_q = new ImageIcon(ImageSource.class.getClassLoader().getResource("info_q.png"));
		btn_replay = new ImageIcon(ImageSource.class.getClassLoader().getResource("replay.png"));
		img_pencil = new ImageIcon(ImageSource.class.getClassLoader().getResource("pencil.png"));	
	}
}
