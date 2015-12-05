package elgamal;

import java.math.BigInteger;

import global.BigIntegerMod;
import global.Consts;

public class TesterElGamal {

	private static final int NUM_OF_CHECKS = 20;
	
	public static void main(String[] args) {
		BigInteger P;
		BigInteger Q;
		BigIntegerMod G;

		P = Consts.getP();
		Q = Consts.getQ();
		G = Consts.getG();
		
		
		System.out.println("P = " + P.toString());
		System.out.println("Q = " + Q.toString());
		System.out.println("G = " + G.toString());
		System.out.println("G^Q = " + G.pow(Q).toString());
		System.out.println("G^(p-1) = " + G.pow(P.subtract(BigInteger.ONE)).toString());
		ElGamal ElGamal = new ElGamal(P, G, new BigIntegerMod(P), null);
		ElGamal = new ElGamal(P, G, null, new BigIntegerMod(Q));
		boolean equal = false;
		BigIntegerMod m;
		CryptObject encrypted;
		CryptObject reencrypted;
		BigIntegerMod decrypted_m;
		for (int i=0; i<NUM_OF_CHECKS; ++i) {
			m = new BigIntegerMod(P);
			System.out.println("\ncheck number " + (i+1) + ":");
			System.out.println("Original message = " + m.toString());
			encrypted = ElGamal.encrypt(m);
			
			// checking decryption using r
			decrypted_m = encrypted.getCiphertext().getB().multiply(ElGamal.getPublicKey().pow(encrypted.getR()).inverse());
			System.out.println("Direct decryption = " + decrypted_m.toString());
			equal = decrypted_m.equals(m);
			System.out.println("Decryption went OK: " + equal);
			if (!equal) return;
			
			// checking the decryption method
			decrypted_m = ElGamal.decrypt(encrypted.getCiphertext());
			System.out.println("Regular decryption = " + decrypted_m.toString());
			equal = decrypted_m.equals(m);
			System.out.println("Decryption went OK: " + equal);
			if (!equal) return;
			
			// checking the re-encryption
			reencrypted = ElGamal.reencrypt(encrypted.getCiphertext());
			equal = reencrypted.getCiphertext().getA().equals(encrypted.getCiphertext().getA().multiply(G.pow(reencrypted.getR())));
			System.out.println("The first value of reencrypted cipher is OK: " + equal);
			if (!equal) return;
			equal = reencrypted.getCiphertext().getB().equals(encrypted.getCiphertext().getB().multiply(ElGamal.getPublicKey().pow(reencrypted.getR())));
			System.out.println("The second value of reencrypted cipher is OK: " + equal);
			if (!equal) return;
			// checking the decryption using r1,r2
			decrypted_m = reencrypted.getCiphertext().getB().multiply(ElGamal.getPublicKey().pow(encrypted.getR().add(reencrypted.getR())).inverse());
			System.out.println("Direct decryption of re-encryption = " + decrypted_m.toString());
			equal = decrypted_m.equals(m);
			System.out.println("Decryption went OK: " + equal);
			if (!equal) return;
			// checking the decryption method on re-encrypted cipher
			decrypted_m = ElGamal.decrypt(reencrypted.getCiphertext());
			System.out.println("Regular decryption of re-encryption = " + decrypted_m.toString());
			equal = decrypted_m.equals(m);
			System.out.println("Decryption went OK: " + equal);
			if (!equal) return;
		}
	}

}
