package dixheure.example;

import java.util.ArrayList;
import java.util.List;

//Devra être créer par l'utilisateur
//avant d'ajouter, il faudra créer la chanson dans le terminal
public class Playlist {
    public List<Song> songs = new ArrayList<>();
    public String name;
    public static String proprietaire;
    
    /**
     * @param name
     * @param proprietaire
     */
    public Playlist(String name, String proprietaire) {
        this.name = name;
        this.proprietaire = proprietaire;
    }

    /**
     * @param song
     */
    public void addSong(Song song) {
        songs.add(song);
    }

    /**
     * @param song
     */
    public void removeSong(Song song) {
        songs.remove(song);
    }

    //On supposera que l'utilisateur connait bien sa playlist
    /**
     * @param name
     * @return
     */
    public Song BrowseSong(String name) {
        for (Song song : songs) {
            if (song.name.equals(name)) {
                System.out.println(song.name + " - " + song.artist);
                return song;
            }
        }
        IllegalArgumentException e = new IllegalArgumentException("Chanson non trouvée");
        throw e;
    }

    public void playSong(Song song) {
        if (!songs.contains(song)) {
            throw new IllegalArgumentException("Chanson non trouvée dans la playlist");
        }
        System.out.println(song.lyrics);
        song.nbEcoute++;
    }

    public void playRandomSong() {
        int n = (int) (Math.random() * songs.size());
        Song song = songs.get(n);
        System.out.println(song.lyrics);
        song.nbEcoute++;
    }

    //joue toutes les musique depuis une musique
    public void playAllSongs(Song FirstSong) {
        if (!songs.contains(FirstSong)) {
            throw new IllegalArgumentException("Chanson non trouvée dans la playlist");
        }
        for (int i=songs.indexOf(FirstSong); i<songs.size(); i++) {
            System.out.println(FirstSong.lyrics);
            FirstSong.nbEcoute++;
        }
    }
    

}
