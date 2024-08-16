package platformer;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	
	/* CHANGES
	
	added "level editor mode" with a bunch of cheats + stuff
	removed sniper enemy's range limit
	window resizing
	grappling hook
	flaming particles for enemy rocket explosion
	lava shiny
	several new/revamped levels
	
	added droneEnemy, droneGunEnemy
	some more commands for level editor
	
	*/
	
	/* to do list
	 	 
	EASY/medium difficulty levels. teaching mechanics
	water droplets to regain moisture
	
	main menu? actual win screen?
	add actual level editing (?) maybe
	 
	more levels + enemies
	destructible map elements?

	visual indication for grappling hook ready?
	
	Enemy ideas:
	Stealth enemy, appears behind player
	Melee enemy?
	Boss enemies? (Mech?)
		 
 	fastest run: 01:35:90
	
	fastest run: 01:01:46 (before other updates)
	fastest run: 00:52:44 (before grappling hook+updates)
	fastest run: 00:56:26 (before sniper buff)

	 */
	

	private int fps;
	private static boolean running;
	private static Thread thread;
	
	public static String recentInput = "";
	public static boolean newInput = false;
	private static Thread inputThread = new Thread() {		
		
		public void run() {			
			Scanner scanner = new Scanner(System.in);
			
			while (true) {
				if (scanner.hasNextLine()) {
					Game.recentInput = scanner.nextLine();
					Game.newInput = true;
					
					
				}
			}
		}
	};
		
	public JFrame frame;
	
	//130, 247, 255
	//114, 244, 252
	//150, 200, 255
	public static Color backgroundColor = new Color(130, 247, 255); //(130, 247, 255)
	public static MapHandler maphandler = new MapHandler();
	public static EntityHandler entityhandler = new EntityHandler();
	public static KeyTracker keytracker = new KeyTracker();
	public static MouseClickTracker mouseclicktracker = new MouseClickTracker();
	public static MouseTracker mousemotiontracker = new MouseTracker();
	public static Scanner scanner = new Scanner(System.in);
	public static Player player;
	public static DoorObject door;
	public static SettingsWindow settingsmenu;
	public static GrapplingHook grapplingHook;
	
	public static double cameraX = 35;
	public static double cameraY = 35;
	public static double cameraXoffset = 0;
	public static double cameraYoffset = 0;
	public static int lastDeathX = 0;
	public static int lastDeathY = 0;
	
	public static int spawnInvincibilityTimer = 0;
	
	public static int jumpTimer = 0;
	public static int midairTime = 0;
	public static double xVelocity = 0;
	public static double yVelocity = 0;
	public static double totalVelocity = 0;

	public static double mouseX = 1;
	public static double mouseY = 1;
	public static boolean mouseDown = false;
	public static boolean rightMouseDown = false;
	public static boolean newClick = true;
	public static boolean newRightClick = true;

	public static boolean slowMotionMode = false;
	public static boolean canToggleSlowMotion = true;
	public static double shotgunAngleOffset = 0;
	public static double shotgunRecoilOffset = 0;
	public static double shotgunX = 500;
	public static double shotgunY = 500;
	public static double shotgunMovementDelay = 3;
	public static int shotgunReloadTimer = 0;
	public static int grapplingHookReloadTimer = 0; 
	public static int deathAnimationTimer = 0;

	public static long gameStart = System.currentTimeMillis();
	public static long lastTime = System.currentTimeMillis();
	
	public static int splashDamageEffectAlpha = 0;
	public static int slowMoAlpha = 0;
	public static int[] rainX = new int[0];
	public static int[] rainY = new int[0];
	public static int rainCount = 50;

	public static boolean paused;
	public static int gameTicks;
	public static int enemiesLeft = 0;
	public static int level = 0;

	public static int comboTimer = -1;
	//240, +180, +180
	//0-240, 240-420, 420-600
	public static double defaultComboTextSize = 50, boostedComboTextSize = 75, comboTextSize = 50;
	
	public static int introScreenTimer = -1;
	
	public static String[] intro = {
			
			"Centuries ago, the Lands Below dried. No water remained.",
			"No life could persist.",
			"",
			"The Clouds had long ruled the skies as a home,",
			"but the Strawberries needed water to sustain their kind.",
			"They led a conquest of the stratosphere, slaughtering Clouds",
			"and constructing massive floating factories to harvest their water.",
			"In their wake they left the heavens bloodied and poisoned,",
			"until just one Cloud still stood.",
			"",
			"The ancient prophecies do not foretell your victory...",
			"...but they have been wrong once already.",
			"",
			"SKY SECTOR 7-13",
			"ALTITUDE 120 HOURS",
			"",
			"",
			"",
			"",
			"",
			""
	};
	
	public static String[] levels = {
			"0,0,1500; ,ground.-300.200.800.100,ground.750.200.350.100,sign:Use [A] and [D] to move and [W] to jump.100.115.75.85,ground.1000.300.100.200,ground.1100.400.700.100,ground.1700.100.100.300,sign:[LEFT-CLICK] to shoot your shotgun and boost yourself.1000.115.-925.-30,lava.1100.250.600.150,,,decorationColor.-200.300.50.2000.140.199.202,decorationColor.350.300.50.2000.140.199.202,decorationColor.850.300.50.2000.140.199.202,ground.1800.100.400.100,sign:Destroy enemies by crashing into them with enough speed.1900.15.-1825.70,ground.2100.200.100.200,ground.2200.300.400.100,ground.2600.100.100.300,ground.2700.100.500.100,crate.2200.200.100.100,door.3000.-125.125.225,decorationColor.1650.500.50.2000.140.199.202,decorationColor.2200.400.50.2000.140.199.202,decorationColor.3050.200.50.2000.140.199.202,sign:You must destroy all enemies to complete a level.2800.15.75.85,decorationColor.-700.-200.400.400.140.199.202,groundColor.-700.-200.400.50.150.150.150,groundColor.-700.150.400.50.150.150.150,groundColor.-700.-150.50.300.150.150.150,groundColor.-350.-150.50.50.150.150.150,groundColor.-350.100.50.50.150.150.150,decorationColor.-660.-2000.20.1800.75.75.75,decorationColor.-360.-2000.20.1800.75.75.75,decorationColor.-660.200.20.2500.75.75.75,decorationColor.-360.200.20.2500.75.75.75; ,basicEnemy.2400.200",
			"0,0,1500; ,ground.-200.100.500.100,decorationColor.-100.-125.125.225.75.75.75,ground.400.-600.100.400,ground.500.-300.1000.100,ground.-200.-600.600.100,ground.-300.-600.100.800,ground.1600.0.400.100,crate.900.-400.100.100,sign:[RIGHT-CLICK] to shoot a \"grappling hook\" and stick to surfaces.200.15.75.85,sign:Try destroying an enemy bullet by ramming into it.1400.-385.-1325.470,sign:[SHIFT] to activate slow-motion.1900.-85.-1825.170,decorationColor.-200.200.50.2500.140.199.202,decorationColor.1200.-200.50.2500.140.199.202,decorationColor.150.200.50.150.140.199.202,decorationColor.-150.350.350.50.140.199.202,decorationColor.1850.100.50.2500.140.199.202,ground.-900.-800.300.100,door.-812.-1025.125.225,decorationColor.-750.-700.50.200.140.199.202,decorationColor.-750.-500.450.50.140.199.202; ,pistolEnemy.915.-500,basicEnemy.600.-400,basicEnemy.-100.-700",
			"0,0,1500; ,ground.-300.100.600.100,decorationColor.-45.-125.125.225.75.75.75,,decorationColor.-600.125.300.50.140.199.202,ground.-1600.100.1000.100,ground.-200.200.100.1200,ground.-800.200.100.900,ground.-1400.500.100.900,ground.-1400.1400.1300.100,ground.-1700.100.100.400,ground.-1700.500.300.100,door.-1575.275.125.225,crate.-850.-50.150.150,crate.100.0.100.100,decorationColor.-275.1500.50.700.140.199.202,decorationColor.-1275.1500.50.700.140.199.202,sign:Use your shotgun at the peak of your jump for the most height.112.-85.75.85,sign:Pull the fishing rod when it's most stretched to get the most boost.-1088.1315.75.85,ground.-1200.200.300.100; ,pistolEnemy.-300.1300,pistolEnemy.-800.1300",
			//LEVEL(S) HERE? more grappling hook usage
			"0,0,1500;ground.-400.100.800.100,ground.-500.-1000.100.1200,ground.400.-900.100.1100,ground.-400.-300.100.100,,ground.0.-800.100.100,ground.-1100.-1000.600.100,ground.-1100.-1400.100.400,ground.-1000.-1400.1900.100,ground.500.-900.600.100,ground.1000.-1400.100.500,crate.850.-1050.150.150,door.550.-1625.125.225,ground.-400.-500.100.50,decorationColor.25.-700.50.800.140.199.202,decorationColor.900.-1375.100.50.140.199.202,decorationColor.-850.-900.50.250.140.199.202,decorationColor.-800.-700.300.50.140.199.202,decorationColor.-700.-650.50.2500.140.199.202,decorationColor.500.-600.250.50.140.199.202,decorationColor.750.-800.50.250.140.199.202,decorationColor.-650.400.550.50.140.199.202,decorationColor.-150.200.50.200.140.199.202,decorationColor.-75.-125.125.225.75.75.75;basicEnemy.600.-1000,shotgunEnemy.850.-1120,pistolEnemy.-480.-1070",
			"0,-1000,1500;ground.-100.-900.200.100,ground.-500.-1300.900.100,ground.-500.-1200.100.1400,ground.400.-1300.100.800,ground.-400.100.800.100,ground.400.-200.100.400,ground.300.-900.100.100,ground.500.-200.600.100,ground.500.-600.200.100,ground.500.-1050.100.100,crate.-400.-50.150.150,crate.200.-100.200.200,crate.-400.-125.75.75,,,decorationColor.-63.-1125.125.225.75.75.75,door.-63.-1525.125.225,decorationColor.-400.-875.300.50.140.199.202,decorationColor.100.-875.200.50.140.199.202,decorationColor.-25.200.50.2500.140.199.202,decorationColor.600.-1025.25.50.140.199.202,decorationColor.625.-1025.50.425.140.199.202,decorationColor.625.-500.50.300.140.199.202,decorationColor.625.-100.50.2500.140.199.202;uziEnemy.-375.-200,basicEnemy.130.30,shotgunEnemy.800.-270,uziEnemy.180.-175",
			"400,300,1500;ground.200.500.600.100,ground.500.200.600.100,ground.1100.25.100.275,ground.100.350.100.250,ground.-500.-50.800.100,ground.-600.-150.100.200,ground.-300.-150.100.100,ground.1100.-500.500.100,ground.1900.-500.350.100,ground.1000.-1000.100.600,ground.2250.-1000.100.600,ground.1200.25.1000.100,ground.1000.-1100.1350.100,crate.1700.-125.150.150,crate.1585.-75.100.100,,door.1300.-725.125.225,lava.-500.-120.200.70,decorationColor.-500.50.50.2500.140.199.202,decorationColor.125.50.50.300.140.199.202,decorationColor.1025.-400.50.600.140.199.202,decorationColor.2275.-400.50.500.140.199.202,decorationColor.2200.50.75.50.140.199.202,decorationColor.2075.-1000.50.500.140.199.202,ground.2250.-400.100.100,crate.2100.-600.100.100,crate.1940.-650.150.150,crate.1950.-750.100.100,decorationColor.1600.-475.300.50.140.199.202,decorationColor.1025.300.50.275.140.199.202,decorationColor.800.525.225.50.140.199.202,decorationColor.325.275.125.225.75.75.75,crate.1125.-650.150.150,crate.1850.-50.75.75,ground.1100.-850.200.50,decorationColor.1225.-1000.50.150.140.199.202,crate.1150.-950.100.100,decorationColor.-575.-2000.50.1850.140.199.202,sign:Warning - Speed limit enforced by sniper!.700.415.75.85;basicEnemy.800.130,sniperEnemy.1600.-400,shotgunEnemy.0.-50,pistolEnemy.1450.-200", 
			"0,0,1500;ground.-300.-300.100.500,ground.-200.-300.800.100,ground.600.-300.100.500,ground.-200.100.500.100,ground.1100.-300.100.500,ground.1200.-300.700.100,ground.1900.-300.100.400,ground.1800.100.500.100,ground.2300.100.100.500,ground.-50.200.100.300,ground.-50.500.2350.100,ground.700.100.1000.100,ground.450.460.300.40,ground.765.430.90.70,crate.1675.350.150.150,crate.50.400.100.100,door.2100.275.125.225,,decorationColor.50.600.50.2000.140.199.202,decorationColor.2250.600.50.2000.140.199.202,decorationColor.700.-100.400.50.140.199.202,decorationColor.-50.-125.125.225.75.75.75;uziEnemy.775.230,uziEnemy.200.230,shotgunEnemy.1815.30,pistolEnemy.1700.250,sniperEnemy.1300.30",
			//LEVEL HERE - introduce rocket enemy
			"0,0,1500;decorationColor.0.-1925.125.225.75.75.75,ground.-200.-300.900.100,ground.-200.-200.100.300,ground.-200.100.900.100,ground.500.200.100.2800,ground.500.-800.100.500,ground.600.-700.200.100,ground.1200.-900.100.3900,ground.1200.-1000.700.100,ground.1200.-1400.700.100,ground.1900.-1400.100.500,ground.1100.-1400.100.100,,ground.500.-1700.100.900,ground.-200.-1700.700.100,ground.-200.-2100.800.100,ground.-200.-2000.100.300,crate.-100.-1850.150.150,crate.60.-1800.100.100,decorationColor.-50.-125.125.225.75.75.75,decorationColor.1700.-1225.125.225.75.75.75,decorationColor.525.-200.50.300.140.199.202,decorationColor.1225.-1300.50.300.140.199.202,decorationColor.525.-2000.50.300.140.199.202,ground.1200.-2300.100.900,ground.500.-3000.100.900,ground.1300.-2300.700.100,ground.1200.-2700.800.100,,ground.1100.-2700.100.100,ground.1200.-5000.100.2300,ground.500.-5000.100.2000,decorationColor.1300.-2525.125.225.75.75.75,decorationColor.1500.-2525.125.225.75.75.75,door.1700.-2525.125.225,decorationColor.1900.-2525.125.225.75.75.75,ground.2000.-2700.100.100,ground.2000.-2300.100.100,ground.2100.-2700.100.500,crate.1950.-2400.100.100,decorationColor.1225.-2600.50.300.140.199.202,decorationColor.525.-7000.50.2000.140.199.202,decorationColor.1225.-7000.50.2000.140.199.202,ground.1300.-5000.200.100,sign:DANGER - WET FLOOR.1400.-5085.75.85; ,rocketEnemy.1205.-1100,uziEnemy.1600.-1100,sniperEnemy.550.-1800,shotgunEnemy.200.-1800,pistolEnemy.75.-1900,pistolEnemy.1965.-2500",
			"0,0,1500; ,ground.-400.100.900.100,ground.-400.-400.900.100,ground.-400.-300.100.400,decorationColor.-50.-125.125.225.75.75.75,ground.500.100.600.100,ground.500.-400.600.100,crate.850.-50.150.150,ground.1100.100.300.100,ground.1100.-400.300.100,ground.1400.0.100.200,ground.1300.-500.200.100,ground.1500.-100.100.200,ground.1600.-200.100.200,ground.1400.-600.200.100,ground.1500.-700.900.100,ground.1700.-200.800.100,ground.1350.50.50.50,ground.1450.-50.50.50,ground.1550.-150.50.50,ground.1400.-400.50.50,ground.1500.-500.50.50,ground.1600.-600.50.50,ground.2400.-700.600.100,ground.2500.-200.500.100,ground.2900.-600.100.400,door.2700.-425.125.225,ground.2050.-250.300.50,ground.1925.-275.100.75,ground.2375.-275.100.75,decorationColor.2200.-325.100.75.150.150.150,,decorationColor.2275.-385.5.60.75.75.75,decorationColor.2210.-315.80.55.100.255.0,text:WORLD ENDING? Mad Cloud raiding Sky Sector 7-13-2.2250.-375,text:CAPITOL says reinforcements deployed.2250.-340,decorationColor.-200.200.50.2500.140.199.202,decorationColor.-150.350.300.50.140.199.202,decorationColor.150.200.50.200.140.199.202,decorationColor.1600.25.500.50.140.199.202,decorationColor.2100.-100.50.175.140.199.202,decorationColor.1925.75.50.2500.140.199.202; ,uziEnemy.780.-100,shotgunEnemy.890.-200,,shotgunEnemy.1280.0,pistolEnemy.2390.-400,rocketEnemy.1570.-300",
			//LEVEL HERE - introduce drone enemy
			"15,50,1500; ,ground.-200.200.500.100,ground.-200.-100.100.300,ground.200.-100.100.300,ground.-200.-200.200.100,ground.100.-200.200.100,ground.-500.-200.300.100,decorationColor.800.600.400.400.140.199.202,ground.300.-200.300.100,ground.-500.-700.100.500,ground.-500.-800.1100.100,decorationColor.-12.-25.125.225.75.75.75,crate.-250.-300.100.100,ground.-400.-550.200.50,ground.250.-500.300.50,decorationColor.-275.-700.50.150.140.199.202,decorationColor.275.-700.50.200.140.199.202,decorationColor.475.-700.50.200.140.199.202,crate.300.-600.100.100,ground.600.-800.600.100,ground.600.-200.300.100,ground.1200.-800.100.700,ground.1100.-200.100.100,ground.850.-550.350.50,decorationColor.875.-700.50.150.140.199.202,crate.900.-650.100.100,crate.1010.-700.150.150,ground.700.-100.100.800,,ground.700.1000.600.100,ground.1300.1000.800.100,ground.1300.500.800.100,ground.1200.-100.100.100,ground.1200.300.100.400,ground.1300.400.800.100,ground.1300.-200.1000.100,ground.2100.400.200.100,ground.2100.500.200.100,ground.2100.1000.200.100,,groundColor.800.600.150.50.150.150.150,groundColor.1050.600.150.50.150.150.150,groundColor.1150.650.50.350.150.150.150,groundColor.800.650.50.100.150.150.150,groundColor.800.950.350.50.150.150.150,groundColor.800.900.50.50.150.150.150,decorationColor.840.-100.20.700.75.75.75,decorationColor.1140.-100.20.700.75.75.75,crate.1050.850.100.100,ground.-200.300.100.300,ground.200.300.100.300,ground.-200.600.200.100,ground.100.600.200.100,door.-12.300.125.225,ground.300.500.400.100,ground.-600.500.400.100,ground.-700.500.100.600,ground.-700.1100.300.100,ground.500.1100.300.100,ground.-500.1200.1100.100,lava.-400.1130.900.70,decorationColor.-600.1200.50.1000.140.199.202,decorationColor.650.1200.50.1000.140.199.202,ground.200.800.200.50,decorationColor.225.700.50.100.140.199.202,groundColor.1250.0.50.300.150.150.150,crate.1450.300.100.100; ,uziEnemy.-200.-400,uziEnemy.-250.-650,uziEnemy.250.-300,uziEnemy.400.-600,shotgunEnemy.1080.750,sniperEnemy.-500.1000",
			"0,0,1500; ,ground.-300.100.700.100,ground.-300.-200.100.300,ground.-300.-300.1100.100,ground.700.-200.100.400,ground.800.0.2000.100,ground.200.200.100.800,ground.200.1000.100.1000,lavaColor.300.900.500.1500.25.240.25,lavaColor.800.900.500.1500.25.240.25,lavaColor.1300.900.500.1500.25.240.25,lavaColor.1800.900.500.1500.25.240.25,lavaColor.2300.900.500.1500.25.240.25,lavaColor.2800.900.500.1500.25.240.25,crate.1100.870.300.50,ground.2800.0.1500.100,lavaColor.3300.900.500.1500.25.240.25,lavaColor.3800.900.500.1500.25.240.25,crate.3200.870.450.50,ground.4300.400.100.1500,4300.0.700.100,ground.4300.0.700.100,ground.4400.400.600.100,ground.5000.0.100.500,door.4800.175.125.225,decorationColor.-25.-125.125.225.75.75.75; ,shotgunEnemy.1270.700,droneEnemy.2100.300,droneGunEnemy.4000.300,droneGunEnemy.4000.500,rocketEnemy.3400.700",			"0,-200,1500;ground.-400.100.800.100,crate.-100.-100.200.200,ground.-400.-600.100.2500,ground.300.-600.100.2500,ground.-300.-300.100.100,ground.200.-300.100.100,ground.-400.-700.325.100,ground.75.-700.325.100,ground.600.-300.800.2500,ground.950.-400.300.100,ground.1550.-550.800.2500,door.2225.-775.125.225,,decorationColor.-63.-325.125.225.75.75.75,decorationColor.-300.200.600.1500.140.199.202;rocketEnemy.200.-400,rocketEnemy.-265.-400,uziEnemy.700.-400,sniperEnemy.1050.-500,basicEnemy.1984.-750,droneEnemy.1300.-600,droneGunEnemy.-400.-1100"
			//more levels??++
			
			/* classic levels
			"0,0,1500;ground.-100.200.600.100,ground.700.200.1450.100,ground.1100.100.100.100,lava.1200.130.600.70,ground.1800.100.350.100,ground.2050.300.500.100,ground.2550.100.100.300,ground.2650.100.500.100,door.2950.-125.125.225,sign:Use WASD to move and click to shoot your shotgun.0.115.75.85,sign:Use the shotgun's recoil to boost yourself.1108.15.75.85,sign:Kill an enemy by ramming into it really fast.2000.15.75.85,sign:You must kill all enemies to progress.2750.15.75.85,;basicEnemy.2250.0,",
			"0,0,1500;ground.-300.100.600.100,ground.450.-300.100.300,ground.550.-100.500.100,ground.-400.-530.500.100,ground.-400.-630.100.100,ground.0.-630.100.100,ground.-1150.-800.600.100,lava.-300.-600.300.70,door.-1100.-1025.125.225,sign:Press SHIFT to activate slow motion.850.-185.75.85,;pistolEnemy.-385.-700,basicEnemy.800.-250,",
			"0,0,1500;ground.-400.100.800.100,ground.-500.-1000.100.1200,ground.400.-900.100.1100,ground.-400.-300.100.100,ground.-400.-450.100.50,ground.0.-800.100.100,ground.-1100.-1000.600.100,ground.-1100.-1400.100.400,ground.-1000.-1400.1900.100,ground.500.-900.600.100,ground.1000.-1400.100.500,crate.850.-1050.150.150,door.550.-1625.125.225,;basicEnemy.600.-1000,shotgunEnemy.850.-1120,pistolEnemy.-480.-1070,",
			"0,-1000,1500;ground.-100.-900.200.100,ground.-500.-1300.900.100,ground.-500.-1200.100.1400,ground.400.-1300.100.800,ground.-400.100.800.100,ground.400.-200.100.400,ground.300.-900.100.100,ground.500.-200.600.100,ground.500.-600.200.100,ground.500.-1050.100.100,crate.-400.-50.150.150,crate.200.-100.200.200,crate.-400.-125.75.75,sign:You can destroy bullets by ramming into them too.300.-985.75.85,door.-75.-1525.125.225,;uziEnemy.-375.-200,basicEnemy.130.30,shotgunEnemy.800.-270,",
			"400,300,1500;ground.200.500.600.100,ground.500.200.600.100,ground.1100.25.100.275,ground.100.350.100.250,ground.-500.-50.800.100,ground.-600.-150.100.200,ground.-300.-150.100.100,ground.1100.-500.500.100,ground.1900.-500.350.100,ground.1000.-1000.100.600,ground.2250.-1000.100.600,ground.1200.25.1000.100,ground.1000.-1100.1350.100,crate.1700.-125.150.150,crate.1585.-75.100.100,lava.-500.-100.200.50,door.1300.-725.125.225,sign:The sniper doesn't miss.700.415.75.85,;basicEnemy.800.130,sniperEnemy.1600.-400,shotgunEnemy.0.-50,pistolEnemy.1450.-200,",
			"0,0,1500;ground.-300.-300.100.500,ground.-200.-300.800.100,ground.600.-300.100.500,ground.-200.100.500.100,ground.1100.-300.100.500,ground.1200.-300.700.100,ground.1900.-300.100.400,ground.1800.100.500.100,ground.2300.100.100.500,ground.-50.200.100.300,ground.-50.500.2350.100,ground.700.100.1000.100,ground.450.460.300.40,ground.765.430.90.70,crate.1675.350.150.150,crate.50.400.100.100,door.2100.275.125.225,;uziEnemy.775.230,uziEnemy.200.230,shotgunEnemy.1815.30,sniperEnemy.1300.30,",
			"0,0,1500;ground.1000.-3000.100.1700,ground.300.-3000.100.1000,ground.300.-1700.100.1400,ground.1000.-900.100.2500,ground.-600.-1700.1000.100,ground.-600.-2100.1000.100,ground.-300.100.800.100,ground.-300.-300.800.100,ground.-300.-200.100.300,ground.400.-750.200.100,ground.1000.-1000.800.100,ground.300.100.100.2000,ground.900.-1400.1000.100,ground.1800.-1300.100.400,ground.-700.-2100.100.500,crate.-350.-1850.150.150,door.-550.-1925.125.225,;uziEnemy.1000.-1100,sniperEnemy.1400.-1100,pistolEnemy.330.-1800,shotgunEnemy.-100.-1800,rocketEnemy.-270.-1950,",
			"-35,130,1500; ,ground.-300.200.600.100,ground.-300.-100.100.100,ground.200.-100.100.100,door.-62.-700; ,uziEnemy.-300.100,uziEnemy.230.100,uziEnemy.-235.-200,uziEnemy.165.-200",
			"0,-200,1500;ground.-400.100.800.100,crate.-100.-100.200.200,ground.-400.-600.100.2500,ground.300.-600.100.2500,ground.-300.-300.100.100,ground.200.-300.100.100,ground.-400.-700.325.100,ground.75.-700.325.100,ground.600.-300.800.2500,ground.950.-400.300.100,ground.1550.-550.800.2500,door.2225.-775.125.225,;rocketEnemy.200.-400,rocketEnemy.-265.-400,uziEnemy.700.-400,sniperEnemy.1050.-500,basicEnemy.1984.-750,"
			 */
	};
	
	
	/* extra levels
	 * 
	 *   "0,-200,1500;decorationColor.-350.-700.700.2700.150.150.150,ground.-400.100.800.100,crate.-100.-100.200.200,ground.-400.-600.100.2500,ground.300.-600.100.2500,ground.-300.-300.100.100,ground.200.-300.100.100,ground.-400.-700.325.100,ground.75.-700.325.100,ground.600.-300.800.2500,ground.950.-400.300.100,ground.1550.-550.800.2500,door.2225.-775.125.225,ground.-300.200.600.1000;rocketEnemy.200.-400,rocketEnemy.-265.-400,uziEnemy.700.-400,sniperEnemy.1050.-500,basicEnemy.1984.-750,"
	 *   "-35,130,1500; ,ground.-300.200.600.100,ground.-300.-100.100.100,ground.200.-100.100.100,door.-62.-700; ,uziEnemy.-300.100,uziEnemy.230.100,uziEnemy.-235.-200,uziEnemy.165.-200"
	 *   
	 *   "0,0,1500; ,ground.-300.200.800.100,ground.750.200.350.100,sign:Use [A] and [D] to move and [W] to jump.100.115.75.85,ground.1000.300.100.200,ground.1100.400.700.100,ground.1700.100.100.300,sign:[LEFT-CLICK] to shoot your shotgun and boost yourself.1000.115.-925.-30,lava.1100.250.600.150,,,decorationColor.-200.300.50.2000.150.150.150,decorationColor.350.300.50.2000.150.150.150,decorationColor.850.300.50.2000.150.150.150,ground.1800.100.400.100,sign:Destroy enemies by crashing into them with enough speed.1900.15.-1825.70,ground.2100.200.100.200,ground.2200.300.400.100,ground.2600.100.100.300,ground.2700.100.500.100,crate.2200.200.100.100,door.3000.-125.125.225,decorationColor.1650.500.50.2000.150.150.150,decorationColor.2200.400.50.2000.150.150.150,decorationColor.3050.200.50.2000.150.150.150; ,basicEnemy.2400.200"
	//
	*/
	
	//MODIFIABLE

	//Physics
	public static double tickRate = 60.0; // (60.0) Ticks per second
	public static double gravity = 0.75; // (0.75) Player falling speed
	public static double friction = 0.9; // (0.9) Horizontal ground friction
	public static double airfriction = 0.95; // (0.95) Horizontal air friction
	public static double yfriction = 0.99; // (0.99) Vertical air friction
	public static double playerSpeed = 1; // (1) Player acceleration speed
	public static double jumpPower = 20; // (20) Velocity gained from jumping
	public static double airControl = 0.6; // (0.6) Speed while midair, relative to playerSpeed
	public static double cameraMovementDelay = 7.5; // (7.5) Speed at which the camera moves to center the player, less = faster, 1 = no delay
	public static boolean noClip = false; // (false) Prevents player collision with GroundObjects
	public static boolean godMode = false; // (false) Prevents player dying to killPlayer() method
	public static boolean flight = false; // (false) Gives player several cheats

	public static double FLYING_airfriction = 0.8; // (0.8)
	public static double FLYING_playerSpeed = 1.5; // (1.5)
	public static double FLYING_airControl = 1; // (1)
	public static double FLYING_jumpPower = 13; // (13)
	public static int FLYING_jumpCooldown = 0; // (0)
	public static double FLYING_gravity = 0; // (0)
	
	//Display
	public static boolean displayTime = false; // (false) Whether the game displays the clock or not
	public static boolean drawHitboxes = false; // (false) If the game draws entity hitboxes
	public static boolean showFPS = false; // (false) Tell the user the frames per second
	public static double mouseLookRange = 0; // (0) How far the player can expand their vision by moving the mouse, relative to window size
	public static boolean grid = false; // (false) 
	public static int gridStepSize = 100; // (100)
	public static boolean mouseCoords = false; // (false) Display game-coordinates of current mouse position
	public static int rainDensity = 35; // (35) Number of raindrops on 1000x1000 screen

	//Crosshair
	public static boolean showCrosshair = true; // (true)
	public static int crosshairThickness = 6; // (6)
	public static Color crosshairColor = new Color(0, 0, 0); // (0, 0, 0)
	public static int crosshairSpread = 10; // (10)
	public static boolean crosshairDot = true; // (true)
	public static boolean crosshairArms = true; // (true)
	public static int crosshairArmLength = 10; // (10)
	
	//Gameplay
	public static double shotgunReloadTime = 70; // (70) Minimum time between shotgun shots. 
	public static double shotgunRecoilVelocity = 24; // (23) Total velocity gained from shooting the shotgun
	public static double grapplingHookReloadTime = 50; // (50) Minimum time between grappling hook uses
	public static double grapplingHookLaunchVelocity = 45; // (45) Grapping hook velocity
	public static double velocityRequired = 20; // (20) velocity required to kill an enemy
	public static double xVelocityMultiplierOnKill = 0.5; //(0.5) X-Velocity multiplied by this value upon killing an enemy
	public static double gainedVelocityOnKill = 17; // (15) Y-Velocity gained upon killing an enemy
	public static boolean slowMotionAbility = true; // (true) Whether the player can turn on slow motion
	public static int slowMotionFactor = 3; // (3) 1/x ticks are skipped when in slow motion
	public static double splashDamageRange = 175; // (175) How far the player can be to kill an enemy
	public static int jumpCooldown = 5; //(5) Minimum time between jumps. This only exists to fix a physics glitch that caused variable jump heights.
	public static int spawnX = 0; // (0) The player's initial X position
	public static int spawnY = 0; // (0) The player's initial Y position
	public static int deathY = 1500; // (1500) If the player is below (y level above) then they died
	public static int deathAnimationTime = 50; // (50) Duration of death animation 
	public static int spawnInvincibilityTime = 60; // (60) Duration of invincibility after spawning
	public static boolean reloadLevel = false; //(true) Whether the game re-generates the level upon death
	public static boolean moistureLevel = false; //(true) Whether the game's moisture level feature is on
	public static int comboTime1 = 360, comboTime2 = comboTime1 + 300, comboTime3 = comboTime2 + 240; //(360, +300, +240) Duration of moisture levels

	//Cheats/Misc
	public static int startLevel = 0; //(0) Level index which is initially loaded
	public static boolean editPerms = false; // (false) If player allowed to edit using console
	public static boolean inputIsSize = true; // (true) Whether player level-editing inputs are in terms of object size or coordinates
	public static boolean lockEnemies = false; // (false) Stops Enemies from ticking
	public static boolean lockPlayer = false; // (false) Stops Player from responding to player input and experiencing physics
	public static boolean renderingPlayer = true; //(true) Whether rendering Player 
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = tickRate;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		int tps = 0;
		
		while (running) {
			
			amountOfTicks = tickRate;
			if (slowMotionMode) {
				amountOfTicks *= (1 - 1/(double)slowMotionFactor);
			}
			ns = 1000000000 / amountOfTicks;
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				tick();
				tps++;
				delta--;
			}
			if (running) {
				render();
			}
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames; //fps counter
				if (fps < 10) {
					stop();
				}
				frames = 0;
				tps = 0;
			}
		}
		stop();
	}
	public synchronized void start() { //starts game

		introScreenTimer = 0;
		
		settingsmenu = new SettingsWindow(250, 200, 500, 600, "Paused");
		settingsmenu.setupSettings();
		
		this.addKeyListener(keytracker);
		this.addMouseMotionListener(mousemotiontracker);
		this.addMouseListener(mouseclicktracker);
		
		gameStart = System.currentTimeMillis();
		
		maphandler.clearMap();
		
		rainX = new int[rainCount];
		rainY = new int[rainCount];
		initializeRain();
		
		player = new Player(spawnX, spawnY);
		grapplingHook = new GrapplingHook(0, 0, 0, 0);
		//entityhandler.addEntity(grapplingHook);

		level = startLevel;
		
		//TODO this is where your level scripts are
		loadLevelFromString(levels[level]);
		
		player.x = spawnX;
		player.y = spawnY;		

		thread = new Thread(this);
		thread.start();
		
		inputThread.start();
		
		running = true;
		
		if (flight) {
			airfriction = 0.8;
			playerSpeed = 1.5;
			airControl = 1;
			jumpPower = 13;
			jumpCooldown = 0;
		}
		
	}
	
	
	
	public synchronized void stop() { //stops game
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void tick() {

		if (editPerms) {
			//checks for user level edits... TODO revamp this to be in-game instead of from the console
			editLevel();
		}
		
		newClick = !mouseDown;
		mouseDown = mouseclicktracker.mouseDown;
		newRightClick = !rightMouseDown;
		rightMouseDown = mouseclicktracker.rightMouseDown;
				
		mouseX = mousemotiontracker.mouseX;
		mouseY = mousemotiontracker.mouseY;
		
		int lastWindowX = Window.windowX;
		int lastWindowY = Window.windowY;
		Window.windowX = frame.getWidth() - 16;
		Window.windowY = frame.getHeight() - 39;
		
		if (Window.windowX != lastWindowX || Window.windowY != lastWindowY) {
			windowSizeChanged();
		}

		if (introScreenTimer > -1) {
			introScreenTimer++;

			if (introScreenTimer > 1560) {
				introScreenTimer = -1;
				gameStart = System.currentTimeMillis();
			}
			
			if (rightMouseDown && newRightClick) {
				introScreenTimer = -1;
				gameStart = System.currentTimeMillis();
			}
			
			return;
		}
		
		if (paused) {
			gameStart += (System.currentTimeMillis() - lastTime);
		}
		lastTime = System.currentTimeMillis();

		if (!paused) {
			
			gameTicks++;
			
			maphandler.tick();
			entityhandler.tick();
			
			if (slowMoAlpha > 0) {
				tickRain();
			}
						
			//death script
			if (player.y >= deathY) {
				killPlayer();
			}
			if (player.touchingKillObjects()) {
				killPlayer();
			}
			if (player.touchingEnemy()) {
				if (totalVelocity > velocityRequired) {
					
					if (entityhandler.touchingCountedEnemy(player.getHitbox())) {
						entityhandler.getCountedEnemyTouching(player.getHitbox()).kill();
						resetCombo();
					}
					
					//splash damage for killing enemy bullets etc
					List<Enemy> enemies = entityhandler.getEnemies();
					
					for (int i = 0; i < enemies.size(); i++) { //kill enemy
						if (enemies.get(i).getEnemyValue() == 0 && enemies.get(i).distanceToPlayer() <= splashDamageRange) {
							enemies.get(i).kill();
						}
					}
					
					splashDamageEffectAlpha = 175;

					xVelocity *= xVelocityMultiplierOnKill;
					yVelocity = -gainedVelocityOnKill;


										
				} else {
					killPlayer();
					
					/*LinkedList<Enemy> enemies = entityhandler.getEnemies();
					for (int i = 0; i < enemies.size(); i++) { //remove unimportant enemies
						if (enemies.get(i).getEnemyValue() == 0) {
							entityhandler.removeEntity(enemies.get(i));
						}
					}*/

				}
			}
			if (player.getHitbox().intersects(door.getHitbox()) && enemiesLeft == 0 && !flight) {
				
				if (level < levels.length - 1) {
					level++;
					
					if (level == 0) {
						gameStart = System.currentTimeMillis();
					}
					
					resetLevel();
					
				} else { //TODO add actual winning system
					maphandler.clearMap();
					entityhandler.clearEntities();

					addWall(-400, 100, 800, 100);
					addSign(300, 15, "Congratulations, you finished! Time: " + getTimeString());
					
					
					addDoor(-400, -125);
					addSign(-250, 15, "Go through this door to restart.");
					
					addPlayer();
					
					level = -1;
	
				}
				resetPlayer();
				
			}
			
			if (comboTimer > -1 && moistureLevel) {
				comboTimer--;
				
				comboTextSize = defaultComboTextSize + (comboTextSize - defaultComboTextSize) * 0.9;
				
				if (comboTimer == -1) {
					killPlayer();
				}
			}
	
			//System.out.println("MapObjects: " + maphandler.map.size());
			//System.out.println("Entities: " + entityhandler.entities.size());
	
			enemiesLeft = entityhandler.countAliveEnemies();
			
			splashDamageEffectAlpha -= 5;
			if (splashDamageEffectAlpha < 0) {
				splashDamageEffectAlpha = 0;
			}
	
			//CONTROLS
			double airfriction = (Game.flight) ? Game.FLYING_airfriction : Game.airfriction;
			double playerSpeed = (Game.flight) ? Game.FLYING_playerSpeed : Game.playerSpeed;
			double airControl = (Game.flight) ? Game.FLYING_airControl : Game.airControl;
			double jumpPower = (Game.flight) ? Game.FLYING_jumpPower : Game.jumpPower;
			int jumpCooldown = (Game.flight) ? Game.FLYING_jumpCooldown : Game.jumpCooldown;
			double gravity = (Game.flight) ? Game.FLYING_gravity : Game.gravity;
			
			
			
			jumpTimer++;
			if (jumpTimer > jumpCooldown) {
				jumpTimer = jumpCooldown;
			}
			
			if (slowMotionAbility) {
				if (keytracker.slowModeKeyPressed) {
					if (canToggleSlowMotion) {
						slowMotionMode = !slowMotionMode;
						canToggleSlowMotion = false;
					}
				} else {
					canToggleSlowMotion = true;
				}
			}
			
			if (!lockPlayer) {
				if (spawnInvincibilityTimer > 0) {
					spawnInvincibilityTimer++;
					if (spawnInvincibilityTimer > spawnInvincibilityTime) {
						if (!editPerms) {
							godMode = false;
						}
						spawnInvincibilityTimer = 0;
					}
				}

				if (keytracker.jumpKeyPressed) {
					if (midairTime < 2 && jumpTimer == jumpCooldown) {
						yVelocity = -jumpPower;
						jumpTimer = 0;
					}
				}
				
				if (keytracker.leftKeyPressed) {
					xVelocity -= playerSpeed * (midairTime > 1 ? airControl : 1);
				}
				
				if (keytracker.rightKeyPressed) {
					xVelocity += playerSpeed * (midairTime > 1 ? airControl : 1);
				}
			
				//shotgun movement calculations
				shotgunX += (player.x - shotgunX)/shotgunMovementDelay;
				shotgunY += (player.y - shotgunY)/shotgunMovementDelay;
				
				if (shotgunReloadTimer > 0) { //reload animation
					shotgunReloadTimer++;
					
					shotgunRecoilOffset *= (shotgunRecoilOffset > 0 ? 0.95 : 1);
					
					double animationLength = Math.round((2*Math.PI) / 0.3);					
					shotgunAngleOffset = (shotgunReloadTimer > shotgunReloadTime - animationLength && shotgunReloadTimer < shotgunReloadTime ? shotgunAngleOffset + 0.3 : 0);
					
					if (shotgunReloadTimer > shotgunReloadTime) {
						shotgunReloadTimer = 0;
						shotgunRecoilOffset = 0;
					}
				}
	
				if (mouseDown && newClick && shotgunReloadTimer == 0) { //shooting
					
					//start the reload animation
					shotgunReloadTimer = 1;
					shotgunRecoilOffset = 50;
					
					double mouseAngle = Math.atan(mouseY / mouseX) + (mouseX < 0 ? Math.PI : 0);
					
					xVelocity += shotgunRecoilVelocity * Math.cos(Math.PI + mouseAngle); //give the player a little velocity
					yVelocity = shotgunRecoilVelocity * Math.sin(Math.PI + mouseAngle);
					
					//spawn a bunch of confetti
					double spread = 30;
		
					for (int i = 0; i < 8; i ++) {
						
						double spreadOffset = Math.toRadians(spread) * 2 * (Math.random() - 0.5);
						
						entityhandler.addEntity(new ConfettiParticle( //similar to Particle but rotation affects position, making it look like paper
								(int)(shotgunX + 35 + 75*Math.cos(mouseAngle)), (int)(shotgunY + 47 + 75*Math.sin(mouseAngle)), //x and y
								15, 15, //size
								generateRandomColor(), //color
								60, //lifespan
								20*Math.cos(mouseAngle + spreadOffset), 20*Math.sin(mouseAngle + spreadOffset)));  //x and y velocity
					}
				}
				
				//grappling hook
				
				if (grapplingHookReloadTimer > 0) {
					grapplingHookReloadTimer++;
					
					if (grapplingHookReloadTimer > grapplingHookReloadTime) {
						grapplingHookReloadTimer = 0;
					}
				}
				
				if (rightMouseDown && newRightClick) {
					if (grapplingHook.active) {
						grapplingHook.tug();
						grapplingHook.endAbility();
					
					} else if (grapplingHookReloadTimer == 0) {
						grapplingHookReloadTimer = 1;
						
						double mouseAngle = Math.atan(mouseY / mouseX) + (mouseX < 0 ? Math.PI : 0);
	
						grapplingHook.startAbility(grapplingHookLaunchVelocity * Math.cos(mouseAngle), grapplingHookLaunchVelocity * Math.sin(mouseAngle));								
						
						grapplingHook.angle = mouseAngle;
					}
					
				}
			
				//PHYSICS
				xVelocity *= (midairTime < 2) ? friction : airfriction;
				yVelocity *= yfriction;
				
				if (!player.touchingLevel()) {
					yVelocity += gravity;
					midairTime++;
				}
				
				if (flight) {
					midairTime = 0;
					if (keytracker.downKeyPressed && jumpTimer == jumpCooldown) {
						yVelocity = jumpPower;
						jumpTimer = 0;
					}			
	
					yVelocity *= airfriction;
				}
				
				player.moveY((int)yVelocity); //collision checks. Don't use player.touchingLevel() to do normal collision checks, instead use midairTime < 2
				
				if (player.touchingLevel()) {
					
					int dy = (yVelocity > 0 ? -1 : 1);
					while (player.touchingLevel()) {
						player.moveY(dy);
					}
					
					if (yVelocity < 0) {
						yVelocity = gravity;
		
					} else {
						if (midairTime>10) {
							for (int i = 0; i < 4; i++) {
								int xOffset = (int)Math.round(10 + Math.random()*50);
								entityhandler.addEntity(new GravityParticle(player.x + xOffset, player.y + 55, (int)(Math.round(Math.random() * 6)) + 9, (int)(Math.round(Math.random() * 6)) + 9, player.playerColor, Math.round(Math.random()*14 - 7), -4 - (Math.random() * 3.5)));
							}
						}
						
						yVelocity = 0;
						midairTime = 0;
					}
				}
		
				player.moveX((int)xVelocity);
				
				if (player.touchingLevel()) {
					int dx = (xVelocity > 0 ? -1 : 1);
					
					while (player.touchingLevel()) {
						player.moveX(dx);
					}
					
					xVelocity = 0;
				}
				
				
				if (midairTime < 2 && Math.abs(xVelocity) > 6) { //walking particles
					int xOffset = 10;
					if (xVelocity < 1) {
						xOffset = 60;
					}
					if (gameTicks % 10 == 0) {
						entityhandler.addEntity(new GravityParticle(player.x + xOffset, player.y + 55, (int)(Math.round(Math.random() * 6)) + 9, (int)(Math.round(Math.random() * 6)) + 9, player.playerColor, 0, -4 - (Math.random() * 3.5)));
					}
					if (gameTicks % 8 == 0) {
						entityhandler.addEntity(new GravityParticle(player.x + xOffset, player.y + 55, (int)(Math.round(Math.random() * 6)) + 9, (int)(Math.round(Math.random() * 6)) + 9, player.playerColor, 0, -4 - (Math.random() * 3.5)));
					}
				}
			}
			
			if (slowMotionMode) {
				slowMoAlpha += 3;
				if (slowMoAlpha > 35) {
					slowMoAlpha = 35;
				}
			} else {
				slowMoAlpha -= 3;
				if (slowMoAlpha < 0) {
					slowMoAlpha = 0;
				}
			}
			
			//camera movement calculations
			
			//mouse look feature, allows user to expand their vision by moving the mouse
			cameraXoffset = (-mousemotiontracker.mouseX) * mouseLookRange;
			cameraYoffset = (-mousemotiontracker.mouseY) * mouseLookRange;
			
			double targetX = Window.windowX/2 - 35 - player.x + cameraXoffset;
			double targetY = Window.windowY/2 - 35 - player.y + cameraYoffset;

			if (deathAnimationTimer > 0) {
				
				int stageOneEnd = deathAnimationTime/2;
				//
				
				if (deathAnimationTimer < stageOneEnd) { //stage one - camera goes to death position
					targetX = Window.windowX/2 - 35 - lastDeathX + cameraXoffset;
					targetY = Window.windowY/2 - 35 - lastDeathY + cameraYoffset;
				} else { //stage two - camera goes to spawn point
					targetX = Window.windowX/2 - 35 - spawnX + cameraXoffset;
					targetY = Window.windowY/2 - 35 - spawnY + cameraYoffset;
				}
				
				deathAnimationTimer++;
				if (deathAnimationTimer > deathAnimationTime) {
					endDeathAnimation();
				}
				
			}
							
			cameraX += (targetX - cameraX)/cameraMovementDelay;
			cameraY += (targetY - cameraY)/cameraMovementDelay;
			
			totalVelocity = Math.sqrt((xVelocity * 1.3) * (xVelocity * 1.3) + (yVelocity) * (yVelocity));
		}

		settingsmenu.shown = paused;
		settingsmenu.tick();
		
	}
	
	private void render() {
		
		
		mouseX = mousemotiontracker.mouseX;
		mouseY = mousemotiontracker.mouseY;
		
		int lastWindowX = Window.windowX;
		int lastWindowY = Window.windowY;
		Window.windowX = frame.getWidth() - 16;
		Window.windowY = frame.getHeight() - 39;
		
		if (Window.windowX != lastWindowX || Window.windowY != lastWindowY) {
			windowSizeChanged();
		}

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		if (introScreenTimer > -1) {

			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, Window.windowX, Window.windowY);
			
			g.setFont(new Font("Arial", Font.PLAIN, 25));
			
			int X = Window.windowX/2;
			int Y = Window.windowY/2;
			
			int timeBetween = 90;
			int startY = Y - 225;
			int stepY = 30;
			for (int i = 0; i < min(introScreenTimer/timeBetween, intro.length); i++) {
				if (i == min(introScreenTimer/timeBetween, intro.length) - 1) {
					g.setColor(new Color(255, 255, 255, (int)(255 * min(1, (double)Math.floorMod(introScreenTimer, timeBetween)/(double)timeBetween))));
				} else {
					g.setColor(new Color(255, 255, 255));
				}
				
				g.drawString(intro[i], X - g.getFontMetrics().stringWidth(intro[i])/2, startY + i * stepY);
			}
			
			g.setColor(new Color(255, 255, 255));
			g.drawString("[RIGHT-CLICK] to skip", X - g.getFontMetrics().stringWidth("[RIGHT-CLICK] to skip")/2, 2*Y - 100);
			
			g.dispose();
			bs.show();
			return;
		}
		
		
		g.setColor(backgroundColor);
		g.fillRect(0, 0, Window.windowX, Window.windowY); //background
		
		//SCROLLING GRAPHICS
		g.translate((int)(cameraX), (int)(cameraY)); //camera movement
		
		maphandler.render(g, g2d); //MAPHANDLER RENDER
		
		if (splashDamageEffectAlpha > 0) {
			g.setColor(new Color(player.playerColor.getRed(), player.playerColor.getGreen(), player.playerColor.getBlue(), splashDamageEffectAlpha));
			g.fillOval((int)(player.x + 35 - (splashDamageRange/1.5)), (int)(player.y + 35 - (splashDamageRange/1.5)), (int)(splashDamageRange*1.333), (int)(splashDamageRange*1.333));
			//g.fillRect((int)(player.x + 35 - (splashDamageRange/1.5)), (int)(player.y + 35 - (splashDamageRange/1.5)), (int)(splashDamageRange*1.333), (int)(splashDamageRange*1.333));
		}

		entityhandler.render(g, g2d); //ENTITYHANDLER RENDER
		

		//shotgun
		if (renderingPlayer) {

			double mouseAngle = Math.atan(mouseY / mouseX);
			g2d.rotate(mouseAngle, shotgunX+35, shotgunY+47);
			
			if (mouseX >= 0) {
				
				g2d.rotate(shotgunAngleOffset, shotgunX+35, shotgunY+47);
				
				g.setColor(new Color(90, 50, 0));
				g.fillRect((int)(shotgunX + 20 - shotgunRecoilOffset), (int)(shotgunY) + 40, 100, 15);
				g.fillRect((int)(shotgunX - 5 - shotgunRecoilOffset), (int)(shotgunY) + 45, 40, 20);
				
				g.setColor(new Color(117, 66, 0));			
				g.fillRect((int)(shotgunX + 45 - shotgunRecoilOffset), (int)(shotgunY) + 48, 50, 12);
				
				g2d.rotate(-shotgunAngleOffset, shotgunX+35, shotgunY+47);
	
				
			} else {
							
				g2d.rotate(-shotgunAngleOffset, shotgunX+35, shotgunY+47);
				
				g.setColor(new Color(90, 50, 0)); //draws it backwards
				g.fillRect((int)(shotgunX - 50 + shotgunRecoilOffset), (int)(shotgunY) + 40, 100, 15);
				g.fillRect((int)(shotgunX + 35 + shotgunRecoilOffset), (int)(shotgunY) + 45, 40, 20);
				
				g.setColor(new Color(117, 66, 0));			
				g.fillRect((int)(shotgunX - 25 + shotgunRecoilOffset), (int)(shotgunY) + 48, 50, 12);
				
				g2d.rotate(shotgunAngleOffset, shotgunX+35, shotgunY+47);
	
	
			}
		
			g2d.rotate(-(mouseAngle), shotgunX+35, shotgunY+47);
		}
		
		//NON-SCROLLING GRAPHICS
		g.translate(-(int)(cameraX), -(int)(cameraY));
		
		renderCombo(g, g2d);
		
		if (grid) {
			grid(g, g2d);
		}
		
		if (slowMoAlpha > 0) {
			renderRain(g, g2d);
		}
		
		//GUI
		
		if (totalVelocity > velocityRequired) {
			int difference = (int)(Math.min(60, Math.max(40, 3 * (totalVelocity - velocityRequired))));
			borderEffect(g, g2d, difference);
		}
		
		if (slowMoAlpha > 0) {
			g.setColor(new Color(0, 90, 173, slowMoAlpha));
			g.fillRect(0, 0, Window.windowX, Window.windowY);
		}
		
		
		if (showFPS) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 32));
			g.drawString("FPS: " + fps, 10, Window.windowY - 10);
		}
		//totalTicks += 100;
		
		//clock
		if (displayTime) {
			
			g.setColor(new Color(255, 255, 255, 225));
			g.setFont(new Font("Arial", Font.PLAIN, 100));
			
			g.drawString(getTimeString(), Window.windowX/2 - g.getFontMetrics().stringWidth(getTimeString())/2, 200);
		}
		
		//settings menu
		settingsmenu.render(g, g2d);
		
		//crosshair
		if (showCrosshair) {
			drawCrosshair(g, g2d);
		}
				
		g.dispose();
		bs.show();
				
		
	}
	
	
	private double min(double i, double j) {
		// TODO Auto-generated method stub
		return (i < j) ? i : j;
	}
	private int min(int i, int j) {
		return  (i < j) ? i : j;
	}
	private void grid(Graphics g, Graphics2D g2d) {
		g.setColor(new Color(0, 0, 0, 50));
		g2d.setStroke(new BasicStroke(2));
		
		for (int i = 0; i < Window.windowX; i += gridStepSize) {
			int renderX = 0;
			if (Window.windowX % gridStepSize != 0) {
				renderX = Math.floorMod(i + (int)cameraX, Window.windowX - Math.floorMod(Window.windowX, gridStepSize) + gridStepSize);
			} else {
				renderX = Math.floorMod(i + (int)cameraX, Window.windowX);
			}
			g.drawLine(renderX, 0, renderX, Window.windowY);
		}
		for (int i = 0; i < Window.windowY; i += gridStepSize) {
			int renderY = 0;
			if (Window.windowY % gridStepSize != 0) {
				renderY = Math.floorMod(i + (int)cameraY, Window.windowY - Math.floorMod(Window.windowY, gridStepSize) + gridStepSize);
			} else {
				renderY = Math.floorMod(i + (int)cameraY, Window.windowY);
			}
			g.drawLine(0, renderY, Window.windowX, renderY);
		}
		
		g.setColor(Color.red);
		g2d.setStroke(new BasicStroke(5));
		g.drawLine(0, deathY + (int)cameraY, Window.windowX, deathY + (int)cameraY);
		
		
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.PLAIN, 32));
		int nearestX = gridStepSize * (int)Math.round((mouseX - cameraX + Window.windowX/2)/(double)gridStepSize);
		int nearestY = gridStepSize * (int)Math.round((mouseY - cameraY + Window.windowY/2)/(double)gridStepSize);
		g.drawString(nearestX + ", " + nearestY, nearestX + (int)cameraX + 10, nearestY + (int)cameraY - 42);
		g.fillOval(nearestX + (int)cameraX - 5, nearestY + (int)cameraY - 5, 10, 10);

		
	}
	public Color generateBrightColor(double requirement) {
		Color output = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		double average = (output.getRed() + output.getGreen() + output.getBlue()) / 3;
		
		double stddev = (Math.abs(output.getRed() - average) + Math.abs(output.getGreen() - average) + Math.abs(output.getBlue() - average)) / 3;

		 while (stddev < requirement) {
			output = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
			average = (output.getRed() + output.getGreen() + output.getBlue()) / 3;
			
			stddev = (Math.abs(output.getRed() - average) + Math.abs(output.getGreen() - average) + Math.abs(output.getBlue() - average)) / 3;

		}
		
		return output;
		
	}
	
	public Color generateBrightColor() {
		float hue = (float)Math.random();
		float sat = 1.0f;
		float bri = (float)(Math.cbrt(Math.random()));
		
		
		return Color.getHSBColor(hue, sat, bri);
	}
	
	public Color generateRandomColor() {
		return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
	}
	
	public static void killPlayer() {
		if (!godMode) {
			startDeathAnimation();
			resetPlayer();
		}
	}
	
	public static void resetLevel() {
		Game.maphandler.clearMap();
		Game.entityhandler.clearEntities();
		Game.loadLevelFromString(levels[level]);
	}
	
	public static void resetPlayer() {
		xVelocity = 0;
		yVelocity = 0;
		comboTimer = -1;
		shotgunRecoilOffset = 0;
		shotgunAngleOffset = 0;
		shotgunReloadTimer = 0;
		grapplingHookReloadTimer = 0;
		grapplingHook.endAbility();
		grapplingHook.stopRendering();
		player.x = spawnX;
		player.y = spawnY;
		spawnInvincibilityTimer = 1;
		midairTime = 5;
	}
	
	public void drawBorder(Graphics g, Graphics2D g2d, Color color, int offset) {
		g2d.setStroke(new BasicStroke(1));
		g.setColor(color);
		g.drawRect(0 + offset, 0 + offset, Window.windowX - (offset * 2), Window.windowY - (offset * 2));
		
	}
	
	public static Color invertColor(Color i) {
		return new Color(255 - i.getRed(), 255 - i.getGreen(), 255 - i.getBlue(), i.getAlpha());
	}

	public void drawCrosshair(Graphics g, Graphics2D g2d) {
		g.translate((int)mouseX, (int)mouseY);
		
		/*
		Color inverse = invertColor(crosshairColor);
		int shadowThickness = 4;
		
		g.setColor(inverse);
		g2d.setStroke(new BasicStroke(crosshairThickness + shadowThickness));

		int X = Window.windowX/2;
		int Y = Window.windowY/2;
		
		if (crosshairDot) {
			g.fillRect(X - crosshairThickness/2 - shadowThickness/2, Y - crosshairThickness/2 - shadowThickness/2, crosshairThickness + shadowThickness, crosshairThickness + shadowThickness);
		}
		if (crosshairArms) {
			g.drawLine(X, Y - crosshairArmLength - crosshairSpread - crosshairThickness, X, Y - crosshairSpread - crosshairThickness);
			g.drawLine(X, Y + crosshairArmLength + crosshairSpread + crosshairThickness, X, Y + crosshairSpread + crosshairThickness);
			g.drawLine(X - crosshairArmLength - crosshairSpread - crosshairThickness, Y, X - crosshairSpread - crosshairThickness, Y);
			g.drawLine(X + crosshairArmLength + crosshairSpread + crosshairThickness, Y, X + crosshairSpread + crosshairThickness, Y);
		}
		*/

		int X = Window.windowX/2;
		int Y = Window.windowY/2;

		g.setColor(crosshairColor);
		g2d.setStroke(new BasicStroke(crosshairThickness));
				
		if (crosshairDot) {
			g.fillRect(X - crosshairThickness/2, Y - crosshairThickness/2, crosshairThickness, crosshairThickness);
		}
		if (crosshairArms) {
			g.drawLine(X, Y - crosshairArmLength - crosshairSpread - crosshairThickness, X, Y - crosshairSpread - crosshairThickness);
			g.drawLine(X, Y + crosshairArmLength + crosshairSpread + crosshairThickness, X, Y + crosshairSpread + crosshairThickness);
			g.drawLine(X - crosshairArmLength - crosshairSpread - crosshairThickness, Y, X - crosshairSpread - crosshairThickness, Y);
			g.drawLine(X + crosshairArmLength + crosshairSpread + crosshairThickness, Y, X + crosshairSpread + crosshairThickness, Y);
		}
		
		if (mouseCoords) {
			g.setFont(new Font("Arial", Font.PLAIN, 32));
			g.drawString(Math.round(mouseX - cameraX + X) + ", " + Math.round(mouseY - cameraY + Y), X + 10, Y + 42);
		}
		
		g.translate(-(int)mouseX, -(int)mouseY);
	}
	
	public void borderEffect(Graphics g, Graphics2D g2d, int thickness) {
		for (int i = 0; i < thickness; i++) {
			drawBorder(g, g2d, new Color(255, 255, 255, 255 - (int)(i * Math.floor(255 / (double)thickness))), i);
		}
	}
	
	public boolean containsMouse(int x, int y, int xSize, int ySize) {
		int mouseX = (int)this.mouseX + 500;
		int mouseY = (int)this.mouseY + 500;
		
		if (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean containsMouse(Rectangle rect) {
		int mouseX = (int)this.mouseX + 500;
		int mouseY = (int)this.mouseY + 500;
		
		if (mouseX >= rect.x && mouseX <= rect.x + rect.width && mouseY >= rect.y && mouseY <= rect.y + rect.height) {
			return true;
		}
		
		return false;

	}
	
	
	public static void addWall(int x, int y, int xSize, int ySize) {
		maphandler.addMapObject(new GroundObject(x, y, xSize, ySize));
	}
	public static void addCrate(int x, int y, int xSize, int ySize) {
		maphandler.addMapObject(new CrateObject(x, y, xSize, ySize));
	}
	public static void addLava(int x, int y, int xSize, int ySize) {
		maphandler.addMapObject(new LavaObject(x, y, xSize, ySize));
	}
	public static void addDoor(int x, int y) {
		door = new DoorObject(x, y);
		maphandler.addMapObject(door);
	}
	public static void addSign(int x, int y, String message) {
		maphandler.addMapObject(new SignObject(x, y, message));
	}
	public static void addText(int x, int y, String message) {
		maphandler.addMapObject(new TextObject(x, y, message));
	}
	public static void addMapObject(MapObject mapobject) {
		maphandler.addMapObject(mapobject);
	}
	
	public static void addLavaColor(int x, int y, int xSize, int ySize, Color color) {
		maphandler.addMapObject(new LavaObject(x, y, xSize, ySize, color));
	}
	public static void addWallColor(int x, int y, int xSize, int ySize, Color color) {
		maphandler.addMapObject(new GroundObject(x, y, xSize, ySize, color));
	}
	public static void addDecorationColor(int x, int y, int xSize, int ySize, Color color) {
		maphandler.addMapObject(new DecorationObject(x, y, xSize, ySize, color));
	}


	public static void addBasicEnemy(int x, int y) {
		entityhandler.addEntity(new BasicEnemy(x, y));
	}
	public static void addPistolEnemy(int x, int y) {
		entityhandler.addEntity(new PistolEnemy(x, y));
	}
	public static void addShotgunEnemy(int x, int y) {
		entityhandler.addEntity(new ShotgunEnemy(x, y));
	}
	public static void addUziEnemy(int x, int y) {
		entityhandler.addEntity(new UziEnemy(x, y));
	}
	public static void addSniperEnemy(int x, int y) {
		entityhandler.addEntity(new SniperEnemy(x, y));
	}
	public static void addRocketEnemy(int x, int y) {
		entityhandler.addEntity(new RocketEnemy(x, y));
	}
	public static void addDroneEnemy(int x, int y) {
		entityhandler.addEntity(new DroneEnemy(x, y));
	}
	public static void addDroneGunEnemy(int x, int y) {
		entityhandler.addEntity(new DroneGunEnemy(x, y));
	}
	
	public static void addMinigunEnemy(int x, int y) {
		entityhandler.addEntity(new MinigunEnemy(x, y));
	}
	
	
	public static void addEntity(Entity entity) {
		entityhandler.addEntity(entity);
	}
	
	public static void addPlayer() {
		entityhandler.addEntity(grapplingHook);
		entityhandler.addEntity(player);
	}
			
	public static String getLevelString() { 		
		// meta;map;entities
		//
		// meta: spawnX, spawnY, deathY
		// map: ground.-500.-100.200.50,lava.-500.-100.200.50
		// entities: basicenemy.800.130,pistolenemy.1450.-200
		
		StringBuilder output = new StringBuilder();
		
		//String output = "";
		
		//META
		output.append(spawnX + "," + spawnY + "," + deathY + ";");
		
		//MAP
		int itemsAdded = 0;

		LinkedList<MapObject> map = maphandler.map;
		for (int i = 0; i < map.size(); i++) {
			if (!map.get(i).getName().equals("null")) {
				output.append(map.get(i).getName());
				
				if (map.get(i).customColor) {
					output.append("Color");
				}
				
				output.append("." + map.get(i).x + "." + map.get(i).y + "." + map.get(i).xSize + "." + map.get(i).ySize);
				
				if (map.get(i).customColor) {
					output.append("." + map.get(i).color.getRed() + "." + map.get(i).color.getGreen() + "." + map.get(i).color.getBlue());
				}
				
				output.append(",");
				itemsAdded++;
			}
		}
		
		if (itemsAdded == 0) {
			output.append(" ");
		}
		output.append(";");
		
		//ENTITIES
		itemsAdded = 0;
		
		List<Entity> entities = entityhandler.entities;
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).getName().equals("null")) {
				output.append(entities.get(i).getName() + "." + entities.get(i).x + "." + entities.get(i).y + ",");
				
				itemsAdded++;
			}
		}
		
		if (itemsAdded == 0) {
			output.append(" ");
		}
		
		return output.toString();
	}
	
	public static void loadLevelFromString(String level) {
		//format: 
		//meta;map;entities
		//
		// meta: spawnX,spawnY,deathY
		// map: ground.-500.-100.200.50,lava.-500.-100.200.50
		// entities: basicenemy.800.130,pistolenemy.1450.-200

		//types:
		//meta: spawnX, spawnY, deathY
		//map: ground, crate, door, lava, sign:text
		//entity: basicEnemy, pistolEnemy, shotgunEnemy, uziEnemy, sniperEnemy, rocketEnemy, droneEnemy, droneGunEnemy
		
		String[] categories = level.split(";");
		
		String[] meta = categories[0].split(","); //LOADING META-DATA
		
		spawnX = Integer.parseInt(meta[0]);
		spawnY = Integer.parseInt(meta[1]);
		deathY = Integer.parseInt(meta[2]);
		
		String[] map = categories[1].split(",");
		
		
		for (int i = 0; i < map.length; i++) { //LOADING MAP
			
			String[] object = map[i].split("\\.");
			
			if (object[0].equals("ground")) {
				addWall(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]));
			}
			if (object[0].equals("crate")) {
				addCrate(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]));
			}
			if (object[0].equals("lava")) {
				addLava(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]));
			}
			if (object[0].equals("door")) {
				addDoor(Integer.parseInt(object[1]), Integer.parseInt(object[2]));
			}
			if (object[0].contains("sign:")) {
				String[] info = object[0].split(":");
				addSign(Integer.parseInt(object[1]), Integer.parseInt(object[2]), info[1]);
			}
			if (object[0].contains("text:")) {
				String[] info = object[0].split(":");
				addText(Integer.parseInt(object[1]), Integer.parseInt(object[2]), info[1]);
			}
			
			if (object[0].equals("groundColor")) {
				addWallColor(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]), new Color(Integer.parseInt(object[5]), Integer.parseInt(object[6]), Integer.parseInt(object[7])));
			}
			if (object[0].equals("lavaColor")) {
				addLavaColor(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]), new Color(Integer.parseInt(object[5]), Integer.parseInt(object[6]), Integer.parseInt(object[7])));
			}
			if (object[0].equals("decorationColor")) {
				addDecorationColor(Integer.parseInt(object[1]), Integer.parseInt(object[2]), Integer.parseInt(object[3]), Integer.parseInt(object[4]), new Color(Integer.parseInt(object[5]), Integer.parseInt(object[6]), Integer.parseInt(object[7])));
			}
		}
		
		String[] entities = categories[2].split(",");
		
		for (int i = 0; i < entities.length; i++) {
			String[] entity = entities[i].split("\\.");
			
			if (entity[0].equals("basicEnemy")) {
				addBasicEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("pistolEnemy")) {
				addPistolEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("shotgunEnemy")) {
				addShotgunEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("uziEnemy")) {
				addUziEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("sniperEnemy")) {
				addSniperEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("rocketEnemy")) {
				addRocketEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("droneEnemy")) {
				addDroneEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}
			if (entity[0].equals("droneGunEnemy")) {
				addDroneGunEnemy(Integer.parseInt(entity[1]), Integer.parseInt(entity[2]));
			}

		}
		
		addPlayer();
		
	}
	
	public static void toggleEditingMode() {
		flight = !flight;
		godMode = !godMode;
		grid = !grid;
		mouseCoords = !mouseCoords;
		noClip = !noClip;
		lockEnemies = !lockEnemies;
		editPerms = !editPerms;
	}
	
	public static void editLevel() {
		
		// FORMAT for INPUTS:
		// META editing (replaces metainfo)
		// meta>>spawnX,spawnY,deathY
		
		// MAP editing (adds new map object)
		// map>>[type].x.y.xSize.ySize
		// CUSTOM color:d
		// map>>[type]Color.x.y.xSize.ySize.R.G.B
		// SIGNS:
		// map>>sign:[text].x.y.75.85
		
		// (toggleInputIsSize interprets x.y.xSize.ySize as x.y.(x + xSize).(y + ySize))
		
		// ENTITIES editing (adds new entity)
		// entities>>[type].x.y
		
		// types:
		// meta: spawnX, spawnY, deathY
		// map: ground, crate, door, lava, sign:[text], text:[text], decoration
		// entity: basicEnemy, pistolEnemy, shotgunEnemy, uziEnemy, sniperEnemy, rocketEnemy, droneEnemy, droneGunEnemy
		
		// ADDITIONAL commands
		// clear
		// getLevelString
		// toggleInputIsSize
		// toggleHitboxes
		// setLevelString>>[levelString]
		// gridStepSize>>[stepSize]
		// setLevel>>[level]
		// remove>>[same syntax as adding]

		boolean editValid = true;
		
		String currentLevel = levels[level];
		String[] elements = currentLevel.split(";");
		
		if (!newInput) {
			return;
		} else {
			newInput = false;
			
			try {
				String[] userInput = recentInput.split(">>");
				
				if (userInput[0].equals("meta")) {
					elements[0] = userInput[1];
				} else if (userInput[0].equals("map")) {
					
					if (!inputIsSize) {
						String[] objectInput = userInput[1].split("\\.");
						objectInput[3] = String.valueOf(Integer.parseInt(objectInput[3]) - Integer.parseInt(objectInput[1]));
						objectInput[4] = String.valueOf(Integer.parseInt(objectInput[4]) - Integer.parseInt(objectInput[2]));
						
						userInput[1] = "";
						for (int i = 0; i < objectInput.length; i++) {
							if (i != 0) {
								userInput[1] += ".";
							}
							userInput[1] += objectInput[i];
					}
						
					}
					
					elements[1] = elements[1] + "," + userInput[1];
				} else if (userInput[0].equals("entities")) {
					elements[2] = elements[2] + "," + userInput[1];
				} else if (userInput[0].equals("clear")) { 
					elements[0] = "0,0,1500";
					elements[1] = " ";
					elements[2] = " ";
				} else if (userInput[0].equals("remove")) { //not sure if removing from every category would cause problems... every reasonable input for a reasonably designed level should be unique?
					elements[1] = elements[1].replace(userInput[1], "");
					elements[2] = elements[2].replace(userInput[1], "");
					
					elements[1] = String.join(",", elements[1].split(",")); //gets rid of ,, and ,,, etc
					elements[2] = String.join(",", elements[2].split(","));
				} else if (userInput[0].equals("gridStepSize")) {
					try {
						gridStepSize = Integer.parseInt(userInput[1]);
						System.out.println("ACCEPTED edit");
												
						return;
					} catch (Exception e) {
						System.out.println("INVALID command");
						return;
					}
				} else if (userInput[0].equals("setLevel")) {
					try {
						int desiredLevel = Integer.parseInt(userInput[1]);
						String levelString = levels[desiredLevel];
						
						level = desiredLevel;
						Game.maphandler.clearMap();
						Game.entityhandler.clearEntities();
						loadLevelFromString(levelString);
						
						System.out.println("ACCEPTED edit");
						return;
					} catch (Exception e) {
						System.out.println("INVALID command");
						return;
					}
				} else if (userInput[0].equals("getLevelString")) {
					System.out.println("Level String:\n" + getLevelString());
					return;
				} else if (userInput[0].equals("setLevelString")) {
					String[] newLevel = userInput[1].split(";");
					elements[0] = newLevel[0];
					elements[1] = newLevel[1];
					elements[2] = newLevel[2];
				} else if (userInput[0].equals("toggleInputIsSize")) {
					inputIsSize = !inputIsSize;
					if (inputIsSize) {
						System.out.println("Input interpreted as object size");
					} else {
						System.out.println("Input interpreted as object coordinates");
					}
				} else if (userInput[0].equals("toggleHitboxes")) {
					drawHitboxes = !drawHitboxes;
				} else {
					editValid = false;
				}
			} catch (Exception e) {
				System.out.println("INVALID edit syntax");
				return;
			}
		}
		
		if (!editValid) {
			System.out.println("INVALID edit category");
			return;
		}
		
		try {
			Game.maphandler.clearMap();
			Game.entityhandler.clearEntities();
			loadLevelFromString(elements[0] + ";" + elements[1] + ";" + elements[2]);
		} catch (Exception e) {
			editValid = false;
		}
		
		if (editValid) {
			System.out.println("ACCEPTED edit.");
			levels[level] = elements[0] + ";" + elements[1] + ";" + elements[2];
		} else {
			System.out.println("INVALID edit content");
			loadLevelFromString(levels[level]);
		}
		System.out.println(levels[level]);
	}
	
	public String getTimeString() {
		long trueTime = System.currentTimeMillis() - gameStart;
		
		int ticks = (int) (trueTime / 10 % 100);
		int seconds = (int) (trueTime / 1000 % 60);
		int minutes = (int) (trueTime / 60000);
		
		String time = "";
		if (String.valueOf(minutes).length() == 1) {
			time += "0";
		}
		time += minutes + ":";
		
		if (String.valueOf(seconds).length() == 1) {
			time += "0";
		}
		time += seconds + ":";
		
		if (String.valueOf(ticks).length() == 1) {
			time += "0";
		}
		time += ticks;

		return time;
	}
	
	public static void togglePaused() {
		paused = !paused;
		
		showCrosshair = !paused;
		if (showCrosshair) {
			Window.hideCursor();
		} else {
			Window.showCursor();
		}
	}
	
	public static void startDeathAnimation() {
		lastDeathX = player.x;
		lastDeathY = player.y;
		
		shotgunX = spawnX;
		shotgunY = spawnY - 750;
		
		lockEnemies = true;
		lockPlayer = true;
		godMode = true;
		renderingPlayer = false;
		
		deathAnimationTimer = 1;

		int particles = 11;
		double angle = Math.random() * 6.28;
		double angleStep = 2 * Math.PI / particles;
		double angleVariation = 0.6;
		
		for (int i = 0; i < particles; i++) {
			entityhandler.addEntity(new PlayerDeathParticle(angle + 2 * angleVariation * (Math.random() - 0.5)));
			angle += angleStep;
		}
	}
	
	public static void endDeathAnimation() {
		if (!editPerms) {
			lockEnemies = false;
		}
		
		lockPlayer = false;
		renderingPlayer = true;
		
		deathAnimationTimer = 0;
		
		if (reloadLevel) {
			resetLevel();
		}
	}
	
	public static void windowSizeChanged() {
		initializeRain();
	}
	
	public static void initializeRain() {
		rainCount = (int)(rainDensity * Window.windowX * Window.windowY / 1000000);
		rainX = new int[rainCount];
		rainY = new int[rainCount];
		
		for (int i = 0; i < rainCount; i++) {
			rainX[i] = (int)(Math.random() * Window.windowX);
			rainY[i] = (int)(Math.random() * Window.windowY);
		}
	}
	public static void renderRain(Graphics g, Graphics2D g2d) {
		double rainSize = 40;
		
		for (int i = 0; i < rainCount; i++) {
			g2d.setStroke(new BasicStroke(3));
			g.setColor(new Color(0, 90, 173, slowMoAlpha));
			g.drawLine(rainX[i], rainY[i], rainX[i] + 7, rainY[i] - 50);
		}
		
	}
	public static void tickRain() {
		int rainSpeedY = 35;
		int rainSpeedX = -5;
		
		for (int i = 0; i < rainCount; i++) {
			rainY[i] += rainSpeedY;
			if (rainY[i] > Window.windowY) {
				rainY[i] = Window.windowY - rainY[i];
				rainX[i] = (int)(Math.random() * Window.windowX);
			}
			rainX[i] += rainSpeedX;
			if (rainX[i] < 0) {
				rainY[i] = Window.windowY - rainY[i];
				rainX[i] = (int)(Math.random() * Window.windowX);
			}
		}
	}
	
	public static void resetCombo() {
		if (!moistureLevel) {
			return;
		}
		
		if (comboTimer == -1) {
			comboTimer = comboTime3;
		} else if (comboTimer < comboTime1) {
			comboTimer = comboTime1;
		} else if (comboTimer < comboTime2) {
			comboTimer = comboTime2;
		} else {
			comboTimer = comboTime3;
		}
	}
	
	public static void renderCombo(Graphics g, Graphics2D g2d) {
		if (!moistureLevel) {
			return;
		}

		String comboLevel;
		double progress;
		Color barColor;
		
		if (comboTimer == -1) {
			return;
		} else if (comboTimer < comboTime1) {
			comboLevel = "1";
			progress = ((double)comboTimer / (double)comboTime1);
			barColor = new Color(255, 72, 0);
		} else if (comboTimer < comboTime2) {
			comboLevel = "2";
			progress = ((double)(comboTimer - comboTime1) / (double)(comboTime2 - comboTime1));
			barColor = new Color(183, 0, 255);
		} else {
			comboLevel = "3";
			progress = ((double)(comboTimer - comboTime2) / (double)(comboTime3 - comboTime2));
			barColor = new Color(0, 51, 255);
		}
		
		if (progress > 0.98) {
			comboTextSize = boostedComboTextSize;
		}
		progress = progress * progress;
		
		int X = Window.windowX;
		int Y = Window.windowY;

		g.setColor(new Color(145, 145, 145));
		g.fillRect(X/2 - 150, Y - 100, 300, 2);
		if (comboLevel == "1") {
			g.setColor(new Color((int)(255 * (1 + Math.sin((double)Game.gameTicks/8))/2), 0, 0));
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("!! CRITICALLY LOW !!", X/2 - g.getFontMetrics().stringWidth("!! CRITICALLY LOW !!")/2, Y - 60);
		} else {
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("MOISTURE LEVEL", X/2 - g.getFontMetrics().stringWidth("MOISTURE LEVEL")/2, Y - 60);
		}
		
		g.setColor(barColor);
		g.fillRect(X/2 - (int)(300 * progress)/2, Y - 102, (int)(300 * progress), 6);
		g.setFont(new Font("SansSerif", Font.BOLD, (int)comboTextSize));
		g.drawString(comboLevel, X/2 - g.getFontMetrics().stringWidth(comboLevel)/2, Y - 110);
	}
}
