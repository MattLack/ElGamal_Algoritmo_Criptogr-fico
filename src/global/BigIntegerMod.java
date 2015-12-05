package global;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class BigIntegerMod implements Serializable, Comparable<BigIntegerMod> {

	private static final long serialVersionUID = 5269651515363770906L;
	private BigInteger value;
	private BigInteger mod;

	// create a random big integer mod
	public BigIntegerMod(BigInteger mod) {
		this.mod = mod;
		if (this.mod.compareTo(BigInteger.ZERO) <= 0) {
			throw new IllegalArgumentException("mod <= 0");
		}
		do {
			value = new BigInteger(this.mod.bitLength(), new Random());
		} while (value.compareTo(BigInteger.ZERO) <= 0);
		value = value.mod(mod);
	}

	// create a big integer mod using the given value
	public BigIntegerMod(BigInteger value, BigInteger mod) {
		this.mod = mod;
		if (this.mod.compareTo(BigInteger.ONE) <= 0) {
			throw new IllegalArgumentException("mod <= 0");
		}
		if (value.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("value < 0");
		}
		this.value = value.equals(BigInteger.ZERO) ? value : value.mod(mod);
	}

	public BigInteger getValue() {
		return value;
	}

	public BigInteger getMod() {
		return mod;
	}

	public String toString() {
		return value.toString() + " (mod " + mod.toString() + ")";
	}

	private void verifyMod(BigIntegerMod comparator, String op) {
		verifyMod(this, comparator, op);
	}

	private static void verifyMod(BigIntegerMod first, BigIntegerMod second, String op) {
		// verify that comparator and this have the same mod
		if (!first.mod.equals(second.mod)) {
			throw new ArithmeticException(op + " (BigIntegerMod)s with different mods");
		}
	}

	public BigIntegerMod multiply(BigIntegerMod multiplier) {
		verifyMod(multiplier, "multiply");
		return new BigIntegerMod(value.multiply(multiplier.value), mod);
	}

	public BigIntegerMod add(BigIntegerMod adder) {
		verifyMod(adder, "add");
		return new BigIntegerMod(value.add(adder.value), mod);
	}

	public BigIntegerMod substract(BigIntegerMod substracter) {
		verifyMod(substracter, "substract");
		return new BigIntegerMod(value.subtract(substracter.value).add(mod), mod);
	}

	public BigIntegerMod pow(BigInteger exponent) {
		return new BigIntegerMod(value.modPow(exponent, mod), mod);
	}

	public BigIntegerMod pow(BigIntegerMod exponent) {
		return new BigIntegerMod(value.modPow(exponent.value, mod), mod);
	}

	public BigIntegerMod inverse() {
		return new BigIntegerMod(value.modInverse(mod), mod);
	}

	public int compareTo(BigIntegerMod comparator) {
		verifyMod(comparator, "compare");
		return value.compareTo(comparator.value);
	}

	public boolean equals(BigIntegerMod other) {
		return (compareTo(other) == 0);
	}

}
