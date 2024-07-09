package dixheure.example;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

//cette classe devra être appelée dans Terminal après avoir chargé les chansons
public class Song {
    public String name;
    public String artist;
    public String lyrics;
    public int nbEcoute;

    public static HashMap<String, Song> songsByTitle = new HashMap<>();

    public static HashMap<String, List<Song>> songsByAtrist = new HashMap<>();

    public static HashMap<String, HashSet<Song>> songsByLyrics = new HashMap<>();


    /**
     * @param song
     * @return
     */
    public boolean equals(Song song){
        if (this.name.equals(song.name) && this.artist.equals(song.artist)){
            return true;
        } else {
            return false;
        }
    }

    public int hashCode(){
        return this.name.hashCode() + this.artist.hashCode();
    }



    //constructeur (il prendra les paramètres du CSV)

    /**
     * @param name
     * @param artist
     * @param lyrics
     * @param nbEcoute
     */
    public Song(String name, String artist, String lyrics, int nbEcoute) {
        this.name = name;
        this.artist = artist;
        this.lyrics = lyrics;
        this.nbEcoute = nbEcoute;
    }

    public void playSong(){
        System.out.println(this.lyrics);
        this.nbEcoute++;
    }

    /**
     * @param songs
     * @param name
     * @return
     */
    public static Song browseSongByName(String name){
        Song result = Song.songsByTitle.get(name);
        return result;
        
    }

    /**
     * @param songs
     * @param artist
     * @return
     */
    public static List<Song> browseSongByArtist(String artist){
        List<Song> songsByArtist = Song.songsByAtrist.get(artist);
        return songsByArtist;
    }

    /**
     * @param songs
     * @param word
     * @return
     */
    public static List<Song> browseSongbyWordInLyrics(List<Song> songs, String word){
        List<Song> songsByWord = new ArrayList<>();
        for (Song song : songs){
            if (song.lyrics.contains(word)){
                System.out.println(song.name);
                songsByWord.add(song);
            }
        } if (songsByWord.isEmpty()){
            System.out.println("No song found :(");
        }
        System.out.println(songsByWord.size() + " songs found");
        System.out.println("voulez vous rechercher un autre mot (o/n)?");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if (answer.equals("o")){
            System.out.println("Entrez un mot :");
            String word2 = sc.nextLine();
            browseSongbyWordInLyrics(songsByWord, word2);
        }
        return songsByWord;
    }

    /**
     * @param song
     */
    public static void indexLyrics(Song song){
        String lyrics_c = song.lyrics.replace("\n", "");
        lyrics_c = lyrics_c.replaceAll("\r", "");
        lyrics_c = lyrics_c.replaceAll("\\p{Punct}", "");
        String[] words = lyrics_c.split(" ");
        for (String word : words){
            String word_c = word.trim().toLowerCase();
            if (songsByLyrics.containsKey(word_c)){
                songsByLyrics.get(word_c).add(song);
            } else {
                HashSet<Song> songs = new HashSet<Song>();
                songs.add(song);
                songsByLyrics.put(word_c, songs);
            }
        }
    } 
    
    

}
//faire browse song là où il faut