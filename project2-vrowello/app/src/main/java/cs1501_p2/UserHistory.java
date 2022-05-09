//Victor Rowello
package cs1501_p2;
import java.util.*;

public class UserHistory implements Dict {
	
	private DLBNode head;
	private int count;
	private ArrayList<Character> searchArray;
	private int searchInt;

	public UserHistory() {
		head = null;
		count = 0;
		searchArray = new ArrayList<Character>();
		searchInt = 0;
	}
	public void add(String key) {
		DLBNode curr = head;
		int i = 0;
		while(i < key.length()) {
			if (count == 0 && i == 0) {
				head = new DLBNode(key.charAt(i));
				curr = head;
			}
			if (curr.getLet() == key.charAt(i)) {
				if (curr.getDown() == null) {
					if (i+1 < key.length()) {
					curr.setDown(new DLBNode(key.charAt(i+1)));
						curr = curr.getDown();
					}
				} else {
					if (i+1 == key.length()) {
						try {
							int frequency = Integer.parseInt(String.valueOf(curr.getDown().getLet()));
							frequency++;
							curr.setDown(new DLBNode(Character.forDigit(frequency, 10)));
						} catch (Exception error) {
							curr = curr.getDown();
						}
					} else {
						curr = curr.getDown();
					}
				}
			} else {
				if (curr.getRight() == null) {
					curr.setRight(new DLBNode(key.charAt(i)));
					curr = curr.getRight();
					if (i+1 < key.length()) {
						i--;
					}
				} else {
					curr = curr.getRight();
					i--;
				}
			}
			i++;
		} if (curr.getDown() == null) {
			curr.setDown(new DLBNode('1'));
			count++;
		}
	}

	public boolean contains(String key) {
		DLBNode curr = head;
		int i = 0;
		while(i < key.length()) {
			if (curr.getLet() == key.charAt(i)) {
				curr = curr.getDown();
			} else {
				if (curr.getRight() != null) {
					curr = curr.getRight();
					i--;
				} else {
					return false;
				}
			}
		}

		try {
			int frequency = Integer.parseInt(String.valueOf(curr.getLet()));
			return true;
		} catch (Exception error) {
			return false;
		}
	}

	public boolean containsPrefix(String pre) {
		DLBNode curr = head; 
		int i = 0;
		while(i < pre.length()) {
			if (curr.getLet() == pre.charAt(i)) {
				curr = curr.getDown();
				if (i == pre.length()-1 && curr.getRight() != null) {
					return true;
				}
			} else {
				if (curr.getRight() != null) {
					curr = curr.getRight();
					i--;
				} else {
					return false;
				}
			}
			i++;
		}
		return false;
	}


	public int searchByChar(char next) {
		searchArray.add(next);
		DLBNode curr = head;
		
		int i = 0;
		while(i < searchArray.size()) {
			if (curr.getLet() == searchArray.get(i)) {
				curr = curr.getDown();
				if (i == searchArray.size()-1) {
					try {
						int frequency = Integer.parseInt(String.valueOf(curr.getLet()));
						if (curr.getRight() == null) {
							return 1;
						} else if (curr.getRight() != null) {
							return 1;
						}
					} catch (Exception error) {
						return 0;
					}
				}
			} else {
				if (curr.getRight() != null) {
					curr = curr.getRight();
					i--;
				} else {
					return -1;
				}
			}
			i++;
		}
		return -1;
	}

	
	public void resetByChar() {
		searchArray = new ArrayList<Character>();
	}

	
	public ArrayList<String> suggest() {
		StringBuilder str = new StringBuilder();
		int i = 0;
		while(i < searchArray.size()) {
			str.append(searchArray.get(i));
			i++;
		}
		
		ArrayList<String> arr = new ArrayList<String>();
		if (searchInt < 0){
			return arr;
		}

		DLBNode curr = head;
		i = 0;
		while(i < searchArray.size()) {
			if (curr.getLet() == searchArray.get(i)) {
				curr = curr.getDown();
			} else {
				if (curr.getRight() != null) {
					curr = curr.getRight();
					i--;
				}
			}
			i++;
		}

		try {
			int frequency = Integer.parseInt(String.valueOf(curr.getLet()));
			arr.add(str.toString());
			curr = curr.getRight();
		} catch (Exception error) {
		}

		StringBuilder str2 = str;
		while (arr.size() < 5 && curr != null) {
			suggestRunner(curr, str2, arr);
			curr = curr.getRight();
		}

		ArrayList<String> arr2 = new ArrayList<String>();

		while (arr.size() > 0) {
			StringBuilder newStr = new StringBuilder(arr.get(0));
			int frequency = Integer.parseInt(String.valueOf(arr.get(0).charAt(arr.get(0).length()-1)));
			for (String s : arr) {
				int frequency1 = Integer.parseInt(String.valueOf(s.charAt(s.length()-1)));
				if (frequency1 > frequency) {
					newStr = new StringBuilder(s);
				}
			}
			arr.remove(newStr.toString());
			newStr.deleteCharAt(newStr.length()-1);
			arr2.add(newStr.toString());
		}

		return arr2;
	}

	private void suggestRunner(DLBNode curr, StringBuilder sb, ArrayList<String> arr) {
		if (arr.size() < 5) {
			boolean dontAdd = false;
			sb.append(curr.getLet());
			try {
				int frequency = Integer.parseInt(String.valueOf(curr.getLet()));
				if (arr.contains(sb.toString()) == false && dontAdd == false) {
					arr.add(sb.toString());
					sb.deleteCharAt(sb.length()-1);
				} else {
					sb.deleteCharAt(sb.length()-1);
				}	
			} catch (Exception error) {
				suggestRunner(curr.getDown(), sb, arr);
				sb.deleteCharAt(sb.length()-1);
				dontAdd = true;
			}

			while (curr.getRight() != null) {
				curr = curr.getRight();
				suggestRunner(curr, sb, arr);
			}
		}
	}

	public ArrayList<String> traverse() {
		DLBNode curr = head;

		ArrayList<String> arr = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();
		traverseRunner(curr, sb, arr);

		return arr;
	}

	private void traverseRunner(DLBNode curr, StringBuilder sb, ArrayList<String> arr) {
		boolean dontAdd = false;
		sb.append(curr.getLet());
		try {
			int frequency = Integer.parseInt(String.valueOf(curr.getLet()));
			if (arr.contains(sb.toString()) == false && dontAdd == false) {
				arr.add(sb.toString());
				sb.deleteCharAt(sb.length()-1);
			} else {
				sb.deleteCharAt(sb.length()-1);	
			}
		} catch (Exception error) {
			traverseRunner(curr.getDown(), sb, arr);
			sb.deleteCharAt(sb.length()-1);
			dontAdd = true;
		}

		while (curr.getRight() != null) {
			curr = curr.getRight();
			traverseRunner(curr, sb, arr);
		}
	}

	
	public int count() {
		return count;
	}
	
	public void setSearchInt(int i)
	{
		searchInt = i;
	}
}
