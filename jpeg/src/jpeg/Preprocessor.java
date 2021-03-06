/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpeg;

import java.io.*;
import java.math.*;
import java.nio.*;
import java.util.*;

/**
 *
 * @author pihla
 */
public class Preprocessor {

    /**
     * Blocks, 4D array which stores all data
     */
    public double blocks[][][][];

    /**
     * Size of x-axel
     */
    public int x;

    /**
     * Size of y-axel
     */
    public int y;
    private int maara;
    private int position;
    private int px;
    private int py;
    private int alkuposition;

    /**
     * Constructor for Preprocessor object
     */
    public Preprocessor() {
        this.blocks = blocks;
        this.maara = 0;
        this.position = 0;
        this.px = 0;
        this.py = 0;
        this.alkuposition = 0;

    }
    
    /**
     * Constructor for preprocessor
     * @param x x
     * @param y x
     */
    public Preprocessor(int x, int y) {
        this.x = x;
        this.y = y;
        this.blocks = blocks;
        this.maara = 0;
        this.position = 0;
        this.px = 0;
        this.py = 0;
        this.alkuposition = 0;

    }

    /**
     * Read byte data from .rgb file and separate it into 8*8 arrays.
     *
     * @param filename filename
     * @param datatype datatype
     * @param x x
     * @param y y
     * @throws FileNotFoundException exception
     * @throws IOException exception
     */
    public void separate(String filename, String datatype, int x, int y) throws FileNotFoundException, IOException {
        //  read the file into a byte array
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        int count = (x * y) / (8 * 8);
        this.x = x;
        this.y = y;
        this.blocks = new double[count][8][8][3];
        for (int i = 0; i < x * y; i++) {
            for (int j = 0; j < 3; j++) {
                int b = fis.read();
                this.blocks[position][px][py][j] = b & 0xff;
            }
            this.blocks[position][px][py] = convertToYCbCr(this.blocks[position][px][py]);
            px++;
            if (px == 8) {
                px = 0;
                position++;
            }
            if (position == alkuposition + (x / 8)) {
                py++;
                position = alkuposition;
                if (py == 8) {
                    position += (x / 8);
                    alkuposition = position;
                    py = 0;
                }
            }
        }
    }

    /**
     * Decrease all values of block with value given a parameter
     *
     * @param value value
     */
    public void decreaseBlocks(int value) {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                for (int k = 0; k < this.blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
                        this.blocks[i][j][k][z] -= value;
                    }
                }
            }
        }
    }

    /**
     * Increase all values of block[][][][] with a given parameter
     * @param value value
     */
    public void increaseBlocks(int value) {
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                for (int k = 0; k < this.blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
                        this.blocks[i][j][k][z] += value;
                    }
                }
            }
        }
    }

    /**
     * Prints all values of double[][][][] blocks.
     *
     * @param blocks blocks
     */
    public void printBlocks(double[][][][] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                for (int k = 0; k < blocks[i][j].length; k++) {
                    for (int z = 0; z < 3; z++) {
                        System.out.println(blocks[i][j][k][z]);
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
        }
    }

    /**
     * Converts RGB-value block of one pixel to YCrCb block
     *
     * @param block block
     * @return block
     */
    public double[] convertToYCbCr(double[] block) {

        double r = block[0];
        double g = block[1];
        double b = block[2];

        double Y = 0.299 * r + 0.587 * g + 0.114 * b;
        double cb = -0.1687 * r - 0.3313 * g + 0.5 * b + 128;
        double cr = 0.5 * r - 0.4187 * g - 0.0813 * b + 128;

        block[0] = Y;
        block[1] = cb;
        block[2] = cr;
        return block;
    }

    /**
     * Converts YCbCr -block of one pixel (3 values) to RGB
     *
     * @param block block
     * @return converted block
     */
    public double[] convertToRgb(double[] block) {
        double[] uusi = new double[3];
        double Y = block[0];
        double cb = block[1];
        double cr = block[2];

        double r = Y + 1.402 * (cr - 128);
        double g = Y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128);
        double b = Y + 1.772 * (cb - 128);

        r = validateRGB(r);
        g = validateRGB(g);
        b = validateRGB(b);
        uusi[0] = r;
        uusi[1] = g;
        uusi[2] = b;
        return uusi;
    }

    /**
     * Validates RGB. It cannot be over 255 or under 0
     *
     * @param a rgb value
     * @return validated value
     */
    public double validateRGB(double a) {
        if (a > 255) {
            return 255;
        }
        if (a < 0) {
            return 0;
        }
        return Math.floor(a);
    }

    /**
     * Writes data to RGB file. This is now just for test. 
     *
     * @param blocks blocks
     * @param filename filename
     * @param x x
     * @param y y
     * @throws IOException
     */
    public void writeToRgbFile(double[][][][] blocks, String filename, int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        int i = 0;
        int position = 0;
        int px = 0;
        int py = 0;
        int alkuposition = 0;
        int maara = 0;
        DataOutputStream os = new DataOutputStream(new FileOutputStream(filename));
        while (i < (x * y)) {
            double[] arvot = blocks[position][px][py];
            arvot = convertToRgb(arvot);
            for (int j = 0; j < arvot.length; j++) {
                BigInteger bigInt = BigInteger.valueOf((int) arvot[j]);
                os.write(bigInt.byteValue());
                maara++;
            }

            px++;
            i++;
            if (px == 8) {
                px = 0;
                position++;
            }
            if (position == alkuposition + (this.x / 8)) {
                py++;
                position = alkuposition;
                if (py == 8) {
                    position += (x / 8);
                    alkuposition = position;
                    py = 0;
                }
            }
        }
        os.close();

    }

}
