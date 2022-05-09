package cs1501_p5;
import java.util.stream.IntStream;

public class HeftyInteger {

    private final byte[] ONE = {(byte) 1};
    private byte[] val;

    /**
     * Construct the HeftyInteger from a given byte array
     *
     * @param b the byte array that this HeftyInteger should represent
     */
    public HeftyInteger(byte[] b) {
        val = b;
    }

    /**
     * Return this HeftyInteger's val
     *
     * @return val
     */
    public byte[] getVal() {
        return val;
    }

    /**
     * Return the number of bytes in val
     *
     * @return length of the val byte array
     */
    public int length() {
        return val.length;
    }

    /**
     * Add a new byte as the most significant in this
     *
     * @param extension the byte to place as most significant
     */
    public void extend(byte extension) {
        byte[] newv = new byte[val.length + 1];
        newv[0] = extension;
        for (int i = 0; i < val.length; i++) {
            newv[i + 1] = val[i];
        }
        val = newv;
    }

    /**
     * If this is negative, most significant bit will be 1 meaning most
     * significant byte will be a negative signed number
     *
     * @return true if this is negative, false if positive
     */
    public boolean isNegative() {
        return (val[0] < 0);
    }

    /**
     * Computes the sum of this and other
     *
     * @param other the other HeftyInteger to sum with this
     */
    public HeftyInteger add(HeftyInteger other) {
        byte[] a, b;
        // If operands are of different sizes, put larger first ...
        if (val.length < other.length()) {
            a = other.getVal();
            b = val;
        } else {
            a = val;
            b = other.getVal();
        }

        // ... and normalize size for convenience
        if (b.length < a.length) {
            int diff = a.length - b.length;

            byte pad = (byte) 0;
            if (b[0] < 0) {
                pad = (byte) 0xFF;
            }

            byte[] newb = new byte[a.length];
            for (int i = 0; i < diff; i++) {
                newb[i] = pad;
            }

            for (int i = 0; i < b.length; i++) {
                newb[i + diff] = b[i];
            }

            b = newb;
        }

        // Actually compute the add
        int carry = 0;
        byte[] res = new byte[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            // Be sure to bitmask so that cast of negative bytes does not
            //  introduce spurious 1 bits into result of cast
            carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

            // Assign to next byte
            res[i] = (byte) (carry & 0xFF);

            // Carry remainder over to next byte (always want to shift in 0s)
            carry = carry >>> 8;
        }

        HeftyInteger res_li = new HeftyInteger(res);

        // If both operands are positive, magnitude could increase as a result
        //  of addition
        if (!this.isNegative() && !other.isNegative()) {
            // If we have either a leftover carry value or we used the last
            //  bit in the most significant byte, we need to extend the result
            if (res_li.isNegative()) {
                res_li.extend((byte) carry);
            }
        }
        // Magnitude could also increase if both operands are negative
        else if (this.isNegative() && other.isNegative()) {
            if (!res_li.isNegative()) {
                res_li.extend((byte) 0xFF);
            }
        }

        // Note that result will always be the same size as biggest input
        //  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
        return res_li;
    }

    /**
     * Negate val using two's complement representation
     *
     * @return negation of this
     */
    public HeftyInteger negate() {
        byte[] neg = new byte[val.length];
        int offset = 0;

        // Check to ensure we can represent negation in same length
        //  (e.g., -128 can be represented in 8 bits using two's
        //  complement, +128 requires 9)
        if (val[0] == (byte) 0x80) { // 0x80 is 10000000
            boolean needs_ex = true;
            for (int i = 1; i < val.length; i++) {
                if (val[i] != (byte) 0) {
                    needs_ex = false;
                    break;
                }
            }
            // if first byte is 0x80 and all others are 0, must extend
            if (needs_ex) {
                neg = new byte[val.length + 1];
                neg[0] = (byte) 0;
                offset = 1;
            }
        }

        // flip all bits
        for (int i = 0; i < val.length; i++) {
            neg[i + offset] = (byte) ~val[i];
        }

        HeftyInteger neg_li = new HeftyInteger(neg);

        // add 1 to complete two's complement negation
        return neg_li.add(new HeftyInteger(ONE));
    }

    /**
     * Implement subtraction as simply negation and addition
     *
     * @param other HeftyInteger to subtract from this
     * @return difference of this and other
     */
    public HeftyInteger subtract(HeftyInteger other) {
        return this.add(other.negate());
    }

