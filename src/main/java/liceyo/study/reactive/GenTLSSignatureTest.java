package liceyo.study.reactive;

import com.tls.tls_sigature.tls_sigature.*;
import static com.tls.tls_sigature.tls_sigature.*;
import static java.lang.System.out;

/**
 * GenTLSSignatureTest
 * @description 测试生成腾讯音视频签名
 * @author lichengyong
 * @date 2019/5/13 11:10
 * @version 1.0
 */
public class GenTLSSignatureTest {
    public static void main(String[] args) {
        GenTLSSignatureResult sig = genSig(1400208268, "liceyo", "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgwZDLrXOujQMN8syN\n" +
                "jKv1DsDLtmPqwunOP4W1bSk+NEmhRANCAASNvd9oImyUtPmmb4eyhstaDbaYDsFS\n" +
                "RkvKDHAcyBC+aow9FkvBvDlRJqBD63m9JuxKmrASYpZzv8Pc8DF5qOgL\n" +
                "-----END PRIVATE KEY-----\n");
        out.println(sig.urlSig);
    }
}
