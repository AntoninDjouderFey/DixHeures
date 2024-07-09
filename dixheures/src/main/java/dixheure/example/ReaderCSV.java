//cette classe a servi à tester la leture de fichier csv
//elle est remplacée par la classe CSVLoader.java

package dixheure.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReaderCSV {

    public static void main(String[] args) {
        String csvFile = "données\\spotify_millsongdata.csv"; // Remplacez par le chemin de votre fichier CSV
        String line;
        String csvSplitBy = ","; // Séparateur de colonnes (virgule par défaut, mais peut être changé selon le fichier)

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Utilisation du séparateur pour diviser la ligne en colonnes
                String[] columns = line.split(csvSplitBy);
                
                // Affichage des colonnes
                for (String column : columns) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
