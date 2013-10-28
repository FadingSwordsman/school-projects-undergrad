package com.putable.frobworld.locd011.simulation;

import java.util.HashMap;

/**
 * A list of settings specific to Frobs
 * @author David
 *
 */
public class FrobSetting extends AbstractLivingSetting {
	private final int DNA_MUTATION_ODDS_PER_BYTE;
	private final int FROB_HIT_PENALTY;

	public FrobSetting(int fixedOverhead, int massTax, int genesisMass,
			int dnaMutationOdds, int frobHitPenalty) {
		super(fixedOverhead, massTax, genesisMass);
		DNA_MUTATION_ODDS_PER_BYTE = dnaMutationOdds;
		FROB_HIT_PENALTY = frobHitPenalty;
	}

	public FrobSetting(HashMap<String, Integer> settingMap) {
		this(settingMap.get("FROB_FIXED_OVERHEAD"), settingMap.get("FROB_MASS_TAX"), settingMap.get("FROB_GENESIS_MASS"),
				settingMap.get("DNA_MUTATION_ODDS"), settingMap.get("FROB_HIT_PENALTY"));
	}

	public int getDnaMutationOdds() {
		return DNA_MUTATION_ODDS_PER_BYTE;
	}

	public int getFrobHitPenalty() {
		return FROB_HIT_PENALTY;
	}
}
