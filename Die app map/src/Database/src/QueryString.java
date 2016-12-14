/**
 * Created by Timothy.
 */
public class QueryString {
    private String string;

    public QueryString() {}

    public String getString() {
        return this.string;
    }

    public void insert(String string) {
        this.string = string;
        checkString();
    }

    private void checkString() {
        if (this.string.isEmpty() || this.string.equals("NULL")) {
            this.string = "NULL";
        } else {
            this.string = this.string.replaceAll("[_'\\(\\)-]", "");
            this.string = String.format("'%s'", this.string);
        }
    }
}
