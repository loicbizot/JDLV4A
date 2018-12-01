package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import init.MouseDragNDrop;

public class DisplayControl {

	private Window w;
	private Auto_refresh_display ARD;
	private Thread auto_refresh;
	private Map_Engine me;
	private Thread map_engine;
	private Clip clip;
	
	private Mode mode;
	
	// arriere plan de tous les boutons
	private Image button_img;
	
	// Home
	private Home home;
	private JButton play_button;
	private JButton help_button;
	private JButton quit_button;

	private Image play_img;
	private Image help_img;
	private Image quit_img;
	private Image back_img;
	private Image launch_img;
	private Image pause_img;
	private Image resume_img;
	
	// Init
	private Init init;
	
	// Layout
	private Layout layout;
	private int game_speed;
	
	// Help
	private Help help;
	
	public DisplayControl() {
		
		// musiques
		
		clip = null;
		
		try {
			AudioInputStream themeAdress = AudioSystem.getAudioInputStream(getClass().getResource("/musiqueJDLV2.wav"));
			clip = AudioSystem.getClip(null);
			clip.open(themeAdress);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("bon bah pas de musique alors");
		}
		
		// initialisation de la fenetre
		Toolkit t = Toolkit.getDefaultToolkit();
		this.w = new Window(t.getScreenSize().width, t.getScreenSize().height);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Fichier");
		JMenu menu2 = new JMenu("Aide");
		JMenuItem menu2Item = new JMenuItem("Afficher l'aide");
		JMenuItem menuItem = new JMenuItem("Agrandir");
		JMenuItem menuItem2 = new JMenuItem("Quitter");
		
		menuBar.add(menu);
		menuBar.add(menu2);
		
		menu.add(menuItem);
		menu.add(menuItem2);
		menu2.add(menu2Item);
		w.setJMenuBar(menuBar);
		
		w.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				stop_auto_refresh();
				System.exit(0);
			}
		});
		
		// agrandissement fen�tre
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				w.setExtendedState(Window.MAXIMIZED_BOTH);
			}
		});
		
		// fermeture fenetre
		menuItem2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stop_auto_refresh();
				System.exit(0);
			}
		});
		
		// afficher l'aide
		menu2Item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == menu2Item){
					startHelpMode();
				}
			}
		});
		
		w.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// imgs
		try {
			
			button_img = ImageIO.read(getClass().getResource("/wooden_plank.png"));
			play_img = ImageIO.read(getClass().getResource("/play_text.png"));
			help_img = ImageIO.read(getClass().getResource("/help_text.png"));
			quit_img = ImageIO.read(getClass().getResource("/quit_text.png"));
			back_img = ImageIO.read(getClass().getResource("/back_text.png"));
			launch_img = ImageIO.read(getClass().getResource("/launch_text.png"));
			pause_img = ImageIO.read(getClass().getResource("/pause_text.png"));
			resume_img = ImageIO.read(getClass().getResource("/resume_text.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// intiailise home
		
		play_button = new JButton("Play") {
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(play_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		
		play_button.setOpaque(false);
		play_button.setContentAreaFilled(false);
		play_button.setBorderPainted(false);
		
		help_button = new JButton("Help"){
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(help_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		
		help_button.setOpaque(false);
		help_button.setContentAreaFilled(false);
		help_button.setBorderPainted(false);
		
		quit_button = new JButton("Quit"){
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(quit_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		
		quit_button.setOpaque(false);
		quit_button.setContentAreaFilled(false);
		quit_button.setBorderPainted(false);
		
		ActionHome go = new ActionHome();
		play_button.addActionListener(go);
		
		ActionHome h = new ActionHome();
		help_button.addActionListener(h);
		
		ActionHome leave = new ActionHome();
		quit_button.addActionListener(leave);
		
		home = new Home(play_button,help_button,quit_button);
		
		// fin initialisation Home
		
		// initialisation Init
		
		init = new Init();
		MouseDragNDrop mDND = new MouseDragNDrop(init);
		
		init.addMouseListener(mDND);
		init.addMouseMotionListener(mDND);
		
		init.addMouseWheelListener(new Zoom(init));
		init.addKeyListener(new CamMovement(init));
		
		JButton launch = new JButton("Launch"){
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(launch_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		launch.setOpaque(false);
		launch.setContentAreaFilled(false);
		launch.setBorderPainted(false);
		launch.setPreferredSize(new Dimension(64,32));
		
		JButton back_init = new JButton("Back") {
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(back_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		back_init.setOpaque(false);
		back_init.setContentAreaFilled(false);
		back_init.setBorderPainted(false);	
		back_init.setPreferredSize(new Dimension(64,32));
		back_init.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				startHomeMode();
			}
			
		});
		
		JPanel bottom = new JPanel();
		bottom.add(launch);
		bottom.add(back_init);
		
		launch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(init.verif()) {
					playMusic();
					startLayoutMode();
				}
			}
			
		});
		init.add(bottom,BorderLayout.SOUTH);
		
		// fin initialisation init
		
		// initialisation Help
		
		help = new Help();
		JButton back = new JButton("Back") {
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(back_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);	
		back.addActionListener(new retourHelp());
		back.setPreferredSize(new Dimension(64,32));
		
		help.add(back);
		
		// fin initialisation Help
		
		// initialisation Layout
		
		game_speed = 500;
		this.layout = new Layout();
		CamMovementMouse cmm = new CamMovementMouse(layout);
		CamMovement cm = new CamMovement(layout);
		Zoom z = new Zoom(layout);
		layout.addMouseListener(cmm);
		layout.addMouseMotionListener(cmm);
		layout.addKeyListener(cm);
		layout.addMouseWheelListener(z);
		layout.setLayout(new BorderLayout());
		
		JPanel bottom_layout = new JPanel();
		
		Pause_resume pause = new Pause_resume();
		
		pause.setOpaque(false);
		pause.setContentAreaFilled(false);
		pause.setBorderPainted(false);	
		pause.setPreferredSize(new Dimension(64, 32));
		
		pause.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(!pause.isPaused()) {
					stop_engine();
					pause.pause();
					pauseMusic();
				}
				else {
					start_map_engine(layout);
					pause.resume();
					playMusic();
				}
				layout.requestFocus();
			}
			
		});
		
		layout.addKeyListener(new KeyAdapter() {
			
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyChar() == 'p') {
					
					if(!pause.isPaused()) {
						stop_engine();
						pause.pause();
						pauseMusic();
					}
					else {
						start_map_engine(layout);
						pause.resume();
						playMusic();
					}
					
				}
				
			}
			
		});
		
		JButton back_layout = new JButton("Back") {
			
			private static final long serialVersionUID = 0L;
			
			public void paintComponent(Graphics g) {
				g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
				g.drawImage(back_img,0,0, this.getWidth(), this.getHeight(),this);
			}
			
		};
		back_layout.setOpaque(false);
		back_layout.setContentAreaFilled(false);
		back_layout.setBorderPainted(false);	
		back_layout.setPreferredSize(new Dimension(64,32));
		back_layout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pause.paused = false;
				stop_engine();
				stop_auto_refresh();
				stopMusic();
				startInitMode();
			}
			
		});
		
		JSlider speed = new JSlider();
		speed.setMinimum(0);
		speed.setMaximum(100);
		speed.setValue(50);
		speed.setMinorTickSpacing(10);
		speed.setMajorTickSpacing(20);
		
		speed.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				int speedValue = speed.getValue();
				game_speed = 500 - speedValue*5/2;
			}
			
		});
		
		speed.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				
				layout.requestFocus();
				
			}
			
		});
		
		bottom_layout.add(pause);
		bottom_layout.add(back_layout);
		bottom_layout.add(new JLabel("Speed"));
		bottom_layout.add(speed);
		
		layout.add(bottom_layout,BorderLayout.SOUTH);
		
		// fin initialisation Layout
		
		this.mode = Mode.HOME;
		this.w.setContentPane(home);
		
		this.w.setVisible(true);
		this.w.reload();
		
	}
	// fin constructeur
	
	void startHomeMode() {
		this.mode = Mode.HOME;
		this.w.setContentPane(home);
		w.reload();
		home.requestFocus();
	}
	
	void startInitMode() {
		w.setContentPane(init);
		w.reload();
		init.requestFocus();
		this.mode = Mode.INIT;
	}
	
	public void startHelpMode() {
		w.setContentPane(help);
		w.reload();
		help.requestFocus();
	}
	
	public void startLayoutMode() {
		layout.setMap(init.loadMap());
		w.setContentPane(layout);
		w.reload();
		layout.requestFocus();
		this.mode = Mode.LAYOUT;
		auto_refresh(layout);
		start_map_engine(layout);
	}
	
	public void retourHelp() {
		switch(mode) {
		case HOME:
			w.setContentPane(home);
			break;
		case INIT:
			w.setContentPane(init);
			break;
		case LAYOUT:
			w.setContentPane(layout);
			break;
		}
		w.reload();
		w.getContentPane().requestFocus();
	}
	
	// Layout && Init Zoom
	private class Zoom implements MouseWheelListener{
		
		private ScaledPan contentPane;
		
		public Zoom(ScaledPan contentPane) {
			this.contentPane = contentPane;
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			
			int scale = contentPane.getScale();
			int new_scale = scale - e.getWheelRotation();
			if(new_scale < 8)
				new_scale = 8;
			if(new_scale > 128)
				new_scale = 128;
			contentPane.setScale(new_scale);
			
		}
		
	}
	
	// Layout && init CamMovement Keyboard
	private class CamMovement implements KeyListener{

		private KeyMovablePan contentPane;
		
		public CamMovement(KeyMovablePan contentPane) {
			this.contentPane = contentPane;
		}
		
		public void keyTyped(KeyEvent e) {
			
		}

		public void keyPressed(KeyEvent e) {

			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_UP:
				contentPane.moveView(0, -4);
				break;
			
			case KeyEvent.VK_RIGHT:
				contentPane.moveView(4, 0);
				break;
				
			case KeyEvent.VK_DOWN:
				contentPane.moveView(0, 4);
				break;
				
			case KeyEvent.VK_LEFT:
				contentPane.moveView(-4, 0);
				break;
				
			default:
				break;
			
			}
			
		}

		public void keyReleased(KeyEvent e) {
			
		}
		
	}
	
	// Layout CamMouvement Mouse
	private class CamMovementMouse implements MouseListener, MouseMotionListener{

		Point2D origine;
		private Layout contentPane;
		
		public CamMovementMouse(Layout l) {
			contentPane = l;
		}
		
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			
			origine = new Point2D.Float(e.getX(), e.getY());
			
		}

		public void mouseReleased(MouseEvent e) {
			
			origine = null;
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
			Point2D nou = new Point2D.Float(e.getX(), e.getY());
			contentPane.moveView(-(int)nou.getX()*8/contentPane.getScale()
					+ (int)origine.getX()*8/contentPane.getScale() , 
					-(int)nou.getY()*8/contentPane.getScale()  
					+ (int)origine.getY()*8/contentPane.getScale() );
			origine = nou;
			
		}

		public void mouseMoved(MouseEvent e) {
			
		}
		
	}
	
	// Layout auto_refresh
	private void auto_refresh(Layout l) {
		
		if(auto_refresh == null) {
			ARD = new Auto_refresh_display(l);
			auto_refresh = new Thread(ARD);
			auto_refresh.start();
		}
		
	}
	
	// Layout stop_auto_refresh
	private void stop_auto_refresh() {
		
		if(auto_refresh != null) {
			
			try {
				ARD.stop();
				auto_refresh.join();
				ARD = null;
				auto_refresh = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	// Layout Auto_refresh_display
	private class Auto_refresh_display implements Runnable{

		private boolean state;
		private Layout l;
		
		public Auto_refresh_display(Layout l) {
			this.state = true;
			this.l = l;
		}
		
		public void run() {
			
			while(state) {
				l.repaint();
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public void stop() {
			this.state = false;
		}
		
	}
	
	private void start_map_engine(Layout l) {
		
		if(map_engine == null) {
			me = new Map_Engine(l);
			map_engine = new Thread(me);
			map_engine.start();
		}
		
	}
	
	private void stop_engine() {
		if(me != null) {
			
			me.stop();
			try {
				map_engine.join();
				me = null;
				map_engine = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//Layout Map_Engine
	private class Map_Engine implements Runnable{

		private Layout to_run;
		private boolean running;
		
		public Map_Engine(Layout to_run) {
			this.to_run = to_run;
			running = true;
		}
		
		public void run() {
			
			try {
				
				while(running)
					to_run.next_turn(game_speed);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public void stop() {
			running = false;
		}
		
	}
	
	private class ActionHome implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == play_button){
				startInitMode();
			}else if(e.getSource() == help_button){
				startHelpMode();
			}else if(e.getSource() == quit_button){
				stop_auto_refresh();
				System.exit(0);
			}
			
		}
	}
	
	private class retourHelp implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			retourHelp();
		}
		
	}
	
	private void playMusic() {
		if(clip != null) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		
	}
	
	private void pauseMusic() {
		if(clip != null)
			clip.stop();
	}
	
	private void stopMusic() {
		if(clip != null) {
			clip.setFramePosition(0);
			clip.stop();
		}
		
	}
	
	private class Pause_resume extends JButton{
			
		private static final long serialVersionUID = 0L;
		
		boolean paused = false;
		
		public void paintComponent(Graphics g) {
			g.drawImage(button_img,0,0, this.getWidth(), this.getHeight(),this);
			if(!paused)
				g.drawImage(pause_img,0,0, this.getWidth(), this.getHeight(),this);
			else
				g.drawImage(resume_img,0,0, this.getWidth(), this.getHeight(),this);
		}
		
		public void pause() {
			paused = true;
		}
		
		public void resume() {
			paused = false;
		}
		
		public boolean isPaused() {
			return paused;
		}
		
	}
	
}
