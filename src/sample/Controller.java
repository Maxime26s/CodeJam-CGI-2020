package sample;

import classes.ComparateurExpiry;
import classes.Gestionnaire;
import classes.ProduitInventaire;
import classes.Recette;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    public static Gestionnaire gestionnaire = new Gestionnaire();
    public static ArrayList<String[]> commande = new ArrayList<String[]>();
    public Timeline timerLoop;
    public Recette recetteSelected;


    @FXML
    ListView listeGardeManger, listeRecettes;

    @FXML
    Label affichageInfo;

    @FXML
    TextField boiteRecherche, boiteRecherche1;

    @FXML
    ListView listApprochantPeremption;

// Timer
    @FXML
    TextField tempsField;

    @FXML
    ChoiceBox unitesTemps;

    @FXML
    Label temps, affichageInfo1, recetteAleatoire;

    @FXML
    ProgressIndicator progression;

    @FXML
    Button boutonStart;

    @FXML
    Button boutonStop;

    public static int selectedIndex = 0;

    // Rafraichit l'affichage des infos et du garde-manger
    public void refresh() {
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
        updatePerimes();
        showInfo();
        Main.gestionnaire.saveProduits();
    }
    // Supprime un aliment du garde-manger
    public void supprimerAliment() {
        try {
            Main.gestionnaire.getInventaire().remove(listeGardeManger.getSelectionModel().getSelectedIndex());
            refresh();
        } catch (Exception e) {
            System.out.println("Aucun aliment selectionné");
        }
    }
    // Modifie la date d'expiration d'un aliment
    public void modifierAliment() {
        try {
            selectedIndex = Main.gestionnaire.getInventaire().indexOf(Main.gestionnaire.getInventaire().get(listeGardeManger.getSelectionModel().getSelectedIndex()));
            Parent modifierAlimentScene = FXMLLoader.load(getClass().getResource("modifierAliment.fxml"));
            Main.modifierAlimentStage.setTitle(Main.gestionnaire.getInventaire().get(selectedIndex).getProduit().getNom());
            try {
                Main.modifierAlimentStage.initModality(Modality.APPLICATION_MODAL);
            } catch (Exception ignored) {
            }
            modifierAlimentScene.getStylesheets().add("modena_dark.css"); //Dark Theme: https://github.com/joffrey-bion/javafx-themes
            Main.modifierAlimentStage.setScene(new Scene(modifierAlimentScene, 380, 220));
            Main.modifierAlimentStage.setResizable(false);
            Main.modifierAlimentStage.show();
            refresh();
        } catch (Exception e) {
            System.out.println("Aucun aliment selectionné");
        }
    }
    // Supprime une recette du livre de recettes
    public void supprimerRecette() {
        try {
            Main.gestionnaire.getRecettes().remove(listeRecettes.getSelectionModel().getSelectedIndex());
            refresh();
        } catch (Exception e) {
            System.out.println("Aucun aliment selectionné");
        }
    }
    // créer une recherche dans le garde-manger
    public void resultatRecherche() {
        ArrayList<String> rechercheGardeManger = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
            if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().toLowerCase().contains(boiteRecherche.getText().toLowerCase())) {
                rechercheGardeManger.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
            }
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(rechercheGardeManger);
        listeGardeManger.setItems(observableList);
    }
    // créer une recherche dans le livre de recette
    public void resultatRecherche1() {
        ArrayList<String> rechercheRecette = new ArrayList<String>();
        for (int i = 0; i < Main.gestionnaire.getRecettes().size(); i++) {
            if (Main.gestionnaire.getRecettes().get(i).getNom().toLowerCase().contains(boiteRecherche1.getText().toLowerCase())) {
                rechercheRecette.add(Main.gestionnaire.getRecettes().get(i).getNom());
            }
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(rechercheRecette);
        listeRecettes.setItems(observableList);
    }
    // montre les informations de l'aliment selectionné dans le garde-manger
    public void showInfo() {
        try {
            String expiration = "";

            String nomProduit = listeGardeManger.getItems().get(listeGardeManger.getSelectionModel().getSelectedIndex()).toString();
            for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++) {
                if (Main.gestionnaire.getInventaire().get(i).getJoursExpiration() > 0) {
                    expiration = Main.gestionnaire.getInventaire().get(i).getJoursExpiration() + " Jours avant expiration.";
                } else {
                    expiration = "Expiré depuis " + -Main.gestionnaire.getInventaire().get(i).getJoursExpiration() + " Jours";
                }
                if (Main.gestionnaire.getInventaire().get(i).getProduit().getNom().contains(nomProduit)) {
                    String day;
                    String month;
                    if (Main.gestionnaire.getInventaire().get(i).getDateExp().getDay() < 10) {
                        day = "0" + Main.gestionnaire.getInventaire().get(i).getDateExp().getDay();
                    } else {
                        day = Integer.toString(Main.gestionnaire.getInventaire().get(i).getDateExp().getDay());
                    }
                    if (Main.gestionnaire.getInventaire().get(i).getDateExp().getMonth() < 10) {
                        month = "0" + Main.gestionnaire.getInventaire().get(i).getDateExp().getMonth();
                    } else {
                        month = Integer.toString(Main.gestionnaire.getInventaire().get(i).getDateExp().getMonth());
                    }
                    String infoBuffer = Main.gestionnaire.getInventaire().get(i).getProduit().getNom()
                            + "\n-------------------\n" +
                            "Expiration: " + day + "-" + month + "-" + Main.gestionnaire.getInventaire().get(i).getDateExp().getYear()
                            + "\n" +
                            Main.gestionnaire.getInventaire().get(i).getQuantite() + " " + Main.gestionnaire.getInventaire().get(i).getProduit().getMesureType()
                            + "\n" + expiration;
                    affichageInfo.setText(infoBuffer);
                }
            }
        } catch (Exception ignored) {
        }
        ;
    }
    // montre les informations de la recette selectionnée dans le livre
    public void showRecette() {
        String nomRecette = listeRecettes.getItems().get(listeRecettes.getSelectionModel().getSelectedIndex()).toString();
        for (int i = 0; i < Main.gestionnaire.getRecettes().size(); i++) {
            if (Main.gestionnaire.getRecettes().get(i).getNom().contains(nomRecette)) {
                recetteSelected = Main.gestionnaire.getRecettes().get(i);
                String infoBuffer = Main.gestionnaire.getRecettes().get(i).getNom()
                        + "\n-------------------" + "\n" +
                        "Ingrédients: "
                        + "\n";
                for (ProduitInventaire produitInventaire :
                        Main.gestionnaire.getRecettes().get(i).getIngredientsRequis()) {
                    infoBuffer = infoBuffer + produitInventaire.getProduit().getNom() + ": "
                            + produitInventaire.getQuantite() + produitInventaire.getTypeMesure() + "\n";
                }
                infoBuffer = infoBuffer + "\n-------------------" + "\n" + Main.gestionnaire.getRecettes().get(i).getInstructions()
                        + "\n-------------------\n" + "\n" + Main.gestionnaire.getRecettes().get(i).getPrix() + "$" + "\n";
                if (Main.gestionnaire.getRecettes().get(i).getTags().size() != 0) {
                    infoBuffer = infoBuffer + "[";
                    for (String tag :
                            Main.gestionnaire.getRecettes().get(i).getTags()) {
                        infoBuffer = infoBuffer + tag + ", ";
                    }
                    infoBuffer = infoBuffer.substring(0, infoBuffer.length() - 2) + "]";
                }
                affichageInfo1.setText(infoBuffer);
            }
        }
    }
    // construit et consume les ingrédients de la recette selectionnée
    public void consommerRecette() {
        Recette selected = recetteSelected;
        boolean assezIngredient = true;
        for (ProduitInventaire produitRecette :
                selected.getIngredientsRequis()) {
            for (ProduitInventaire produitInventaire :
                    Main.gestionnaire.getInventaire()) {
                if (produitRecette.getQuantite() > produitInventaire.getQuantite() && produitInventaire.getProduit().getNom().equals(
                        produitRecette.getProduit().getNom()
                )) {
                    assezIngredient = false;
                }
            }

        }
        if (assezIngredient) {
            for (ProduitInventaire produitRecette :
                    selected.getIngredientsRequis()) {
                for (ProduitInventaire produitInventaire :
                        Main.gestionnaire.getInventaire()) {
                    if (produitInventaire.getProduit().getNom().equals(produitRecette.getProduit().getNom())) {
                        produitInventaire.setQuantite(produitInventaire.getQuantite() - produitRecette.getQuantite());
                    }

                }
            }
        }
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
    // Ouvre fenetre achat
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
    // Ouvre fenetre ajout de recette
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
    // vérifie si des aliments vont périmer bientot
    public void updatePerimes() {
        ArrayList<ProduitInventaire> produitsPerimes = new ArrayList<>();
        ArrayList<ProduitInventaire> produitsEnDanger = new ArrayList<>();

        Main.gestionnaire.checkExpiry();
        for (ProduitInventaire produit :
                Main.gestionnaire.getInventaire()) {
            if (produit.getJoursExpiration() <= 15) {
                produitsPerimes.add(produit);
            }
        }

        Collections.sort(produitsPerimes, new ComparateurExpiry());

        ArrayList<String> produitsPerimesString = new ArrayList<>();
        for (ProduitInventaire produit :
                produitsPerimes) {
            if (produit.getJoursExpiration() > 0) {
                produitsPerimesString.add(produit.getProduit().getNom() + "   " + Integer.toString(produit.getJoursExpiration()) + " Jours restants");
            } else {
                produitsPerimesString.add(produit.getProduit().getNom() + "   " + " Expiré depuis " + Integer.toString(-produit.getJoursExpiration()) + " Jours");
            }
        }
        ObservableList<String> observableList2 = FXCollections.observableList(produitsPerimesString);
        listApprochantPeremption.setItems(observableList2);
    }
    // timer
    public void initUnitesTimer() {
        ArrayList<String> unites = new ArrayList<>();
        unites.add("secondes");
        unites.add("minutes");
        unites.add("heures");
        ObservableList<String> observableList = FXCollections.observableList(unites);
        unitesTemps.setItems(observableList);
        unitesTemps.setValue("secondes");
    }
    // debut timer
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
                temps.setText("Temps restant : " + myCurrentTime.get());
                progression.setProgress(Double.valueOf(Double.valueOf((timerInit.get() - myCurrentTime.get())) / Double.valueOf(timerInit.get())));
            }
        }));
        timerLoop.setCycleCount(timerInit.get());
        timerLoop.setOnFinished(event -> {
            String musicFile = "alarme.wav";

            Media sound = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

            Alert alerte = new Alert(Alert.AlertType.INFORMATION);
            alerte.setTitle("Alerte minuterie");
            alerte.setHeaderText("Minuterie terminée");
            alerte.setContentText(
                    "Appuyez sur le bouton pour l'arrêter");
            alerte.setOnCloseRequest(event1 -> {
                mediaPlayer.stop();
            });
            alerte.show();
        });
        timerLoop.play();
    }
    // stop timer
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
    // Genere une recette aléatoire avec les ingrédients possédés
    public void generateRandomRecette() {
        String[] verbeArray = {"Ajouter", "Piler", "Mariner", "Mélanger", "Faire cuire", "Battre"};
        String[] adjectifArray = {"vigoureusement", "abondamment", "efficacement", "rapidement", "à l'ancienne", "avec précaution"};
        String[] timeArray = {"pendant 5 minutes", "jusqu'à texture uniforme", "à souhait", "jusqu'à ébullition", "jusqu'à ce que le mélange épaississe", "généreusement"};
        String[] containerArray = {"dans un bol", "dans une assiette", "dans une chaudron", "dans un bocal", "dans le four", "dans le micro-ondes"};
        ArrayList<String> ingredTitre = new ArrayList<>();
        ArrayList<ProduitInventaire> gardemanger = (ArrayList<ProduitInventaire>) Main.gestionnaire.getInventaire().clone();
        int minIngredient, maxIngredient;
        String nomRecette = "";
        String finalrecette = "";
        minIngredient = 2;
        if (gardemanger.size() < 2) {
            minIngredient = gardemanger.size();
        }
        if (gardemanger.size() > 10) {
            maxIngredient = 10;
        } else {
            maxIngredient = gardemanger.size() - 1;
        }
        int nbIngredient = (int) (Math.random() * maxIngredient + minIngredient);
        int[] quantite = new int[nbIngredient + 1];
        ArrayList<ProduitInventaire> gardemangerSave = (ArrayList<ProduitInventaire>) gardemanger.clone();
        for (int i = 0; i < nbIngredient; i++) {
            int ingredientIndex = (int) (Math.random() * gardemanger.size());
            float mesure = Math.round(Float.parseFloat(gardemanger.get(ingredientIndex).getProduit().getMesurePoids()) * 1000.0f) / 1000.0f;
            if (mesure >= 1000) {
                mesure *= 0.001;

            } else if (mesure < 1) {
                mesure *= 1000;
            }
            Random rand = new Random();
            quantite[i] = (int) (Math.random() * mesure) + 1;

            finalrecette = finalrecette + quantite[i] + " " + gardemanger.get(ingredientIndex).getProduit().getMesureType() + " de " + gardemanger.get(ingredientIndex).getProduit().getNom().toLowerCase() + "\n";
            ingredTitre.add(gardemanger.get(ingredientIndex).getProduit().getNom());
            gardemanger.remove(ingredientIndex);

        }
        gardemanger = (ArrayList<ProduitInventaire>) gardemangerSave.clone();
        for (int i = 0; i < nbIngredient; i++) {
            int ingredientIndex = (int) (Math.random() * gardemanger.size());
            float mesure = Math.round(Float.parseFloat(gardemanger.get(ingredientIndex).getProduit().getMesurePoids()) * 1000.0f) / 1000.0f;
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
            finalrecette = finalrecette + verbeArray[verbeint] + " " + adjectifArray[adjectifint] + " " + quantite[i] + " " + gardemanger.get(ingredientIndex).getProduit().getMesureType() + " de " + gardemanger.get(ingredientIndex).getProduit().getNom().toLowerCase() + " " + containerArray[containerint] + " " + timeArray[timeint] + "\n";
            gardemanger.remove(ingredientIndex);
        }

        String[] ingredients = {"agneau", "boeuf", "dinde", "fruit de mer", "gibier", "légumineuses", "oeufs", "oie", "pintade", "volaille", "orge", "quinoa", "poisson", "porc", "poulet", "riz", "tofu", "soya", "veau"};
        String[] debuts = {"bol de", "bol de riz au", "mijoté de", "pilon de", "pizza au", "pâte au", "fondue au", "macaroni au", "poulet grillé au", "pain doré au", "granola au", "tofu au", "gauffres au", "bacon au"};
        Random rand = new Random();
        int ingredientint = rand.nextInt(ingredTitre.size());
        int ingredientint2 = rand.nextInt(ingredTitre.size());
        int debutsint = rand.nextInt(debuts.length);
        int auint = rand.nextInt(2);
        if (auint == 1) {
            nomRecette = debuts[debutsint].substring(0, 1).toUpperCase() + debuts[debutsint].substring(1) + " " + ingredTitre.get(ingredientint);
        } else {
            nomRecette = debuts[debutsint].substring(0, 1).toUpperCase() + debuts[debutsint].substring(1) + " " + ingredTitre.get(ingredientint) + " et au " + ingredTitre.get(ingredientint2);
        }
        recetteAleatoire.setText("Recette Aléatoire:\n\n" + nomRecette + "\n-----------------\n" + finalrecette);
    }
    // choisir un distributeur pour l'achat d'aliments
    public void chooseDistributeur() {
        try {
            ChoiceDialog<String> alerte = new ChoiceDialog<>("Default");
            alerte.setTitle("Sélection du distributeur");
            alerte.setHeaderText("Veuillez choisir le distributeur");
            alerte.setContentText("Votre choix: ");

            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("client\n");
            sortie.write("changeDistributeur\n");
            sortie.flush();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            int amount = Integer.parseInt(entree.readLine());
            for (int i = 0; i < amount; i++)
                alerte.getItems().add(entree.readLine());

            try {
                sortie.write(alerte.showAndWait().get() + "\n");
            } catch (Exception e) {
                sortie.write("Default");
            }

            sortie.close();
            entree.close();

            openAddToGardeMangerWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
