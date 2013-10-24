package com.putable.frobworld.locd011.beings;

import java.util.EnumSet;

import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.simulation.GrassSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

public class Grass extends AbstractLiveable {
	private GrassSetting settings;

	/**
	 * Create a vanilla, start of world grass.
	 * 
	 * @param world
	 */
	public Grass(SimulationWorld world) {
		super(PlaceType.GRASS, world, world.getSimulationSettings()
				.getGrassSettings().getGrassBirthMass());
		settings = world.getSimulationSettings().getGrassSettings();
		setMass(settings.getGenesisMass());
		setUpdatePeriod(settings.getGrassInitialUpdatePeriod());
		updateNextMove(settings.getGrassInitialUpdatePeriod());
	}

	/**
	 * Create a new Grass object at the specified location on the world.
	 * 
	 * @param world
	 * @param location
	 * @param mass
	 */
	public Grass(SimulationWorld world, int[] location, int mass) {
		super(PlaceType.GRASS, world, location, world.getSimulationSettings()
				.getGrassSettings().getGrassInitialUpdatePeriod(),
				world.getSimulationSettings().getGrassSettings()
						.getGrassBirthMass(), mass);
		settings = world.getSimulationSettings().getGrassSettings();
	}

	@Override
	public CollisionResult collideInto(Frob collider) {
		die();
		GraphicsDelta change = GraphicsDeltaHelper.append(GraphicsDeltaHelper.removeAt(getLocation()),
			GraphicsDeltaHelper.moveTo(collider.getLocation(), getLocation(), collider));
		return new CollisionResult(-getMass(), true, change);
	}

	@Override
	public GraphicsDelta takeTurn() {
		int newMass = getMass();
		GraphicsDelta graphicsChange = GraphicsDeltaHelper.nothing();
		newMass -= (settings.getMassTax() * getUpdatePeriod())
				/ (1000 + settings.getFixedOverhead());
		setMass(newMass);
		if (newMass <= 0) {
			getWorld().killLiveable(this);
			return GraphicsDeltaHelper.removeAt(getLocation());
		}
		if (newMass > getBirthMass()) {
			if (canSplit()) {
				Grass newGrass = doSplit();
				graphicsChange = GraphicsDeltaHelper.updateLiveables(this,
						newGrass);
			} else {
				setMass(getBirthMass());
				int nextUpdate = getUpdatePeriod() << 1;
				if (nextUpdate > settings.getGrassMaxUpdatePeriod())
					nextUpdate = settings.getGrassMaxUpdatePeriod();
				setUpdatePeriod(nextUpdate);
			}
		}
		updateNextMove(getUpdatePeriod());
		return graphicsChange;
	}

	/**
	 * Check to see if a grass can split: There's an adjacent, empty area, and
	 * it isn't crowded out.
	 * 
	 * @return
	 */
	private boolean canSplit() {
		PlaceType[] adjacent = getWorld().getAdjacent(getLocation());
		int adjacentGrass = 0;
		int emptySpace = 0;
		for (PlaceType type : adjacent) {
			if (type == null)
				emptySpace++;
			else if (type == PlaceType.GRASS)
				adjacentGrass++;
		}
		return emptySpace > 0 && adjacentGrass < settings.getGrassCrowdLimit();
	}

	/**
	 * Create the grass offspring, which will add itself to the world.
	 * 
	 * @return
	 */
	private Grass doSplit() {
		PlaceType[] adjacent = getWorld().getAdjacent(getLocation());
		EnumSet<Direction> nextPlaces = EnumSet.noneOf(Direction.class);
		for (int x = 0; x < 4; x++)
			if (adjacent[x] == null)
				nextPlaces.add(Direction.getDirection(x));
		Direction[] moves = new Direction[nextPlaces.size()];
		nextPlaces.toArray(moves);
		Direction nextPlace = moves[getWorld().getRandom()
				.nextInt(moves.length)];
		int[] newLocation = nextPlace.getLocationFrom(getLocation());
		int newGrassMass = getOffspringMass();
		setMass(getMass() - newGrassMass);

		return new Grass(getWorld(), newLocation, newGrassMass);
	}

	/**
	 * Calculate the new grass' mass
	 * 
	 * @return
	 */
	private int getOffspringMass() {
		return (settings.getGrassBirthPercent() * getMass()) / 100;
	}

	@Override
	public boolean applyCollision(CollisionResult result) {
		return false;
	}
}
