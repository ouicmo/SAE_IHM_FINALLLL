package appli.modele;

import java.util.List;

public class ClientGrapheOriente {
    public static void main(String[] args) throws Exception {
        GrapheOriente g = new GrapheOriente(new Scenario("s7"));
        GrapheOriente g2 = new GrapheOriente(new Scenario("s8"));
        System.out.println("Scénario 7");
        System.out.println(g.triTopologique());
        System.out.println(g.triDistance());
        System.out.println(g.meilleurschemins());
        System.out.println("\nScénario 8");
        System.out.println(g2.triTopologique());
        System.out.println(g2.triDistance());
        System.out.println(g2.meilleurschemins());

    }
}
