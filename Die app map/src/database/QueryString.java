package database;

public class QueryString {
    /**
     * Deze klasse verzorgt de correcte opmaak vin een string voor
     * een query. Hierbij wordt de string van tevoren gedefinieerd.
     */
    private String string;

    public QueryString() {}

    public String getString() {
        /**
         * Deze methode zorgt voor het returnen van de opgeslagen
         * String.
         */
        return this.string;
    }

    public void insert(String string) {
        /**
         * Deze methode zorgt voor het opslaan van een String.
         * Daarna wordt de string gecheckt m.b.v checkString().
         */
        this.string = string;
        checkString();
    }

    private void checkString() {
        /**
         * Deze methode zorgt voor het goed zetten van de string.
         * Als deze leeg is of er staat NULL, dan wordt het NULL.
         * Anders worden alle illegale tekens weggehaald en twee
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