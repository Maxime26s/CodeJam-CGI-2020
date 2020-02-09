package sample;

import classes.Gestionnaire;
import classes.ProduitInventaire;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    public static Gestionnaire gestionnaire = new Gestionnaire();
    public static ArrayList<String[]> commande = new ArrayList<String[]>();
    public Timeline timerLoop;


    @FXML
    ListView listeGardeManger, listeRecettes;

    @FXML
    Label affichageInfo;

    @FXML
    TextField boiteRecherche, boiteRecherche1;

    @FXML
    ListView listApprochantPeremption;

    @FXML
    ListView listePerimes;

    //Timer stuff
    @FXML
    TextField tempsField;

    @FXML
    ChoiceBox unitesTemps;

    @FXML
    Label temps, affichageInfo1;

    @FXML
    ProgressIndicator progression;

    @FXML
    Button boutonStart;

    @FXML
    Button boutonStop;

    public void refresh()
    {
        ArrayList<String> arrayGardeManger = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            arrayGardeManger.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(arrayGardeManger);
        Main.gestionnaire.checkExpiry();
        listeGardeManger.setItems(observableList);

        ArrayList<String> arrayRecettes = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getRecettes().size(); i++) {
            arrayRecettes.add(Main.gestionnaire.getRecettes().get(i).getNom());
        }
        ObservableList<String> observableList1 = FXCollections.observableArrayList(arrayRecettes);
        Main.gestionnaire.checkExpiry();
        listeRecettes.setItems(observableList1);
        Main.gestionnaire.saveInventaire();
        Main.gestionnaire.saveRecettes();
    }

    public void supprimerAliment() {
        try {
            Main.gestionnaire.getInventaire().remove(listeGardeManger.getSelectionModel().getSelectedIndex());
            refresh();
        } catch (Exception e) {
            System.out.println("Aucun aliment selectionné");
        }
    }

    public void supprimerRecette()
    {
        try
        {
            Main.gestionnaire.getRecettes().remove(listeRecettes.getSelectionModel().getSelectedIndex());
            refresh();
        }
        catch(Exception e)
        {
            System.out.println("Aucun aliment selectionné");
        }
    }

    public void resultatRecherche()
    {
        ArrayList<String> rechercheGardeManger = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().toLowerCase().contains(boiteRecherche.getText().toLowerCase())) {
                rechercheGardeManger.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
            }
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(rechercheGardeManger);
        listeGardeManger.setItems(observableList);
    }

    public void resultatRecherche1()
    {
        ArrayList<String> rechercheRecette = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getRecettes().size(); i++) {
            if (Main.gestionnaire.getRecettes().get(i).getNom().toLowerCase().contains(boiteRecherche1.getText().toLowerCase()))
            {
                rechercheRecette.add(Main.gestionnaire.getRecettes().get(i).getNom());
            }
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(rechercheRecette);
        listeRecettes.setItems(observableList);
    }

    public void showInfo()
    {
        String nomProduit = listeGardeManger.getItems().get(listeGardeManger.getSelectionModel().getSelectedIndex()).toString();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().contains(nomProduit)) {
                String infoBuffer = Main.gestionnaire.getInventaire().get(i).getProduit().getNom()
                        + "\n" +
                        "Expiration: " + Main.gestionnaire.getInventaire().get(i).getDateExp().getDay() + "-" + Main.gestionnaire.getInventaire().get(i).getDateExp().getMonth() + "-" + Main.gestionnaire.getInventaire().get(i).getDateExp().getYear()
                        + "\n" +
                        Main.gestionnaire.getInventaire().get(i).getQuantite() + " " + Main.gestionnaire.getInventaire().get(i).getProduit().getMesureType()
                        + "\n" + "Jours avant expiration: " +
                        Main.gestionnaire.getInventaire().get(i).getJoursExpiration();
                affichageInfo.setText(infoBuffer);
            }
        }
    }

    public void showRecette() {
        String nomRecette = listeRecettes.getItems().get(listeRecettes.getSelectionModel().getSelectedIndex()).toString();
        for (int i = 0; i < Main.gestionnaire.getRecettes().size(); i++) {
            if (Main.gestionnaire.getRecettes().get(i).getNom().contains(nomRecette)) {
                String infoBuffer = Main.gestionnaire.getRecettes().get(i).getNom()
                        + "\n" + "\n" +
                        "Ingrédients: "
                        + "\n";
                for (ProduitInventaire produitInventaire :
                        Main.gestionnaire.getRecettes().get(i).getIngredientsRequis()) {
                    infoBuffer = infoBuffer + produitInventaire.getProduit().getNom() + ": "
                            + produitInventaire.getQuantite() + produitInventaire.getTypeMesure() + "\n";
                }
                infoBuffer = infoBuffer +  "\n" +  "\n" + Main.gestionnaire.getRecettes().get(i).getInstructions()
                        + "\n" + "\n" + Main.gestionnaire.getRecettes().get(i).getPrix() + "$" + "\n";
                if (Main.gestionnaire.getRecettes().get(i).getTags().size() != 0){
                    infoBuffer = infoBuffer + "[";
                    for (String tag:
                            Main.gestionnaire.getRecettes().get(i).getTags()) {
                        infoBuffer = infoBuffer + tag + ", ";
                    }
                    infoBuffer = infoBuffer.substring(0, infoBuffer.length()-2) + "]";
                }
                affichageInfo1.setText(infoBuffer);
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

    public void addInventaire() {

    }

    public void addCommande() {
        String parts[] = new String[3];
        parts[0] = "read name";
        parts[1] = "amount wanted";
        parts[2] = "price per unit";
        commande.add(parts);
    }

    public static void sendData() {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("commande\n");
            sortie.write(commande.size() + "\n");
            for (int i = 0; i < commande.size(); i++) {
                sortie.write(commande.get(i)[0] + "\n");
                sortie.write(commande.get(i)[1] + "\n");
                sortie.write(commande.get(i)[2] + "\n");
            }
            sortie.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAddToGardeMangerWindow() {
        try {
            Parent addToGardeMangerScene = FXMLLoader.load(getClass().getResource("addToGardeManger.fxml"));
            Main.addToGardeMangerStage.setTitle("Que voulez-vous commander?");
            try {
                Main.addToGardeMangerStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception ignored) {
            }
            addToGardeMangerScene.getStylesheets().add("modena_dark.css"); //Dark Theme: https://github.com/joffrey-bion/javafx-themes
            Main.addToGardeMangerStage.setScene(new Scene(addToGardeMangerScene, 765, 640));
            Main.addToGardeMangerStage.setResizable(false);
            Main.addToGardeMangerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAddRecetteWindow() {
        try {
            Parent addRecetteScene = FXMLLoader.load(getClass().getResource("AddRecette.fxml"));
            Main.addRecetteStage.setTitle("Nouvelle recette");
            try {
                Main.addRecetteStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception ignored) {
            }
            addRecetteScene.getStylesheets().add("modena_dark.css"); //Dark Theme: https://github.com/joffrey-bion/javafx-themes
            Main.addRecetteStage.setScene(new Scene(addRecetteScene, 695, 640));
            Main.addRecetteStage.setResizable(false);
            Main.addRecetteStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePerimes() {
        ArrayList<ProduitInventaire> produitsPerimes = new ArrayList<>();
        ArrayList<ProduitInventaire> produitsEnDanger = new ArrayList<>();

        Main.gestionnaire.checkExpiry();
        for (ProduitInventaire produit :
                Main.gestionnaire.getInventaire()) {
            if (produit.isExpire()) {
                produitsPerimes.add(produit);
            } else if (produit.getJoursExpiration() <= 3) {
                produitsEnDanger.add(produit);
            }
        }

        ArrayList<String> produitsPerimesString = new ArrayList<>();
        for (ProduitInventaire produit :
                produitsPerimes) {
            produitsPerimesString.add(produit.getProduit().getNom());
        }

        ObservableList<String> observableList1 = FXCollections.observableList(produitsPerimesString);
        listePerimes.setItems(observableList1);

        ArrayList<String> produitsEnDangerString = new ArrayList<>();
        for (ProduitInventaire produit :
                produitsEnDanger) {
            produitsEnDangerString.add(produit.getProduit().getNom() + "   " + Integer.toString(produit.getJoursExpiration()));
        }
        ObservableList<String> observableList2 = FXCollections.observableList(produitsEnDangerString);
        listApprochantPeremption.setItems(observableList2);
    }

    public void initUnitesTimer() {
        ArrayList<String> unites = new ArrayList<>();
        unites.add("secondes");
        unites.add("minutes");
        unites.add("heures");
        ObservableList<String> observableList = FXCollections.observableList(unites);
        unitesTemps.setItems(observableList);
    }

    public void startTimer() {
        boutonStart.setDisable(true);
        boutonStop.setDisable(false);
        boutonStop.setVisible(true);
        boutonStart.setVisible(false);
        AtomicInteger timerInit = new AtomicInteger(0);
        if (unitesTemps.getSelectionModel().getSelectedItem().equals("secondes")) {
            timerInit.set(Integer.parseInt(tempsField.getText()));
        } else if (unitesTemps.getSelectionModel().getSelectedItem().equals("minutes")) {
            timerInit.set(Integer.parseInt(tempsField.getText()) * 60);
        } else if (unitesTemps.getSelectionModel().getSelectedItem().equals("heures")) {
            timerInit.set(Integer.parseInt(tempsField.getText()) * 3600);
        }


        AtomicInteger myCurrentTime = new AtomicInteger(timerInit.get());
        timerLoop = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent arg) {
                myCurrentTime.set(myCurrentTime.get() - 1);
                temps.setText("Timer :" + myCurrentTime.get());
                progression.setProgress(Double.valueOf(Double.valueOf((timerInit.get() - myCurrentTime.get())) / Double.valueOf(timerInit.get())));
            }
        }));
        timerLoop.setCycleCount(timerInit.get());
        timerLoop.play();
    }

    public void stopTimer() {
        boutonStart.setDisable(false);
        boutonStop.setDisable(true);
        boutonStop.setVisible(false);
        boutonStart.setVisible(true);
        timerLoop.stop();
    }

    public void openAddRecette() {
        try {
            Parent addToGardeMangerScene = FXMLLoader.load(getClass().getResource("AddRecette.fxml"));
            Main.addToGardeMangerStage.setTitle("Watchu Puttin' in yer Frigo?");
            try {
                Main.addToGardeMangerStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception ignored) {
            }
            Main.addToGardeMangerStage.setScene(new Scene(addToGardeMangerScene, 480, 400));
            Main.addToGardeMangerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateRandomRecette() {
        String[] verbeArray = {"Ajouter", "Piler", "Mariner", "Mélanger", "Faire cuire", "Battre"};
        String[] adjectifArray = {"vigoureusement", "abondamment", "efficacement", "rapidement", "à l'ancienne", "avec précaution"};
        String[] timeArray = {"pendant 5 minutes", "jusqu'à texture uniforme", "à souhait", "jusqu'à ébullition", "jusqu'à ce que le mélange épaississe", "généreusement"};
        String[] containerArray = {"dans un bol", "dans une assiette", "dans une chaudron", "dans un bocal", "dans le four", "dans le micro-ondes"};
        ArrayList<ProduitInventaire> gardemanger = Main.gestionnaire.getInventaire();
        String finalrecette = "";
        int[] quantite = new int[gardemanger.size()];
        for (int i = 0; i < gardemanger.size(); i++) {
            float mesure = Math.round(Float.parseFloat(gardemanger.get(i).getProduit().getMesurePoids()) * 1000.0f) / 1000.0f;
            if (mesure >= 1000) {
                mesure *= 0.001;

            } else if (mesure < 1) {
                mesure *= 1000;
            }
            Random rand = new Random();
            quantite[i] = (int) (Math.random() * mesure) + 1;

            finalrecette = finalrecette + quantite[i] + " " + gardemanger.get(i).getProduit().getMesureType() + " de " + gardemanger.get(i).getProduit().getNom().toLowerCase() + "\n";
        }
        for (int i = 0; i < gardemanger.size(); i++) {
            float mesure = Math.round(Float.parseFloat(gardemanger.get(i).getProduit().getMesurePoids()) * 1000.0f) / 1000.0f;
            if (mesure >= 1000) {
                mesure *= 0.001;

            } else if (mesure < 1) {
                mesure *= 1000;
            }
            Random rand = new Random();
            int verbeint = rand.nextInt(verbeArray.length);
            int adjectifint = rand.nextInt(adjectifArray.length);
            int timeint = rand.nextInt(timeArray.length);
            int containerint = rand.nextInt(containerArray.length);
            finalrecette = finalrecette + verbeArray[verbeint] + " " + adjectifArray[adjectifint] + " " + quantite[i] + " " + gardemanger.get(i).getProduit().getMesureType() + " de " + gardemanger.get(i).getProduit().getNom().toLowerCase() + " " + containerArray[containerint] + " " + timeArray[timeint] + "\n";
        }
        System.out.print(finalrecette);
        String[] ingredients = {"agneau", "boeuf", "dinde", "fruit de mer", "gibier", "légumineuses", "oeufs", "oie", "pintade", "volaille", "orge", "quinoa", "poisson", "porc", "poulet", "riz", "tofu", "soya", "veau"};
        String[] debuts = {"bol de", "bol de riz au", "mijoté de", "pilon de", "pizza au", "pâte au", "fondue au", "macaroni au", "poulet grillé au", "pain doré au", "granola au", "tofu au", "gauffres au", "bacon au"};
        Random rand = new Random();
        int ingredientint = rand.nextInt(ingredients.length);
        int ingredientint2 = rand.nextInt(ingredients.length);
        int debutsint = rand.nextInt(debuts.length);
        int auint = rand.nextInt(2);
        if (auint == 1) {
            System.out.print(debuts[debutsint].substring(0, 1).toUpperCase() + debuts[debutsint].substring(1) + " " + ingredients[ingredientint]);
        } else {
            System.out.print(debuts[debutsint].substring(0, 1).toUpperCase() + debuts[debutsint].substring(1) + " " + ingredients[ingredientint] + " et au " + ingredients[ingredientint2]);
        }
    }
}