    /**
     * Compute the product of this and other
     *
     * @param other HeftyInteger to multiply by this
     * @return product of this and other
     */
    public HeftyInteger multiply(HeftyInteger other) {
        HeftyInteger product;
        HeftyInteger mult1 = new HeftyInteger(this.getVal());
        HeftyInteger mult2 = new HeftyInteger(other.getVal());
        
        boolean isNeg = false;
        
        if (!mult1.isNegative() || !mult2.isNegative()) {
            if (mult1.isNegative()) {
                mult1 = mult1.negate();
                isNeg = true;
                product = mult1.multiRunner(mult2);
            } else if (mult2.isNegative()) {
                mult2 = mult2.negate();
                isNeg = true;
                product = mult2.multiRunner(mult1);
            } else {
                product = mult1.multiRunner(mult2);
            }
        } else {
            mult1 = mult1.negate();
            mult2 = mult2.negate();
            product = mult1.multiRunner(mult2);
        }
        
        if (isNeg) {
            return product.negate();
        }
        
        return product;
    }
    
    public HeftyInteger multiRunner(HeftyInteger other) {
        byte[] bytes = new byte[this.length() + other.length()];
        HeftyInteger update = new HeftyInteger(bytes);
        int k = 0;
        int count = 1;
        int i = other.length() - 1;
        
        while (i >= 0) {
            for (int j = this.length() - 1; j >= 0; j--) {
                HeftyInteger addVal = multiByte(this.getVal()[j], other.getVal()[i]);
                addVal.updateVal(k++);
                update = update.add(addVal);
            }
            k = count++;
            i--;
        }
        
        return update.reverse();
    }
    
    public HeftyInteger multiByte(byte one, byte two) {
        byte[] result = new byte[3];
        result[2] = (byte) ((((int) one & 0xFF) * ((int) two & 0xFF)));
        result[1] = (byte) ((((int) one & 0xFF) * ((int) two & 0xFF) >> 8));
        
        return new HeftyInteger(result);
    }

    /**
     * Run the extended Euclidean algorithm on this and other
     *
     * @param other another HeftyInteger
     * @return an array structured as follows:
     * 0:  the GCD of this and other
     * 1:  a valid x value
     * 2:  a valid y value
     * such that this * x + other * y == GCD in index 0
     */
    public HeftyInteger[] XGCD(HeftyInteger other) {

        HeftyInteger otherHeftyVal = new HeftyInteger(this.getVal());
        
        boolean isReserved = false;
        
        if (other.greaterOrLess(otherHeftyVal)) {
            otherHeftyVal = other;
            other = new HeftyInteger(this.getVal());
            isReserved = true;
        }
        
        HeftyInteger[] gcdHeftyVal = this.runXGCD(otherHeftyVal, other);
        
        if (!isReserved) {
            return gcdHeftyVal;
        }
        
        return swap(gcdHeftyVal, otherHeftyVal);
    }
    
    public HeftyInteger[] runXGCD(HeftyInteger xgcdComp, HeftyInteger other) {
        if (other.equals(new HeftyInteger(new byte[1]))) {
            HeftyInteger heftyIntegerByte = new HeftyInteger(new byte[1]);
            HeftyInteger heftyIntegerTem = new HeftyInteger(ONE);
            return new HeftyInteger[]{xgcdComp, heftyIntegerTem, heftyIntegerByte};
        }

        HeftyInteger[] heftyIntegers = runXGCD(other, xgcdComp.divide(other)[1]);
        HeftyInteger gcdCalc = heftyIntegers[0];
        HeftyInteger mult1 = heftyIntegers[2];
        HeftyInteger mult2 = xgcdComp.divide(other)[0];
        mult2 = mult2.multiply(mult1);
        mult2 = heftyIntegers[1].subtract(mult2);
        
        return new HeftyInteger[]{gcdCalc, mult1, mult2};
    }
    

    public HeftyInteger getGCD(int checkMid) {
        HeftyInteger update = new HeftyInteger(new byte[4]);
        update.getVal()[0] = (byte) (checkMid >> (8 * 3));
        update.getVal()[1] = (byte) (checkMid >> (8 * 2));
        update.getVal()[2] = (byte) (checkMid >> (8));
        update.getVal()[3] = (byte) (checkMid);
        
        return update;
    }

