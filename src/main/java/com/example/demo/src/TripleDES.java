package com.example.demo.src;

public class TripleDES extends DES {

    private Key k2;
    private Key k3;

    public TripleDES(String k1, String k2, String k3) {
        super(k1);
        this.k2 = new Key(k2);
        this.k3 = new Key(k3);
    }

    public byte[] encrypt3DES(byte[] msg) {
        byte[] a = encrypt(msg,get_key());
        byte[] b = decrypt(a,k2);
        byte[] c = encrypt(b,k3);
        return c;
    }

    public byte[] decrypt3DES(byte[] msg) {
        byte[] a = decrypt(msg,k3);
        byte[] b = encrypt(a,k2);
        byte[] c = decrypt(b,get_key());
        return c;
    }
}
