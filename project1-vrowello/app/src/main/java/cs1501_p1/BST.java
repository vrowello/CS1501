package cs1501_p1;
public class BST<T extends Comparable<T>> implements BST_Inter<T> {

	public BTNode root;
	private BTNode gc;
	
	public void put(T key) {
		if(root == null) {
			root = new BTNode(key);
		} else {
			this.putRunner(key, root);
		}
	}
	private <T extends Comparable<T>> void putRunner(T key, BTNode<T> node) {
		if(key.compareTo(node.getKey()) > 0) {
			if(node.getRight() == null) {
				node.setRight(new BTNode(key));
			} else {
				this.putRunner(key, node.getRight());
			}
		} 
		
		if(key.compareTo(node.getKey()) < 0) {
			if(node.getLeft() == null) {
				node.setLeft(new BTNode(key));
			} else {
				this.putRunner(key, node.getLeft());
			}
		} 
	}
	
	public boolean contains(T key) {
		return containsRunner(key, root);
	}
	
	private boolean containsRunner(T key, BTNode<T> node) {
		if(node == null) {
			return false;
		}
		
		if(node.getKey().equals(key)) {
			return true;
		}
		
		if(key.compareTo(node.getKey()) > 0) {
			return containsRunner(key, node.getRight());
		} 
		
		else {
			return containsRunner(key, node.getLeft());
		} 
		
		
	}
	
	public void delete(T key) {
	}
	
	private void deleteRunner(T key, BTNode<T> node) {
		if(node == null) {
			return;
		} 
		
		if(key.compareTo(node.getKey()) > 0) {
			deleteRunner(key, node.getRight());
		} else if(key.compareTo(node.getKey()) < 0) {
			deleteRunner(key, node.getLeft());
		} else {
			if(node.getRight() != null) {
				T placeholder = lowest(node.getRight()).getKey();
				BTNode<T> newnode = new BTNode(placeholder);
				newnode.setLeft(node.getLeft());
				newnode.setRight(node.getRight());
				gc = lowest(node.getRight());
				gc = null;
				node = newnode;
			} else if(node.getLeft() != null) {
				node = node.getLeft();
			} else {
				node = null;
			}
		}
	}
	
	private BTNode<T> lowest(BTNode<T> node) {
		if(node.getLeft() == null) {
			return node;
		} else {
			return lowest(node.getLeft());
		}
	}
	
	public int height() {
		return heightRunner(root);
	}
	
	private int heightRunner(BTNode<T> node) {
  		if (node == null) return 0;
  		return 1 + Math.max(heightRunner(node.getLeft()), heightRunner(node.getRight()));
	}
	
	public boolean isBalanced() {
		return balanceRunner(root);
	}
	
	private boolean balanceRunner(BTNode<T> node) {
		if(node == null) {
			return true;
		}
		else if(Math.abs((heightRunner(node.getLeft()) - heightRunner(node.getRight()))) < 2) {
			return(balanceRunner(node.getLeft()) && balanceRunner(node.getRight()));
		} else {
			return false;
		}
	}
	public String inOrderTraversal() {
		return "testing";
	}
	
	private String iotRunner(BTNode<T> node) {
		if(node.getLeft() == null && node.getRight() == null) {
			return node.getKey().toString() + ":";
		} else if(node == null) {
			return "";
		} else {
			return iotRunner(node.getLeft()) + node.getKey().toString() + ":" + iotRunner(node.getRight());
		}
		
	}
	
	
	public String serialize() {
		return "testing";
	}
	
	public BST reverse() {
		BST rev = new BST();
		rev.put(this.root.getKey());
		revRunner(rev.root, this.root);
		return rev;
	}
	
	public void revRunner(BTNode<T> rev, BTNode<T> node) {
		if(node.getLeft() != null) {
			rev.setRight(new BTNode(node.getLeft().getKey()));
			revRunner(rev.getRight(), node.getLeft());
		} 
		if(node.getRight() != null) {
			rev.setLeft(new BTNode(node.getRight().getKey()));
			revRunner(rev.getLeft(), node.getRight());
		} 
		
		return;
		
		
	}

}
