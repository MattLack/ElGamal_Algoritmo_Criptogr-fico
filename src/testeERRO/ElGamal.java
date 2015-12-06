package testeERRO;
/*
 * ElGamal.java
 * part of the TUDvote electronic voting prototype client/server software
 * $Id: ElGamal.java 113 2006-03-11 20:21:12Z klink $
 * (c) 2006 Alexander Klink.
 * Released under the MIT license.
 */
//package de.tud.cdc.tudvote.common;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.security.Security;
import java.security.MessageDigest;
//import de.flexiprovider.core.FlexiCoreProvider;

/**
 * This class implements various static functions for the ElGamal
 * cryptosystem, such as encryption and signing.
 */
public final class ElGamal {
    /**
     * As this is a utility class, it should not be instantiable.
     */
    private ElGamal() {
    }

    /**
     * ElGamal-encrypt a message using a public key.
     * @param pubKey the public key to use for encryption
     * @param M the group element to encrypt
     * @return an ElGamal-encrypted message
    */
    public static ElGamalEncryptedMessage encrypt(final ElGamalPublicKey pubKey,
                                                  final Group.GroupElement M) {
        Group G = pubKey.getGroup();
        Random rand = new SecureRandom(); /* random source */
        // r  is a new random number with the same bitlength as the order of G
        BigInteger r = new BigInteger(G.getOrder().bitLength(), rand);
        r = r.mod(G.getOrder()); // r = r mod p-1, cf. Smith, p. 11
        Group.GroupElement z = G.power(pubKey.getG(), r);   // z = g^r
        Group.GroupElement c = G.power(pubKey.getH(), r);   // c = h^r
        c.multiplyWith(M);                                  // c = M*h^r now
        return new ElGamalEncryptedMessage(z, c);
    }

