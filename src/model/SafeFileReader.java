package model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Stream;

public class SafeFileReader {

	public static BufferedReader createLenientBufferedReader(Path path) throws IOException {
		return createLenientBufferedReader(path, StandardCharsets.UTF_8);
	}

	public static BufferedReader createLenientBufferedReader(Path path, Charset cs) throws IOException {
		CharsetDecoder decoder = cs.newDecoder().onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
		Reader reader = new InputStreamReader(newInputStream(path), decoder);
		return new BufferedReader(reader);
	}

	public static Stream<String> lines(Path path) throws IOException {
		return lines(path, StandardCharsets.UTF_8);
	}

	public static Stream<String> lines(Path path, Charset cs) throws IOException {
		BufferedReader br = createLenientBufferedReader(path, cs);

		try {
			return br.lines().onClose(asUncheckedRunnable(br));
		} catch (Error | RuntimeException e) {
			try {
				br.close();
			} catch (IOException ex) {
				try {
					e.addSuppressed(ex);
				} catch (Throwable ignore) {
				}
			}
			throw e;
		}
	}

	private static Runnable asUncheckedRunnable(Closeable c) {
		return () -> {
			try {
				c.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	private static FileSystemProvider provider(Path path) {
		return path.getFileSystem().provider();
	}
	
	 public static InputStream newInputStream(Path path, OpenOption... options)
		        throws IOException
		    {
		        return provider(path).newInputStream(path, options);
		    }
}
