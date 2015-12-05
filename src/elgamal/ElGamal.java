package elgamal;

import java.math.BigInteger;

import global.BigIntegerMod;
import global.Consts;
import global.Consts.DebugOutput;

public class ElGamal implements IElGamal {

	private BigInteger p;
	private BigInteger q;
	private BigIntegerMod g;
	private BigIntegerMod ONE;
	private BigIntegerMod privateKey;
	private BigIntegerMod publicKey;

	public ElGamal(BigIntegerMod publicKey) {
		AssignAndCheckValues(Consts.getP(), Consts.getG(), publicKey, null);
	}

	public ElGamal(BigIntegerMod publicKey, BigIntegerMod privateKey) {
		AssignAndCheckValues(Consts.getP(), Consts.getG(), publicKey, privateKey);
	}

	public ElGamal(BigInteger P, BigIntegerMod G, BigIntegerMod publicKey, BigIntegerMod privateKey) {
		AssignAndCheckValues(P, G, publicKey, privateKey);
	}
	
	private void AssignAndCheckValues(BigInteger P, BigIntegerMod G, BigIntegerMod publicKey, BigIntegerMod privateKey) {
		SetPAndG(P, G);
		if (publicKey == null) {
			publicKey = G.pow(privateKey);
		}
		if (!publicKey.getMod().equals(P)) {
			Consts.log("Creating an ElGamal object where publicKey.getMod()!= P", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		if ((privateKey != null) && (!privateKey.getMod().equals(q))) {
			Consts.log("Creating an ElGamal object where privateKey.getMod()!= Q", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		if ((privateKey != null) && (!G.pow(privateKey).equals(publicKey))) {
			Consts.log("Creating an ElGamal object where G.pow(privateKey) != publicKey", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public void SetPAndG(BigInteger P, BigIntegerMod G) {
		this.p = P;
		this.q = P.subtract(BigInteger.ONE).divide(Consts.TWO);
		this.ONE = new BigIntegerMod(BigInteger.ONE, P);
		if (!(G.getMod().equals(P))) {
			Consts.log("Trying to assign G where G.getMod() != P", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		if (!G.pow(q).getValue().equals(BigInteger.ONE)) {
			Consts.log("Trying to assign G where G is not of order Q", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		this.g = G;
	}
	
	public void SetPublicKey(BigIntegerMod publicKey) {
		if (!publicKey.getMod().equals(p)) {
			Consts.log("Trying to assign public key where publicKey.getMod()!=P", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		privateKey = null;
		this.publicKey = publicKey;
	}
	
	public void SetPrivateAndPublicKeys(BigIntegerMod privateKey) {
		if (!privateKey.getMod().equals(q)) {
			Consts.log("Trying to assign private key where privateKey.getMod()!=Q", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
		}
		this.privateKey = privateKey; 
		this.publicKey = g.pow(privateKey);
	}
	
	public BigIntegerMod decrypt(Ciphertext c) {
		if (privateKey == null) {
			Consts.log("Trying to decrypt a ciphertext when the private key is null", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
			return null;
		}
		BigIntegerMod PadInverse = c.getA().pow(privateKey).inverse();
		return c.getB().multiply(PadInverse);
	}

	public CryptObject encrypt(BigIntegerMod message) {
		return encrypt(message, new BigIntegerMod(q));
	}

	public CryptObject encrypt(BigIntegerMod message, BigIntegerMod r) {
		if (!message.getMod().equals(p)) {
			Consts.log("Trying to encrypt a message where message.getMod()!=P", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
			return null;
		}
		if (!r.getMod().equals(q)) {
			Consts.log("Trying to encrypt a message where r.getMod()!=Q", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
			return null;
		}
		Ciphertext C = new Ciphertext(g.pow(r), publicKey.pow(r).multiply(message));
		return new CryptObject(message, C, r);
	}

	public CryptObject reencrypt(Ciphertext ciphertext) {
		return reencrypt(ciphertext, new BigIntegerMod(q));
	}

	public CryptObject reencrypt(Ciphertext c, BigIntegerMod r) {
		if (!r.getMod().equals(q)) {
			Consts.log("Trying to reencrypt a message where r.getMod()!=Q", DebugOutput.STDERR);
			Consts.log((new Exception()).getStackTrace().toString(), DebugOutput.STDERR);
			return null;
		}
		CryptObject result = encrypt(ONE, r);
		result.setCiphertext(result.getCiphertext().multiply(c));
		return result;
	}

	public BigIntegerMod getPrivateKey() {
		return privateKey;
	}

	public BigIntegerMod getPublicKey() {
		return publicKey;
	}

}
