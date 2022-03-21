package LinearProgrammingProblem;

import java.util.*;
import java.util.stream.Collectors;


public class Problem
{
    private List<Constraint> constraints;
    private List<Double> minFuncComponents;
    private List<Sign> signs;
    private int varsNum;
    private int slackVarsNum;
    private int surplusVarsNum;
    private int consNum;

    private static final Sign[] variableSigns = new Sign[]{Sign.MORE, Sign.LESS, Sign.NON};

    public static Problem createProblem(List<String> data){
        Problem problem = new Problem();
        try{
            problem = new Problem(data);
        }
        catch (NumberFormatException nfe){
            System.out.println("Error:\n" + nfe);
        }
        catch (SimplexProblemException spe){
            System.out.println("Error:\n" + spe);
        }
        catch(Exception e){
            System.out.println("Error:\n" + e);
            System.out.println("Not expected problem detected");
            e.printStackTrace(System.out);
        }
        finally {
            return problem;
        }
    }

    private Problem(){}

    private Problem (List<String> data)
            throws IllegalArgumentException{

        List<List<String>> separatedValues = getSeparatedValues(data);
        Iterator<List<String>> iterator = separatedValues.iterator();

        minFuncComponents = createMinFuncComponents(iterator.next());
        signs = createSigns(iterator.next());
        constraints = createConstraints(iterator);
        varsNum = getProperVarsNum();
        consNum = constraints.size();
    }

    private static List<List<String>> getSeparatedValues(List<String> data){
        return data.stream()
                .map(str -> str.split("\\s+"))
                .map(array -> Arrays.asList(array))
                .toList();
    }

    private static List<Double> createMinFuncComponents(List<String> minFuncComponentsString)
            throws NumberFormatException{

        return minFuncComponentsString.stream()
                .map(str -> Double.valueOf(str))
                .collect(Collectors.toList());
    }

    private static List<Sign> createSigns(List<String> signsString)
            throws SignException{

        return signsString.stream()
                .map(signString -> Sign.getProperSign(signString, variableSigns))
                .collect(Collectors.toList());
    }

    private static List<Constraint> createConstraints(Iterator<List<String>> iterator)
            throws IllegalArgumentException{

        List<Constraint> constraints = new ArrayList<>();
        while(iterator.hasNext()){
            constraints.add(new Constraint(iterator.next()));
        }
        return constraints;
    }

    private int getProperVarsNum()
            throws InappropriateNumberOfVarsException{

        Set<Integer> varsSizes = new HashSet<>();

        varsSizes.add(signs.size());
        varsSizes.add(minFuncComponents.size());
        for(Constraint constraint: constraints){
            varsSizes.add(constraint.getVarComponents().size());
        }

        if(varsSizes.size() != 1){
            throw new InappropriateNumberOfVarsException(
                    "Different rows have different" +
                    "number of components:"+
                    varsSizes);}

        return varsSizes.iterator().next();
    }


    public boolean hasSlackIdentityMatrix(){
        return slackVarsNum == consNum;
    }
    public void toStandartForm(){
        varsTransform();
        constraintsTransform();
    }


    private void varsTransform(){
        for(int i = 0; i < varsNum; i++){
            if (signs.get(i) == Sign.LESS){
                varReverse(i);
            }
            else if (signs.get(i) == Sign.NON){
                varSplitIntoPositive(i);
            }
        }
    }
    private void constraintsTransform(){
        for(int j = 0; j < consNum; j++){
            get(j).makeRHSPositive();
            constraintMakeEquation(j);
        }
    }


    private void varSplitIntoPositive(int varsIndex){
        for (int j = 0; j < consNum; j++){
            double newComponent =  -get(j).getVarComponents().get(varsIndex);
            get(j).getVarComponents().add(varsIndex+1, newComponent);
        }

        minFuncComponents.add(varsIndex, -minFuncComponents.get(varsIndex));
        signs.set(varsIndex, Sign.MORE);
        signs.add(varsIndex + 1, Sign.MORE);

    }
    private void varReverse(int varsIndex){
        for(int j = 0; j < consNum; j++){
            double newValue =  -get(j).getVarComponents().get(varsIndex);
            get(j).getVarComponents().set(varsIndex, newValue);
        }
        signs.set(varsIndex, Sign.getOpposite(signs.get(varsIndex)));
    }


    private void addSlackVar(int consIndex){
        for(int i = 0; i < consNum; i++){
            if(i == consIndex){
                get(i).addSlackComponent();
                get(i).setSign(Sign.EQUAL);
            }
            else get(i).addSlackEmpty();
        }
        minFuncComponents.add(0.);
        signs.add(Sign.MORE);
        slackVarsNum++;
    }
    private void addSurplusVar(int consIndex){
        for(int i = 0; i < consNum; i++){
            if(i == consIndex){
                get(i).addSurplusComponent();
                get(i).setSign(Sign.EQUAL);
            }
            else get(i).addSurplusEmpty();
        }
        minFuncComponents.add(0.);
        signs.add(Sign.MORE);
        surplusVarsNum++;
    }

    private void constraintMakeEquation(int consIndex){
        if(get(consIndex).getSign() == Sign.LESS){
            addSlackVar(consIndex);
        }
        else if(get(consIndex).getSign() == Sign.MORE){
            addSurplusVar(consIndex);
        }
    }


    public int getConsNum() {
        return consNum;
    }

    public int getVarsNum() {
        return varsNum;
    }

    public int getSlackVarsNum() {
        return slackVarsNum;
    }

    public int getSurplusVarsNum() {
        return surplusVarsNum;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public List<Double> getMinFuncComponents() {
        return minFuncComponents;
    }

    public Constraint get(int consIndex){
        return constraints.get(consIndex);
    }

    public boolean isEmpty(){
        return constraints == null;
    }

    @Override
    public String toString(){
        if(isEmpty()) {return "...empty table...";}

        StringBuilder builder = new StringBuilder();
        builder.append(minFuncComponents.toString());
        builder.append("\n"+ signs.toString());

        for(Constraint constraint: constraints){
            builder.append("\n" + constraint);
        }
        return builder.toString();
    }
}

class SimplexProblemException extends IllegalArgumentException{
    SimplexProblemException(String msg){
        super(msg);
    }
    SimplexProblemException(){}
}
class InappropriateNumberOfVarsException extends SimplexProblemException{
    InappropriateNumberOfVarsException(String msg){
        super(msg);
    }
    InappropriateNumberOfVarsException(){}
}