/**
 * Created by zero on 2018/8/14.
 */

public class DateBean {

    private String dateStr;
    private String weekStr;

    private String dateformat;

    private long date;

    public DateBean() {
    }

    public DateBean(String dateStr, String weekStr, String dateformat, long date) {
        this.dateStr = dateStr;
        this.weekStr = weekStr;
        this.dateformat = dateformat;
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getWeekStr() {
        return weekStr;
    }

    public void setWeekStr(String weekStr) {
        this.weekStr = weekStr;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "dateStr='" + dateStr + '\'' +
                ", weekStr='" + weekStr + '\'' +
                ", dateformat='" + dateformat + '\'' +
                ", date=" + date +
                '}';
    }
}
