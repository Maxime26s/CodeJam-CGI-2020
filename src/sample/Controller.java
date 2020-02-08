package sample;
import classes.Mesures;
import classes.Produit;
import classes.Gestionnaire;
import classes.ProduitInventaire;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;

import javax.swing.text.html.ImageView;

public class Controller {



    public static Gestionnaire gestionnaire = new Gestionnaire();
    public static ArrayList<String[]> commande = new ArrayList<String[]>();


    @FXML
    ListView listeGardeManger;

    @FXML
    DialogPane affichageInfo;

    @FXML
    TextField boiteRecherche;

    public void refresh()
    {
        ArrayList<String> arrayGardeManger = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            arrayGardeManger.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(arrayGardeManger);
        listeGardeManger.setItems(observableList);
    }

    public void resultatRecherche()
    {
        ArrayList<String> arrayGardeManger = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().contains(boiteRecherche.getText()))
            {
            arrayGardeManger.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
            }
            ObservableList<String> observableList = FXCollections.observableArrayList(arrayGardeManger);
            listeGardeManger.setItems(observableList);
        }
    }

    public void showInfo()
    {
        String nomProduit = Main.gestionnaire.getInventaire().get(listeGardeManger.getSelectionModel().getSelectedIndex()).getProduit().getNom();
        for(int i = 0; i < Main.gestionnaire.getInventaire().size(); i++)
        {
            if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().contains(nomProduit))
            {
                String infoBuffer = Main.gestionnaire.getInventaire().get(i).getProduit().getNom()
                        + "\t" +
                "Expiration: " + Main.gestionnaire.getInventaire().get(i).getDateExp().getDay() + "-" + Main.gestionnaire.getInventaire().get(i).getDateExp().getMonth() + "-" + Main.gestionnaire.getInventaire().get(i).getDateExp().getYear()
                        + "\t" +
                Main.gestionnaire.getInventaire().get(i).getQuantite() + " " + Main.gestionnaire.getInventaire().get(i).getTypeMesure().toString();
                affichageInfo.setContentText(infoBuffer);
            }
        }
    }

    /*
    public void addProduit(){
        LocalDate date = LocalDate.now();
        Produit produit = new Produit("nom", "1111", 10.50f, 10, Mesures.LITRE);
        gestionnaire.getInventaire().add(new ProduitInventaire(produit));
    }
    */

    public void addInventaire(){

    }

    public void addCommande(){
        String parts[] = new String[3];
        parts[0] = "read name";
        parts[1] = "amount wanted";
        parts[2] = "price per unit";
        commande.add(parts);
    }

    public static void sendData(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("commande\n");
            sortie.write(commande.size()+"\n");
            for(int i=0;i<commande.size();i++) {
                sortie.write(commande.get(i)[0]+"\n");
                sortie.write(commande.get(i)[1]+"\n");
                sortie.write(commande.get(i)[2]+"\n");
            }
            sortie.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void openAddToGardeMangerWindow()
    {
        try
        {
            Parent addToGardeMangerScene = FXMLLoader.load(getClass().getResource("addToGardeManger.fxml"));
            Main.addToGardeMangerStage.setTitle("Watchu Puttin' in yer Frigo?");
            try
            {
                Main.addToGardeMangerStage.initModality(Modality.APPLICATION_MODAL);
            }
            catch(Exception ignored) {}
            Main.addToGardeMangerStage.setScene(new Scene(addToGardeMangerScene, 480, 400));
            Main.addToGardeMangerStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
