package com.zero.zifuchuanlib;

public class BF1Main {

    public static void main(String... args) {
        System.out.println("Hello 字符串匹配算法...");

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please input main string:");
//        String mainString = scanner.nextLine();// 从标准输入读取主串
//        System.out.println("Please input sub string:");
//        String subString = scanner.nextLine();// 从标准输入读取子串
//        scanner.close();
        String mainString = "BBC ABCDAB ABCDABCDABDE";
        String subString = "ABCDABD";
        System.out.println("主串");
        System.out.println(mainString);
        System.out.println("子串");
        System.out.println(subString);

        bruteForce(mainString, subString);
    }


    /**
     * @param s 主串
     * @param p 子串
     */
    public static void bruteForce(String s, String p) {
        int index = -1;// 成功匹配的位置
        int sLength = s.length();// 主串长度
        int pLength = p.length();// 子串长度
        if (sLength < pLength) {
            System.out.println("Error.The main string is greater than the sub string length.");
            return;
        }
        int i = 0;
        int j = 0;
        while (i < sLength && j < pLength) {
            if (s.charAt(i) == p.charAt(j)) {// 判断对应位置的字符是否相等
                i++;// 若相等，主串、子串继续依次比较
                j++;
            } else {// 若不相等
                i = i - j + 1;// 主串回溯到上次开始匹配的下一个字符
//                i = i + 1;//主串不回溯
                j = 0;// 子串从头开始重新匹配
            }
        }
        if (j >= pLength) {// 匹配成功
            index = i - j;
            System.out.println("Successful match,index is:" + index);
        } else {// 匹配失败
            System.out.println("Match failed.");
        }
    }




}
