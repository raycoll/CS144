import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import javax.xml.bind.DatatypeConverter;


public class ComputeSHA {
	static final int BUF_SIZ = 1024;

	public static void main(String[] args) {

		/* Open input file for reading */
		FileInputStream f = null;
		try {
			f = new FileInputStream(args[0]);
		} catch(Exception e) {
			System.err.println("Failed to open file! " + e.toString());
			System.exit(1);
		}
	
		/* Create the SHA-1 MessageDigest instance */
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException n) {
			System.out.println("Message digest algorithm not supported!");
			System.exit(1);
		}

		/* Compute the digest for the input */ 
		try {
			byte[] buf = new byte[BUF_SIZ];
			while(true) {
				int amt = f.read(buf);
				if (amt < 0) {
					break;
				}
				
				md.update(buf,0,amt); // add buffer to current digest
				
				if (amt < BUF_SIZ) {
					break;
				}
			}
		}
		catch(IOException e) {
			System.out.println("Error while reading! " + e.toString());
			System.exit(1);
		}

		/* Output the generated digest */
		byte[] digestBytes = md.digest();
		System.out.print(DatatypeConverter.printHexBinary(digestBytes).toLowerCase());	
	}
}

