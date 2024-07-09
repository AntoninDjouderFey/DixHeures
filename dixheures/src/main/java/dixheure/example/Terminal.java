package dixheure.example;
// Cette classe a été en grande partie réalisée par Rémi Guiseppi (mes remerciements à lui)

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Terminal {
    // Scanner pour lire les entrées de l'utilisateur
    private static final Scanner scan = new Scanner(System.in);

    // Fonction pour parser les arguments de la commande

    /**
     * @param cmd
     * @return
     * @autor Rémi Guiseppi
     */
    private static ArrayList<String> parseArguments(String cmd){
        // Pattern pour les arguments entre guillemets
        Pattern quotedPattern = Pattern.compile("\".+?\"");
        ArrayList<Integer> indexList = new ArrayList<>();

        // Matcher pour trouver les chaînes entre guillemets
        Matcher matcher = quotedPattern.matcher(cmd);
        while (matcher.find()) {
            indexList.add(matcher.start());
            indexList.add(matcher.end()-1);
        }
        
        int parser = 0;
        int size   = cmd.length();
        int iQuote =indexList.size()!=0?0:-1;
        ArrayList<String> args = new ArrayList<>();
        
        // Boucle pour parcourir la commande et extraire les arguments
        for (int i=0; i<size; ++i){
            // Si l'index actuel est celui d'une chaîne entre guillemets
            if (iQuote != -1 && i==indexList.get(iQuote) ){
                args.add(cmd.substring(indexList.get(iQuote)+1,indexList.get(iQuote+1)));
                i=indexList.get(iQuote+1); parser=i+1;
                iQuote=iQuote+2<indexList.size()?iQuote+2:-1;
            }

            // Si le caractère actuel n'est pas un espace, continuer
            if (!Character.isWhitespace(cmd.charAt(i)))continue;
            //si on a un espace après un guillemet, on continue
            if (i - parser + 1 <= 1){++parser; continue;}

            args.add(cmd.substring(parser, i));
            parser = i + 1;
        }

        // Ajouter le dernier argument
        if (size - parser > 0)args.add(cmd.substring(parser, size));

        return args;
    }

    /**
     * @param args
     * @autor Rémi Guiseppi (sauf le switch qui est de moi)
     */
    public static void main(String[] args){
        // Charger les chansons à partir du fichier CSV
        List<Song>  songs=CSVLoader.loadCSV("données\\spotify_millsongdata.csv");
        System.out.println("Si vous êtes perdu, tapez 'Help' pour afficher la liste des commandes");
        try {
            scan.useDelimiter("\n");
            // Boucle pour lire les commandes de l'utilisateur
            while (true) {
                if (!scan.hasNext())continue;
                String s = scan.next();
                ArrayList<String> cmd = parseArguments(s);

                String command = cmd.get(0);

                if (command.trim().length()==0){
                    continue;
                }
                boolean exit = false;
                switch (command) {
                    case "exit":
                        exit=true;
                        break;
                    case "quit":
                        exit = true;
                        break;
                    case "CreateAccount":
                        System.out.println("Entrez le pseudo");
                        String pseudo = scan.next();
                        if(Utilisateur.utilisateursMap.keySet().contains(pseudo)){
                            System.out.println("Ce pseudo est déjà utilisé");
                            break;
                        }
                        System.out.println("Entrez la date de naissance (format: yyyy mm dd) ex: 1999 12 31");
                        LocalDate date = Utils.parseDate(scan.next());

                        System.out.println("Entrez le mot de passe");
                        String mdp = scan.next();
                        Utilisateur user = new Utilisateur(pseudo, date, mdp);
                        Utilisateur.utilisateurs.add(user);
                        Utilisateur.utilisateursMap.put(pseudo, user);
                        
                        System.out.println("User created: " + user);
                        break;
                    case "AddFriend":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo1 = scan.next();
                        if(!Utilisateur.utilisateursMap.keySet().contains(pseudo1)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        }
                        else{
                            System.out.println("Entrez le pseudo de l'ami");
                            String pseudo2 = scan.next();
                            if(!Utilisateur.utilisateursMap.keySet().contains(pseudo2)){
                                System.out.println("Cet utilisateur "+pseudo2+" n'existe pas");
                                break;
                            }
                            Utilisateur user1 = Utilisateur.utilisateursMap.get(pseudo1);
                            Utilisateur user2 = Utilisateur.utilisateursMap.get(pseudo2);
                            user1.ajouterAmi(user2);
                            System.out.println("Utilisateur "+pseudo2+" est un ami de "+pseudo1);
                        }                       
                        break;
                    case "AddFamily":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo4 = scan.next();
                        if(!Utilisateur.utilisateursMap.keySet().contains(pseudo4)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        }
                        else{
                            System.out.println("Entrez le pseudo du membre");
                            String pseudo5 = scan.next();
                            if(!Utilisateur.utilisateursMap.keySet().contains(pseudo5)){
                                System.out.println("Cet utilisateur "+pseudo5+" n'existe pas");
                                break;
                            }
                            Utilisateur user4 = Utilisateur.utilisateursMap.get(pseudo4);
                            Utilisateur user5 = Utilisateur.utilisateursMap.get(pseudo5);
                        }
                        break;
                    case "CreatePlaylist":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo3 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo3)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user3 = Utilisateur.utilisateursMap.get(pseudo3);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName = scan.next();
                            user3.CreatePlaylist(playlistName);
                        }
                        break;
                    case "AddSongToPlaylist":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo6 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo6)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user6 = Utilisateur.utilisateursMap.get(pseudo6);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName2 = scan.next();
                            Playlist playlist = user6.getPlaylist(playlistName2);
                            System.out.println("Entrez le nom de la chanson");
                            String songName = scan.next();
                            Song song = Song.browseSongByName(songName);
                            playlist.addSong(song);
                        }
                        break;
                    case "SharePlaylist":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo7 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo7)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user7 = Utilisateur.utilisateursMap.get(pseudo7);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName3 = scan.next();
                            Playlist playlist2 = user7.getPlaylist(playlistName3);
                            System.out.println("Entrez le pseudo de l'ami");
                            String pseudo8 = scan.next();
                            Utilisateur user8 = Utilisateur.utilisateursMap.get(pseudo8);
                            user7.SharePlaylistAmi(playlist2, user8);
                        }
                        break;
                    case "CreateBlindTest":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo9 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo9)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user9 = Utilisateur.utilisateursMap.get(pseudo9);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName4 = scan.next();
                            Playlist playlist3 = user9.getPlaylist(playlistName4);
                            List<Utilisateur> participants = new ArrayList<>();
                            System.out.println("Entrez le pseudo des participants (séparés par des espaces)");
                            String[] participantsPseudo = scan.next().split(" ");
                            for (String psEudo : participantsPseudo){
                                participants.add(user9.getUser(psEudo));
                            }
                            user9.createBlindTest(playlist3, participants);
                        }break;
                    case "PlayBlindTest":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo10 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo10)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user10 = Utilisateur.utilisateursMap.get(pseudo10);
                            System.out.println("Entrez le nom du blind test");
                            String blindTestName = scan.next();
                            BlindTest blindTest = user10.getBlindTest(blindTestName);
                            user10.JeuBlindtest(blindTest);
                        }
                        break;
                    case "ShowSharedPlaylists":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo11 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo11)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user11 = Utilisateur.utilisateursMap.get(pseudo11);
                            System.out.println("Les playlists partagées avec "+pseudo11+":");
                            for (Playlist playlist : user11.playlists) {
                                System.out.println(playlist.name);
                            }
                        }
                        break;
                    case "ShowPlaylists":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo12 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo12)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user12 = Utilisateur.utilisateursMap.get(pseudo12);
                            System.out.println("Les playlists de "+pseudo12+":");
                            for (Playlist playlist : user12.playlists) {
                                System.out.println(playlist.name);
                            }
                        }
                        break;
                    case "ShowFriends":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo13 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo13)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user13 = Utilisateur.utilisateursMap.get(pseudo13);
                            System.out.println("Les amis de "+pseudo13+":");
                            user13.afficherAmis();
                        }
                        break;
                    case "ShowFamily":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo14 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo14)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user14 = Utilisateur.utilisateursMap.get(pseudo14);
                            System.out.println("La famille de "+pseudo14+":");
                            user14.afficherFamille();
                        }
                        break;
                    case "ShowBlindTests":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo15 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo15)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user15 = Utilisateur.utilisateursMap.get(pseudo15);
                            System.out.println("Les blind tests de "+pseudo15+":");
                            for (BlindTest blindTest : user15.blindTests) {
                                System.out.println(blindTest.blindTest.name);
                            }
                        }
                        break;
                    case "RecommendSong":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo16 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo16)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user16 = Utilisateur.utilisateursMap.get(pseudo16);
                            System.out.println("Entrez le nom de la chanson");
                            System.out.println("à qui souhaitez vous recommander cette chanson?");
                            String pseudo17 = scan.next();
                            if (!Utilisateur.utilisateursMap.keySet().contains(pseudo17)){
                                System.out.println("Cet utilisateur n'existe pas");
                                break;
                            } else {
                                Utilisateur user17 = Utilisateur.utilisateursMap.get(pseudo17);
                                System.out.println("Entrez le nom de la chanson");
                                String songName = scan.next();
                                Song song = Song.browseSongByName(songName.trim());
                                user16.rocommander(user17,song);
                            }
                        }
                        break;
                    case "BrowseSongByName":
                        System.out.println("Entrez le nom de la chanson");
                        String songName = scan.next();
                        Song song = Song.browseSongByName(songName.trim());
                        if(null == song){
                            System.out.println("Chanson non trouvée");
                            break;}
                        System.out.println(song.name + " - " + song.artist);
                        break;
                    case "BrowseSongByArtist":
                        System.out.println("Entrez le nom de l'artiste");
                        String artistName = scan.next();
                        List<Song> songsByArtist = Song.browseSongByArtist(artistName.trim());
                        if(null == songsByArtist){
                            System.out.println("Aucune chanson trouvée");
                            break;
                        }
                        for (Song song2 : songsByArtist) {
                            System.out.println(song2.name + " - " + song2.artist);
                        }
                        break;
                    case "PlaySong":
                        System.out.println("Entrez le nom de la chanson");
                        String songName2 = scan.next();
                        Song song2 = Song.browseSongByName(songName2);
                        if(song2 == null){
                            System.out.println("Chanson non trouvée");
                            break;
                        }
                        song2.playSong();
                        break;
                    case "PlayRandomSong":
                        int n = (int) (Math.random() * songs.size());
                        Song song3 = songs.get(n);
                        song3.playSong();
                        break;
                    case "PlayPlaylist":
                        System.out.println("Entrez le nom de l'utilisateur");
                        String pseudo18 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo18.trim())){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user18 = Utilisateur.utilisateursMap.get(pseudo18);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName5 = scan.next();
                            Playlist playlist4 = user18.getPlaylist(playlistName5.trim());
                            System.out.println("Entrez le nom de la chanson de départ");
                            String songName3 = scan.next();
                            Song song4 = Song.browseSongByName(songName3.trim());
                            playlist4.playAllSongs(song4);
                        }
                        break;
                    case "PlayInPlaylist":
                        System.out.println("Entrez le nom de l'utilisateur");
                        String pseudo19 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo19)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user19 = Utilisateur.utilisateursMap.get(pseudo19);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName6 = scan.next();
                            Playlist playlist5 = user19.getPlaylist(playlistName6);
                            System.out.println("Entrez le nom de la chanson");
                            String songName4 = scan.next();
                            Song song5 = playlist5.BrowseSong(songName4);
                            playlist5.playSong(song5);
                        }
                        break;
                    case "RemoveSongFromPlaylist":
                        System.out.println("Entrez le nom de l'utilisateur");
                        String pseudo20 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo20)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user20 = Utilisateur.utilisateursMap.get(pseudo20);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName7 = scan.next();
                            Playlist playlist6 = user20.getPlaylist(playlistName7);
                            System.out.println("Entrez le nom de la chanson");
                            String songName5 = scan.next();
                            Song song6 = playlist6.BrowseSong(songName5);
                            playlist6.removeSong(song6);
                        }
                        break;
                    case "RemoveFriend":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo21 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo21)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user21 = Utilisateur.utilisateursMap.get(pseudo21);
                            System.out.println("Entrez le pseudo de l'ami");
                            String pseudo22 = scan.next();
                            if (!Utilisateur.utilisateursMap.keySet().contains(pseudo22)){
                                System.out.println("Cet utilisateur n'existe pas");
                                break;
                            }
                            Utilisateur user22 = Utilisateur.utilisateursMap.get(pseudo22);
                            user21.supprimerAmi(user22);
                        }
                        break;
                    case "RemoveFamily":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo23 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo23)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user23 = Utilisateur.utilisateursMap.get(pseudo23);
                            System.out.println("Entrez le pseudo de l'ami");
                            String pseudo24 = scan.next();
                            if (!Utilisateur.utilisateursMap.keySet().contains(pseudo24)){
                                System.out.println("Cet utilisateur n'existe pas");
                                break;
                            }
                            Utilisateur user24 = Utilisateur.utilisateursMap.get(pseudo24);
                            user23.supprimerFamille(user24);
                        }
                        break;
                    case "RemovePlaylist":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo25 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo25)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user25 = Utilisateur.utilisateursMap.get(pseudo25);
                            System.out.println("Entrez le nom de la playlist");
                            String playlistName8 = scan.next();
                            Playlist playlist7 = user25.getPlaylist(playlistName8);
                            user25.DeletePlaylist(playlist7);
                        }
                        break;
                    case "RemoveBlindTest":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo26 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo26)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user26 = Utilisateur.utilisateursMap.get(pseudo26);
                            System.out.println("Entrez le nom du blind test");
                            String blindTestName2 = scan.next();
                            BlindTest blindTest2 = user26.getBlindTest(blindTestName2);
                            user26.deleteBlindTest(blindTest2);
                        }
                        break;
                    case "DeleteUser":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo27 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo27)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user27 = Utilisateur.utilisateursMap.get(pseudo27);
                            Utilisateur.utilisateurs.remove(user27);
                            Utilisateur.utilisateursMap.remove(pseudo27);
                        }
                        break;
                    case "RecommandedSong":
                        System.out.println("Entrez le pseudo de l'utilisateur");
                        String pseudo28 = scan.next();
                        if (!Utilisateur.utilisateursMap.keySet().contains(pseudo28)){
                            System.out.println("Cet utilisateur n'existe pas");
                            break;
                        } else {
                            Utilisateur user28 = Utilisateur.utilisateursMap.get(pseudo28);
                            System.out.println("Les chansons recommandées à "+pseudo28+":");
                            for (Song song7 : user28.recomandées) {
                                System.out.println(song7.name + " - " + song7.artist);
                            }
                        }
                        break;
                    case "BrowseSongByLyrics":
                        System.out.println("Entrez un mot (minuscule uniquement)");
                        String word = scan.next();
                        HashSet<Song> songsByWord = Song.songsByLyrics.get(word.trim());
                        for(Song ss : songsByWord){
                            System.out.println(ss.name + " - " + ss.artist);
                        }
                        break;
                    case "Help":
                        System.out.println("Liste des commandes :");
                        System.out.println("CreateAccount");
                        System.out.println("AddFriend");
                        System.out.println("AddFamily");
                        System.out.println("CreatePlaylist");
                        System.out.println("AddSongToPlaylist");
                        System.out.println("SharePlaylist");
                        System.out.println("CreateBlindTest");
                        System.out.println("PlayBlindTest");
                        System.out.println("ShowSharedPlaylists");
                        System.out.println("ShowPlaylists");
                        System.out.println("ShowFriends");
                        System.out.println("ShowFamily");
                        System.out.println("ShowBlindTests");
                        System.out.println("RecommendSong");
                        System.out.println("RecommandedSong");
                        System.out.println("BrowseSongByName");
                        System.out.println("BrowseSongByArtist");
                        System.out.println("BrowseSongByWordInLyrics");
                        System.out.println("PlaySong");
                        System.out.println("PlayRandomSong");
                        System.out.println("PlayPlaylist");
                        System.out.println("PlayInPlaylist");
                        System.out.println("RemoveSongFromPlaylist");
                        System.out.println("RemoveFriend");
                        System.out.println("RemoveFamily");
                        System.out.println("RemovePlaylist");
                        System.out.println("RemoveBlindTest");
                        System.out.println("DeleteUser");
                        System.out.println("Help");
                        System.out.println("Exit");
                        System.out.println("Quit");
                        break;
                    
                    
                    default:
                        System.out.println("Commande non reconnue");
                        break;
                }
                if(exit){
                    break;
                }
                // ici, tu gère les cmds classiques

                System.out.println("found :");
                System.out.println(cmd);
            }
        }finally {
            scan.close();
        }
    }
}