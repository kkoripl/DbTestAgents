package sag_projekt.agents.tester.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DatabaseInvestigation{
    private List<Double> investigationResults;
    private List<String> investigationActions;

    public DatabaseInvestigation(String dbToInvestigate) {
        /* Todo: MECHANIZM DO NADAWANIA AKCJI DO SPRAWDZENIA BAZY ZALEZNIE OD TEGO, KTORY AGENT WYWOLUJE,
                 MOZE BYC NA PODSTAWIE NAZWY BAZY LUB POSZUKIWANEGO SLOWA, PACHNIE JAKIMS FACTORY CZY COS
                 AKTUALNIE NASTAWIONE U GORY PO NAZWIE BAZY, ALE WSZYSTKIE DANE BADANIA PODSTAWIANE
                 JEST TO POLE DBNAME W TESTRZE - JAK ZMIANA TO REFACTOR
                 TAK SAMO WYNIKI TESTOW SA AKTUALNIE LADOWANE W DOUBLE - JAK BEDZIEMY PYTAC O LICZBE REKORDOW TRZEBA TO ZMIENIC NA INT
         */
        this.investigationActions = Arrays.asList("firstAction", "secondAction", "thirdAction");
        makeDBInvestigation();
    }

    private void makeDBInvestigation(){
        investigationResults = new ArrayList<>();
        Random rand = new Random(); //dodane pod "mockowanie" wynikow bazy danych - przy polaczeniu z nimi do usuniecia
        for (String action : this.investigationActions){
            // @Todo: WYWOLANIE AKCJI Z LISTY I NA JEJ PODSTAWIE NADANIE WYNIKU
            investigationResults.add(rand.nextDouble()+1);
        }
    }

    public List<Double> getInvestigationResults() {
        return investigationResults;
    }
}
