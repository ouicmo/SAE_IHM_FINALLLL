package appli.modele;

public class ClientScenario {
    public static void main(String[] args) throws Exception {
        Scenario s0 = new Scenario("s0");
        Membre psy = new Membre("Psykokwak");
        System.out.println(s0.getVendeurs());

    }
}
