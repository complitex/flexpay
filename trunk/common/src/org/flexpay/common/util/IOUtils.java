package org.flexpay.common.util;

import java.io.*;
import java.nio.channels.FileLock;
import java.net.Socket;
import java.net.ServerSocket;

public class IOUtils
{
	private static final int COPY_BUF_SIZE = 32768;

	/**
	 * Safely close {@link java.io.InputStream}
	 *
	 * @param is InputStream to close
	 */
	public static void close( InputStream is )
	{
		try
		{
			if ( is != null )
				is.close();
		}
		catch ( IOException e )
		{
			// do nothing
		}
	}

	/**
	 * Safely close {@link java.net.Socket}
	 *
	 * @param socket Socket to close
	 */
	public static void close( Socket socket )
	{
		try
		{
			if ( socket != null )
				socket.close();
		}
		catch ( IOException e )
		{
			// do nothing
		}
	}

	/**
	 * Safely close {@link java.net.ServerSocket}
	 *
	 * @param socket ServerSocket to close
	 */
	public static void close( ServerSocket socket )
	{
		try
		{
			if ( socket != null )
				socket.close();
		}
		catch ( IOException e )
		{
			// do nothing
		}
	}

	/**
	 * Safely close {@link java.io.Reader}
	 *
	 * @param reader Reader to close
	 */
	public static void close( Reader reader )
	{
		try
		{
			if ( reader != null )
				reader.close();
		}
		catch ( IOException e )
		{
			// do nothing
		}
	}

	/**
	 * Safely close {@link java.io.OutputStream}
	 *
	 * @param os OutputStream to close
	 */
	public static void close( OutputStream os )
	{
		try
		{
			if ( os != null )
				os.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			// do nothing
		}
	}

	/**
	 * Safely close {@link java.io.Writer}
	 *
	 * @param writer Writer to close
	 */
	public static void close( Writer writer )
	{
		try
		{
			if ( writer != null )
				writer.close();
		}
		catch ( IOException e )
		{
			// do nothing
		}
	}

	/**
	 * Read contents of InputStream
	 *
	 * @param inputStream data source
	 * @return StringBuffer with source contents
	 * @throws java.io.IOException on IO failure
	 */
	public static StringBuffer readFile( InputStream inputStream ) throws IOException
	{
		BufferedInputStream bis = null;
		BufferedReader br = null;

		StringBuffer buf = new StringBuffer();

		try
		{
			bis = new BufferedInputStream( inputStream );
			br = new BufferedReader( new InputStreamReader( bis ) );
			while ( br.ready() )
			{
				int i = br.read();
				if ( i == -1 )
					break;
				buf.append( ( char ) i );
			}
		}
		finally
		{
			close( br );
			close( bis );
		}

		return buf;
	}

	/**
	 * Read contents of InputStream
	 *
	 * @param is data source
	 * @return StringBuffer with source contents
	 * @throws java.io.IOException on IO failure
	 */
	public static byte[] read( InputStream is ) throws IOException
	{
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try
		{
			copy( is, baos );
			return baos.toByteArray();
		}
		finally
		{
			close( bis );
			close( baos );
		}
	}

	/**
	 * Write to output stream contents of string
	 *
	 * @param os  OutputStream
	 * @param src Source string
	 * @throws java.io.IOException on IO failure
	 */
	public static void writeFile( OutputStream os, String src ) throws IOException
	{
		BufferedOutputStream bos = null;
		BufferedWriter bw = null;

		try
		{
			bos = new BufferedOutputStream( os );
			bw = new BufferedWriter( new OutputStreamWriter( bos ) );
			bw.write( src );
		}
		finally
		{
			close( bw );
			close( bos );
		}
	}

	/**
	 * Copy contents of input stream to output stream
	 *
	 * @param is Input stream
	 * @param os Output stream
	 * @throws IOException on read/write failure
	 */
	public static void copy( InputStream is, OutputStream os ) throws IOException
	{
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			bos = new BufferedOutputStream( os );
			bis = new BufferedInputStream( is );
			byte[] buff = new byte[COPY_BUF_SIZE];
			int length;
			while ( bis.available() > 0 )
			{
				length = bis.read( buff );
				if ( length > 0 )
					bos.write( buff, 0, length );
			}
			bos.flush();
		}
		finally
		{
			close( bis );
			close( bos );
		}
	}

	/**
	 * Safely release {@link FileLock}
	 *
	 * @param tryLock FileLock to release
	 */
	public static void release( FileLock tryLock )
	{
		if ( tryLock != null )
			try
			{
				tryLock.release();
			}
			catch ( IOException e )
			{
				// do nothing
			}
	}
}
