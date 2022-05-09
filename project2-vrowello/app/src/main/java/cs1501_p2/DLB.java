//Victor Rowello
package cs1501_p2;

import java.util.*;

public class DLB implements Dict
{
	
	private DLBNode head;
	private int count;
	private ArrayList<Character> searchArray;
	private int searchInt;

	public DLB() {
		head = null;
		count = 0;
		searchArray = new ArrayList<Character>();
		searchInt = 0;
	}

	
	public void add(String key) {
		if (count == 0){
			head = new DLBNode(key.charAt(0));
			head.setDown(new DLBNode('*'));
		} else {
			DLBNode curr = head;
			int i = 0;
			while (i < key.length()) {
				if (curr.getLet() == key.charAt(i)) {
					if (curr.getDown() == null) {
						if (i+1 < key.length()) {
							curr.setDown(new DLBNode(key.charAt(i+1)));
							curr = curr.getDown();
						}
					} else {
						curr = curr.getDown();
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
			}
			curr.setDown(new DLBNode('*'));
		}
		count++;
	}

	public boolean contains(String key) {
		DLBNode curr = head;
		int i = 0;
		while(i < key.length()){
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
			i++;
		}
		if (curr.getLet() == '*') {
			return true;
		} else {
			return false;
		}
	}

	public boolean containsPrefix(String pre){
		DLBNode curr = head;
		int i = 0;
		while(i < pre.length()) {
			if (curr.getLet() == pre.charAt(i)) {
				curr = curr.getDown();
				if (i+1 == pre.length() && curr != null) {
					if (curr.getLet() != '*' || (curr.getLet() == '*' && curr.getRight() != null)) {
						return true;
					}
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
		while (i < searchArray.size()) {
			if (curr.getLet() == searchArray.get(i)) {
				curr = curr.getDown();
				if (i == (searchArray.size()-1)) {
					if (curr.getLet() != '*') {
						return 0;
					} else if (curr.getLet() == '*' && curr.getRight() == null) {
						return 1;
					} else if (curr.getLet() == '*' && curr.getRight() != null) {
						return 2;
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
		while (i<searchArray.size()) {
			str.append(searchArray.get(i));
			i++;
		}

		ArrayList<String> arr = new ArrayList<String>();
		if (searchInt < 0) {
			return arr;
		}
		i = 0;
		DLBNode curr = head;
		while (i<searchArray.size()) {
			if (curr.getLet() == searchArray.get(i)) {
				curr = curr.getDown();
			}
			else {
				if (curr.getRight() != null) {
					curr = curr.getRight();
					i--;
				}
			}
			i++;
		}

		if (curr.getLet() == '*') {
			arr.add(str.toString());
		}

		StringBuilder newStr = str;
		if (curr.getLet() == '*') {
			curr = curr.getRight();
		}
		if (curr != null) {
			suggestRunner(curr, newStr, arr);
		}
		return arr;
	}

	
	private void suggestRunner(DLBNode curr, StringBuilder sb, ArrayList<String> arr) {
		if (arr.size() < 5) {
			boolean stop = false;
			if (curr.getLet() != '*') {
				sb.append(curr.getLet());
				suggestRunner(curr.getDown(), sb, arr);
				sb.deleteCharAt(sb.length()-1);
				stop = true;
			}

			if (arr.contains(sb.toString()) == false && stop == false) {
				arr.add(sb.toString());
				stop = false;
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
		
		int i = 0;
		while(i < count) {
			StringBuilder str = new StringBuilder();
			traverseRunner(curr, str, arr);
			
			if (curr.getRight() != null) {
				curr = curr.getRight();
			} else {
				break;
			}
			i++;
		}

		return arr;
	}

	private void traverseRunner(DLBNode curr, StringBuilder sb, ArrayList<String> arr) {
		boolean stop = false;
		if (curr.getLet() != '*') {
			sb.append(curr.getLet());
			traverseRunner(curr.getDown(), sb, arr);
			sb.deleteCharAt(sb.length()-1);
			stop = true;
		}

		if (arr.contains(sb.toString()) == false && stop == false) {
			arr.add(sb.toString());
			stop = false;
		}

		while (curr.getRight() != null) {
			curr = curr.getRight();
			traverseRunner(curr, sb, arr);
		}
	}

	public int count() {
		return count;
	}

	
	public void setSearchInt(int i) {
		searchInt = i;
	}
}
