package dixheure.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CSVLoader {

    // Méthode pour charger le CSV
    /**
     * @param filePath
     * @return
     */
    public static List<Song> loadCSV(String filePath){
        String csvRowSplit = "^\"\s*$"; //détecte un guillemet en début de ligne, suivi d'espace ou rien puis fin de ligne
        Pattern p = Pattern.compile(csvRowSplit);

        String csvFieldsSplit0 = ",\"\""; //détecte le debut des paroles sur la premiere ligne d'une entrée
        //Pattern pFields = Pattern.compile(csvFieldsSplit0);

        String fieldPattern = "(\\w+\\.*!*)\s*,\s*([a-zA-Z'\s-\\.\\?0-9:]*[\s\\(a-zA-Z\\)]*[\s\\[a-zA-Z\\]]*|\"[a-zA-Z',\s]*\"|[a-zA-Z',\\s]*\"\")\s*,\s*([/a-zA-Z0-9+_\\.]*)\s*,\s*\"*(.*)"; // |\"[a-zA-Z]+\\p{Punct}*[a-zA-Z]+\"
        Pattern pFields1 = Pattern.compile(fieldPattern);
        
        File csvFile = new File(filePath);

        List<Song> songs = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){

            String line = null;
            boolean inSong = false;
            boolean inLyrics = false;
            String artist = null;
            String title = null;
            StringBuilder lyrics = null;
            int lineCount = 0;
            line = br.readLine();
            while(line!= null) {
                Matcher m = p.matcher(line);
                if (m.find()) { // si on a trouvé le guillemet de début/fin d'entrée
                    if(!inSong){ //si c'est celui d'"entrée"
                        inSong = true;
                        lineCount=0;
                        artist = null;
                        title   = null;
                        lyrics  = null;
                    }
                    else{ //si c'est celui de "fin"
                        Song song = new Song(title,artist, lyrics.toString(),0);
                        System.out.println("Adding Song: "+song.artist+" "+song.name);
                        Song.songsByTitle.put(title,song);
                        Song.indexLyrics(song);
                        if(Song.songsByAtrist.containsKey(artist)){
                            Song.songsByAtrist.get(artist).add(song);
                        }
                        else{
                            List<Song> songsByArtist = new ArrayList<>();
                            songsByArtist.add(song);
                            Song.songsByAtrist.put(artist,songsByArtist);
                        }
                        songs.add(song);
                        inSong = true;
                        lineCount=0;
                        lyrics = null;
                        inLyrics = false;
                        //break;
                    }
                
                }
                else{ 
                    if(inSong){ //si on est dans une entrée
                        lineCount+=1;
                        if(lineCount==1){
                            System.out.println("Working on: " + line);
                            
                            Matcher m2 = pFields1.matcher(line);
                            if (m2.find()) {
                                //System.out.println("Found value: " + m2.groupCount());
                                if(m2.groupCount()<4)  {
                                    System.out.println("Fields MATCH failed");
                                    break;
                                }
                                else{
                                    artist = m2.group(1);
                                    title = m2.group(2);
                                    lyrics = new StringBuilder(m2.group(4));
                                    inLyrics = true;
                                }
                            } else {
                                System.out.println("NO MATCH");
                            }
                            
                            
                            /* 
                            String[] fields_lyrics0 = line.split(csvFieldsSplit0);
                            if(fields_lyrics0.length>1){
                                String[] fields = fields_lyrics0[0].split(",");
                                System.out.println(fields_lyrics[0]);
                            }
                            Song song = new Song(columns[3], columns[0], columns[2],0);
                            songs.add(song);
                            */
                        }
                        else{
                            if(inLyrics){
                                lyrics.append("\n"+line);
                            }
                        
                        }
                    }
                }
                line = br.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        
        
        }
        return songs;
    }

    // Méthode pour rechercher des lignes correspondant à un critère
    //peut être utilisée pour rechercher une chanson par nom, auteur ou autre

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Charger le fichier CSV
        List<Song> songs = loadCSV("données\\spotify_millsongdata.csv");
        System.out.println(songs.get(48).artist);
    }
}
