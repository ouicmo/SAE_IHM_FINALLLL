package appli.modele;

public class ClientGrapheOriente {
    public static void main(String[] args) throws Exception {
        GrapheOriente g = new GrapheOriente(new Scenario("s8"));

        System.out.println(g.triTopologique());
    }
}
