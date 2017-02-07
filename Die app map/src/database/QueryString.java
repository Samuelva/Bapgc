package database;

public class QueryString {
    private String string;

    public QueryString() {}

    public String getString() {
        /* Deze methode zorgt voor het returnen van de opgeslagen
         * String.
         */
        return this.string;
    }

    public void insert(String string) {
        /* Deze methode zorgt voor het opslaan van een String.
         * Daarna wordt de string gechecked m.b.v checkString().
         */
        this.string = string;
        checkString();
    }

    private void checkString() {
        /* Deze methode zorgt voor het geodzetten van de string.
         * Als deze leeg is of er staat NULL, dan wordt het NULL.
         * Anders worden allen illegale tekens weggehaald en twee
         * aanhalingstekens toegevoegd.
         */
        if (this.string.isEmpty() || this.string.equals("NULL")) {
            this.string = "NULL";
        } else {
            this.string = this.string.replaceAll("[_'\\(\\)-]", "");
            this.string = String.format("'%s'", this.string);
        }
    }
}
