package kr.co.kbiid.license;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import kr.co.kbiid.license.bouncycastle.BouncyCastle;
import kr.co.kbiid.license.util.FileUtil;
import kr.co.kbiid.license.util.HostInfoUtil;
import kr.co.kbiid.license.util.KeyUtil;

public class BouncyCastleTest {

	private static Log logger = LogFactory.getLog(BouncyCastleTest.class);

	private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCIN4f9XBXl8nybTkEUO/fk89yHBOW/wM6hLULp1qkMscJWSpFKiDLgongKJtaazFjIYi79mQhqrXM/wyib1XBBWDlS26vvOTsxbfkEAamWYY0a4hR6mQySv2//sNPfwKdcJlju1vWJh9tKOaDitXGWl0a7ALPHjL4dSVx+N2NlQIDAQAB";
	private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMIg3h/1cFeXyfJtOQRQ79+Tz3IcE5b/AzqEtQunWqQyxwlZKkUqIMuCieAom1prMWMhiLv2ZCGqtcz/DKJvVcEFYOVLbq+85OzFt+QQBqZZhjRriFHqZDJK/b/+w09/Ap1wmWO7W9YmH20o5oOK1cZaXRrsAs8eMvh1JXH43Y2VAgMBAAECgYAisf8hFRuz/APy2QSEufZ1LhS4Xz49EWzBS7CT2aBAknayPAZrACGGXdlIf9lsKpZUEk0+0sWIotsFPVxDt8RKgHFs6oGIlS805hsDGTsoKLLLCOWlDdgpdDnUeXPyt5pBvHGWbccF+9quuppMTi7G1eY5MzJ2+gmPcWLtYsL2wQJBAOaLR4mgqOPCyaFBFx2buw8SFHDHLT5HYNbE0wFiKeY39W6ZpEDvCN7Q6gCLFgDUftGlWbjviVqomT8ZhL6wpKUCQQDXkDy9UIFmuLOrcvMO//Jw4Kr9pX8Ug6T367b/xj/P5C0/tqRplgF5Nhu06bDjkb9gveThmm0z+mrcj07z1cIxAkEAiPQsZyG9sq/AP77/EGO219kRs31e7yRP7sW145iA83ZCopU68igNyO+qUvBM/ek9/SSRDD+gBO8rOkTair080QJBAKklfBPNNQzGh+1TkJfHjJS4JlRL5XDSb8M+SeVfybi2Qi9JgK99ToCvTYRjDzMyrWoa95tzUpdDu1CAfyJkLDECQCL6A486Pzn5AxdHoPXBZ57LFh+h0MOqPzBURZosXZ9UFGjTqBPNaGVOrdNxZ1nbLg5RxLHj4s4PJ3js+OaT/lw=";

	private String publicKeyPath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\public.pem";
	private String privateKeyPath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\private.pem";
	
	private License license = new License(HostInfoUtil.getHostName(), "D8-C4-97-D6-5F-92",
			LocalDate.now().plusYears(1));
	private License license_differentDate = new License(HostInfoUtil.getHostName(), "D8-C4-97-D6-5F-92",
			LocalDate.now().minusMonths(1));
	private License license_differentMacAddress = new License(HostInfoUtil.getHostName(), "D8-C4-97-D6-5F-22",
			LocalDate.now().plusYears(1));
	private License license_differentHost = new License("kbiid", "D8-C4-97-D6-5F-92", LocalDate.now().plusYears(1));

	private String licensePath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license";
	private String differentDateLicensePath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license_different_date";
	private String differentMacAddressLicensePath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license_different_mac";
	private String differentHostPath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license_different_host";

	private String modulatedLicensePath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license_modulated";
	private String overLengthLicensePath = "D:\\eclipse_workspace\\license-machine\\file\\bouncycastle\\license_over_length";

	@Test
	public void genKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPair keyPair = BouncyCastle.genKeyPair();

		logger.info("publicKey : " + KeyUtil.toStringByBase64(keyPair.getPublic().getEncoded()));
		logger.info("privateKey : " + KeyUtil.toStringByBase64(keyPair.getPrivate().getEncoded()));
	}
	
	@Test
	public void writePemFile() throws NoSuchAlgorithmException, NoSuchProviderException, FileNotFoundException, IOException {
		KeyPair keyPair = BouncyCastle.genKeyPair();
		
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		
		logger.info("publicKey : " + KeyUtil.toStringByBase64(rsaPrivateKey.getEncoded()));
		logger.info("privateKey : " + KeyUtil.toStringByBase64(rsaPublicKey.getEncoded()));
		
		BouncyCastle.writePemFile(rsaPrivateKey, "RSA PRIVATE KEY", privateKeyPath);
		BouncyCastle.writePemFile(rsaPublicKey, "RSA PUBLIC KEY", publicKeyPath);
	}

	@Test
	public void cipher() {
		try {
			byte[] encrypted = LicenseMachine.issue(license, publicKey);
			FileUtil.makeFile(licensePath, encrypted);
			byte[] licenseByte = FileUtil.readFile(licensePath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
				Assert.assertTrue("일치합니다.", result);
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void verifyOverLengthLicense() {
		try {
			byte[] licenseByte = FileUtil.readFile(overLengthLicensePath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void verifyModulatedLicense() {
		try {
			byte[] licenseByte = FileUtil.readFile(modulatedLicensePath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void differentDateLicense() {
		try {
			byte[] encrypted = LicenseMachine.issue(license_differentDate, publicKey);
			FileUtil.makeFile(differentDateLicensePath, encrypted);
			byte[] licenseByte = FileUtil.readFile(differentDateLicensePath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
				Assert.assertTrue("일치합니다.", result);
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void differentMacAddressLicense() {
		try {
			byte[] encrypted = LicenseMachine.issue(license_differentMacAddress, publicKey);
			FileUtil.makeFile(differentMacAddressLicensePath, encrypted);
			byte[] licenseByte = FileUtil.readFile(differentMacAddressLicensePath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
				Assert.assertTrue("일치합니다.", result);
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void differentHostNameLicense() {
		try {
			byte[] encrypted = LicenseMachine.issue(license_differentHost, publicKey);
			FileUtil.makeFile(differentHostPath, encrypted);
			byte[] licenseByte = FileUtil.readFile(differentHostPath);
			boolean result = LicenseMachine.verify(licenseByte, privateKey);
			if (result) {
				logger.info("일치합니다.");
				Assert.assertTrue("일치합니다.", result);
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void encryptByPrivateAndDecryptByPublic() {
		try {
			byte[] encrypted = LicenseMachine.issueByPrivate(license, privateKey);
			FileUtil.makeFile(licensePath, encrypted);
			byte[] licenseByte = FileUtil.readFile(licensePath);
			boolean result = LicenseMachine.verifyByPublic(licenseByte, publicKey);
			if (result) {
				logger.info("일치합니다.");
				Assert.assertTrue("일치합니다.", result);
			} else {
				Assert.fail("일치하지 않습니다.");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}