package dixheure.example;
//à la mm utilité que la classe ReaderCSV.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSpecificCell {

    public static void main(String[] args) {
        String csvFile = "données\\spotify_millsongdata.csv"; // Remplacez par le chemin de votre fichier CSV
        String line;
        String csvSplitBy = ","; // Séparateur de colonnes (virgule par défaut, mais peut être changé selon le fichier)
        int targetLine = 23; // La ligne que vous voulez lire (10e ligne)
        int targetColumn = 4; // La colonne que vous voulez lire (3e colonne)
        int currentLine = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                currentLine++;
                if (currentLine == targetLine) {
                    // Diviser la ligne en colonnes
                    String[] columns = line.split(csvSplitBy);
                    if (columns.length >= targetColumn) {
                        // Afficher la valeur de la colonne cible
                        System.out.println("Valeur de la colonne " + targetColumn + " à la ligne " + targetLine + " : " + columns[targetColumn - 1]);
                    } else {
                        System.out.println("La ligne " + targetLine + " ne contient pas assez de colonnes.");
                    }
                    break; // Sortir de la boucle une fois la ligne cible trouvée
                }
            }
            if (currentLine < targetLine) {
                System.out.println("Le fichier ne contient pas " + targetLine + " lignes.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
