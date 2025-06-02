package appli.modele;

public class ClientVille {
    public static void main(String[] args) throws Exception {
        Ville Nantes = new Ville("Nantes");
        Ville Toulouse = new Ville("Toulouse");
        Ville Dijon = new Ville("Dijon");
        System.out.println(Dijon.getChDistanceVille(Toulouse));
    }
}