    public void updateVal(int k) {
        byte[] bytes = new byte[this.length() + k];
        IntStream.range(0, this.length()).forEach(i -> bytes[i] = this.getVal()[i]);
        val = bytes;
    }

    public HeftyInteger heftyBuilder(int k) {
        byte[] bytes = new byte[this.length() + k];
        IntStream.range(0, this.length()).forEach(i -> bytes[i] = this.getVal()[i]);
        
        return new HeftyInteger(bytes);
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        
        if (other instanceof HeftyInteger) {
            HeftyInteger otherUpate = (HeftyInteger) other;
            return !this.subtract(otherUpate).isNegative() && this.subtract(otherUpate).subtract(new HeftyInteger(ONE)).isNegative();
        }
        
        return false;
    }


    private HeftyInteger[] swap(HeftyInteger[] gcdHeftyVal, HeftyInteger otherHeftyVal) {
        otherHeftyVal = gcdHeftyVal[1];
        gcdHeftyVal[1] = gcdHeftyVal[2];
        gcdHeftyVal[2] = otherHeftyVal;
        
        return gcdHeftyVal;
    }

    boolean greaterOrLess(HeftyInteger other) {
        return !this.subtract(other).isNegative();
    }

    public HeftyInteger reverse() {
        byte[] valTemp = this.getVal();
        if (valTemp.length < 3) {
            return this;
        }
        
        byte[] updateVal;
        int i = 0;
        
        while ((!((valTemp[i] & 0xFF) != 0xFF || !this.isNegative()) || valTemp[i] == 0x00 && !this.isNegative()) && i < valTemp.length - 1) {
            i++;
        }
        
        if (i != 0) {
            i--;
        }
        
        if (i == valTemp.length) {
            return new HeftyInteger(new byte[2]);
        }
        
        updateVal = new byte[valTemp.length - i];
        if (valTemp.length - i >= 0) {
            System.arraycopy(valTemp, i, updateVal, 0, valTemp.length - i);
            return new HeftyInteger(updateVal);
        } else {
            return new HeftyInteger(updateVal);
        }
    }

    public HeftyInteger[] divide(HeftyInteger other) {
        HeftyInteger resultVal = new HeftyInteger(new byte[this.length()]);
        HeftyInteger div1 = new HeftyInteger(this.getVal());
        HeftyInteger div2 = new HeftyInteger(other.getVal());
        HeftyInteger[] heftyIntegers = new HeftyInteger[2];
        
        int i = 0;
        
        if (other.greaterOrLess(this)) {
            return div2.divide(div1);
        }
        
        while (div1.greaterOrLess(div2)) {
            HeftyInteger divideValue = new HeftyInteger(new byte[this.length()]);
            while (div1.greaterOrLess(div2.heftyBuilder(i))) {
                i++;
            }
            div2 = div2.heftyBuilder(--i);
            HeftyInteger loc = this.divRunner(div1, div2);
            divideValue = divideValue.add(loc.heftyBuilder(i));
            HeftyInteger multiply = divideValue.multiply(other);
            div1 = div1.subtract(multiply);
            i = 0;
            div2 = other;
            resultVal = resultVal.add(divideValue);
        }
        
        heftyIntegers[0] = resultVal.reverse();
        heftyIntegers[1] = div1.reverse();
        return heftyIntegers;
    }
    
    public HeftyInteger divRunner(HeftyInteger thisCopy, HeftyInteger otherCopy) {
        int leftIdx = 0;
        int rightIdx = 255;
        int mid = 255 / 2;
        
        HeftyInteger convert = getGCD(mid);
        convert = convert.multiply(otherCopy);
        
        if (!((convert.add(otherCopy)).greaterOrLess(thisCopy) && thisCopy.greaterOrLess(convert))) {
            while (!((convert.add(otherCopy)).greaterOrLess(thisCopy) && thisCopy.greaterOrLess(convert))) {
                if (convert.greaterOrLess(thisCopy)) {
                    rightIdx = mid;
                } else {
                    leftIdx = mid;
                }
                mid = (rightIdx + leftIdx) / 2;
                convert = getGCD(mid);
                convert = convert.multiply(otherCopy);
            } 
        }
        
        return getGCD(mid);
    }

}

