package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

import com.putable.frobworld.locd011.beings.Frob;
import com.putable.frobworld.locd011.beings.Genome;

/**
 * Hold the status of Liveables in the SimulationWorld
 * @author David
 *
 */
public class LiveableStatus
{
    private int remainingFrobs;
    private int totalFrobs = 0;
    private int remainingGrass;
    private List<Integer> timeAlive;
    private List<Frob> livingFrobs;
    private List<Genome> deadFrobGenomes;
    
    private Frob lastFrobAlive;
    
    //Singletons to make calculating statistics quicker:
    private Double averageLife;
    private Integer longestLife;
    private Integer shortestLife;
    private Double lifeDeviation;
    
    private Integer numberFrobsTotal;
    
    private double[] averageGenome;
    private double[] genomeDeviation;
    
    /**
     * In the beginning, there was nothing...
     */
    public LiveableStatus()
    {
	remainingFrobs = 0;
	remainingGrass = 0;
	timeAlive = new LinkedList<Integer>();
	livingFrobs = new LinkedList<Frob>();
	deadFrobGenomes = new LinkedList<Genome>();
    }
    
    /**
     * Change the number of Frobs in the world, whether through death or birth
     * @param offset
     */
    public void updateFrobs(int offset)
    {
	remainingFrobs += offset;
	if(offset > 0)
	    totalFrobs++;
    }
    
    /**
     * Change the number of Grass in the world.
     * @param offset
     */
    public void updateGrass(int offset)
    {
	remainingGrass += offset;
    }
    
    public void addFrobDeath(Frob dyingFrob)
    {
	timeAlive.add(dyingFrob.timeAlive());
	deadFrobGenomes.add(dyingFrob.getGenome());
	lastFrobAlive = dyingFrob;
    }
    
    /**
     * Return the number of current Frobs
     * @return
     */
    public int getRemainingFrobs()
    {
	return remainingFrobs;
    }
    
    public Frob lastFrobAlive()
    {
	return lastFrobAlive;
    }
    
    public void addSurvivingFrob(Frob frob)
    {
	timeAlive.add(frob.timeAlive());
	livingFrobs.add(frob);
    }
    
    public void addGenome(Genome gene)
    {
	deadFrobGenomes.add(gene);
    }
    
    /**
     * Return the number of current Grass
     * @return
     */
    public int getRemainingGrass()
    {
	return remainingGrass;
    }
    
    public double getFrobLifeAverage()
    {
	if(averageLife == null)
	    createStats();
	return averageLife;
    }
    
    public double getFrobDeviation()
    {
	return lifeDeviation;
    }
    
    public int getLongestLivedFrob()
    {
	if(longestLife == null)
	    createStats();
	return longestLife;
    }
    
    public double[] getAverageGenome()
    {
	if(averageGenome == null)
	    createStats();
	return averageGenome;
    }
    
    public double[] getGenomeDeviation()
    {
	if(genomeDeviation == null)
	    createStats();
	return genomeDeviation;
    }
    public int getShortestLivedFrob()
    {
	if(shortestLife == null)
	    createStats();
	return shortestLife;
    }
    
    private void createStats()
    {
	createLifeStats();
	createGenomeStats();
    }
    
    private void createLifeStats()
    {
	longestLife = shortestLife = timeAlive.get(0);
	averageLife = 0.0;
	
	for(Integer lifespan : timeAlive)
	{
	    if(lifespan > longestLife)
		longestLife = lifespan;
	    else if(lifespan < shortestLife)
		shortestLife = lifespan;
	    averageLife += lifespan;
	}
	numberFrobsTotal = timeAlive.size();
	averageLife /= numberFrobsTotal;
	lifeDeviation = 0.0;
	for(Integer lifespan : timeAlive)
	{
	    double nextTerm = (((double)lifespan) - averageLife);
	    nextTerm *= nextTerm;
	    lifeDeviation += nextTerm;
	}
	lifeDeviation /= numberFrobsTotal;
	lifeDeviation = Math.sqrt(lifeDeviation);
    }

    public int getTotalFrobsEver()
    {
	return totalFrobs;
    }
    
    private void createGenomeStats()
    {
	if(deadFrobGenomes.size() < 1)
	    return;
	int geneLength = Genome.getGenomeLength();
	averageGenome = new double[geneLength];
	genomeDeviation = new double[geneLength];
	for(Genome gene : deadFrobGenomes)
	{
	    for(int x = 0; x < averageGenome.length; x++)
	    {
		averageGenome[x] += gene.getRawValue(x);
	    }
	}
	for(int x = 0; x < averageGenome.length; x++)
	    averageGenome[x] /= deadFrobGenomes.size();
	for(Genome gene : deadFrobGenomes)
	{
	    for(int x = 0; x < genomeDeviation.length; x++)
	    {
		double geneValue = gene.getRawValue(x) - averageGenome[x];
		geneValue *= geneValue;
		genomeDeviation[x] += geneValue;
	    }
	}
	for(int x = 0; x < genomeDeviation.length; x++)
	{
	    genomeDeviation[x] /= deadFrobGenomes.size();
	    genomeDeviation[x] = Math.sqrt(genomeDeviation[x]);
	}
    }
    
    @Override
    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	sb.append("Frobs Alive: ").append(remainingFrobs).append("     Grass Alive: ").append(remainingGrass);
	return sb.toString();
    }
}
