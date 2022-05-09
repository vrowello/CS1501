//Victor Rowello
package cs1501_p3;

public class Car implements Car_Inter
{
	private String vin;
	private String make;
	private String model;
	private int price;
	private int mileage;
	private String color;

	public Car(String v, String ma, String mo, int p, int m, String c) {
		vin = v;
		make = ma;
		model = mo;
		price = p;
		mileage = m;
		color = c;
	}

	/**
	 * Getter for the VIN attribute
	 *
	 * @return 	String The VIN
	 */
	public String getVIN() {
		return vin;
	}

	/**
	 * Getter for the make attribute
	 *
	 * @return 	String The make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Getter for the model attribute
	 *
	 * @return 	String The model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Getter for the price attribute
	 *
	 * @return 	String The price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Getter for the mileage attribute
	 *
	 * @return 	String The mileage
	 */
	public int getMileage() {
		return mileage;
	}

	/**
	 * Getter for the color attribute
	 *
	 * @return 	String The color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Setter for the price attribute
	 *
	 * @param 	newPrice The new Price
	 */
	public void setPrice(int newPrice) {
		price = newPrice;
	}

	/**
	 * Setter for the mileage attribute
	 *
	 * @param 	newMileage The new Mileage
	 */
	public void setMileage(int newMileage) {
		mileage = newMileage;
	}

	/**
	 * Setter for the color attribute
	 *
	 * @param 	newColor The new color
	 */
	public void setColor(String newColor) {
		color = newColor;
	}

	public void print() {
		System.out.println(vin);
		System.out.println(make);
		System.out.println(model);
		System.out.println(price);
		System.out.println(mileage);
		System.out.println(color);
		System.out.println();
	}
}
