package serveur;

import classes.Produit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Serveur {
    public static HashMap<String, HashMap<String, ProduitEpicerie>> hashMapEpicerie = new HashMap<>();
    public static HashMap<String, Revenu> hashMapRevenu = new HashMap<>();
    public static String distributeurCourant = "Default";
    public static void main(String[] args) {
        loadInventory();
        if (!hashMapEpicerie.containsKey("Default")){
            hashMapEpicerie.put("Default", new HashMap<>());
            hashMapRevenu.put("Default", new Revenu(0f));
        }

        while (true) {
            try {
                ServerSocket serveur = new ServerSocket(8080);
                System.out.println("Waiting...");
                Socket socket = serveur.accept();
                System.out.println("Found!");
                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String message = entree.readLine();
                System.out.println("Protocole " + message + " demandé");
                switch (message) {
                    case "client":
                        client(socket, entree, serveur);
                        break;
                    case "distributeur":
                        distributeur(socket, entree, serveur);
                        break;
                }
                entree.close();
                socket.close();
                serveur.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERREUR: Le client n'a pas pu se connecter");
            }
            saveInventory();
        }
    }

    private static void client(Socket socket, BufferedReader entree, ServerSocket serveur) {
        try {
            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            String nom;
            String message = entree.readLine();
            switch (message) {
                case "commande":

                    int nbCommande = Integer.parseInt(entree.readLine());
                    String[] parts = new String[nbCommande * 3];
                    for (int i = 0; i < parts.length; i++)
                        parts[i] = entree.readLine();

                    save(parts, "Commande");

                    String[] envoie = new String[nbCommande * 3];
                    for (int i = 0; i < envoie.length; i++)
                        envoie[i] = parts[i];

                    sortie.write(nbCommande + "\n");
                    for (int i = 0; i < nbCommande; i++) {
                        sortie.write(parts[i * 3] + "\n");
                        envoie[i * 3] = parts[i * 3];
                        if (hashMapEpicerie.get(distributeurCourant).get(parts[i * 3]).quantite - Integer.parseInt(parts[i * 3 + 1]) >= 0) {
                            envoie[i * 3 + 1] = parts[i * 3 + 1];
                            sortie.write(envoie[i * 3 + 1] + "\n");
                            hashMapEpicerie.get(distributeurCourant).get(parts[i * 3]).quantite -= Integer.parseInt(parts[i * 3 + 1]);
                        } else {
                            envoie[i * 3 + 1] = Integer.toString(Integer.parseInt(parts[i * 3 + 1]) + hashMapEpicerie.get(distributeurCourant).get(parts[i * 3]).quantite - Integer.parseInt(parts[i * 3 + 1]));
                            sortie.write(envoie[i * 3 + 1] + "\n");
                            hashMapEpicerie.get(distributeurCourant).get(parts[i * 3]).quantite = 0;
                        }
                        hashMapRevenu.get(distributeurCourant).setRevenu(hashMapRevenu.get(distributeurCourant).getRevenu()+(Math.round(Float.parseFloat(hashMapEpicerie.get(distributeurCourant).get(parts[i * 3]).produit.getPrix()) * 100.0f) / 100.0f) * Integer.parseInt(envoie[i * 3 + 1]));
                        envoie[i * 3 + 2] = Float.toString(hashMapRevenu.get(distributeurCourant).getRevenu());
                    }
                    sortie.close();
                    //save(parts, "Envoie");
                    System.out.println("Commande terminé");

                    break;
                case "reloadItems":
                    TreeMap<String, ProduitEpicerie> sorted = new TreeMap<>(hashMapEpicerie.get(distributeurCourant));
                    sortie.write(sorted.size() + "\n");
                    sorted.forEach((k, v) -> {
                        try {
                            sortie.write(k + "\n" +
                                    v.produit.getCodeBar() + "\n" +
                                    v.produit.getPrix() + "\n" +
                                    v.produit.getMesurePoids() + "\n" +
                                    v.produit.getMesureType() + "\n" +
                                    v.produit.getQuantite() + "\n" +
                                    v.produit.getLongevite() + "\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    sortie.close();
                    break;
                case "changeDistributeur":
                    sortie.write(hashMapEpicerie.size() + "\n");
                    Set<String> keys = hashMapEpicerie.keySet();
                    for (String key : keys) {
                        sortie.write(key + "\n");
                    }
                    sortie.flush();
                    distributeurCourant = entree.readLine();
                    sortie.close();
                    break;
            }
            entree.close();
            socket.close();
            serveur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void distributeur(Socket socket, BufferedReader entree, ServerSocket serveur) {
        try {
            String username = entree.readLine();
            if (!hashMapEpicerie.containsKey(username)){
                hashMapEpicerie.put(username, new HashMap<>());
                hashMapRevenu.put(username, new Revenu(0f));
            }

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            String nom;
            String message = entree.readLine();
            switch (message) {
                case "findByName":
                    nom = entree.readLine();
                    sortie.write(nom + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).produit.getCodeBar() + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).produit.getMesurePoids() + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).produit.getPrix() + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).quantite + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).produit.getMesureType() + "\n");
                    sortie.write(hashMapEpicerie.get(username).get(nom).produit.getLongevite() + "\n");
                    sortie.close();
                    break;
                case "addItem":
                    nom = entree.readLine();
                    if (!hashMapEpicerie.get(username).containsKey(nom)) {
                        String code = entree.readLine();
                        String mesure = entree.readLine();
                        String prix = entree.readLine();
                        String owned = entree.readLine();
                        String type = entree.readLine();
                        String quantite = entree.readLine();
                        String longevite = entree.readLine();
                        if (quantite.equals("Rien"))
                            quantite = "";
                        hashMapEpicerie.get(username).put(nom, new ProduitEpicerie(new Produit(nom, code, prix, mesure, type, quantite, longevite), Integer.parseInt(owned)));

                        sortie.write("blanc" + "\n" +
                                "Le produit a été ajouté \n");
                        sortie.close();
                    } else {
                        sortie.write("rouge" + "\n" +
                                "ERREUR: LE PRODUIT EXISTE DÉJÀ \n");
                        sortie.close();
                    }
                    break;
                case "modItem":
                    String oldName = entree.readLine();
                    nom = entree.readLine();
                    if (oldName.equals(nom) && hashMapEpicerie.get(username).containsKey(nom) || !oldName.equals(nom) && !hashMapEpicerie.get(username).containsKey(nom)) {
                        hashMapEpicerie.get(username).remove(oldName);
                        String code = entree.readLine();
                        String mesure = entree.readLine();
                        String prix = entree.readLine();
                        String owned = entree.readLine();
                        String type = entree.readLine();
                        String quantite = entree.readLine();
                        String longevite = entree.readLine();
                        if (quantite.equals("Rien"))
                            quantite = "";
                        hashMapEpicerie.get(username).put(nom, new ProduitEpicerie(new Produit(nom, code, prix, mesure, type, quantite, longevite), Integer.parseInt(owned)));
                        sortie.write("blanc" + "\n" +
                                "Le produit a été ajouté \n");
                        sortie.close();
                        saveInventory();
                    } else {
                        sortie.write("rouge" + "\n" +
                                "ERREUR \n");
                        sortie.close();
                    }
                    break;
                case "reloadItems":
                    TreeMap<String, ProduitEpicerie> sorted = new TreeMap<>(hashMapEpicerie.get(username));
                    sortie.write(sorted.size() + "\n");
                    sorted.forEach((k, v) -> {
                        try {
                            sortie.write(k + "\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    sortie.write(Float.toString(hashMapRevenu.get(username).getRevenu()));
                    sortie.close();
                    break;
                case "supItem":
                    String toDelete = entree.readLine();
                    hashMapEpicerie.get(username).remove(toDelete);
                    break;
            }
            entree.close();
            socket.close();
            serveur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveInventory() {
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("inventaireEpicerie.dat")));
            sortie.writeObject(hashMapRevenu);
            sortie.writeObject(hashMapEpicerie);
            sortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadInventory() {
        try {
            ObjectInputStream entree = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("inventaireEpicerie.dat")));
            try {
                hashMapRevenu = (HashMap<String, Revenu>) entree.readObject();
                hashMapEpicerie = (HashMap<String, HashMap<String, ProduitEpicerie>>) entree.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void save(String[] parts, String name) {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String log = "";
            for (int i = 0; i < parts.length; i++)
                log += parts[i] + "\n";
            Files.write(Paths.get(name + "_" + df.format(date) + ".txt"), log.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}