package main;

import utils.Plotter;


public class Main {

	public static void main(String[] args) {
		
		boolean hlFlag= false, llFlag= false;
		String path= null;
		
		if(args.length == 0)
			System.err.println("No input");
		
		try {
			
			if(args[0].equalsIgnoreCase("-plot")) {
				Plotter.plot(args);
			} else if(args[0].equalsIgnoreCase("-all") && args.length == 2) {
				hlFlag= true;
				llFlag= true;
				path= args[1];
			} else if(args[0].equalsIgnoreCase("-hl") && args.length == 2) {
				hlFlag= true;
				path= args[1];
			} else if(args[0].equalsIgnoreCase("-ll") && args.length == 2) {
				llFlag= true;
				path= args[1];
			} else {
				System.err.println("USAGE: ");
				System.exit(1);
			}
			
			if(hlFlag) {
				System.out.println("ARTISTS PHASE: Please wait...");
				MasterMetadata.artistMetadata(path, hlFlag);
				System.out.println("ALBUMS PHASE: Please wait...");
				MasterMetadata.albumMetadata(path, hlFlag);
			}
			if(hlFlag || llFlag) {
				System.out.println("SONGS PHASE: Please wait...");
				MasterMetadata.songMetadata(path, hlFlag, llFlag);
			}
					
		} catch(Exception e) {
			System.err.println(e.getMessage());
			MasterMetadata.shutDownMFE();
			System.err.println("MFE is shutting down");
		}
	}
}
