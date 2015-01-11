import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import javax.xml.bind.DatatypeConverter;


public class ComputeSHA {
	public static void main(String[] args) {

		/* Open input file for reading */
		BufferedReader br = null;
		try {
			br  = new BufferedReader(new FileReader(args[0]));
		}
		catch(FileNotFoundException f) {
			System.out.println("Could not open file! " + f.toString());
			System.exit(1);
		}

		/* Create the SHA-1 MessageDigest instance */
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		}
		catch(NoSuchAlgorithmException n) {
			System.out.println("Message digest algorithm not supported!");
			System.exit(1);
		}

		/* Compute the digest for the input */ 
		try {
			String s = br.readLine();
			while(s != null) {
				md.update(s.getBytes());
				s = br.readLine();
			}
		}
		catch(IOException e) {
			System.out.println("Error while reading! " + e.toString());
			System.exit(1);
		}

		/* Output the generated digest */
		byte[] digestBytes = md.digest();
		System.out.println(DatatypeConverter.printHexBinary(digestBytes));	
	}
}

