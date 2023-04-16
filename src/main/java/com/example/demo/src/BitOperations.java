package com.example.demo.src;
import java.math.BigInteger;

public class BitOperations {
    public static int GetBitAt(byte[] data, int pos)
    {
        int bytePos = pos / 8;
        int bitPos = pos % 8;
        byte valByte = data[bytePos];
        int valInt = valByte >> (7 - bitPos) & 1;
        return valInt;
    }

    public static void SetBitAt(byte[] data, int pos, int val)
    {
        if (val == 1)
        {
            //set bit
        }
        else
        {
            //clear bit
        }
    }

    public static String binToHex(String bin) {

        BigInteger b = new BigInteger(bin, 2);
        String HexTxt = b.toString(16);

        return HexTxt;
    }

    public static String hexToBin(String hex) {

        BigInteger b = new BigInteger(hex, 16);
        String Binary = b.toString(2);

        return String.format("%64s", Binary).replace(' ', '0');  //trzeba dodawać zera z przodu, żeby dopełnić do 64 znaków, bo był z tym problem
    }

    public static byte[] rotateBitsToLeft(byte[] input, int howMuch){
        BigInteger temp = new BigInteger(input);

        temp = temp.shiftLeft(howMuch);

//        Integer temp = new Integer(String.valueOf(input));
//
//        temp = Integer.rotateLeft(temp,howMuch);

        return temp.toByteArray();
    }

    public static int getBitAt(byte[] data, int poz)
    {
        int posByte = poz / 8;
        int posBit = poz % 8;
        byte valByte = data[posByte];
        int valInt = valByte >> (7 - posBit) & 1;
        return valInt;
    }

    //ustawia lub kasuje bit na podanej pozycji w podanej tablicy bajtów
    public static void setBitAt(byte[] data, int pos, int val)
    {
        byte oldByte = data[pos / 8];
        oldByte = (byte) (((0xFF7F >> (pos % 8)) & oldByte) & 0x00FF);
        byte newByte = (byte) ((val << (7 - (pos % 8))) | oldByte);
        data[pos / 8] = newByte;
    }

    public static byte[] rotateLeft(byte[] in, int len, int step)
    {
        byte[] out = new byte[(len - 1) / 8 + 1];
        for (int i = 0; i < len; i++)
        {
            int val = getBitAt(in, (i + step) % len);
            setBitAt(out, i, val);
        }
        return out;
    }

    public static String bytesToStringUTF8(byte[] bytes)
    {
        char[] buffer = new char[bytes.length >> 1];

        for(int i = 0; i < buffer.length; i++)
        {
            int bpos = i << 1;
            char c = (char)(((bytes[bpos]&0x00FF)<<8) + (bytes[bpos+1]&0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }

    public static byte SetBit(byte myByte, int whichBit)
    {
        myByte |= 1 << whichBit;
        return myByte;
    }

    public static byte ClearBit(byte myByte, int whichBit)
    {
        myByte &= ~(1 << whichBit);
        return myByte;
    }

    //returns true if bit is set and false if bit is not set
    public static boolean TestBit(byte myByte, int whichBit)
    {
        boolean temp = (myByte & (1<<whichBit)) != 0;
        return temp;
    }
}
