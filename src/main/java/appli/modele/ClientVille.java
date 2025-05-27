package appli.modele;

public class ClientVille {
    public static void main(String[] args) throws Exception {
        Ville Bordeaux = new Ville("Bordeaux");
        Ville Velizy = new Ville("Velizy");
        Ville Paris = new Ville("Paris");
        System.out.println(Bordeaux.getChDistanceVille(Paris));
    }
}
