package com.ioally.amoeba.utils.security;

import com.ioally.amoeba.utils.other.Str;
import com.ioally.amoeba.utils.other.ParseSystemUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class SecurityUtil {

    private SecurityUtil() {
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 对指定字符串进行混淆，生成一个带有时间戳的密文
     *
     * @param content   要混淆的字符串
     * @param validDate 有效时间
     * @return 混淆后的密文
     * @throws Exception
     */
    public static String encode(String content, Date validDate) throws Exception {
        String byteTo128Ary = ParseSystemUtil.parseByteTo128Ary(content.getBytes());
        String byteTo128AryMD5 = DigestUtils.md5Hex(byteTo128Ary);
        LinkedList<String> characters = new LinkedList<>();
        for (int i = 0; i < byteTo128AryMD5.length(); i++) {
            characters.add(String.valueOf(byteTo128AryMD5.charAt(i)));
        }
        StringBuilder indexStr = new StringBuilder();
        String dateStr = sdf.format(validDate);
        String[] dates = dateStr.split("-");
        List<Integer> randomIndex = Str.getRandomIndex(byteTo128AryMD5.length(), dates.length + 1);
        for (int i = 0; i < dates.length; i++) {
            Integer index = randomIndex.get(i);
            String in = index.toString();
            if (in.length() != 2) {
                in = Str.getCurNo(Long.valueOf(index), 2, "0");
            }
            indexStr.append(in);
            characters.add(index, dates[i]);
        }
        Integer lastIndex = randomIndex.get(randomIndex.size() - 1);
        String indexDis128Ary = ParseSystemUtil.parseByte2HexStr(indexStr.toString().getBytes());
        characters.add(lastIndex, indexDis128Ary);
        characters.addFirst(Str.getCurNo(Long.valueOf(lastIndex), 2, "0"));
        StringBuilder resStr = new StringBuilder();
        for (String character : characters) {
            resStr.append(character);
        }
        return resStr.toString();
    }

    /**
     * 对指定字符串进行混淆，生成一个带有时间戳的密文，并且用AES算法加密
     *
     * @param content   要混淆的字符串
     * @param validDate 有效时间
     * @param aesKey    aes密钥
     * @return 加密后的base64串
     * @throws Exception
     */
    public static String encodeByAESToBase64(String content, Date validDate, String aesKey) throws Exception {
        String encode = encode(content, validDate);
        return AESUtil.encryptToBase64(encode, aesKey);
    }

    /**
     * 使用AES算法解密并验证密文是否正确
     *
     * @param text      待验证的字符串
     * @param resBase64 混淆加密后的base64密文
     * @param aesKey    aes密钥
     * @return 若通过验证，则返回密文中的时间戳
     * @throws Exception 验证不通过则抛出异常
     */
    public static Date decodeBase64ByAES(String text, String resBase64, String aesKey) throws Exception {
        byte[] resBytes = AESUtil.decryptByBase64(resBase64, aesKey);
        return decode(text, new String(resBytes));
    }

    /**
     * 解密并验证密文是否正确
     *
     * @param content 待验证的字符串
     * @param res     混淆后的密文
     * @return 若通过验证，则返回密文中的时间戳
     * @throws Exception 验证不通过则抛出异常
     */
    public static Date decode(String content, String res) throws Exception {
        String indexNums = res.substring(0, 2);
        int indexNum = Integer.parseInt(indexNums);
        res = res.substring(2);
        String indexs = res.substring(indexNum, indexNum + 24);
        String deIndex = new String(ParseSystemUtil.parseHexStr2Byte(indexs));
        res = res.substring(0, indexNum) + res.substring(indexNum + 24);

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < deIndex.length(); i = i + 2) {
            integers.add(Integer.parseInt(deIndex.substring(i, i + 2)));
        }
        Collections.sort(integers);
        String[] deDates = new String[5];
        for (int i = 0; i < deDates.length; i++) {
            Integer index = integers.get(i);
            String time = res.substring(index, index + 2);
            deDates[i] = time;
            res = res.substring(0, index) + res.substring(index + 2);
        }
        Integer yearIndex = integers.get(integers.size() - 1);
        String year = res.substring(yearIndex, yearIndex + 4);
        res = res.substring(0, yearIndex) + res.substring(yearIndex + 4);
        String date = year + "-" + deDates[4] + "-" + deDates[3] + "-" + deDates[2] + "-" + deDates[1] + "-" + deDates[0];
        String md5Hex = DigestUtils.md5Hex(ParseSystemUtil.parseByteTo128Ary(content.getBytes()));
        if (!res.equalsIgnoreCase(md5Hex)) {
            throw new Exception("密文未通过校验！");
        }
        return sdf.parse(date);
    }
}
