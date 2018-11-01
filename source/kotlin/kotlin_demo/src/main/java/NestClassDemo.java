import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NestClassDemo {

    int outValue = 10;
    final int finalValue = 1;
    static int staticValue = 1;

    class Inner {
        public void printInfo() {
            System.out.println("返回外面的成员变量:" + outValue);
        }
    }

    static class StaticInner {
        public void printInfo() {
//            System.out.println("返回外面的成员变量:" + outValue);
//            System.out.println("返回外面的成员变量:" + finalValue);
            System.out.println("返回外面的成员变量:" + staticValue);
        }
    }


    public static void main(String... agrs) {

//        Calendar calendar = Calendar.getInstance();
//
//        System.out.println("calender: " + calendar);
//
//        SimpleDateFormat sFmt1=new SimpleDateFormat("MM-dd");
//        Date now=new Date();
//        SimpleDateFormat sFmt2=new SimpleDateFormat("E");
//        System.out.println(sFmt1.format(now));
//        System.out.println(sFmt2.format(now));

        getWeekDates();

    }
    public static void getWeekDates() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        for (int i = 0; i <490; i++) {
            SimpleDateFormat sFmt1 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sFmt2 = new SimpleDateFormat("E");
            SimpleDateFormat sFmt3 = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            Date datei = calendar.getTime();

            DateBean dateBean = new DateBean(sFmt1.format(datei), sFmt2.format(datei), sFmt3.format(datei), calendar.getTimeInMillis());
            System.out.println(dateBean);
        }
        return;
    }
}
