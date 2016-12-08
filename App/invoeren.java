
package sample;


import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/*
Programmeur: Anne van Winzum
Datum laatste aanpassing: 08-12-2016 (door Davy Cats, layout aangepast)
Beschrijving: In dit script wordt de eerste layout van het invoerscherm getoond.
*/

public final class Invoeren extends StackPane {
    /*
    De volgende globale variabelen worden gemaakt:
    btn1, btn2, btn3 en btn4, deze worden later gebruikt voor de knoppen
    onderaan het scherm
    Ook worden de labels lbl1 en lbl2 gemaakt en de pointsTable.
    */
    protected Button btn1;
    protected Button btn2;
    protected Button btn3;
    protected Button btn4;
    protected Label lbl1;
    protected Label lbl2;
    protected Button btn5;
    protected TableView pointsTable;
    protected ChoiceBox year;
    protected ChoiceBox studyyear;
    protected ChoiceBox period;
    protected ChoiceBox module;
    protected ChoiceBox type;
    protected ChoiceBox chance;
   
        
    public Invoeren() {
        /*
        De methode menuUnder wordt aangeroepen.
        */
        
        HBox hbox = menuUnder();
        VBox vbox2 = MenuMaken();
        BoxenVullen(vbox2, hbox); 

        
    }
    
    public HBox menuUnder(){
        /*
        btn1 krijgt de label Toets laden en de afmetingen 250 bij 50 worden
        meegegeven aan de methode maakObject.
        btn2 krijgt de label Leeg maken en de afmetingen 250 bij 50 worden
        meegegeven aan de methode maakObject.
        btn3 krijgt de label Wijzigingen opslaan en de afmetingen 250 bij 50
        worden meegegeven aan de methode maakObject.
        btn4 krijgt de label Import CSV en de ametingen 250 bij 50 worden
        meegegeven aan de methode Import CSV.
        Er wordt een HBox aangemaakt met de naam hbox.
        setHgrow wordt toegepast op alle knoppen.
        De grootte van de hbox wordt op 100 bij 1000 gezet.
        Vervolgens worden alle knoppen toegevoegd aan de hbox.
        Ten slotte wordt de methode MenuMaken aangeroepen,
        deze krijgt de parameter hbox mee.
        */
        Region leftFill = new Region();
        HBox.setHgrow(leftFill, Priority.ALWAYS);
        btn2 = maakObject(new Button(), "Leeg maken", 30, 150);
        btn3 = maakObject(new Button(),"Wijzigingen opslaan", 30, 150);
        btn4 = maakObject(new Button(),"Import CSV", 30, 150);
        Region rightFill = new Region();
        HBox.setHgrow(rightFill, Priority.ALWAYS);
            
        HBox hbox = new HBox();
        hbox.getChildren().addAll(rightFill, btn2, btn3, btn4, leftFill);
        hbox.setSpacing(20);
        return hbox;
        
    }

