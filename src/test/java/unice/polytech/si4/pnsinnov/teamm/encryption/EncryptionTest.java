package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class EncryptionTest
 *
 * @author Joël CANCELA VAZ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EncryptionTest {

	private SecretKey secretKey;
	private FileEncryption fileEncryption;
	private FileDecryption fileDecryption;
	private ClassLoader classLoader;
	private File fileCrypted;
	private File fileUncrypted;

	@BeforeEach
	void setUp() throws NoSuchAlgorithmException {
		secretKey = KeyGenerator.getInstance(KeyGeneration.CIPHER_ALGO).generateKey();
		fileEncryption = new FileEncryption();
		fileDecryption = new FileDecryption();
		classLoader = getClass().getClassLoader();
		// necessary inits in some cases
		fileCrypted = new File("");
		fileUncrypted = new File("");
	}

	@Test
	void testGenerateKeys() throws NoSuchAlgorithmException {//FIXME: KeyGeneration should become not static
		List<String> keysList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			SecretKey secretKey = KeyGenerator.getInstance(KeyGeneration.CIPHER_ALGO).generateKey();
			keysList.add(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
		}
		Set<String> keysString = new HashSet<>(keysList);
		assertEquals(keysList.size(), 10);
		assertEquals(keysList.size(), keysString.size());
	}

	@Test
	void testEncipherAndDecipherFile() throws BadPaddingException, NoSuchAlgorithmException, IOException,
			IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
		URL tocryptFile = classLoader.getResource("tocrypt_clear.txt");
		File fileClear = new File(tocryptFile.getFile());
		String absolutePath = fileClear.getAbsolutePath();
		String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		fileCrypted = fileEncryption.encryptFile(fileClear, secretKey);
		fileUncrypted = fileDecryption.decryptFile(fileCrypted, secretKey, Paths.get(path));
		assertTrue(FileUtils.contentEquals(fileClear, fileUncrypted));
	}

	@Test
	void getNewNameTest(){
		String filename = "foo.txt";
		assertEquals(filename, fileEncryption.getNewName(fileEncryption.getNewName(filename, true), false));
		assertEquals("foo-crypted.txt", fileEncryption.getNewName(filename, true));
//		assertEquals("foo.txt", fileEncryption.getNewName(filename, false));
		String filename2 = "foo.bar.txt";
		assertEquals(filename2, fileEncryption.getNewName(fileEncryption.getNewName(filename2, true), false));
		assertEquals("foo.bar-crypted.txt", fileEncryption.getNewName(filename2, true));
//		assertEquals("foo.bar.txt", fileEncryption.getNewName(filename2, false));
		String filename3 = ".bar";
		assertEquals(filename3, fileEncryption.getNewName(fileEncryption.getNewName(filename3, true), false));
		assertEquals(".bar-crypted", fileEncryption.getNewName(filename3, true));
//		assertEquals(".bar", fileEncryption.getNewName(filename3, false));
		String filename4 = "foo";
		assertEquals(filename4, fileEncryption.getNewName(fileEncryption.getNewName(filename4, true), false));
		assertEquals("foo-crypted", fileEncryption.getNewName(filename4, true));
//		assertEquals("foo", fileEncryption.getNewName(filename4, false));
	}

	@AfterAll
	void tearDown() {
		fileCrypted.delete();
		fileUncrypted.delete();
	}
}
