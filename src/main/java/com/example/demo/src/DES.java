package com.example.demo.src;


import java.util.Arrays;

public class DES {


    private static String _HexValues = "0123456789abcdef";

    private static final byte[] PC1_TABLE = {57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};

    private static final byte[] PC2_TABLE = {14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,
            41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};

    private static final byte[] SHIFT_TABLE = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    private static final byte[] IP = {58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7 };

    private static final byte[] E = {32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    private static final byte[][][] S_BOXES = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 13, 6}},

            {       {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},

            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},

            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},

            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},

            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},

            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},

            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};

    private static final byte[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};

    private static final byte[] FP_TABLE = {40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    private Key _key;
    private byte[][] _blocks;
    private byte[][] _encryptedBlocks;

    private static int counter;

    public DES(String s) {
        _key = new Key(s);
    }

    public byte[] encrypt(byte[] msg, Key key)
    {
        _blocks = divideIntoBlocks(msg);

        _encryptedBlocks = new byte[_blocks.length][];

        byte[][] subkeys = key.getSubkeys();

        for (int i = 0; i < _blocks.length; i++)
        {
            byte[] block = _blocks[i].clone();

            block = applyPermutationTable(block,IP);

            byte[] left = Arrays.copyOfRange(block, 0, 4);
            byte[] right = Arrays.copyOfRange(block, 4, 8);
            for (int j = 0; j < 16; j++) {
                byte[] prevLeft = left;
                left = right;
                right = xor(prevLeft, feistel(right, subkeys[j]));
            }

            byte[] ciphertextBlock = applyPermutationTable(concatenate(right, left), FP_TABLE);
            _encryptedBlocks[i] = ciphertextBlock;
        }

        byte[] ciphertext = new byte[_blocks.length * 8];
        for (int i = 0; i < _blocks.length; i++) {
            System.arraycopy(_encryptedBlocks[i], 0, ciphertext, i * 8, 8);
        }


        return ciphertext;
    }

    public byte[] decrypt(byte[] msg, Key key){
        byte[][] subkeys = key.getSubkeys();

        _encryptedBlocks = divideIntoBlocks(msg);

        _blocks = new byte[_encryptedBlocks.length][];

        for (int j = 0; j < _encryptedBlocks.length;j++)
        {
            byte[] currMsg = _encryptedBlocks[j].clone();
            byte[] initialPermuted = applyPermutationTable(currMsg, IP);

            byte[] left = Arrays.copyOfRange(initialPermuted, 0, 4);
            byte[] right = Arrays.copyOfRange(initialPermuted, 4, 8);
            byte[] temp;

            for (int i = 15; i >= 0; i--) {
                temp = left;
                left = right;
                right = xor(temp, feistel(right, subkeys[i]));
            }

            byte[] combined = concatenate(right, left);
            byte[] permuted = applyPermutationTable(combined, FP_TABLE);

            _blocks[j] = permuted;
        }

        byte[] ciphertext = new byte[_blocks.length * 8];
        for (int i = 0; i < _blocks.length; i++) {
            System.arraycopy(_blocks[i], 0, ciphertext, i * 8, 8);
        }

        return ciphertext;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    private static byte[] feistel(byte[] halfBlock, byte[] subkey) {
        byte[] expandedHalfBlock = applyPermutationTable(halfBlock, E);
        byte[] xoredBlock = xor(expandedHalfBlock, subkey);
        byte[] sBoxOutputs = applySBoxes(xoredBlock);
        byte[] permutedBlock = applyPermutationTable(sBoxOutputs, P);
        return permutedBlock;
    }

    private static byte[] applySBoxes(byte[] input) {
        byte[] output = new byte[4];
        byte[][][] sboxes = S_BOXES;

        for (int i = 0; i < 4; i++) {
            int row = ((input[i] & 0x80) >> 6) | ((input[i] & 0x04) >> 2);
            int col = (input[i] & 0x78) >> 3;
            output[i] = (byte) sboxes[i][row][col];
        }

        return output;
    }

    public static byte[][] divideIntoBlocks(byte[] message) {
        int numBlocks = (int) Math.ceil(message.length / 8.0); // Round up to get number of blocks needed
        byte[][] blocks = new byte[numBlocks][8];
        int blockIndex = 0;
        int messageIndex = 0;
        int counter0 = 0;
        while (blockIndex < numBlocks) {
            for (int i = 0; i < 8; i++) {
                if (messageIndex < message.length) {
                    blocks[blockIndex][i] = message[messageIndex];
                    messageIndex++;
                } else {
                    // Add padding if message length is not a multiple of 8
                    blocks[blockIndex][i] = (byte) 0;
                    counter0++;

                    if(i == 7)
                        blocks[blockIndex][i] = (byte) counter0;
                }
            }
            blockIndex++;
        }

        counter = counter0;
        return blocks;
    }

    public static byte[] applyPermutationTable(byte[] input, byte[] permutationTable) {
        byte[] output = new byte[permutationTable.length / 8];
        for (int i = 0; i < permutationTable.length; i++) {
            int bitIndex = permutationTable[i] - 1;
            int byteIndex = bitIndex / 8;
            int byteOffset = bitIndex % 8;
            int bitValue = getBitValue(input, byteIndex, byteOffset);
            setBitValue(output, i / 8, i % 8, bitValue);
        }
        return output;
    }

    public static int getBitValue(byte[] array, int byteIndex, int bitIndex) {
        return (array[byteIndex] >> (7 - bitIndex)) & 0x01;
    }

    public static void setBitValue(byte[] array, int byteIndex, int bitIndex, int value) {
        if (value == 0) {
            array[byteIndex] &= ~(0x01 << (7 - bitIndex));
        } else {
            array[byteIndex] |= 0x01 << (7 - bitIndex);
        }
    }

    public static byte[][] generateSubkeys(byte[] key) {
        // Apply the PC1 table to the key to get a 56-bit permutation
        byte[] permutedKey = applyPermutationTable(key, PC1_TABLE);

        // Split the permuted key into two 28-bit halves
        byte[] leftHalf = Arrays.copyOfRange(permutedKey, 0, 28);
        byte[] rightHalf = Arrays.copyOfRange(permutedKey, 28, 56);

        // Generate 16 subkeys by rotating the halves and applying the PC2 table
        byte[][] subkeys = new byte[16][48];
        for (int i = 0; i < 16; i++) {
            // Rotate the halves by the appropriate number of bits
            leftHalf = leftRotate(leftHalf, SHIFT_TABLE[i]);
            rightHalf = leftRotate(rightHalf, SHIFT_TABLE[i]);

            // Combine the halves and apply the PC2 table to get the subkey
            byte[] combinedHalf = concatenate(leftHalf, rightHalf);
            subkeys[i] = applyPermutationTable(combinedHalf, PC2_TABLE);
        }

        return subkeys;
    }

    public static byte[] leftRotate(byte[] arr, int d) {
        byte[] rotatedArr = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rotatedArr[i] = (byte) ((arr[i] << d) | (arr[(i + 1) % arr.length] >>> (8 - d)));
        }
        return rotatedArr;
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static byte[] removePadding(byte[] data, int len) {
        int paddingLength = len;
        byte[] unpaddedData = new byte[data.length - paddingLength];
        System.arraycopy(data, 0, unpaddedData, 0, unpaddedData.length);
        return unpaddedData;
    }

    public byte[][] get_encryptedBlocks() {
        return _encryptedBlocks;
    }

    public Key get_key() {
        return _key;
    }
}