package dixheure.example;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utilisateur  {
    // Attributs
    public static ArrayList<Utilisateur> utilisateurs = new ArrayList<>(); // liste des pseudos déjà utilisés
    public static HashMap<String, Utilisateur> utilisateursMap = new HashMap<>(); // liste des utilisateurs
    //on a enlevé les statics car ça créé un problème pour les tests
    private /*static*/ String pseudo;
    private /*static*/ LocalDate dateNaissance;
    private /*static*/ String mdp;
    private ArrayList<Utilisateur> amis;
    private ArrayList<Utilisateur> famille;
    public ArrayList<Playlist> playlists = new ArrayList<>();
    public ArrayList<Song> recomandées = new ArrayList<>();
    public ArrayList<BlindTest> blindTests = new ArrayList<>();

    // Constructeur
    /**
     * @param pseudo
     * @param dateNaissance
     * @param mdp
     */
    public Utilisateur(String pseudo, LocalDate dateNaissance, String mdp) {

        if (pseudoDisponible(pseudo)) {
            this.pseudo = pseudo;
            System.out.println("Pseudo créé : " + pseudo);
        } else {
            throw new IllegalArgumentException("Pseudo déjà utilisé : " + pseudo);
        }
        if (!estMajeur(dateNaissance)) {
            System.out.println("Etes vous dans la famille d'un utilisateur majeur ? Si oui, veuillez entrer son pseudo :");
            Scanner sc = new Scanner(System.in);
            String pseudoMajeur = sc.nextLine();
            int a=0;
            for (Utilisateur membre : utilisateurs) {
                System.out.println(membre.getPseudo());
                if (membre.getPseudo().equals(pseudoMajeur)) {
                    this.dateNaissance=dateNaissance;
                    this.famille=membre.famille;
                    this.amis=new ArrayList<>();
                    membre.famille.add(this);
                     //on va supposer l'utilisateur bienveillant
                    //plus peut être cohérent, car l'autre utilisateur peut le bannir
                    System.out.println("Vous avez été ajouté à la famille de " + pseudoMajeur);
                    a=1;
                    break;
                }
            } if (a==0) { //si l'utilisateur n'est pas trouvé
                throw new IllegalArgumentException("Vous devez être majeur pour vous inscrire");
            }
        }
        else {
            this.dateNaissance = dateNaissance;
        }
        this.mdp = mdp;
        this.amis = new ArrayList<>();
        if (estMajeur(dateNaissance)) {
            this.famille = new ArrayList<>();
        }
        
        System.out.println("Utilisateur créé : " + pseudo);
    }   

    // Getters
    /**
     * @param pseudo
     * @return
     */
    public Utilisateur getUser(String pseudo) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getPseudo().equals(pseudo)) {
                return utilisateur;
            }
        }
        return null;
    }
    /**
     * @param name
     * @return
     */
    public Playlist getPlaylist(String name) {
        for (Playlist playlist : playlists) {
            if (playlist.name.equals(name)) {
                return playlist;
            }
        }
        return null;
    } 

    
    /**
     * @return
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return
     */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @param name
     * @return
     */
    public BlindTest getBlindTest(String name) {
        for (BlindTest blindTest : blindTests) {
            if (blindTest.blindTest.name.equals(name)) {
                return blindTest;
            }
        } System.out.println("BlindTest not found");
        return null;
    }

    //fonction vérifiant la disponibilité d'un pseudo
    /**
     * @param pseudo
     * @return
     */
    public boolean pseudoDisponible(String pseudo) {
        if (utilisateurs.contains(pseudo)) {
            return false;
        }
        return true;
    }

    //fonction vérifiant si l'utilisateur est majeur
    /**
     * @param dateNaissance
     * @return
     */
    public boolean estMajeur(LocalDate dateNaissance) {
        LocalDate dateActuelle = LocalDate.now();
        System.out.println(dateActuelle.getYear());
        System.out.println(dateNaissance.getYear());
        System.out.println(dateActuelle.getYear() - dateNaissance.getYear());
        if (dateActuelle.getYear() - dateNaissance.getYear() > 18) {
            return true;
        }
        return false;
    }
    
    //fonction ajoutant un ami
    /**
     * @param ami
     */
    public void ajouterAmi(Utilisateur ami) {
        System.out.println("Date de naissance de l'ami : " + ami.getDateNaissance());
        if (ami == this) {
            throw new IllegalArgumentException("Impossible de s'ajouter soi-même en ami");
        }

        else if (estMajeur(ami.getDateNaissance()) && estMajeur(this.getDateNaissance())) {
            if (this.amis.contains(ami)) {
                throw new IllegalArgumentException("Cet utilisateur est déjà dans votre liste d'amis");
            } else {
                System.out.println("Ami ajouté : " + ami.getPseudo());
                amis.add(ami);
            }
        } 
        else if(!estMajeur(ami.getDateNaissance()) && estMajeur(this.getDateNaissance())) {
            throw new IllegalArgumentException("Impossible d'ajouter un mineur en ami");
        }
        
        else if(!estMajeur(ami.getDateNaissance()) && !estMajeur(this.getDateNaissance())) {
            if (this.amis.contains(ami)) {
                throw new IllegalArgumentException("Cet utilisateur est déjà dans votre liste d'amis");
            } else {
                System.out.println("Ami ajouté : " + ami.getPseudo());
                amis.add(ami);
            }
        }

        else if (estMajeur(ami.getDateNaissance()) && !estMajeur(this.getDateNaissance())) {
            throw new IllegalArgumentException("Impossible d'ajouter un majeur en ami si vous êtes mineur");
        }
    }

    /**
     * @param membre
     */
    public void ajouterFamille(Utilisateur membre) {
        if (membre == this) {
            throw new IllegalArgumentException("Impossible de s'ajouter soi-même en famille");
        }

        if(!estMajeur(this.getDateNaissance())) {
            throw new IllegalArgumentException("Impossible d'ajouter un membre de famille si vous êtes mineur");
        }
        
        if (!famille.contains(membre)) {
            famille.add(membre);
        }

        else {
            throw new IllegalArgumentException("Cet utilisateur est déjà dans votre liste de famille");
        }
    }

    /**
     * @param ami
     */
    public void supprimerAmi(Utilisateur ami) {
        if (amis.contains(ami)) {
            amis.remove(ami);
        } else {
            throw new IllegalArgumentException("Cet utilisateur n'est pas dans votre liste d'amis");
        }
    }

    /**
     * @param membre
     */
    public void supprimerFamille(Utilisateur membre) {
        if (famille.contains(membre)) {
            famille.remove(membre);
        } else {
            throw new IllegalArgumentException("Cet utilisateur n'est pas dans votre liste de famille");
        }
    }

    /**
     * 
     */
    public void afficherAmis() {
        for (Utilisateur ami : amis) {
            System.out.println(ami.getPseudo());
        }
    }

    /**
     * 
     */
    public void afficherFamille() {
        for (Utilisateur membre : famille) {
            System.out.println(membre.getPseudo());
        }
    }

    /**
     * 
     */
    public void desabonnement() {
        for (Utilisateur ami : amis) {
            ami.supprimerAmi(this);
        }
        for (Utilisateur membre : famille) {
            membre.supprimerFamille(this);
        }
        utilisateurs.remove(this);
    }

    /**
     * @param name
     */
    public void CreatePlaylist(String name) {
        Playlist playlist = new Playlist(name, this.pseudo);
        playlists.add(playlist);
    }

    /**
     * @param playlist
     */
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * @param playlist
     */
    public void DeletePlaylist(Playlist playlist) {
        playlist = null;
        playlists.remove(playlist);
    }

    /**
     * @param playlist
     * @param ami
     */
    public void SharePlaylistAmi(Playlist playlist, Utilisateur ami) {
        ami.addPlaylist(playlist);
    }

    /**
     * @param playlist
     * @param membre
     */
    public void SharePlaylist(Playlist playlist, Utilisateur membre) {
        if (!this.famille.contains(membre) || !this.amis.contains(membre)) {
            throw new IllegalArgumentException("Cet utilisateur n'est ni dans votre famille ni dans votre liste d'amis");
        }
        membre.addPlaylist(playlist);
    }

    /**
     * @param AFaireRecommandation
     * @param Song
     */
    public void rocommander(Utilisateur AFaireRecommandation, Song Song) {
        AFaireRecommandation.recomandées.add(Song);
    }

    //on créera un blindtest avec une playlist, 
    //ça permettra aux amis d'y jouer sans le modifier car il n'y a pas de fonction modifier blindtest
    /**
     * @param playlist
     * @param AFaireBlindTest
     */
    public void createBlindTest(Playlist playlist, List<Utilisateur> AFaireBlindTest) {
        for (int i=0;i<AFaireBlindTest.size();i++) {
            if (!this.amis.contains(AFaireBlindTest.get(i)) && !this.famille.contains(AFaireBlindTest.get(i))) {
                throw new IllegalArgumentException("Cet utilisateur n'est ni dans votre famille ni dans votre liste d'amis");
            }
        }
        BlindTest blindTest = new BlindTest(playlist, AFaireBlindTest);
        blindTests.add(blindTest);
    }

    /**
     * @param blindTest
     */
    public void deleteBlindTest(BlindTest blindTest) {
        blindTest = null;
        blindTests.remove(blindTest);
    }

    /**
     * @param blindTest
     * @param apartager
     */
    public void shareBlindTest(BlindTest blindTest, Utilisateur apartager) {
        if (this.amis.contains(apartager) || this.famille.contains(apartager)) {
            apartager.blindTests.add(blindTest);
        }
        else {
            throw new IllegalArgumentException("Cet utilisateur n'est ni dans votre famille ni dans votre liste d'amis");
        }
    }

    //selection du mode de jeu
    /**
     * @param blindTest
     */
    public void GM(BlindTest blindTest) {
        System.out.println("Choisissez le mode de jeu :");
        System.out.println("1. Facile");
        System.out.println("2. Moyen");
        System.out.println("3. Difficile");
        Scanner sc = new Scanner(System.in);
        int mode = sc.nextInt();
        switch (mode) {
            case 1:
                System.out.println("1. Auteur");
                System.out.println("2. Titre");
                System.out.println("3. Auteur et titre");
                int choix = sc.nextInt();
                blindTest.playBlindTest(blindTest, 100,choix);
                break;
            case 2:
                System.out.println("1. Auteur");
                System.out.println("2. Titre");
                System.out.println("3. Auteur et titre");
                int choix2 = sc.nextInt();
                blindTest.playBlindTest(blindTest, 50,choix2);
                break;
            case 3:
                System.out.println("1. Auteur");
                System.out.println("2. Titre");
                System.out.println("3. Auteur et titre");
                int choix3 = sc.nextInt();
                blindTest.playBlindTest(blindTest, 10,choix3);
                break;
            default:
                System.out.println("Mode de jeu inconnu");
        }
    }

    //jeu en tour par tour (comme on est sur terminal, on ne peut pas faire de jeu en temps réel)
    /**
     * @param blindTest
     */
    public void JeuBlindtest(BlindTest blindTest) {
        for (Utilisateur participant : blindTest.participants) {
            participant.GM(blindTest);
            System.out.println(blindTest.score);
        }
        blindTest.compareScore();
    }
        

}