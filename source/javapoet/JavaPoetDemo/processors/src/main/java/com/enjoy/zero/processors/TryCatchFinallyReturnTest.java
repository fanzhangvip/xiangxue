package com.enjoy.zero.processors;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class TryCatchFinallyReturnTest {

    public static int finallyMethodTest(int i) {
        if (i == 0) {
            return i;
        } else {
            throw new RuntimeException("This a Exception! finally test");
        }
    }

    ///try-catch-finally-return执行顺序
    public static String finallyTest(int i) {
        try {
            int itest = finallyMethodTest(i);
            if (itest == 0) {//finally里面的return会覆盖这里的结果？？？
                return "finallyTest i = $i";
            }
        } catch (Exception e) {
            //可以试试删除catch语句，只用try...finally是什么效果
            ///结构 没得catch 不会报错 直接执行finally的语句
            System.out.println("Catch Exception finally test: " + e.getMessage());
            return "catch finally test";
        } finally {
            System.out.println("Close");
            return " finally test";
        }
    }

    public static int NoException() {
//        情况1：try块中没有抛出异常try和finally块中都有return语句
//        执行顺序：
//        1. 执行try块，执行到return语句时，先执行return的语句，--i，但是不返回到main 方法，
//        2. 执行finally块，遇到finally块中的return语句，执行--i,并将值返回到main方法，这里就不会再回去返回try块中计算得到的值
        int i = 10;
        try {
            System.out.println("i 现在是try代码块: " + i);
            int j = 100;
            return i = (--i + j);
        } catch (Exception e) {
            int j = 1000;
            --i;
            System.out.println("i 现在是在catch代码块: " + i);
            return i = (--i + j);
        } finally {
            System.out.println("i 现在是在finally代码块: " + i);
            int j = 10000;
            return i = (--i + j);
        }
    }

    public static int NoException1() {
//        情况2：try块中没有抛出异常，仅try中有return语句
//        执行顺序：
//        try中执行完return的语句后，不返回，执行finally块，finally块执行结束后，
//        返回到try块中，返回i在try块中最后的值
        int i = 10;
        try {
            System.out.println("i 现在是try代码块: " + i);
            int j = 100;
            return i = (--i + j);
        } catch (Exception e) {
            int j = 1000;
            --i;
            System.out.println("i 现在是在catch代码块: " + i);
            return i = (--i + j);
        } finally {
            System.out.println("i 现在是在finally代码块before: " + i);
            --i;
            System.out.println("i 现在是在finally代码块after: " + i);
        }
    }

    public static int WithException() {
//        情况3：try块中抛出异常try,catch,finally中都有return语句
//        执行顺序：
//        抛出异常后，执行catch块，在catch块的return的--i执行完后，并不直接返回而是执行finally，
//        因finally中有return语句，所以，执行，返回结果6
        int i = 10;
        try {
            System.out.println("i 现在是try代码块: " + i);
            i = i / 0;
            int j = 100;
            return i = (--i + j);
        } catch (Exception e) {
            System.out.println("i 现在是在catch代码块before: " + i);
            int j = 1000;
            --i;
            System.out.println("i 现在是在catch代码块after: " + i);
            return i = (--i + j);
        } finally {
            System.out.println("i 现在是在finally代码块before: " + i);
            int j = 10000;
            --i;
            System.out.println("i 现在是在finally代码块after: " + i);
            return i = (--i + j);
        }
//        情况4，catch中有return,finally中没有，同上，执行完finally语句后，依旧返回catch中的执行return语句后的值，而不是finally中修改的值
    }

    public static int CatchException(){
//        情况5：try和catch中都有异常，finally中无return语句
//        在try块中出现异常，到catch中，执行到异常，到finally中执行，finally执行结束后判断发现异常，抛出
        int i = 10;
        try {
            System.out.println("i 现在是try代码块: " + i);
            i = i / 0;
            int j = 100;
            return i = (--i + j);
        } catch (Exception e) {
            System.out.println("i 现在是在catch代码块before: " + i);
            int k = i / 0;
            int j = 1000;
            System.out.println("i 现在是在catch代码块after: " + i);
            return i = (--i + j);
        } finally {

            System.out.println("i 现在是在finally代码块before:" + i);
            --i;
            System.out.println("i 现在是在finally代码块after: " + i);
            //return --i;
        }
    }

    public static int CatchException1() {
//        情况6：try,catch中都出现异常，在finally中有返回
//        try块中出现异常到catch，catch中出现异常到finally，finally中执行到return语句返回，不检查异常
        int i = 10;
        try {
            System.out.println("i 现在是try代码块: " + i);
            i = i / 0;
            int j = 100;
            return i = (--i + j);
        } catch (Exception e) {
            System.out.println("i 现在是在catch代码块before: " + i);
            int k = i / 0;
            int j = 1000;
            System.out.println("i 现在是在catch代码块after: " + i);
            return i = (--i + j);
        } finally {
            System.out.println("i 现在是在finally代码块before:" + i);
            --i;
            System.out.println("i 现在是在finally代码块after: " + i);
            int j = 10000;
            return i = (--i + j);
        }
    }


    public static void main(String[] args) {
//        System.out.println("-----------------异常测试-----------------");
//        String test = finallyTest(0);
//        System.out.println("test: " + test);
//        System.out.println("----------------------------------");
//        String test1 = finallyTest(1);
//        System.out.println("test1: " + test1);
        System.out.println("-----------------------------------情况1---------------------------------");
        int result = NoException();
        System.out.println("try 块中没有抛出异常try和finally块中都有return语句 结果: " + result);

        System.out.println("-----------------------------------情况2---------------------------------");
        int result1 = NoException1();
        System.out.println("try块中没有抛出异常，仅try中有return语句 结果: " + result1);

        System.out.println("-----------------------------------情况3---------------------------------");
        int result2 = WithException();
        System.out.println("try块中抛出异常try,catch,finally中都有return语句 结果: " + result2);

        System.out.println("-----------------------------------情况6---------------------------------");
        int result4 = CatchException1();
        System.out.println("try,catch中都出现异常，在finally中有返回 结果: " + result4);

        System.out.println("-----------------------------------情况5---------------------------------");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int result3 = CatchException();
        System.out.println("try和catch中都有异常，finally中无return语句 结果: " + result3);
    }
}
