//Victor Rowello
package cs1501_p3;

import java.util.*;

public class PriceQueue {

	private Car[] arr;
	private int size;
	private Hashtable<String, Integer> table;

	public PriceQueue() {
		arr = new Car[15];
		size = 0;
		table = new Hashtable<String, Integer>();
	}

	
	public void add(Car c) throws IllegalStateException {
		if (size == arr.length)
			arr = resizeArr();

		try {
			int a = get(c.getVIN());
			if (a < -1) {
				throw new IllegalStateException("This VIN already here");
			}
		}
		catch (NoSuchElementException ignored) {
		}

		table.put(c.getVIN(), size);
		arr[size] = c;

		int index = size;
		while (index > 0) {
			if (index % 2 == 1) {
				index = (index-1)/2;
				Car swap = arr[index];
				if (c.getPrice() < swap.getPrice()) {
					arr[(2*index)+1] = swap;
					table.put(swap.getVIN(), (2*index)+1);
				} else {
					arr[(2*index)+1] = c;
					table.put(c.getVIN(), (2*index)+1);
					break;
				}
			} else if (index % 2 == 0) {
				index = (index-2)/2;
				Car swap = arr[index];
				if (c.getPrice() < swap.getPrice()) {
					arr[(2*index)+2] = swap;
					table.put(swap.getVIN(), (2*index)+2);
				} else {
					arr[(2*index)+2] = c;
					table.put(c.getVIN(), (2*index)+2);
					break;
				}
			}
			if (index == 0) {
				arr[index] = c;
				table.put(c.getVIN(), index);
			}
		}
		size++;
	}

	
	public int get(String vin) throws NoSuchElementException {
		try {
			int a = table.get(vin);
			return a;
		} 
		catch (NullPointerException e) {
			throw new NoSuchElementException("This VIN not here");		
		}
	}

	
	public void updatePrice(String vin, int newPrice) throws NoSuchElementException {
		int index = get(vin);
		Car car = arr[index];

		car.setPrice(newPrice);

		while (index > 0) {
			if (index % 2 == 1) {
				index = (index-1)/2;
				Car swap = arr[index];
				if (car.getPrice() < swap.getPrice()) {	
					swapLeft(swap, index);
					car = arr[index];
				} else {
					break;
				}
			} else if (index % 2 == 0) {
				index = (index-2)/2;
				Car swap = arr[index];
				if (car.getPrice() < swap.getPrice()) {	
					swapRight(swap, index);
					car = arr[index];
				}
				else {
					break;
				}
			}
		}

		try {
			while (arr[(2*index)+1] != null) {
				if (arr[(2*index)+2] != null) {
					if (arr[index].getPrice() < arr[(2*index)+1].getPrice() && arr[index].getPrice() < arr[(2*index)+2].getPrice()) {
						break;
          }
          
					if (arr[(2*index)+1].getPrice() < arr[(2*index)+2].getPrice()) {
						swapLeft(car, index);

						index = (2*index)+1;
						car = arr[index];					
					} else {
						swapRight(car, index);

						index = (2*index)+2;
						car = arr[index];	
					}
				} else {
					if (arr[index].getPrice() > arr[(2*index)+1].getPrice()) {
						swapLeft(car, index);

						index = (2*index)+1;
						car = arr[index];
					} else {
						break;
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignored) {
		}
	}
	
	public void updateMileage(String vin, int newMileage) throws NoSuchElementException {
		Car car = arr[get(vin)];

		car.setMileage(newMileage);
	}

	
	public void updateColor(String vin, String newColor) throws NoSuchElementException {
		Car car = arr[get(vin)];

		car.setColor(newColor);
	}

	
	public void remove(String vin) throws NoSuchElementException {
		Car last = arr[size-1];
		arr[size-1] = null;

		int index = get(vin);

		if (index != size-1) {
			arr[index] = last;
		}

		Car car = arr[index];
		table.remove(vin);

		try {
			while (arr[(2*index)+1] != null) {
				if (arr[(2*index)+2] != null) {
					if (arr[index].getPrice() < arr[(2*index)+1].getPrice() && arr[index].getPrice() < arr[(2*index)+2].getPrice()) {
						break;
					}

					if (arr[(2*index)+1].getPrice() < arr[(2*index)+2].getPrice()) {
						swapLeft(car, index);

						index = (2*index)+1;
						car = arr[index];					
					} else {
						swapRight(car, index);

						index = (2*index)+2;
						car = arr[index];	
					}
				}
				else {
					if (arr[index].getPrice() > arr[(2*index)+1].getPrice()) {
						swapLeft(car, index);

						index = (2*index)+1;
						car = arr[index];
					}
					else {
						break;
					}
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignored) {
		}
		size--;
	}

	
	public Car getRoot() {
		return arr[0];
	}

	private void swapLeft(Car node, int index) {
		Car car = arr[(2*index)+1];
		arr[(2*index)+1] = node;
		table.put(node.getVIN(), (2*index)+1);
		arr[index] = car;
		table.put(car.getVIN(), index);
	}

	private void swapRight(Car node, int index) {
		Car car = arr[(2*index)+2];
		arr[(2*index)+2] = node;
		table.put(node.getVIN(), (2*index)+2);
		arr[index] = car;
		table.put(car.getVIN(), index);
	}

	public Car getAtIndex(int i) {
		return arr[i];
	}

	public void print() {
		for (int i=0; i<arr.length; i++) {
			if (arr[i] != null) {
				arr[i].print();
			}
			
			else {
				break;
			}
		}
	}

	private Car[] resizeArr() {
		Car[] newArr = new Car[arr.length*2];

		for (int i=0; i<arr.length; i++) {
			if (arr[i] != null) {
				newArr[i] = arr[i];
			}
		}
		return newArr;
	}
}
