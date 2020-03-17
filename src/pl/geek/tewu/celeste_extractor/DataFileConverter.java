package pl.geek.tewu.celeste_extractor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class DataFileConverter {
    static final boolean IS_LITTLE_ENDIAN = false;


    public boolean convert(final String inputFilePath, final String outputFilePath) throws IOException {
        try (InputStream inStream = new BufferedInputStream(new FileInputStream(inputFilePath))) {
            byte[] imgDimensions = new byte[8];
            if (inStream.read(imgDimensions) == -1) throw new EOFException();
            final int width = convertToInt32(imgDimensions, 0, IS_LITTLE_ENDIAN);
            final int height = convertToInt32(imgDimensions, 4, IS_LITTLE_ENDIAN);
            final boolean isTransparent = inStream.read() != 0;

            System.out.print("Converting " + inputFilePath + " to " + outputFilePath + " (" + width + "x" + height + ", " + (isTransparent ? 32 : 24) + " bits/pixel) ... ");

            //OLD WAY: IntBuffer outBuffer = IntBuffer.allocate(width * height);
            BufferedImage img = new BufferedImage(width, height, isTransparent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
            byte a, b, g, r;  // Input file pixel format: [Alpha, Blue, Green, Red] if isTransparent or [Blue, Green, Red] otherwise
            int x = 0, y = 0;
            int pixelData;
            int runLength;  // The texture format uses a run-length encoding which allows the same bytes to represent data for multiple sequential pixels.
            while ((runLength = inStream.read()) != -1) {
                // Read pixel data from input file
                if (isTransparent) {
                    a = (byte) inStream.read();
                    if (a == 0) {
                        b = g = r = 0;
                    } else {
                        b = (byte) inStream.read();
                        g = (byte) inStream.read();
                        r = (byte) inStream.read();
                    }
                    pixelData = (b & 0xFF) | (g & 0xFF) << 8 | (r & 0xFF) << 16 | (a & 0xFF) << 24;
                } else {
                    b = (byte) inStream.read();
                    g = (byte) inStream.read();
                    r = (byte) inStream.read();
                    pixelData = (b & 0xFF) | (g & 0xFF) << 8 | (r & 0xFF) << 16;
                }

                // Write pixel data to a buffer
                for (int i = 0; i < runLength; i++) {
                    img.setRGB(x, y, pixelData);
                    x++;
                    if (x >= width) {
                        x = 0;
                        y++;
                    }
                    //OLD WAY: outBuffer.put(pixelData);
                }
            }

//OLD WAY:            img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferInt(outBuffer.array(), outBuffer.array().length), null));
            boolean success = ImageIO.write(img, "png", new File(outputFilePath));
            System.out.println(success ? "[ SUCCESS ]" : "[ FAILURE ]");
            return success;
        }
    }


    // SOURCE: https://referencesource.microsoft.com/mscorlib/system/bitconverter.cs.html#https://referencesource.microsoft.com/mscorlib/system/bitconverter.cs.html
    private int convertToInt32(final byte[] value, int startIndex, boolean isLittleEndian) {
        if (value == null) throw new NullPointerException();
        if (startIndex >= value.length) throw new IndexOutOfBoundsException();
        if (startIndex > value.length - 4) throw new IndexOutOfBoundsException();
        if (startIndex % 4 != 0) throw new RuntimeException("convertToInt32 Not Fully Implemented");
        return byteArrayToInt(value, startIndex, isLittleEndian);
    }

    private int byteArrayToInt(byte[] data, int startIndex, boolean isLittleEndian) {
        if (isLittleEndian)
            return (data[startIndex] & 0xFF) << 24 |
                    (data[startIndex + 1] & 0xFF) << 16 |
                    (data[startIndex + 2] & 0xFF) << 8 |
                    (data[startIndex + 3] & 0xFF);
        else
            return (data[startIndex] & 0xFF) |
                    (data[startIndex + 1] & 0xFF) << 8 |
                    (data[startIndex + 2] & 0xFF) << 16 |
                    (data[startIndex + 3] & 0xFF) << 24;
    }
}