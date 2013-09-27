package edu.unm.cs.locd011;

public class Driver {
	//Give the user some information on how to use this program
	private static String invalidInput = 	"Incorrect input!\n"+
							"Valid usages of this program are: \n" +
							"StableMatcher [filename] <filenames>\n" +
							"StableMatcher -g [number] <output>\n" +
							"Switches:\n" + 
							" -g [number] <output>: Generate two files (A best and worst case) with [number]"+
							"people, and output it to <output>\n"+
							" -t: Any files after this switch only output performance data for the algorithm";
	
	//By default, we want to print stable matchings
	private static boolean toTime = false;

	/**
	 * Driver for the program.
	 * StableMatcher [filename] (filenames) iterates through test files and outputs stable matches
	 * StableMatcher -t [filename] (filenames) iterates through test files and outputs performance data in ms
	 * StableMatcher -g [number] (filename) makes a best and worst matching, and outputs them to filename.best.txt or filename.worst.txt
	 * 		N.B. This command uses number as the filename if no argument comes after the number.
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0)
			System.out.println(invalidInput);
		for(int x = 0; x < args.length; x++){
			String fileIn = args[x];
			if(fileIn.matches("-g")){
				if(args.length > x + 2){
					MatchGen.createMatchPrefFile(Integer.parseInt(args[x+1]), args[x+2]);
					x += 2;
				}
				else if (args.length > x + 1){
					MatchGen.createMatchPrefFile(Integer.parseInt(args[x+1]));
					x++;
				}
				else{
					System.out.println(invalidInput);
					return;
				}
			} else if (fileIn.matches("-t")){
				toTime = true;
			} else {
				Matcher solutionFinder = new Matcher(fileIn, toTime);
				System.out.println(solutionFinder);
			}
		}
	}
}
