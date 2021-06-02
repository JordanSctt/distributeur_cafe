package fr.greta.java;

import java.util.Scanner;

public class MachineUserInterface {

    private static final String SEPERATEUR_ACTION = ":";

    private final Scanner scanner = new Scanner(System.in);

    public UserAction waitAndScan() {
        return toUserAction(scanner.nextLine());
    }

    private UserAction toUserAction(String str) {
        UserAction userAction = new UserAction();
        userAction.type = UserActionType.INCONNUE;


        String[] split = str.split(SEPERATEUR_ACTION);
        if(split.length == 2) {
            for(UserActionType eachType : UserActionType.values()) {
                if(eachType.name().equalsIgnoreCase(split[0])) {
                    userAction.type = eachType;
                }
            }
            userAction.valeur = split[1];
        }
        return userAction;
    }

    public void demanderBoisson() {
        System.out.println("MESSAGE:Merci de choisir une boisson");
    }

    public void rendre(double monnaie) {
        System.out.println("RENDRE_MONNAIE:" + monnaie);
    }

    public void demanderArgent(double prix) {
        System.out.println("MESSAGE:Merci de payer " + prix + " €");
    }

    public void distribuer(Boisson boisson) {
        System.out.println("DISTRIB:" + boisson.name());
    }

    public void demanderSucre() {
        System.out.println("MESSAGE:Merci de choisir la quantité de sucre souhaité(de 0 à 5)");
    }
}
