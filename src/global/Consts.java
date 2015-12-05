package global;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class Consts {

	public enum DebugOutput {
		STDOUT,
		STDERR,
		FILE
	}

	private static final boolean USE_RANDOM_P = false;
	private static final boolean DISABLE_LOGGER = false;
	private static final boolean DEBUG_MODE = false;
	private static final String LOG_FILE = "c:\\Threshold_Log.txt";
	private static BufferedWriter outputFile = null;
	
	public static BufferedWriter mcOutputFile = null;
	public static final String MC_RESULTS_FILE = "c:\\formal_Log_";

	public static final int PARTIES_AMOUNT = 7;
	public static final int VOTERS_AMOUNT = 20;
	public static final int BITS_AMOUNT = 1024;
	public static final int CERTAINTY = 60;
	public static final int THRESHOLD = 5;
	public static final String THRESHOLD_CENTER_HOSTNAME = "localhost";
	public static final int THRESHOLD_CENTER_PORT = 7777;
	public static final int CONNECTION_TIMEOUT = 1000; // ms = 1sec
	public static final BigInteger TWO = BigInteger.valueOf(2);

	public static final BigInteger q = getRandomLargePrimeQ();
	public static final BigInteger p = getP(q);
	public static final BigIntegerMod G = calculateG();

	// Mix Centers IPs and ports. First item in the array is Pret a vote data.
	public static final String[] MIX_CENTERS_IP = {	"192.168.2.180", //Pret a vote
													"192.168.2.180", //Mix Center 1
													"192.168.2.180", //Mix Center 2
													"192.168.2.107", //Mix Center 3
													"192.168.2.180", //Mix Center 4
													"192.168.2.103", //Mix Center 5
													"192.168.2.107", //Mix Center 6
													"192.168.2.180", //Mix Center 7
													"192.168.2.103", //Mix Center 8
													"192.168.2.107", //Mix Center 9
													"127.0.0.1"};//Mix Center 10

	public static final int[] MIX_CENTERS_PORT = {	40000, //Pret a vote
													40001, //Mix Center 1
													40002, //Mix Center 2
													40003, //Mix Center 3
													40004, //Mix Center 4
													40005, //Mix Center 5
													40006, //Mix Center 6
													40007, //Mix Center 7
													40008, //Mix Center 8
													40009, //Mix Center 9
													40010};//Mix Center 10

	public static void log(String message, DebugOutput logger) {
		if (DISABLE_LOGGER) return;
		switch (logger) {
			case STDOUT : {
				if (DEBUG_MODE) {
					System.out.println(message);
				}
				break;
			}
			case STDERR : {
				if (DEBUG_MODE) {
					System.err.println(message);
				}
				break;
			}
			case FILE : {
				try {
					if (outputFile == null) {
						outputFile = new BufferedWriter(new FileWriter(LOG_FILE));
					}
					outputFile.write(message + "\r\n");
					outputFile.flush();
				} catch (IOException e) {
					System.err.println(e);
				}
				break;
			}
		}
	}

	private static BigInteger getRandomLargePrimeQ() {
		if (USE_RANDOM_P) {
			BigInteger q = null;
			BigInteger p = null;
			do {
				System.out.print(".");
				q = new BigInteger(BITS_AMOUNT, CERTAINTY, new Random());
				p = getP(q);
			} while (!Utils.isPrime(p));
			System.out.println("");
			return q;
		} else {
			return new BigInteger("102878210466625684448564152718275770164400015795599081323142484261174204395771542931556573005457548994300390542836390656590342719190546003475313704107511340660051255130886898195350488634781241593291629806029940967099127171111741121800016354471000128327690358673195988965015732141003244933469931582512668226603");
		}
	}

	private static BigInteger getP(BigInteger q) {
		// p = 2*q + 1;
		return q.multiply(TWO).add(BigInteger.ONE);
	}

	private static BigIntegerMod calculateG() {
		Random random = new Random();
		BigInteger lowerBound = TWO;
		BigInteger upperBound = p.subtract(TWO);
		BigInteger result;
		do {
			result = new BigInteger(p.bitLength(), random);
		} while ((result.compareTo(lowerBound) < 0) || (result.compareTo(upperBound) > 0));
		return new BigIntegerMod(result.modPow(TWO, p), p);
	}

	public static BigInteger getQ() {
		return q;
	}

	public static BigInteger getP() {
		return p;
	}

	public static BigIntegerMod getG() {
		return G;
	}

}
