package engineering.thesis.PSR;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import org.sat4j.core.VecInt;
import org.sat4j.maxsat.WeightedMaxSatDecorator;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import org.sat4j.tools.OptToSatAdapter;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    /*
    Co klasa ma robić:
    W konstruktorze:
        Klasa otrzymuje dane na temat lokacji użytkownika i otaczających go stref
        (Strefy w której klient się znajduje i 6 przyległych)
        Na podstawie zajętości i zapotrzebowania na samochody tych stref oraz preferencji klienta
        wybieramy klauzule ze zmiennymi opisujące cechy idealnego parkingu.
        Następnie solver (Idzie na kompromis) wybiera takie wartości zmiennych
        aby jak najwięcej klauzul było spełnionych.
    W metodzie:
        Klasa porównuje parking z klauzulami a następnie zwraca
        jego przydatność lub ile zmiennych jest niespełnionych

             / \ / \
            | 2 | 3 |
           / \ / \ / \
          | 7 | 1 | 4 |
           \ / \ / \ /
            | 6 | 5 |
             \ / \ /
    Zmienne
        zmienna ujemna - zaprzeczenie
        S1  ..  S7  -   Parking znajduje się w strefie 1-7
        S8  -   Parking znajduje się w strefie preferowanej przez klienta
        S9  -   Parking ma conajmniej 10 wolnych miejsc parkingowych
        S10 -   Parking jest strzeżony
        S11 -   Parking jest płatny
        S12 -   Parking jest dla niepełnosprawnych
        S13 -   Rozmiar parkingu > 5
    Klauzule
        U0  -   Należy wybrać co najmniej jedną strefę
        U1  ..  U7  =   Strefa ma wysokie zapotrzebowanie
            [x & -1 ..-(x-1) & -(x+1) .. -7]
        U8  -   Preferowana strefa klienta to ta, którą wskazał solver // to pewnie do wyjebania
        U9  -   Klient jest niepełnosprawny ->  [12] waga 25
       -U9  -   Pełnosprawna osoba = [-12] waga 15
        U10 -   Rozmiar samochodu() >5 -  dużo wolnych miejsc lub duże miejsca [10] waga 20
       -U10 -   Rozmiar samochodu() <5 =  [-10] waga 10
        U11 -   Klient jest skąpy =  [-11] waga 15
       -U11 -   Klient nie jest skąpy = [11] waga 25
        U12 -   Klient dba o wygodę parkowania =  [10,9] waga 20
       -U12 -   Klient nie dba o wygodę parkowania = [-9] waga 10
    */

    private final List<Integer> result = new ArrayList<>();
    private final List<Long> zoneIds = new ArrayList<>();

    public Solver(List<ZoneEntity> zones, String[] usersChoices) {

        final int MAXVAR = 13;
        final int NBCLAUSES = zones.size()+1+5;

        int[] searchedZones = new int[zones.size()];
        for (int i = 0; i < zones.size(); i++) {
            searchedZones[i] = (i+1);
        }

        WeightedMaxSatDecorator maxSatSolver = new WeightedMaxSatDecorator(
                org.sat4j.maxsat.SolverFactory.newDefault());
        ModelIterator solver = new ModelIterator(new OptToSatAdapter(
                new PseudoOptDecorator(maxSatSolver)));

        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        for (int i=0;i<zones.size();i++) {
            int [] clause = new int[1];
            clause[0] =(-1)*(i+1);
            ZoneEntity e = zones.get(i);
            zoneIds.add(e.getZoneId());
            try {
                maxSatSolver.addSoftClause((int) (e.getPriority()),new VecInt(clause));
            }catch (ContradictionException exception ){
                exception.printStackTrace();
            }
        }

        try {
            maxSatSolver.addSoftClause(999999,new VecInt(searchedZones));
            //U8
            if (usersChoices[0].equals("Yes")){
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{8,10}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{}));
            }
            //U9
            if (usersChoices[3].equals("Yes")){
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{9,11}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{}));
            }
            //U11
            if (usersChoices[1].equals("Yes")){
                maxSatSolver.addSoftClause(15,new VecInt(new int[]{-11}));
            }else{
                maxSatSolver.addSoftClause(20,new VecInt(new int[]{11}));
            }
            //U12
            if (usersChoices[3].equals("Yes")){
                maxSatSolver.addSoftClause(20,new VecInt(new int[]{10,9}));
            }else{
                maxSatSolver.addSoftClause(10,new VecInt(new int[]{-9}));
            }
        } catch (ContradictionException exception) {
            exception.printStackTrace();
        }

        try {
            if (solver.isSatisfiable()){
                int [] temp =solver.model();
                for (int t : temp) result.add(t);
                System.out.println(result);
            }else{
                throw new Exception("Nierozwiązywale warunki");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int test(ParkingLotEntity parking) {
        int score = 0;

        long zone =parking.getZoneId();
        int index = zoneIds.indexOf(zone);
        //strefa
        if (index >(-1) && result.get(index)>0)
            score+=10;

        //S12 - niepełnosprawni
        if (result.contains(12) && parking.getIsForHandicapped())
            score++;
        else if (result.contains(-12) && !parking.getIsForHandicapped())
            score++;

        //S9 - 10 wolnych miejsc
        if (result.contains(9) && parking.getFreeSpaces() > 10)
            score++;

        else if (result.contains(-9) && parking.getFreeSpaces() < 10)
            score++;

        //S10 - Guarded
        if (result.contains(10) && parking.getIsGuarded())
            score++;

        else if (result.contains(-10) && !parking.getIsGuarded())
            score++;

        //S11 - Płatny
        if(result.contains(11) && parking.getIsPaid())
            score++;

        else if(result.contains(-11) && !parking.getIsPaid())
            score++;



        return score;
    }

}
