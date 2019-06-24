package com.ioally.amoeba.utils.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Str {
    /**
     * 根据要求来填充字符串
     *
     * @param curno   表示需要被填充的数字
     * @param length  表示要填充的长度
     * @param fillStr 表示需要填充的字符
     * @return
     */
    public static String getCurNo(Long curno, int length, String fillStr) {
        Long temp = curno;
        StringBuffer sb = new StringBuffer(length);
        int count = 0;
        while (curno / 10 != 0) {
            curno = curno / 10;
            count++;
        }
        int size = length - count - 1;
        for (int i = 0; i < size; i++) {
            sb.append(fillStr);
        }
        sb.append(temp);
        return sb.toString();
    }

    /**
     * 获取指定条件的随机序号
     *
     * @param maxIndex 最大序号-序列长度
     * @param indexNum 序号个数
     * @return 随机生成的序号
     */
    public static List<Integer> getRandomIndex(int maxIndex, int indexNum) throws Exception {
        if (maxIndex < 1) {
            throw new Exception("序列长度不能为0!");
        }
        if (indexNum > maxIndex) {
            throw new Exception("序号个数需要小于序列长度!");
        }
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < maxIndex; i++) {
            integers.add(i);
        }
        // 随机置换
        Collections.shuffle(integers);
        List<Integer> randomIndex = integers.subList(0, indexNum);
        Collections.sort(randomIndex, (a, b) -> b - a);
        return randomIndex;
    }
}
