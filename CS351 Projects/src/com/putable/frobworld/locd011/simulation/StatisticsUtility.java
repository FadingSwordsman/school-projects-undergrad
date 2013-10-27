package com.putable.frobworld.locd011.simulation;

import com.putable.frobworld.locd011.beings.Frob;

public final class StatisticsUtility
{
    private StatisticsUtility()
    {}
    
    public static double[] calculateMetabolismStats(Iterable<Frob> frobs)
    {
	double average = 0;
	double standardDeviation = 0;
	int numberFrobs = 0;
	for(Frob frob : frobs)
	{
	    average += frob.getUpdatePeriod();
	    numberFrobs++;
	}
	average /= numberFrobs;
	for(Frob frob : frobs)
	{
	    double value = frob.getUpdatePeriod() - average;
	    value *= value;
	    standardDeviation += value;
	}
	standardDeviation /= numberFrobs;
	standardDeviation = Math.sqrt(standardDeviation);
	return new double[]{average, standardDeviation};
    }
}
