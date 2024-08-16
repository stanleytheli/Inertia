package platformer;

public class SettingsWindow extends InGameWindow {

	public SettingsWindow(int x, int y, int xSize, int ySize, String title) {
		super(x, y, xSize, ySize, title);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void tick() {
		if (shown) {
			if (backgroundAlpha < 125) {
				backgroundAlpha += 5;
			}
			
			displayY += (int)(((y + 10) - displayY)/8);
			displayY += 1;

			if (displayY > y) {
				displayY = y;
			}
			
			
		} else {
			
			displayY -= (int)((displayY - (0 - ySize))/8);
			displayY -= 15;

			for (int i = 0; i < components.size(); i++) {
				components.get(i).shown = false;
				components.get(i).active = false;
			}
			
			if (displayY < 0 - ySize) {
				displayY = 0 - ySize;
			}
			
			backgroundAlpha -= 10;
			if (backgroundAlpha < 0) {
				backgroundAlpha = 0;
			}
		}
		
		for (int i = 0; i < components.size(); i++) {
			components.get(i).tick();
		}
	}
	
	
	public void setupSettings() {
		
		closebutton.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				Game.togglePaused();
			}
			
		});
		
		WindowButton button = new WindowBooleanButton(265, 263, 470, 50, "Game Timer", this);
		((WindowBooleanButton) button).setValue(Game.displayTime);
		button.setActionListener(new WindowActionListener () {

			@Override
			public void actionOccured(WindowComponent source) {
				Game.displayTime = !Game.displayTime;
				WindowBooleanButton booleansource = (WindowBooleanButton)source;
				booleansource.value = Game.displayTime;
			}
			
		});
		addComponent(button);
		
		button = new WindowBooleanButton(265, 263+65, 470, 50, "Display FPS", this);
		((WindowBooleanButton) button).setValue(Game.showFPS);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				Game.showFPS = !Game.showFPS;
				WindowBooleanButton booleansource = (WindowBooleanButton)source;
				booleansource.value = Game.showFPS;
			}
			
		});
		addComponent(button);
		
		button = new WindowBooleanButton(265, 263+65+65, 470, 50, "Enemies Respawn", this);
		((WindowBooleanButton) button).setValue(Game.reloadLevel);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				Game.reloadLevel = !Game.reloadLevel;
				WindowBooleanButton booleansource = (WindowBooleanButton)source;
				booleansource.value = Game.reloadLevel;
			}
			
		});
		addComponent(button);
		
		button = new WindowBooleanButton(265, 263+65+65+65, 470, 50, "Limited Moisture", this);
		((WindowBooleanButton) button).setValue(Game.moistureLevel);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				Game.moistureLevel = !Game.moistureLevel;
				WindowBooleanButton booleansource = (WindowBooleanButton)source;
				booleansource.value = Game.moistureLevel;
			}
			
		});
		addComponent(button);


		button = new WindowBooleanButton(265, 263+65+65+65+65, 470, 50, "Cheats Mode", this);
		((WindowBooleanButton) button).setValue(Game.editPerms);

		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				if (Game.level == -1) {
					return;
				}
				
				Game.toggleEditingMode();
				
				WindowBooleanButton booleansource = (WindowBooleanButton)source;
				booleansource.value = Game.editPerms;
			}
			
		});
		
		addComponent(button);
		
		/*button = new WindowButton(265, 263+65+65+65+65, 470, 50, "Get Level String", this);
		
		button.setActionListener(new WindowActionListener() {
			@Override
			public void actionOccured(WindowComponent source) {
				System.out.println("Level String: \n" + Game.getLevelString());
			}
		});
		
		addComponent(button);*/
		
		button = new WindowButton(265, 733-65-65, 228, 50, "< Last Level", this);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				
				if (Game.level == 0 || Game.level == -1) {
					return;
				}
				Game.level--;
				
				Game.resetLevel();
				Game.resetPlayer();
				Game.endDeathAnimation();
				
			}
			
		});
		addComponent(button);
		
		button = new WindowButton(507, 733-65-65, 228, 50, "Next Level >", this);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				
				if (Game.level == Game.levels.length - 1) {
					return;
				}
				Game.level++;
				
				Game.resetLevel();
				Game.resetPlayer();
				Game.endDeathAnimation();
				
			}
			
		});
		addComponent(button);


		button = new WindowButton(265, 733-65, 470, 50, "Restart Level", this);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				
				Game.resetLevel();
				Game.resetPlayer();
				Game.endDeathAnimation();
				
				Game.togglePaused();
			}
			
		});
		addComponent(button);
		
		
		button = new WindowButton(265, 733, 470, 50, "Restart Game", this);
		button.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
	
				Game.level = 0;
				Game.introScreenTimer = 0;
				
				Game.resetLevel();
				Game.resetPlayer();
				Game.endDeathAnimation();
	
				Game.togglePaused();

			}
			
		});
		addComponent(button);
	}

}