    /** Creates a SHA256 hash of a string.
     * @param s the string to hash
     * @return a SHA256 hash of the string, represented as a BigInteger
    */
    private static BigInteger hash(final String s) {
        MessageDigest md = null;
        byte[] output = null;
        BigInteger result = null;

        Security.addProvider(new FlexiCoreProvider());
        try {
            md = MessageDigest.getInstance("SHA256", "FlexiCore");
            md.reset();
            md.update(s.getBytes());
            result = new BigInteger(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Hash a message M using SHA256.
     * @param m the group element to hash
     * @return a hash of the message in form of a BigInteger
    */
    static BigInteger hash(final Group.GroupElement m) {
        Security.addProvider(new FlexiCoreProvider());
        byte[] output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256", "FlexiCore");
            md.reset();
            md.update(m.toFormattedString().getBytes());
            output = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigInteger(output);
    }

    /** ElGamal-re-encrypt an already encrypted message.
     * Re-encryption is actually just encrypting 1 and multiplying
     * the original message with it.
     * @param m the encrypted message
     * @param pubKey the public key used for re-encryption
     * @return a re-encryption of the message
    */
    public static ElGamalEncryptedMessage reEncrypt(
            final ElGamalEncryptedMessage m,
            final ElGamalPublicKey pubKey) {
        Group.GroupElement z = m.getZ();
        ZModZP G = (ZModZP) z.getGroup();
        ZModZP.ZModZPElement one = G.new ZModZPElement(1);
        ElGamalEncryptedMessage oneEnc = encrypt(pubKey, one);
        ElGamalEncryptedMessage mCopy = (ElGamalEncryptedMessage) m.clone();
        mCopy.multiplyWith(oneEnc);
        return mCopy;
    }

    /** ElGamal sign a string.
     * This is done by creating the SHA256 hash and then signing the
     * group element represented by this hash.
     * @param message the message to sign
     * @param privKey the private key to sign the message with
     * @param pubKey the corresponding public key
     * @return a signature of the string
     */
    public static ElGamalSignature sign(final String message,
                                        final ElGamalPrivateKey privKey,
                                        final ElGamalPublicKey pubKey) {
        BigInteger messageHash = hash(message);
        ZModZP G = (ZModZP) pubKey.getGroup();
        /* create new GroupElement from the messageHash */
        ZModZP.ZModZPElement elt = G.new ZModZPElement(messageHash);
        return sign(privKey, pubKey, elt);
    }

    /** ElGamal-sign a message M. This is the method that actually
     * implements the generation of the signature.
     * @param privKey the private key used for signing
     * @param pubKey the corresponding public key
     * @param M the group element to sign
     * @return a signature of the message
    */
    static ElGamalSignature sign(final ElGamalPrivateKey privKey,
                                 final ElGamalPublicKey pubKey,
                                 final Group.GroupElement M) {
        /* create a new group with the same order as the public key group */
        ZModZP G = new ZModZP(pubKey.getGroup().getOrder());
        /* create a new Z/Z(p-1) group */
        ZModZP H = new ZModZP(pubKey.getGroup().getOrderMinusOne());
        Group.GroupElement g = pubKey.getG();
        /* x is the private key in Z/Z(p-1) */
        ZModZP.ZModZPElement x = H.new ZModZPElement(privKey.getKey());
        /* get random generator in Z/Z(p-1) */
        ZModZP.ZModZPElement k = (ZModZP.ZModZPElement) H.getRandomGenerator();
        Group.GroupElement r = G.power(g, k.getValue()); // r = g^k mod p
        ZModZP.ZModZPElement s = H.new ZModZPElement();
        s.setValue(k.getValue());                        // s = k (in Z/Z(p-1))
        s = (ZModZP.ZModZPElement) s.getInverse();       // s = k^-1
        /* hashvalue is the hash of M in Z/Z(p-1) */
        ZModZP.ZModZPElement hashvalue = H.new ZModZPElement(hash(M));
        /* xr = x*r */
        ZModZP.ZModZPElement xr = (ZModZP.ZModZPElement) H.multiply(x, r);
        xr.negate();                                    // xr = -x*r
        xr.addTo(hashvalue);                            // xr = H(M) - xr
        s.multiplyWith(xr);                             // s = (H(M) - xr)*k^-1
        return new ElGamalSignature(r, s);
    }

    /**
     * Check whether a given ElGamalSignature is a valid signature for
     * a message string, i.e. if it is a correct signature on the hash
     * of the string.
     * @param message the message that is supposed to be signed
     * @param sig the signature on the message
     * @param pubKey the public key to check the signature with
     * @return a boolean indicating whether the signature is valid or not
     */
    public static boolean isValidSignature(final String message,
                                           final ElGamalSignature sig,
                                           final ElGamalPublicKey pubKey) {
        BigInteger messageHash = hash(message);
        ZModZP G = (ZModZP) pubKey.getGroup();
        /* create new GroupElement from the messageHash */
        ZModZP.ZModZPElement elt = G.new ZModZPElement(messageHash);
        return isValidSignature(elt, sig, pubKey);
    }

    /**
     * Check whether a given ElGamalSignature is a valid signature for
     * a message M.
     * @param M the message corresponding to the signature
     * @param sig the ElGamal signature
     * @param pubKey the public key corresponding to the signer's private key
     * @return true if the signature is valid, false otherwise
    */
    public static boolean isValidSignature(final Group.GroupElement M,
                                           final ElGamalSignature sig,
                                           final ElGamalPublicKey pubKey) {
        Group G = pubKey.getGroup(); // this is the group we are working in
        /* create a new Z/Z(p-1) group of order one less */
        ZModZP H = new ZModZP(pubKey.getGroup().getOrderMinusOne());
        Group.GroupElement g = pubKey.getG();
        Group.GroupElement y = pubKey.getH();
        ZModZP.ZModZPElement r = sig.getR();
        ZModZP.ZModZPElement s = sig.getS();
        /* hashvalue is the hash of M in Z/Z(p-1) */
        ZModZP.ZModZPElement hashvalue = H.new ZModZPElement(hash(M));
        /* left hand side: g^(H(m)) */
        Group.GroupElement lefthand = G.power(g, hashvalue.getValue());
        /* right hand side: y^r*r^s */
        Group.GroupElement righthand = G.multiply(G.power(y, r.getValue()),
                                                  G.power(r, s.getValue()));
        /* the signature is valid if the left and right hand side are equal */
        return lefthand.equals(righthand);
    }

    /** Decrypt a given ElGamal encrypted message using a private key.
     * @param privKey the private key used for decryption
     * @param pubKey the corresponding public key
     * @param mEncrypted the encrypted message to be decrypted
     * @return a group element which is the decryption of the message
    */
    public static Group.GroupElement decrypt(final ElGamalPrivateKey privKey,
        final ElGamalPublicKey pubKey,
        final ElGamalEncryptedMessage mEncrypted) {
        Group G = pubKey.getGroup();
        Group.GroupElement z = mEncrypted.getZ();
        Group.GroupElement c = mEncrypted.getC();
        BigInteger k = privKey.getKey();
        Group.GroupElement M = G.power(z, k);        // M = z^k
        M = M.getInverse();                          // M = (z^k)^-1
        M.multiplyWith(c);                           // M = (z^k)^-1 *c
        return M;
    }
}