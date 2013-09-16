package edu.unm.cs.locd011;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Matcher {

	ArrayList<Tuple<Person>> stableMatching = new ArrayList<Tuple<Person>>();
	String currentFile;
	
	private HashMap<String, Man> manIndexer = new HashMap<String, Man>();
	private HashMap<String, Woman> womanIndexer = new HashMap<String, Woman>();
	
	private LinkedList<Man> singleMen = new LinkedList<Man>();
	
	private boolean measureTime = false;
	private String timeString = "Run time for ";
	
	/**
	 * Creates a matcher for a specified file input
	 * @param filename
	 */
	public Matcher(String filename){
		currentFile = filename;
		timeString += filename + ": ";
	}
	
	/**
	 * Explicitly sets the matcher to measure performance, or not.
	 * @param filename
	 * @param measureTime
	 */
	public Matcher(String filename, boolean measureTime){
		this(filename);
		this.measureTime = measureTime;
	}
	
	/**
	 * Manages loading and matching.
	 */
	private void findMatching(){
		loadMatches();
		long start = System.currentTimeMillis();
		solveMatching();
		long end = System.currentTimeMillis();
		if(measureTime){
			timeString += (end - start) + " ms";
		}
	}
	
	/**
	 * Loads the preference lists from the file, and stores them to Hashtables.
	 * Closes those files at the end.
	 */
	private void loadMatches(){
		try{
			ArrayList<String[]> preferenceLists = new ArrayList<String[]>();
			FileReader inputFileReader = new FileReader(currentFile);
			BufferedReader bufferedInputReader = new BufferedReader(inputFileReader);
			while(bufferedInputReader.ready()){
				preferenceLists.add(bufferedInputReader.readLine().split(" "));
			}
			bufferedInputReader.close();
			inputFileReader.close();
			sortData(preferenceLists);
		} catch(IOException e){
			System.out.println("An error occured while parsing the file!");
			System.err.println(e.getMessage());
		}	
	}
	
	/**
	 * Sort through the data, grouping into men/woman and their preference lists
	 * @param data
	 */
	private void sortData(AbstractList<String[]> data){
		int halfway = data.size()/2;
		int size = data.size();
		Man[] menList = new Man[halfway];
		for(int x = 0; x < halfway; x++){
			Man nextMan;
			String[] preferences = data.get(x);
			LinkedList<String> preferenceByName = new LinkedList<String>();
			for(int y = 1; y < preferences.length; y++){
				preferenceByName.add(preferences[y]);
			}
			nextMan = new Man(preferenceByName, x, preferences[0]);
			manIndexer.put(nextMan.getName(), nextMan);
			menList[x] = nextMan;
			singleMen.add(nextMan);
		}
		
		for(int x = halfway; x < size; x++){
			String[] preferences = data.get(x);
			int[] preferenceList = new int[preferences.length - 1];
			for(int y = 1; y < preferences.length; y++){
				preferenceList[manIndexer.get(preferences[y]).getIndex()] = y;
			}
			Woman nextWoman = new Woman(preferenceList, x - halfway, preferences[0]);
			womanIndexer.put(nextWoman.getName(), nextWoman);
			stableMatching.add(new Tuple<Person>(nextWoman));
		}
		
	}
	
	/**
	 * The Gale-Shapely algorithm. Solves the matching
	 */
	private void solveMatching(){
		while(!singleMen.isEmpty()){
			Man testMan = singleMen.remove();
			Man toAdd = checkMatch(testMan, womanIndexer.get(testMan.getNextMatch()));
			if(toAdd != null)
				singleMen.add(toAdd);
		}
	}
	
	/**
	 * Checks which man wins out over the other, and returns the "loser" to readd to the singles list.
	 * @param proposing
	 * @param proposee
	 * @return
	 */
	private Man checkMatch(Man proposing, Woman proposee){
		Tuple<Person> tuple = stableMatching.get(proposee.getIndex());
		if(tuple.isFull()){
			Man toReplace = proposee.prefers(proposing, (Man)tuple.getSecond());
			if(toReplace == proposing)
				return (Man)tuple.setSecond(proposing);
			return proposing;
		}
		return (Man)tuple.setSecond(proposing);
	}
	
	/**
	 * Solves the algorithm if it hasn't been already.
	 * Output the stable matching, or print performance data.
	 */
	@Override
	public String toString(){
		String matchList = "{";
		if(stableMatching.isEmpty())
			findMatching();
		for(Tuple<Person> x : stableMatching)
			matchList += x;
		return measureTime ? timeString : matchList + "}";
	}
}
