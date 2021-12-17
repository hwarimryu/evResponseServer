package org.example.evresponseserver.utils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * byte[] 를 사용하는데 도움주는 클래스
 * 주요 역할 :
 *          String <-> bytes[]
 *          short <-> bytes[]
 *          특정 길이의 byte[] 생성  등
 * Created by user on 2018-06-22.
 */
public class ByteArrayManager {
    public static String toString(byte[] arr) {
        if (arr == null)
            return null;

        StringBuilder buf = new StringBuilder();
        for (byte b : arr) {
            buf.append(String.format("%02x", b));
        }
        return buf.toString();
    }

    public static String toString(byte[] arr, int startIdx, int lastIdx) {
        if (arr == null)
            return null;

        StringBuilder buf = new StringBuilder();
        for (int idx = startIdx; idx < lastIdx; idx++) {
            buf.append(String.format("%02x", arr[idx]));
        }
        return buf.toString();
    }

    public static String toString(byte b) {
        return String.format("%02x", b);
    }

    public static byte[] fixLengthArr(int size, byte[] arr) {
        byte[] str = new byte[size];
        Arrays.fill(str, (byte) 0x00);
        System.arraycopy(arr, 0, str, 0, arr.length);
        return str;
    }

    public static byte[] fixLengthArr(int size, int startIdx, byte[] arr) {
        byte[] str = new byte[size];
        Arrays.fill(str, (byte) 0x00);
        if (arr.length - startIdx >= 0) System.arraycopy(arr, startIdx, str, startIdx, arr.length - startIdx);
        return str;
    }

    public static byte[] fixLengthArr(int size, Charset charset, String target) {
        byte[] dstBuf = new byte[size];
        byte[] srcBuf = (target != null) ? target.getBytes(charset) : new byte[size];
        System.arraycopy(srcBuf, 0, dstBuf, 0, srcBuf.length < size ? srcBuf.length : size);
        return dstBuf;
    }

    public static byte[] shortToByte(short a) {
        byte[] shortToByte = new byte[2];
        shortToByte[0] |= (byte) ((a & 0xFF00) >>> 8);
        shortToByte[1] |= (byte) (a & 0xff);
        return shortToByte;
    }

    public static byte[] fillBlank(int size) {
        byte[] temp = new byte[size];
        Arrays.fill(temp, (byte) 0x00);
        return temp;
    }

    public static byte[] hexStringToByteArray(String s) {
        return hexStringToByteArray(s, false);
    }

    public static byte[] hexStringToByteArray(String s, boolean isRfid) {
        if (s.length() % 2 == 1) {
            s = (isRfid ? "0" + s : s + "0");
        }

        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // convert byte array from big endian to little endian
    public static byte[] convertEndian(byte[] arr) {
        int len = arr.length;
        for (int j = 0; j < len / 2; j++) {
            byte tmp = arr[j];
            arr[j] = arr[len - 1 - j];
            arr[len - 1 - j] = tmp;
        }
        return arr;
    }

    public static int bytesToShort(byte[] bytes) {
        int newValue = 0;
        newValue |= (((int)bytes[0])<<8)&0xFF00;
        newValue |= (((int)bytes[1]))&0xFF;
        return newValue;
    }

    public static int bytesToShortLE(byte[] bytes) {
        int newValue = 0;
        newValue |= (((int)bytes[1])<<8)&0xFF00;
        newValue |= (((int)bytes[0]))&0xFF;
        return newValue;
    }
}