    public Button maakObject(Button btn, String tekst, double hoogte, 
            double breedte){
        /*
        Deze functie zet de naam, hoogte en breedte van de knoppen.
        De maximale breedte en hoogte wordt op maximaal gezet.
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(150);
        
        return btn;
        
    }
    
    public Label maakObject(Label lbl, String tekst){
        /*
        Deze functie zet de naam, hoogte en breedte van de labels.
        */
        lbl.setText(tekst);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(new Font("Arial", 18));
        return lbl;
    }
    
    
    public VBox MenuMaken(){
        /*
        In deze functie wordt het menu aan de linkerkant gemaakt.
        lbl1 krijgt de tekst Keuzemnu en krijgt de afmetingen 100 bij 100 mee
        aan de methode maakObject.
        De eerste MenuButton is jaartal: de items zijn: 2014-2015, 2015-2016,
        2016-2017.
        De tweede MenuButton is leerjaar: de items zijn: jaar 1, jaar 2, 
        jaar 3 of jaar 4.
        De derde MenuButton is periode: de items zijn: periode 1, periode 2.
        periode 3 en periode 4.
        De vierde MenuButton is module, er zijn hier nog geen items, 
        dit wordt nader bepaald.
        De vijfde MenuButton is toetsvorm, de items zijn: theorietoets,
        praktijktoets, opdracht, aanwezigheid, logboek en project.
        De laatste MenuButton is gelegenheid, de items zijn: gelegenheid1 en 
        gelegenheid2.
        De MenuButtons worden toegevoegd aan een vbox, de grootte van de buttons
        wordt zo gezet dat de grootte meebeweegt met het groter en kleiner 
        maken van het scherm.
        Ten slotte wordt de methode BoxenVullen aangeroepen,
        deze krijgt de parameters vbox2 en hbox mee.
        */
        lbl1 = maakObject(new Label(), "Keuzemenu");
        lbl1.setPrefWidth(150);
        year = new ChoiceBox(FXCollections.observableArrayList("Jaartal", 
                new Separator(), "placeholder"));
        year.setValue("Jaartal");
        year.setPrefWidth(150);
        year.setPrefHeight(30);
        studyyear = new ChoiceBox(FXCollections.observableArrayList("Leerjaar", 
                new Separator(), "Leerjaar 1", "Leerjaar 2", "Leerjaar 3", 
                "Leerjaar 4"));
        studyyear.setValue("Leerjaar");
        studyyear.setPrefWidth(150);
        studyyear.setPrefHeight(30);
        period = new ChoiceBox(FXCollections.observableArrayList("Periode",
                new Separator(), "Periode 1", "Periode 2", "Periode 3", 
                "Periode 4"));
        period.setValue("Periode");
        period.setPrefWidth(150);
        period.setPrefHeight(30);
        module = new ChoiceBox(FXCollections.observableArrayList("Module", 
                new Separator(), "placeholder"));
        module.setValue("Module");
        module.setPrefWidth(150);
        module.setPrefHeight(30);
        type = new ChoiceBox(FXCollections.observableArrayList("Toetsvorm", 
                new Separator(), "Theorietoets", "Praktijktoets", "Opdracht",
                "Aanwezigheid", "Logboek", "Project"));
        type.setValue("Toetsvorm");
        type.setPrefWidth(150);
        type.setPrefHeight(30);
        chance = new ChoiceBox(FXCollections.observableArrayList("Gelegenheid",
                new Separator(), "1e kans", "2e kans"));     
        chance.setValue("Gelegenheid");
        chance.setPrefWidth(150);
        chance.setPrefHeight(30);
        Region fill = new Region();
        VBox.setVgrow(fill, Priority.ALWAYS);
        
        btn1 = maakObject(new Button(), "Toets laden", 30, 150);
        btn1.setPrefWidth(150);
        
        VBox vbox2 = new VBox();
        vbox2.setPrefSize(150, 600);

        vbox2.getChildren().addAll(lbl1, year, studyyear, period, module, 
                type, chance, fill, btn1);
        
        vbox2.setSpacing(20);
        return vbox2;   
        
    }
    

    public void BoxenVullen(VBox vbox2, HBox hbox){
        /*
        Er wordt eerst een label aangemaakt met de naam: Vragen,
        deze krijgt de afmetingen 50 bij 700 mee aan de maakObject methoden.
        Ook wordt er een knop aangemaakt met de label: nieuwe student.
        Er wordt een HBox aangemaakt,
        de label en de knop worden toegevoegd aan een hbox.
        Vervolgens wordt er een pointsTable gemaakt, 
        met een grootte van 500 bij 500.
        In deze ruimte komen later de studenten en hun punten te staan.
        Er wordt een VBox aangemaakt,
        de pointsTable wordt toegevoegd aan de vbox en hier wordt een Vgrow
        aan toegevoegd. Aan de vbox wordt ook de hbox met de label en knop
        toegevoegd.
        Er wordt nog een HBox aangemaakt,
        aan deze hbox wordt de vbox met het keuzemenu toegevoegd,
        ook wordt de vbox toegevoegd met de pointsTable, knop en label die
        net is gemaakt.
        Ten slotte wordt nog een VBox aangemaakt,
        aan deze vbox wordt de net gemaakte hbox toegevoegd en de hbox met
        de knoppen die onderaan het scherm komen.
        Er wordt een Vgrow toegepast op de hboxen die in de laatste vbox
        aanwezig zijn.
        Ten slotte wordt de vbox toegepast aan this, hierdoor kan
        deze klasse ook gebruikt worden in de main.
        */
        
        Region fillLeft = new Region();
        HBox.setHgrow(fillLeft, Priority.ALWAYS);
        lbl2 = maakObject(new Label(), "Vragen");
        Region fillRight = new Region();
        HBox.setHgrow(fillRight, Priority.ALWAYS);
        btn5 = maakObject(new Button(),"Nieuwe Student", 30, 80);
        btn5.setPrefWidth(150);
        
        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(btn5, fillLeft, lbl2, fillRight);
       
        pointsTable = new TableView();
        VBox.setVgrow(pointsTable, Priority.ALWAYS);
        
        VBox vbox3 = new VBox();
        VBox.setVgrow(pointsTable, Priority.ALWAYS);
        
        vbox3.setSpacing(10);
        vbox3.getChildren().addAll(hbox3, pointsTable, hbox);
        
        
        HBox hbox2 = new HBox();
        HBox.setHgrow(vbox3, Priority.ALWAYS);
        
        HBox.setMargin(vbox2, new Insets(5));
        HBox.setMargin(vbox3, new Insets(5));
        
        hbox2.setSpacing(20);
        hbox2.getChildren().addAll(vbox2, vbox3);
        
        this.getChildren().add(hbox2);
        
    }
    
 }
    



    


