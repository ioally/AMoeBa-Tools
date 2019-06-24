package com.ioally.amoeba.utils.other;

import java.util.HashMap;

public class ParseSystemUtil {

    /**
     * 顺序的128位字符
     */
    private static final char[] ORDER = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '·', '！', '@', '#', '¥', '%', '&', '*', '（', '）',
            '+', '-', '=', '·', '【', '】', '；', '\'', '，', '。',
            '/', '、', '~', '`', '$', '^', '{', '}', ':', '<', '>',
            '?', '|', '(', ')', '[', ']', '《', '》', '/', '\\', '"',
            'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ',
            'λ', 'μ', 'ν', 'ξ', 'ο', 'π', 'ρ', 'σ', 'τ', 'υ',
            'φ', 'χ', 'ψ', 'ω'
    };

    /**
     * 无序的128位字符
     */
    private static final char[] DISORDER = new char[]{
            'd', 'S', 'β', 'G', '*', '【', 'n', 'm', '·', 'α',
            'g', 'ε', 'μ', '$', 'v', 'X', 'η', 't', 'T', 'C',
            'f', '5', 'A', 'b', '`', 'Y', 'χ', '&', 'Q', 'k',
            'υ', 'U', '、', '0', 'z', 'a', 'O', '-', ']', '|',
            'σ', 'e', '>', '(', '4', 'ζ', 'π', 'P', 'o', 'V',
            'ξ', 'R', '1', '2', 'ψ', 'N', 'κ', 'y', 's', 'λ',
            '7', 'ω', '】', 'r', '[', '！', 'ρ', '·', 'M', '^',
            '%', 'x', 'j', '《', '¥', 'B', 'h', 'w', 'l', '"',
            '/', '}', '@', 'F', '》', '。', ')', ':', 'K', '；',
            '{', 'ο', '#', '\\', 'W', 'ν', '9', 'i', 'H', '3',
            '8', 'u', '）', 'γ', 'φ', 'D', '6', ',', 'ι', 'τ',
            '?', 'E', '（', 'Z', 'θ', 'p', 'δ', '/', '~', '+',
            'I', '=', '\'', '<', 'q', 'J', 'L', 'c'
    };

    /**
     * 要使用的数组
     */
    private static final char[] BYTES = DISORDER;

    /**
     * 序列表
     */
    private static final HashMap<Character, Integer> indexTable = new HashMap<>();

    static {
        for (int j = 0; j < BYTES.length; j++) {
            indexTable.put(BYTES[j], j);
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 二进制转换成128进制
     *
     * @param data
     * @return
     */
    public static String parseByteTo128Ary(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int tail = 0;
        for (int i = 0; i < data.length; i++) {
            int mov = (i % 7 + 1);
            int curr = 0xFF & data[i];
            int code = tail + (curr >> mov);
            result.append(BYTES[code]);
            tail = (0xFF & (curr << (8 - mov))) >> 1;
            if (mov == 7) {
                result.append(BYTES[tail]);
                tail = 0;
            }
        }
        result.append(BYTES[tail]);
        return result.toString();
    }

    /**
     * 128进制转换成二进制
     *
     * @param base128
     * @return
     */
    public static byte[] parse128AryToByte(String base128) {
        if (base128 == null || base128.length() == 0) {
            return new byte[]{};
        }
        int length = (int) Math.floor(base128.length() * 0.875);
        byte[] result = new byte[length];
        int idx = 0;
        int head = indexTable.get(base128.charAt(0)) << 1;
        for (int i = 1; i < base128.length(); ) {
            int mod = i % 8;
            int code = indexTable.get(base128.charAt(i++));
            result[idx++] = (byte) (0xFF & (head + (code >> (7 - mod))));
            if (mod == 7) {
                head = 0xFF & (indexTable.get(base128.charAt(i++)) << 1);
            } else {
                head = 0xFF & (code << (mod + 1));
            }
        }
        return result;
    }
}
