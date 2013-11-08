package gdx.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Provides utility methods to copy streams */
public class StreamUtils {
        public static final byte[] EMPTY_BYTES = new byte[0];

        /** Copy the data from an {@link InputStream} to an {@link OutputStream}.
         * @throws IOException */
        public static void copyStream (InputStream input, OutputStream output) throws IOException {
                copyStream(input, output, 8192);
        }

        /** Copy the data from an {@link InputStream} to an {@link OutputStream}.
         * @throws IOException */
        public static void copyStream (InputStream input, OutputStream output, int bufferSize) throws IOException {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                }
        }
        
        /** Close and ignore all errors. */
        public static void closeQuietly (Closeable c) {
                if (c != null) try {
                        c.close();
                } catch (IOException e) {
                        // ignore
                }
        }
}