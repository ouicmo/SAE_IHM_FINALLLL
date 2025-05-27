package appli.modele;

public class ClientScenario {
    public static void main(String[] args) throws Exception {
        Scenario s0 = new Scenario("s0");
        Membre psy = new Membre("Psykokwak");
        System.out.println(psy.getChVille().toString()+" "+s0.getTransactions().get(psy).getChVille().toString());

    }
}
