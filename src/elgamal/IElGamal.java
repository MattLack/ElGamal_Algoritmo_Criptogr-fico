package elgamal;

import global.BigIntegerMod;

public interface IElGamal {

	/**
	 * The implementing class will be elgamal.ElGamal 
	 * The following 2 will be the constructors of elgamal.ElGamal

	public ElGamal(BigIntegerMod publicKey);
	public ElGamal(BigIntegerMod publicKey, BigIntegerMod privateKey);

	 * Note that the following expressions should be true:
	 * 		publicKey.getMod()==Consts.getQ()
	 * 		privateKey.getMod()==Consts.getQ()
	 */


	/**
	 * Encrypts the given message with a randomly selected r
	 * @param message - the message to encrypt, where message.getMod()==P
	 * @return a CryptObject representing the encrypted message, along with its r and zkp
	 */
	public CryptObject encrypt(BigIntegerMod message); // with a random r

	/**
	 * Encrypts the given message with the given r
	 * @param message - the message to encrypt, where message.getMod()==Consts.getP()
	 * @param r - the random number used for encryption, where r.getMod()==Consts.getQ()
	 * @return a CryptObject representing the encrypted message, along with its r and zkp
	 */
	public CryptObject encrypt(BigIntegerMod message, BigIntegerMod r);

	/**
	 * Decrypts the given ciphertext using the private key
	 * @param ciphertext - the ciphertext to decrypt
	 * @return the decrypted message
	 */
	public BigIntegerMod decrypt(Ciphertext ciphertext);

	/**
	 * Re-encrypts the given ciphertext using a random r
	 * @param ciphertext - the ciphertext to re-encrypt
	 * @return a CryptObject representing the re-encrypted ciphertext, along with its r and zkp
	 */
	public CryptObject reencrypt(Ciphertext ciphertext);

	/**
	 * Re-encrypts the given ciphertext using the given r
	 * @param ciphertext - the ciphertext to re-encrypt
	 * @param r - the random number used for encryption, where r.getMod()==Consts.getQ()
	 * @return a CryptObject representing the re-encrypted ciphertext, along with its r and zkp
	 */
	public CryptObject reencrypt(Ciphertext ciphertext, BigIntegerMod r);

	/**
	 * @return the private key of this object, where (private key).getMod()==Consts.getQ()
	 */
	public BigIntegerMod getPrivateKey();

	/**
	 * @return the public key of this object, where (public key).getMod()==Consts.getQ()
	 */
	public BigIntegerMod getPublicKey();

}
