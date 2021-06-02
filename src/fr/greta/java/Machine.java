package fr.greta.java;

public class Machine {

    private static final String PANNEAU_VALEUR_ANNULER = "A";
    private static final String PANNEAU_VALEUR_CAFE = "1";
    private static final String PANNEAU_VALEUR_DECA = "2";
    private static final String PANNEAU_VALEUR_DOUBLE_CAFE = "3";

    private static final String PANNEAU_VALEUR_PAS_SUCRE = "1";
    private static final String PANNEAU_VALEUR_PEU_SUCRE = "2";
    private static final String PANNEAU_VALEUR_MOYEN_SUCRE = "3";
    private static final String PANNEAU_VALEUR_IMPORTANT_SUCRE = "4";
    private static final String PANNEAU_VALEUR_BEAUCOUP_SUCRE = "5";

    private MachineUserInterface ui = new MachineUserInterface();

    private Boisson boissonSelectionne;
    private Sucre sucreSelectionne;

    private double argentEnAttente;

    private StockCafe stock = new StockCafe();

    public void run() {
        init();
        while (true) {
            UserAction action = ui.waitAndScan();
            switch (action.type) {
                case PANNEAU:
                    actionPanneau(action.valeur);
                    break;
                case MONNAIE:
                    actionMonnaie(action.valeur);
                    break;
            }
        }
    }

    private void init() {
        argentEnAttente = 0;
        boissonSelectionne = null;
        sucreSelectionne = null;
        ui.demanderBoisson();
    }

    private void actionPanneau(String valeur) {
        if (valeur.equals(PANNEAU_VALEUR_ANNULER)) {
            rendreTout();
            init();
        } else if (boissonSelectionne == null) {
            Boisson boisson = choisirBoisson(valeur);
            if (boisson != Boisson.INCONNUE) {
                boissonSelectionne = boisson;
                if (validStock(boissonSelectionne)){
                    ui.demanderSucre();
                } else {
                    init();
                }
            }
        } else {
            Sucre sucre = demanderQuantiteSucre(valeur);
            if (sucre != Sucre.INCONNUE) {
                sucreSelectionne = sucre;
                System.out.println("Vous voulez: " + sucreSelectionne.name());
                ui.demanderArgent(resteAPayer());
            }
        }
    }


    private Boisson choisirBoisson(String valeur) {
        switch (valeur) {
            case PANNEAU_VALEUR_CAFE:
                return Boisson.CAFE;
            case PANNEAU_VALEUR_DECA:
                return Boisson.DECA;
            case PANNEAU_VALEUR_DOUBLE_CAFE:
                return Boisson.DOUBLE_CAFE;
            default:
                return Boisson.INCONNUE;
        }
    }

    private void actionMonnaie(String valeur) {
        argentEnAttente += Double.parseDouble(valeur);
        if (boissonSelectionne != null && sucreSelectionne != null) {
            if (argentEnAttente >= prixBoisson()) {
                ui.distribuer(boissonSelectionne);
                diminuerStockCafe();
                if (argentEnAttente > prixBoisson()) {
                    ui.rendre(argentEnAttente - prixBoisson());
                }
                init();
            } else {
                ui.demanderArgent(resteAPayer());
            }
        }
        if (boissonSelectionne == null && argentEnAttente > 0) {
            rendreTout();
            init();
        }
        if (sucreSelectionne == null && argentEnAttente > 0) {
            rendreTout();
            argentEnAttente = 0;
        }
    }

    private void rendreTout() {
        if (argentEnAttente > 0) {
            ui.rendre(argentEnAttente);
        }
    }

    private double resteAPayer() {
        return prixBoisson() - argentEnAttente;
    }

    private double prixBoisson() {
        switch (boissonSelectionne) {
            case CAFE:
                return 1;
            case DECA:
                return 0.85;
            case DOUBLE_CAFE:
                return 1.6;
            default:
                return 0;
        }
    }

    private Sucre demanderQuantiteSucre(String valeur) {

        switch (valeur) {
            case PANNEAU_VALEUR_PAS_SUCRE:
                return Sucre.PAS_SUCRE;
            case PANNEAU_VALEUR_PEU_SUCRE:
                return Sucre.PEU_SUCRE;
            case PANNEAU_VALEUR_MOYEN_SUCRE:
                return Sucre.MOYEN_SUCRE;
            case PANNEAU_VALEUR_IMPORTANT_SUCRE:
                return Sucre.IMPORTANT_SUCRE;
            case PANNEAU_VALEUR_BEAUCOUP_SUCRE:
                return Sucre.BEAUCOUP_SUCRE;
            default:
                return Sucre.INCONNUE;

        }
    }

    public void diminuerStockCafe() {

        switch (boissonSelectionne) {
            case CAFE:
                stock.stockCafe -= 20;
            case DOUBLE_CAFE:
                stock.stockCafe -= 30;
            case DECA:
                stock.stockDeca -= 15;
        }
    }

    private boolean validStock(Boisson boissonSelectionne) {


        switch (boissonSelectionne) {
            case CAFE:
                 if (stock.stockCafe < 20){
                     System.out.println("Stock de café indisponible, veuillez changer de boisson");
                    return false;
                 } break;
            case DOUBLE_CAFE:
                if (stock.stockCafe < 30){
                    System.out.println("Stock de café indisponible, veuillez changer de boisson");
                    return false;
                } break;
            case DECA:
                if (stock.stockDeca < 15){
                    System.out.println("Stock de déca indisponible, veuillez changer de boisson");
                    return false;
                } break;
        } return true;
    }
}
