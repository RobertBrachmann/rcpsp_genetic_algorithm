import rcpsp.geneticAlgorithm.*;
import java.awt.*;

public class Main {

    public static void main(String args[]) throws Exception {

        // Select PspLib instance
        FileDialog dialog = new FileDialog((Frame)null, "Select PspLip instance");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory() + dialog.getFile();
        dialog.dispose();

        // Create instance of genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(file);

        // Genetic algorithm with one point cross over, random activity list mutation and tabu list
        geneticAlgorithm.solveOnePoint(200,100, 0.4, 100);
    }
}
