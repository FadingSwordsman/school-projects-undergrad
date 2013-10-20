package com.putable.frobworld.locd011.simulation;

public abstract class AbstractLivingSetting
{
    private final int FIXED_OVERHEAD;
    private final int MASS_TAX;
    private final int GENESIS_MASS;
    
    public AbstractLivingSetting(int fixedOverhead, int massTax, int genesisMass)
    {
	FIXED_OVERHEAD = fixedOverhead;
	MASS_TAX = massTax;
	GENESIS_MASS = genesisMass;
    }

    public int getFixedOverhead()
    {
        return FIXED_OVERHEAD;
    }

    public int getMassTax()
    {
        return MASS_TAX;
    }

    public int getGenesisMass()
    {
        return GENESIS_MASS;
    }
}
