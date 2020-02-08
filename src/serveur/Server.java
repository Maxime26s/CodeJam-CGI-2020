package serveur;

import classes.Produit;
import classes.ProduitInventaire;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Server {
    public static HashMap<String, ProduitEpicerie> hashMapEpicerie;
    public static float revenu = 0;

    public static void main(String[] args) {
        loadInventory();
        while(true) {
            try {
                ServerSocket serveur = new ServerSocket(8080);
                System.out.println("Waiting...");
                Socket socket = serveur.accept();
                System.out.println("Found!");
                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String message = entree.readLine();
                System.out.println("Protocole " + message + " demandé");
                if (message.equals("commande")) {
                    int nbCommande = Integer.parseInt(entree.readLine());
                    String parts[] = new String[nbCommande*3];
                    for(int i=0;i<parts.length;i++)
                        parts[i] = entree.readLine();
                    save(parts,"Commande");
                    String envoie[] = new String[nbCommande*3];
                    OutputStream fluxSortant = socket.getOutputStream();
                    OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
                    for(int i=0;i<nbCommande;i++){
                        sortie.write(parts[i] + "\n");
                        envoie[i]=parts[i];
                        if(hashMapEpicerie.get(parts[i]).quantite-Integer.parseInt(parts[i+1])>=0){
                            envoie[i+1]=parts[i+1];
                            sortie.write(envoie[i+1] + "\n");
                            hashMapEpicerie.get(parts[i]).quantite-=Integer.parseInt(parts[i+1]);
                        } else{
                            envoie[i+1]=Integer.toString(Integer.parseInt(parts[i+1])+hashMapEpicerie.get(parts[i]).quantite-Integer.parseInt(parts[i+1]));
                            sortie.write(envoie[i+1] + "\n");
                            hashMapEpicerie.get(parts[i]).quantite = 0;
                        }
                        envoie[i+2]=Float.toString(hashMapEpicerie.get(parts[i]).produit.getPrix()*Integer.parseInt(envoie[i+1]));
                        sortie.write(envoie[i+2] + "\n");
                        revenu+=Float.parseFloat(envoie[i+2]);
                    }
                    save(parts,"Envoie");
                    System.out.println("Commande terminé");
                    saveInventory();
                }
                entree.close();
                socket.close();
                serveur.close();
            } catch (Exception e) {
                System.out.println("ERREUR: Le client n'a pas pu se connecter");
            }
        }
    }

    public static void saveInventory(){
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("inventaireEpicerie.dat")));
            sortie.writeObject(revenu);
            sortie.writeObject(hashMapEpicerie);
            sortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadInventory(){
        try {
            ObjectInputStream entree = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("inventaireEpicerie.dat")));
            try {
                revenu = entree.readInt();
                hashMapEpicerie = (HashMap<String, ProduitEpicerie>) entree.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void save(String[] parts, String name){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=new Date();
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(name+"_"+df.format(date)+".dat")));
            for(int i=0;i<parts.length;i++)
                sortie.writeObject(parts[i]);
            sortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}