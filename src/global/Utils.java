package global;

import java.io.IOException;
import java.math.BigInteger;

public class Utils {

	/**
	 * Runs another process according to the given command
	 * for example - command="java Server.jar"
	 * @param command - the command which runs the wanted process
	 * @return the created process, of null upon exception
	 */
	public static Process runProcess(String command) {
		try {
			return Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			// what you want
		}
		return null;
	}

	public static boolean isPrime(BigInteger n) {
		return n.isProbablePrime(Consts.CERTAINTY);
	}

}
