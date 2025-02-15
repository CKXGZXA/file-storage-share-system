package top.ckxgzxa.filestoragesharesystem.common.utils;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 赵希奥
 * @date 2023/4/24 21:41
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class IPFSUtils {

    private static final IPFS Ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");

    public static String upload(String fileName) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(fileName));

        MerkleNode addResult = Ipfs.add(file).get(0);

        return addResult.hash.toString();
    }

    public static String upload(byte[] data) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);

        MerkleNode addResult = Ipfs.add(file).get(0);

        return addResult.hash.toString();
    }

    public static byte[] download(String hash) {
        byte[] data = null;

        try {
            data = Ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void download(String hash, String destFile) {
        byte[] data = null;
        try {
            data = Ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 0) {
            File file = new File(destFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
