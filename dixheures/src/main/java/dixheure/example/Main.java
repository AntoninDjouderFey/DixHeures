package dixheure.example;
//cette classe a servie pour qqs tests, elle ne sert plus à rien
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        // Créer des utilisateurs
        Date date1 = new Date(100, 5, 19); // 19 juin 2000
        Date date2 = new Date(107, 10, 25); // 25 novembre 2007
        Date date3 = new Date(90, 8, 12); // 12 septembre 1990

        Utilisateur user1 = new Utilisateur("Alice", date1, "password1");
        Utilisateur user2 = new Utilisateur("Bob", date2, "password2");
        Utilisateur user3 = new Utilisateur("Charlie", date3, "password3");

        System.out.println(user1+" "+user1.getPseudo()+" "+user1.getDateNaissance());
        System.out.println(user2+" "+user2.getPseudo()+" "+user2.getDateNaissance());
        System.out.println(user3+" "+user3.getPseudo()+" "+user3.getDateNaissance());
        // Ajouter des amis
        //user1.ajouterAmi(user2); // user1 ajoute user2 comme ami
        user1.ajouterAmi(user3); // user1 ajoute user3 comme ami
        //user1.ajouterAmi(user3);

        // Afficher les amis
        System.out.println("Les amis de Alice:");
        user1.afficherAmis();

        // Créer des playlists
        user1.CreatePlaylist("Playlist Alice");
        user2.CreatePlaylist("Playlist Bob");

        // Ajouter des chansons aux playlists
        Playlist playlistAlice = user1.playlists.get(0);
        Playlist playlistBob = user2.playlists.get(0);
        /* 
        Song song1 = new Song("Dancing Queen", "ABBA", "You can dance, you can jive, having the time of your life",0);
        Song song2 = new Song("Counting Airplanes", "Train", "I put ketchup on my scrambled eggs",0);
        Song song3 = new Song("Never gonna give you up", "Rick Ashley", "Never gonna give you up, Never gonna let you down",0);
        playlistAlice.addSong(song1);
        playlistAlice.addSong(song2);
        playlistBob.addSong(song3);
        */

        // Partager une playlist
        user1.SharePlaylistAmi(playlistAlice, user2); // Alice partage sa playlist avec Bob
        System.out.println(user2.playlists);
        
        // Créer un blind test
        //user1.ajouterAmi(user3);
        List<Utilisateur> participants = new ArrayList<>(Arrays.asList(/*user2,*/ user3));
        user1.createBlindTest(playlistAlice, participants); // Alice crée un blind test avec Bob et Charlie
        
        // Afficher les playlists partagées de Bob
        System.out.println("Les playlists partagées avec Bob:");
        for (Playlist playlist : user2.playlists) {
            System.out.println(playlist.name);
        }
        
        // Jouer un blind test
        BlindTest blindTest = user1.blindTests.get(0);
        user1.JeuBlindtest(blindTest);

        // Afficher les scores des participants du blind test
        System.out.println("Scores du blind test:");
        user1.JeuBlindtest(blindTest);
    }
    
}
