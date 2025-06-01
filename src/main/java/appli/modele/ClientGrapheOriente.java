package appli.modele;

import java.util.List;

public class ClientGrapheOriente {
    public static void main(String[] args) throws Exception {
        GrapheOriente g = new GrapheOriente(new Scenario("s0"));

        /*List<String> top5 = g.meilleurschemins();
        for (String desc : top5) {
            System.out.println(desc);
        }*/
        System.out.println(g.triTopologique());
        //System.out.println(g.triDistance());
    }
}
