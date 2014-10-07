package mds.test.web;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dongsong
 */
public class ManifestManager {
    private static ManifestManager instance = new ManifestManager();
    private int length=1024;
    private boolean compress = true;
    private boolean defaultCompress = true;
    private static AtomicReference<Object> manifestCache = new AtomicReference<>();

    public static ManifestManager instance() {
        return instance;
    }

    public synchronized void set(int length, boolean compress, boolean defaultCompress) {
        this.length = length;
        this.compress = compress;
        this.defaultCompress = defaultCompress;
        this.update();
    }

    private String update() {
        String manifest = this.generateManifest(this.length);
        this.cacheManifest(manifest);
        return manifest;
    }

    public String getManifest() {
        Object compressedManifest = manifestCache.get();
        if (compressedManifest == null) {
            return this.update();
        }
        if (compressedManifest instanceof String)
           return (String)compressedManifest;
        return new String(gzipDecompress((byte[])compressedManifest));
    }

    private void cacheManifest(String manifest) {
        if (this.compress) {
            byte[] compressedManifest;
            if (this.defaultCompress) {
                compressedManifest = gzipCompressByDefault(manifest.getBytes());

            } else {
                compressedManifest = gzipCompressByCustom(manifest.getBytes());
            }
            manifestCache.set(compressedManifest);
        } else {
            manifestCache.set(manifest);
        }
    }

    private String generateManifest(int length) {
        return StringUtils.repeat("#", length);
    }

    private static byte[] gzipCompressByCustom(byte[] in) {
        if (in == null) {
            throw new NullPointerException("Can't compress null");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        MyGZIPOutputStream gz = null;
        try {
            gz = new MyGZIPOutputStream(bos);
            gz.write(in);
        } catch (IOException e) {
            throw new RuntimeException("IO exception compressing data", e);
        } finally {
            if (gz != null) {
                try {
                    gz.close();
                } catch (IOException e) {

                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {

                }
            }
        }
        byte[] rv = bos.toByteArray();
        // log.debug("Compressed %d bytes to %d", in.length, rv.length);
        return rv;
    }

    private static byte[] gzipCompressByDefault(byte[] in) {
        if (in == null) {
            throw new NullPointerException("Can't compress null");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        GZIPOutputStream gz = null;
        try {
            gz = new GZIPOutputStream(bos);
            gz.write(in);
        } catch (IOException e) {
            throw new RuntimeException("IO exception compressing data", e);
        } finally {
            if (gz != null) {
                try {
                    gz.close();
                } catch (IOException e) {

                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {

                }
            }
        }
        byte[] rv = bos.toByteArray();
        // log.debug("Compressed %d bytes to %d", in.length, rv.length);
        return rv;
    }

    private byte[] gzipDecompress(byte[] in) {
        ByteArrayOutputStream bos = null;
        if (in != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(in);
            bos = new ByteArrayOutputStream();
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(bis);

                byte[] buf = new byte[16 * 1024];
                int r = -1;
                while ((r = gis.read(buf)) > 0) {
                    bos.write(buf, 0, r);
                }
            } catch (IOException e) {
                bos = null;
            } finally {
                if (gis != null) {
                    try {
                        gis.close();
                    } catch (IOException e) {

                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
        return bos == null ? null : bos.toByteArray();
    }
}
