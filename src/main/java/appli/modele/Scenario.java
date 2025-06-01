package appli.modele;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Scenario {
    private String chScenarioChoisi;
    private Map<Membre, Membre> chTransactions;

    /* Lis le scénario choisi en paramètre (de forme s0 pour le scénario 0 par exemple)
    * et donnes la liste des transactions sous forme de dictionnaire de la forme (Vendeur,Acheteur)*/
    public Scenario(String parScenario) throws Exception {
        /*Lecture des scénarios du fichier scénario.txt permettant d'obtenir un dictionnaire transactions tel que (Vendeur,Acheteur) */
        Map<String,String> scenarios = new LinkedHashMap<>();
        for (int i=0;i<9;i++) {
            scenarios.put("s"+i,"scenario_"+i);
        }
        chScenarioChoisi = parScenario;
        File scenarioFile = new File("data/"+scenarios.get(chScenarioChoisi)+".txt");
        Scanner scan3 = new Scanner(scenarioFile);

        chTransactions = new LinkedHashMap<>();

        while (scan3.hasNextLine()) {
            String line = scan3.nextLine();
            String[] split = line.split(" -> ");
            chTransactions.put(new Membre(split[0]),new Membre(split[1]));
        }
    }


    public Map<Membre, Membre> getTransactions() {
        return chTransactions;
    }

    public ArrayList<Membre> getAcheteurs(){
        ArrayList<Membre> acheteurs = new ArrayList<>();
        for (Membre m : chTransactions.keySet()) {
            acheteurs.add(chTransactions.get(m));
        }
        return acheteurs;
    }

    public ArrayList<Membre> getVendeurs(){
        ArrayList<Membre> vendeurs = new ArrayList<>();
        for (Membre m : chTransactions.keySet()) {
            vendeurs.add(m);
        }
        return vendeurs;
    }
}



    /*public Map<String, String> lectureScenario(String scenario) throws FileNotFoundException {
        //Lecture des scénarios du fichier scénario.txt permettant d'obtenir un dictionnaire transactions tel que (Vendeur,Acheteur)
        Map<String,String> scenarios = new LinkedHashMap<>();
        for (int i=0;i<9;i++) {
            scenarios.put("s"+i,"scenario_"+i);
        }
        File scenarioFile = new File("src/Données/"+scenarios.get(scenario)+".txt");
        Scanner scan3 = new Scanner(scenarioFile);

        Map<String, String> transactions = new LinkedHashMap<>();

        while (scan3.hasNextLine()) {
            String line = scan3.nextLine();
            String[] split = line.split(" -> ");
            transactions.put(split[0], split[1]);
        }
        return transactions;
    }*/

