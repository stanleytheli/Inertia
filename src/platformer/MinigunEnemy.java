package platformer;

//TODO finish coding this!

public class MinigunEnemy extends UziEnemy {
	
	public int spreadDegrees = 0;

	
	public MinigunEnemy(int x, int y) {
		super(x, y);
		
		this.magSize = 15;
		this.shotsUntilReload = magSize;
		this.shotTime = 5;
		this.reloadTimer = 60;
		this.reloadTime = 120;
	
	}
	
	public void shoot() {
		double angle = angleToPlayer + (2*Math.random() - 1) * Math.toRadians(spreadDegrees);
		int xCoordinate = (int)(x + 35 + (90 * Math.cos(angle)));
		int yCoordinate = (int)(y + 40 + (90 * Math.sin(angle)));
		gunRecoilOffset = 30;

		Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angle));

	}


}
