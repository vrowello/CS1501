//Victor Rowello
package cs1501_p3;

import java.util.*;
import java.io.*;

public class CarsPQ implements CarsPQ_Inter
{
	/**
	 * CarsPQ sorted for lowest price as priority
	 */
	private PriceQueue carsPrice;

	/**
	 * CarsPQ sorted for lowest mileage as priority
	 */
	private MileageQueue carsMileage;

	/**
	 * Number of added cars
	 */
	private int size;

	public CarsPQ(String fileName) {
		carsPrice = new PriceQueue();
		carsMileage = new MileageQueue();
		size = 0;

		try {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String newCar = scan.nextLine();
				String[] newCarData = newCar.split(":");
				if (newCarData[0].compareTo("# VIN") != 0) {
					int price = Integer.parseInt(newCarData[3]);
					int mileage = Integer.parseInt(newCarData[4]);
					Car car = new Car(newCarData[0], newCarData[1], newCarData[2], price, mileage, newCarData[5]);
					
					add(car);
				}
			}
			scan.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a new Car to the data structure
	 * Should throw an `IllegalStateException` if there is already car with the
	 * same VIN in the datastructure.
	 *
	 * @param 	c Car to be added to the data structure
	 */
	public void add(Car c) throws IllegalStateException {
		carsPrice.add(c);
		carsMileage.add(c);
		size++;
	}

	/**
	 * Retrieve a new Car from the data structure
	 * Should throw a `NoSuchElementException` if there is no car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 */
	public Car get(String vin) throws NoSuchElementException {
		int i = carsPrice.get(vin);
		return carsPrice.getAtIndex(i);
	}

	/**
	 * Update the price attribute of a given car
	 * Should throw a `NoSuchElementException` if there is no car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newPrice The updated price value
	 */
	public void updatePrice(String vin, int newPrice) throws NoSuchElementException {
		carsPrice.updatePrice(vin, newPrice);
		carsMileage.updatePrice(vin, newPrice);
	}

	/**
	 * Update the mileage attribute of a given car
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newMileage The updated mileage value
	 */
	public void updateMileage(String vin, int newMileage) throws NoSuchElementException {
		carsPrice.updateMileage(vin, newMileage);
		carsMileage.updateMileage(vin, newMileage);
	}

	/**
	 * Update the color attribute of a given car
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be updated
	 * @param	newColor The updated color value
	 */
	public void updateColor(String vin, String newColor) throws NoSuchElementException {
		carsPrice.updateColor(vin, newColor);
		carsMileage.updateColor(vin, newColor);
	}

	/**
	 * Remove a car from the data structure
	 * Should throw a `NoSuchElementException` if there is not car with the 
	 * specified VIN in the datastructure.
	 *
	 * @param 	vin VIN number of the car to be removed
	 */
	public void remove(String vin) throws NoSuchElementException {
		carsPrice.remove(vin);
		carsMileage.remove(vin);
		size--;
	}

	/**
	 * Get the lowest priced car (across all makes and models)
	 * Should return `null` if the data structure is empty
	 *
	 * @return	Car object representing the lowest priced car
	 */
	public Car getLowPrice() {
		return carsPrice.getRoot();
	}

	/**
	 * Get the lowest priced car of a given make and model
	 * Should return `null` if the data structure is empty
	 *
	 * @param	make The specified make
	 * @param	model The specified model
	 * 
	 * @return	Car object representing the lowest priced car
	 */
	public Car getLowPrice(String make, String model) {
		PriceQueue newCarsPrice = new PriceQueue();

		for (int i=0; i<size; i++) {
			if (carsPrice.getAtIndex(i).getMake().compareTo(make) == 0 && carsPrice.getAtIndex(i).getModel().compareTo(model) == 0) {
				newCarsPrice.add(carsPrice.getAtIndex(i));
			}
		}
		return newCarsPrice.getRoot();
	}

	/**
	 * Get the car with the lowest mileage (across all makes and models)
	 * Should return `null` if the data structure is empty
	 *
	 * @return	Car object representing the lowest mileage car
	 */
	public Car getLowMileage() {
		return carsMileage.getRoot();
	}

	/**
	 * Get the car with the lowest mileage of a given make and model
	 * Should return `null` if the data structure is empty
	 *
	 * @param	make The specified make
	 * @param	model The specified model
	 *
	 * @return	Car object representing the lowest mileage car
	 */
	public Car getLowMileage(String make, String model) {
		MileageQueue newCarsMileage = new MileageQueue();

		for (int i=0; i<size; i++) {
			if (carsMileage.getAtIndex(i).getMake().compareTo(make) == 0 && carsMileage.getAtIndex(i).getModel().compareTo(model) == 0) {
				newCarsMileage.add(carsMileage.getAtIndex(i));
			}
		}
		return newCarsMileage.getRoot();
	}

	public void print() {
		System.out.println("Cars Price:");
		carsPrice.print();
		System.out.println("Cars Mileage:");
		carsMileage.print();
	}
	
	
}
