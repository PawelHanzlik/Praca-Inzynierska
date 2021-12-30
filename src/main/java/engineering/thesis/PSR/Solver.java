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

    private final List<Integer> result = new ArrayList<>();
    private final List<Long> zoneIds = new ArrayList<>();

    public Solver(List<ZoneEntity> zones, String[] usersChoices) {

        final int MAXVAR = 11;
        final int NBCLAUSES = zones.size()+9;

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
                throw new Exception("Niespełnialna formuła");
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
