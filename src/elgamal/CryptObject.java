package elgamal;

import global.BigIntegerMod;

public class CryptObject {

	private BigIntegerMod text;
	private Ciphertext ciphertext;
	private BigIntegerMod r;

	public CryptObject() {
		this.text = null;
		this.ciphertext = null;
		this.r = null;
	}

	public CryptObject(BigIntegerMod text, Ciphertext ciphertext, BigIntegerMod r) {
		this.text = text;
		this.ciphertext = ciphertext;
		this.r = r;
	}

	public BigIntegerMod getText() {
		return text;
	}

	public Ciphertext getCiphertext() {
		return ciphertext;
	}

	public BigIntegerMod getR() {
		return r;
	}
	
	public String toString() {
		return "text: " + text + " ciphertext: " + ciphertext + " r: " + r;
	}

	public void setText(BigIntegerMod text) {
		this.text = text;
	}

	public void setCiphertext(Ciphertext ciphertext) {
		this.ciphertext = ciphertext;
	}

	public void setR(BigIntegerMod r) {
		this.r = r;
	}

}
