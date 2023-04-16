package com.example.demo.src;

import javafx.scene.control.Alert;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Key {
    private static String _validationString = "0123456789abcdef";

    private static final byte[] PC1_TABLE = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};

    private static final byte[] PC2_TABLE = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,
            41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};

    private static final byte[] SHIFT_TABLE = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    private String _keyString;
    private byte[] _byteKey;
    private byte[][] subkeys = new byte[16][6];

    public Key(String key)
    {
        if(ValidateKey(key))
        {
            _keyString = key;
            _byteKey = _keyString.getBytes(StandardCharsets.UTF_8);
            GenerateSubKeys();
        }
    }

    public String get_keyString() {
        return _keyString;
    }

    public static boolean ValidateKey(String key)
    {
        if(key.length() != 16) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Key length is wrong.");
            alert.showAndWait();
            return false;
        }

        for (char a : key.toCharArray()) {
            if(!_validationString.contains(String.valueOf(a))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("Key contains wrong characters.");
                alert.showAndWait();
                //TODO::Do we have to terminate this? Even with alert code proceeds. Check it. Plus, can we have alerts in this class? There's no problem with frontend being in backend class?
                return false;
            }
        }

        return true;
    }

    private void GenerateSubKeys()
    {
        byte[] key = _byteKey.clone();

        byte[] c = new byte[4];
        byte[] d = new byte[4];
        for (int i = 0; i < 56; i++) {
            if (i < 28) {
                c[i/8] |= ((key[PC1_TABLE[i] / 8] >> (7 - (PC1_TABLE[i] % 8))) & 1) << (7 - (i % 8));
            } else {
                d[(i-28)/8] |= ((key[PC1_TABLE[i] / 8] >> (7 - (PC1_TABLE[i] % 8))) & 1) << (7 - ((i-28) % 8));
            }
        }

// generate subkeys
        for (int i = 0; i < 16; i++) {
            // apply shift to left and right halves
            byte[] cLeft;
            byte[] cRight;
            if(i == 0)
            {
                cLeft = Arrays.copyOfRange(c, 0, 4);
                cRight = Arrays.copyOfRange(d, 0, 4);
            }
            else
            {
                cLeft = Arrays.copyOfRange(subkeys[i - 1], 0, 4);
                cRight = Arrays.copyOfRange(subkeys[i - 1], 4, 8);
            }

            for (int j = 0; j < SHIFT_TABLE[i]; j++) {
                RotateLeft(cLeft,1);
                RotateLeft(cRight,1);
            }

            // apply PC2 permutation to merged halves to generate subkey
            byte[] cd = new byte[8];
            for (int j = 0; j < 8; j++) {
                if (j < 4) {
                    cd[j] = cLeft[j];
                } else {
                    cd[j] = cRight[j-4];
                }
            }

            for (int j = 0; j < 48; j++) {
                int bit = 7 - (j % 8);
                int byteIndex = j / 8;
                subkeys[i][byteIndex] |= ((cd[PC2_TABLE[j] / 8] >> (7 - (PC2_TABLE[j] % 8))) & 1) << bit;
            }
        }
    }


    public byte[] rotateLeft(byte[] arr, int len)
    {
        byte[] tempArr = new byte[28];

        for (int i = 0; i < 28; i++)
        {
            tempArr[i] = (byte) getBit(arr,i);
        }

        byte last = tempArr[27];
        tempArr[27] = tempArr[0];
        for (int i = 0; i < 27; i++)
        {
            if(i == 26)
            {
                tempArr[26] = last;
            }
            else
            {
                tempArr[i] = tempArr[i + 1];
            }
        }

        byte[] newArr = new byte[4];

        for (int i = 0; i < 28; i++)
        {
            newArr[i / 8] += tempArr[i] * Math.pow(2,7 - i % 8);
        }

        return newArr;
    }

    public static int getBit(byte[] array, int index) {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        byte b = array[byteIndex];
        return (b >> (7 - bitIndex)) & 1;
    }

    public static void setBit(byte[] data, int index, boolean value) {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        byte mask = (byte) (1 << (7 - bitIndex));
        if (value) {
            data[byteIndex] |= mask;
        } else {
            data[byteIndex] &= ~mask;
        }
    }



    public static void RotateLeft(byte[] bytes, int shift) {
        // Calculate the number of bytes to shift and the number of bits to shift within the last byte
        int byteShift = shift / 8;
        int bitShift = shift % 8;

        // Rotate the bytes to the left by the specified number of bytes
        for (int i = 0; i < bytes.length - byteShift; i++) {
            bytes[i] = bytes[i + byteShift];
        }

        // Fill the remaining bytes with zeroes
        for (int i = bytes.length - byteShift; i < bytes.length; i++) {
            bytes[i] = 0;
        }

        // Rotate the bits within the last byte to the left by the specified number of bits
        if (bitShift != 0) {
            byte lastByte = bytes[bytes.length - 1];
            byte rotatedByte = (byte) ((lastByte << bitShift) | ((lastByte & 0xFF) >>> (8 - bitShift)));
            bytes[bytes.length - 1] = rotatedByte;
        }
    }

    public byte[][] getSubkeys() {
        return subkeys;
    }


}
