package main;

public class MainClass {

	public static void main(String[] args) {
		if(args.length < 1)
		{
			System.err.println("No file");
			return;
		}
		ReplaceName rn = new ReplaceName();
		rn.performTransition(args[0]);
	}

}
