package dixheure.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
//Devra être créer par l'utilisateur depuis une playlist
public class BlindTest {
    //il prendra le nom de la playlist
    public Playlist blindTest;
    int score;
    List<Utilisateur> participants = new ArrayList<>();

    /**
     * @param blindTest
     * @param participants
     */
    public BlindTest(Playlist blindTest, List<Utilisateur> participants) {
        this.blindTest = blindTest;
        this.score = 0;
        this.participants = participants;
    }

    //Devra être géré par une boucle
    /**
     * @param answer
     * @param song
     * @return
     */
    public boolean checkAnswer(List<String[]> answer, Song song) {
        for (String[] a : answer) {
            if (a[0].equals(song.name)) {
                if (a[1].equals(song.artist)) {
                    System.out.println("Bonne réponse !");
                    return true;
                }
                else {
                    System.out.println("Bonne chanson mais mauvais artiste");
                    return false;
                }
            } if (a[1].equals(song.artist)) {
                System.out.println("Mauvaise chanson mais bon artiste");
                return false;
            }
        } return false;
    }

    /**
     * @param song
     * @param maxword
     */
    public void playSong(Song song,int maxword) {
        String[] lyrics = song.lyrics.split(" ");
        if (lyrics.length<maxword) {
            maxword=lyrics.length;
        }
        for (int i=0; i<lyrics.length; i++) {
            if (i<maxword) {
                System.out.print(lyrics[i] + " ");
            } else {
                System.out.println("...");
                break;
            }
        }
    }
    //Ce n'est certes pas intuitif, cela est dû au fait que j'ai mal compris l'énoncé.
    //En effet, je pensais que le choix du mode de jeu était le choix d'une difficulté
    //Or, il s'agit en fait du choix entre deviner le nom de la chanson, de l'artiste ou les deux
    /**
     * @param blindTest
     * @param maxword
     * @param choix
     */
    public void playBlindTest(BlindTest blindTest, int maxword,int choix) {
        int a=9;
        while (a!=0) {
            int nbPropositions=1;
            Random rand = new Random();
            int n = rand.nextInt(blindTest.blindTest.songs.size());
            Song song = blindTest.blindTest.songs.get(n);
            playSong(song, maxword);
            List<String[]> answer = new ArrayList<>();
            if (choix==2){
                System.out.println("Quelle est le nom de la chanson ?");
                Scanner sc = new Scanner(System.in);
                String name = sc.nextLine();
                answer.add(new String[]{name, song.artist});
            }
            if (choix==1){
                System.out.println("Quel est le nom de l'artiste ?");
                Scanner sc = new Scanner(System.in);
                String artist = sc.nextLine();
                answer.add(new String[]{song.name, artist});
            }
            if (choix==3) {
                System.out.println("Quelle est le nom de la chanson ?");
                Scanner sc = new Scanner(System.in);
                String name = sc.nextLine();
                System.out.println("Quel est le nom de l'artiste ?");
                String artist = sc.nextLine();
                answer.add(new String[]{name, artist});
            }

            if (checkAnswer(answer, song)) {
                System.out.println("Chanson suivante");
                score+=nbPropositions;
                nbPropositions = 0;
            } else {
                System.out.println("Réessayez");
                nbPropositions++;
            }
            if (nbPropositions==10) {
                System.out.println("La réponse était : " + song.name + " - " + song.artist);
                System.out.println("Chanson suivante");
                nbPropositions = 0;
            }
            a--;
        }
    }

    public void compareScore() {
        int minscore=90; //pire score possible
        int vainqueur=0;
        for (Utilisateur u : participants) {
            System.out.println(u.getPseudo()  + " a un score de " + score);
            if (score<minscore) {
                minscore=score;
                vainqueur=participants.indexOf(u);
            }
        } System.out.println("Le vainqueur est " + participants.get(vainqueur).getPseudo() + " avec un score de " + minscore);
    }

}
