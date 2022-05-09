//Victor Rowello
package cs1501_p2;

import java.util.*;
import java.io.*;

public class AutoCompleter implements AutoComplete_Inter {
	public DLB dlb;
	public UserHistory uH;

	
	public AutoCompleter(String dictionary)
	{
		System.out.println("No History Found\n");
		dlb = new DLB();

		try
		{
			File dict = new File(dictionary);
			Scanner scan = new Scanner(dict);
			while (scan.hasNextLine())
			{
				String nextWord = scan.nextLine();
				dlb.add(nextWord);
			}
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("No Dict");
		}

		uH = new UserHistory();
	}

	/**
	 * Constructor that accepts the file name of the dictionary
	 */
	public AutoCompleter(String dictionary, String history)
	{
		System.out.println("History Detected\n");

		dlb = new DLB();

		try
		{
			File dict = new File(dictionary);
			Scanner scan = new Scanner(dict);
			while (scan.hasNextLine())
			{
				String nextWord = scan.nextLine();
				dlb.add(nextWord);
			}
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("No Dict");
		}

		uH = new UserHistory();

		try
		{
			File historyFile = new File(history);
			Scanner hisScan = new Scanner(historyFile);
			while (hisScan.hasNextLine())
			{
				String nextWord = hisScan.nextLine();
				uH.add(nextWord);
			}
			hisScan.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("No History");
		}
	}

	
	public ArrayList<String> nextChar(char next)
	{
		ArrayList<String> history = new ArrayList<String>();

		if (uH.count() > 0)
		{
			uH.setSearchInt(uH.searchByChar(next));
			history = uH.suggest();
		}

		dlb.setSearchInt(dlb.searchByChar(next));
		ArrayList<String> dict = dlb.suggest();

		for (String s : dict)
		{
			if (history.size() >= 5)
				break;
			else if (!history.contains(s))
				history.add(s);
		}
		return history;
	}

	public void finishWord(String cur)
	{
		dlb.resetByChar();
		uH.resetByChar();
		uH.add(cur);

		ArrayList<String> fullHistory = uH.traverse();
		System.out.println("User History:");
		for (String s : fullHistory) {
			System.out.println(s);
		}
		System.out.println();
	}

	
	public void saveUserHistory(String fname)
	{
		File history = new File(fname);
		if (history.exists() == false)
		{
			try {
				history.createNewFile();
			} catch (Exception error) {
				System.out.println("Couldn't Create History");
			}
		}

		try {
    		FileWriter userHWrite = new FileWriter(fname, false);
    		ArrayList<String> fullHistory = uH.traverse();
    		for (String s : fullHistory) {
				userHWrite.write(s+"\n");
			}
    		userHWrite.close();
		} catch (IOException error) {
    		System.out.println("Error writing");
		}
	}
}
