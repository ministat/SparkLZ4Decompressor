package sparklz4decompressor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import net.jpountz.lz4.LZ4BlockInputStream;

public class Decompressor {

	private static byte[] readBinFile(String fileFullPath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		try
		{
			FileInputStream fis = new FileInputStream(fileFullPath);
			while ((len = fis.read(buffer)) != -1)
			{
				baos.write(buffer, 0, len);
			}
			fis.close();
			baos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
		
	}
	
	private static boolean writeFile(String content, String fullPath) {
		try {
			File file = new File(fullPath);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			return false;
		}
		return true;
	}
	

	private static byte[] decompressArray(byte[] data)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		LZ4BlockInputStream lzis = new LZ4BlockInputStream(bais, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			int count;
			byte[] buffer = new byte[2048];
			while ((count = lzis.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
			lzis.close();
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	private static void decompressFile(String lz4File, String unzippedFile)
	{
		byte[] lz4Content = readBinFile(lz4File);
		byte[] unzippedContent = decompressArray(lz4Content);
		if (unzippedContent.length > 0)
		{
			String unzippedStr = new String(unzippedContent, StandardCharsets.UTF_8);
			writeFile(unzippedStr, unzippedFile);
		}
	}
	
	private static void decompress(String args[])
	{
		if (args.length < 1) {
			System.out.println("Specify the compressed *.lz4 file");
			System.exit(0);
		}
    	
    	String lzFile = args[0];
    	String unzippedFile = lzFile.replaceAll(".lz4", ".txt");
    	decompressFile(lzFile, unzippedFile);
	}
	
    public static void main(String args[])
    {
    	decompress(args);
    }
}
