package edu.unm.cs.locd011;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MatchGen {
	
	/**
	 * Creates two sample files, using the number as the file name
	 * @param numMatches
	 */
	public static void createMatchPrefFile(Integer numMatches){
		createMatchPrefFile(numMatches, numMatches.toString());
	}
	
	/**
	 * Creates two sample files for best and worst runtimes
	 * @param numMatches
	 * @param fileout
	 */
	public static void createMatchPrefFile(int numMatches, String fileout){
		String prefList;
		try{
			File best = new File(fileout + ".best.txt");
			File worst = new File(fileout + ".worst.txt");
			best.createNewFile();
			worst.createNewFile();
			FileWriter bestOut = new FileWriter(best);
			FileWriter worstOut = new FileWriter(worst);
			BufferedWriter bestOutput = new BufferedWriter(bestOut);
			BufferedWriter worstOutput = new BufferedWriter(worstOut);
			String worstCase = "";
			for(int y = 0; y < numMatches; y++){
				worstCase += " w" + y;
			}
			for(int x = 0; x < numMatches; x++){
				prefList = "";
				prefList += "m" + x + worstCase;
				worstOutput.write(prefList + "\n");
				worstOutput.flush();
				prefList = "m" + x;
				for(int y = 0; y < numMatches; y++){
					prefList += " w"+((y+x)%numMatches);
				}
				bestOutput.write(prefList + "\n");
				bestOutput.flush();
			}
			for(int x = 0; x < numMatches - 1; x++){
				prefList = "w" + x;
				for(int y = 0; y < numMatches; y++)
					prefList += " m" + ((y + x)%numMatches);
				bestOutput.write(prefList + "\n");
				bestOutput.flush();
				prefList = "w" + x;
				for(int y = 0; y < numMatches; y++){
					prefList += " m" + (numMatches - (y+1));
				}
				worstOutput.write(prefList + "\n");
			}
			prefList = "w" + (numMatches - 1);
			for(int x = 0; x < numMatches; x++){
				prefList += " m" + ((numMatches - 1 + x) % numMatches);
			}
			bestOutput.write(prefList);
			prefList = "w" + (numMatches - 1);
			for(int x = 0; x < numMatches; x++){
				prefList += " m" + ((numMatches - (x + 1)) % numMatches);
			}
			worstOutput.write(prefList);
			bestOutput.close();
			worstOutput.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
