
package sample;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/*
Programmeur: Anne van Winzum
Datum laatste aanpassing: 5-12-2016
Beschrijving: In dit script wordt de eerste layout van het invoerscherm getoond.
*/

public final class Invoeren extends Pane {
    
   
        
    public Invoeren() {
        /*
        Eerst worden er 4 knoppen gemaakt, deze komen onderaan het scherm.
        De knoppen krijgen de label: Toets laden, Leeg maken, Wijzigingen opslaan
        en Import CSV. De afmetingen worden meegegeven aan de functie maakObject
        Button.
        De knoppen worden in een hbox geplaatst en er wordt een Hgrow toegepast,
        hierdoor zal de grootte meebewegen als het scherm grote of kleiner 
        wordt gemaakt.
        */
        
        menuUnder();
        

        
    }
    
    public void menuUnder(){
        Button btn1 = maakObject(new Button(), "Toets laden", 250, 50);
        Button btn2 = maakObject(new Button(), "Leeg maken", 250, 50);
        Button btn3 = maakObject(new Button(),"Wijzigingen opslaan", 250, 50);
        Button btn4 = maakObject(new Button(),"Import CSV", 250,50);

            
        HBox hbox = new HBox();
        HBox.setHgrow(btn1,Priority.ALWAYS);
        HBox.setHgrow(btn2,Priority.ALWAYS);
        HBox.setHgrow(btn3,Priority.ALWAYS);
        HBox.setHgrow(btn4,Priority.ALWAYS);
        
        hbox.setPrefSize(1000, 100);
        hbox.getChildren().addAll(btn1, btn2, btn3, btn4);
        
        MenuMaken(hbox);
        
    }

    public Button maakObject(Button btn, String tekst, double hoogte,
            double breedte){
        /*
        Deze functie zet de naam, hoogte en breedte van de knoppen.
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(breedte);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        
        return btn;
        
    }
    
    public Label maakObject(Label lbl, String tekst, 
            double hoogte, double breedte){
        /*
        Deze functie zet de naam, hoogte en breedte van de labels.
        */
        lbl.setText(tekst);
        lbl.setPrefHeight(hoogte);
        lbl.setPrefWidth(breedte);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(Font.font(null, FontWeight.BOLD, 15));
        return lbl;
    }
    
    
    public void MenuMaken(HBox hbox){
        /*
        In deze functie wordt het menu aan de linkerkant gemaakt.
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
        */
        Label lbl1 = maakObject(new Label(), "Keuzemenu", 100, 100);
 
        MenuItem jaartal1 = new MenuItem("2014-2015");
        MenuItem jaartal2 = new MenuItem("2015-2016");
        MenuItem jaartal3 = new MenuItem("2016-2017");
        MenuButton jaartal = new MenuButton("Jaartal", null, 
                jaartal1, jaartal2, jaartal3);
        
        MenuItem leerjaar1 = new MenuItem("Jaar 1");
        MenuItem leerjaar2 = new MenuItem("Jaar 2");
        MenuItem leerjaar3 = new MenuItem("Jaar 3");
        MenuItem leerjaar4 = new MenuItem("Jaar 4");
        MenuButton leerjaar = new MenuButton("Leerjaar", null, leerjaar1, 
                leerjaar2, leerjaar3, leerjaar4);
        
        MenuItem periode1 = new MenuItem("Periode 1");
        MenuItem periode2 = new MenuItem("Periode 2");
        MenuItem periode3 = new MenuItem("Periode 3");
        MenuItem periode4 = new MenuItem("Periode 4");
        MenuButton periode = new MenuButton("Periode", null, periode1, 
                periode2, periode3, periode4);
        
        MenuItem module1 = new MenuItem("Modules");
        MenuButton modules = new MenuButton("Module", null, module1);
        
        MenuItem theorietoets = new MenuItem("Theorietoets");
        MenuItem praktijktoets = new MenuItem("Praktijktoets");
        MenuItem opdracht = new MenuItem("Opdracht");
        MenuItem aanwezigheid = new MenuItem("Aanwezigheid");
        MenuItem logboek = new MenuItem("Logboek");
        MenuItem project = new MenuItem("Project"); 
        MenuButton toetsvorm = new MenuButton("Toetsvorm", null, theorietoets, 
                praktijktoets, opdracht, aanwezigheid, logboek, project);
        
        MenuItem gelegenheid1 = new MenuItem("Gelegenheid 1");
        MenuItem gelegenheid2 = new MenuItem("Gelegenheid 2");
        MenuButton gelegenheid = new MenuButton("Gelegenheid", null, 
                gelegenheid1, gelegenheid2);     
        
        VBox vbox2 = new VBox();
        vbox2.setPrefSize(150, 600);
        jaartal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        leerjaar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        periode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        modules.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toetsvorm.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gelegenheid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbox2.getChildren().addAll(lbl1, jaartal, leerjaar, periode, modules, 
                toetsvorm, gelegenheid);
        
        BoxenVullen(vbox2, hbox);    
        
    }
    

    public void BoxenVullen(VBox vbox2, HBox hbox){
        /*
        Er wordt eerst een label aangemaakt met de naam: Vragen.
        Ook wordt er een knop aangemaakt met de label: nieuwe student.
        De label en de knop worden toegevoegd aan een hbox.
        Vervolgens wordt er een textArea gemaakt, met een grote van 850 bij 500.
        In deze ruimte komen later de studenten en hun punten te staan.
        De textArea wordt toegevoegd aan een vbox en hier wordt een Vgrow
        aan toegevoegd. Aan de vbox wordt ook de hbox met de label en knop
        toegevoegd.
        Aan een nieuwe hbox wordt de vbox met het keuzemenu toegevoegd,
        ook wordt de vbox toegevoegd met de textArea, knop en label die
        net is gemaakt.
        Aan een nieuwe vbox wordt de net gemaakte hbox toegevoegd en de hbox met
        de knoppen die onderaan het scherm komen.
        De scene wordt gezet op de vbox en de grootte van het scherm wordt 
        1000 bij 600.
        
        */
        
        Label lbl2 = maakObject(new Label(), "Vragen", 50, 700);
        Button btn5 = maakObject(new Button(),"Nieuwe Student", 50, 150);
        
        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(btn5, lbl2);
       
        TextArea textArea = new TextArea();
        textArea.setPrefSize(500,500);
        
        VBox vbox3 = new VBox();
        VBox.setVgrow(textArea, Priority.ALWAYS);
        vbox3.getChildren().addAll(hbox3, textArea);
        
        HBox hbox2 = new HBox();
        HBox.setHgrow(vbox3, Priority.ALWAYS);
        hbox2.getChildren().addAll(vbox2, vbox3);
        
        VBox vbox = new VBox();
        VBox.setVgrow(hbox2, Priority.ALWAYS);
        VBox.setVgrow(hbox, Priority.ALWAYS);
        vbox.getChildren().addAll(hbox2, hbox);
        this.getChildren().add(vbox);
        
    }
    
 }
    


