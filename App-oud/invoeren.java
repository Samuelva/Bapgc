
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
        De methode menuUnder wordt aangeroepen en maakt een HBox aan met
        drie knoppen.
        De methode MenuMaken wordt aangeroepen, deze maakt het keuzemenu aan.
        BozenVullen zet vervolgens het scherm in elkaar.
        */
        
        HBox hbox = menuUnder();
        VBox vbox2 = MenuMaken();
        BoxenVullen(vbox2, hbox); 

        
    }
    
    public HBox menuUnder(){
        /*
        Er wordt een regio gemaakt voor centreren van de knoppen.
        btn2 krijgt de label Leeg maken en de afmetingen 150 bij 30 worden
        meegegeven aan de methode maakObject.
        btn3 krijgt de label Wijzigingen opslaan en de afmetingen 150 bij 30
        worden meegegeven aan de methode maakObject.
        btn4 krijgt de label Import CSV en de ametingen 150 bij 30 worden
        meegegeven aan de methode Import CSV.
        Er wort nog een regio gemaakt voor het centreren van de knoppen.
        Er wordt een HBox aangemaakt met de naam hbox.
        Vervolgens worden alle knoppen en regios toegevoegd aan de hbox.
        De spacing in hbox wordt op 20 gezet.
        hbox wordt terug gegeven.
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
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(150);
        
        return btn;
        
    }
    
    public Label maakObject(Label lbl, String tekst){
        /*
        Deze functie zet de naam van de labels, laat ze centreren en
        geeft ze het font Arial met grootte 18.
        */
        lbl.setText(tekst);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(new Font("Arial", 18));
        return lbl;
    }
    
    
    public VBox MenuMaken(){
        /*
        In deze functie wordt het menu aan de linkerkant gemaakt.
        lbl1 krijgt de tekst Keuzemnu, de breedte wordt op 150 gezet.
        De eerste ChoiceBox is year.
        De tweede ChioceBox is studyyear: de items zijn: jaar 1, jaar 2, 
        jaar 3 of jaar 4.
        De derde ChoiceBox is periode: de items zijn: periode 1, periode 2.
        periode 3, periode 4 en periode 5.
        De vierde ChoiceBox is module, er zijn hier nog geen items, 
        dit wordt nader bepaald.
        De vijfde ChoiceBox is toetsvorm, de items zijn: theorietoets,
        praktijktoets, opdracht, aanwezigheid, logboek en project.
        De laatste ChoiceBox is gelegenheid, de items zijn: 1e kans en 
        2e kans.
        Alle ChoiceBoxes krijgen een hoogte van 30 en een breedte van 150.
        er wordt een Region gemaakt die (door VGrow) btn1 naar beneden duwt.
        btn1 krijgt het label Toets laden, een hoogte van 30 en een breedte
        van 150.
        De choiceboxes en btn1 worden toegevoegd aan een vbox.
        De spacing van de vbox wordt op 20 gezet.
        De vbox wordt terug gegeven.
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

        vbox2.getChildren().addAll(lbl1, year, studyyear, period, module, 
                type, chance, fill, btn1);
        
        vbox2.setSpacing(20);
        return vbox2;   
        
    }
    

    public void BoxenVullen(VBox vbox2, HBox hbox){
        /*
        Er wordt eerst een label aangemaakt met de naam: Vragen.
        Ook wordt er een knop aangemaakt met de label: nieuwe student.
        Er wordt een HBox aangemaakt,
        de label en de knop worden toegevoegd aan een hbox, same met twee
        Region die er voor zorgen dat het label gecentreerd wordt.
        Vervolgens wordt er een TableView (pointsTable) gemaakt.
        In deze ruimte komen later de studenten en hun punten te staan.
        Er wordt een VBox aangemaakt,
        de pointsTable wordt toegevoegd aan de vbox en hier wordt een Vgrow
        aan toegevoegd. Aan de vbox wordt ook de hbox met de label en knop
        toegevoegd en de hbox die als parameter meegegeven wordt.
        De vbox krijgt een spacing van 10.
        Er wordt nog een HBox aangemaakt,
        aan deze hbox wordt de vbox met het keuzemenu toegevoegd,
        ook wordt de vbox toegevoegd met de pointsTable, knoppen en label die
        net is gemaakt.
        Hiervoor worden margins van 5 om beide vboxes gezet.
        De laatste hbox krijget een spacing van 20.
        Ten slotte wordt de hbox toegepast aan this, hierdoor kan
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
    



    

