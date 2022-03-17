package LinearProgrammingProblem;

import java.util.*;


public class Problem
{
    private List<Constraint> constraints;
    private List<Double> minFuncComponents;
    private List<Sign> signs;
    private int varsNum;
    private int slackVarsNum;
    private int surplusVarsNum;
    private int consNum;


    public Problem (List<String> data){
        Iterator<String> iterator = data.iterator();

        minFuncComponents = createMinFuncComponents(iterator.next());
        signs = createSigns(iterator.next());
        constraints = createConstraints(iterator);
        varsNum = signs.size();
        consNum = constraints.size();
    }

    private static List<Sign> createSigns(String signsString){
        List<Sign> signs = new ArrayList<>();
        List<String> signsListString =
                getElementsSplit(signsString);
        for(String elementString: signsListString){
            signs.add(Sign.valueOf(elementString));
        }
        return signs;
    }

    private static List<Constraint> createConstraints(Iterator<String> iterator){
        List<Constraint> constraints = new ArrayList<>();
        while(iterator.hasNext()){
            constraints.add(new Constraint(getElementsSplit(iterator.next())));
        }
        return constraints;
    }

    private static List<Double> createMinFuncComponents(String minFuncComponentsString){
        List<Double> minFuncComponents = new ArrayList<>();
        List<String> minFuncComponentsListString =
                getElementsSplit(minFuncComponentsString);

        for(String elementString: minFuncComponentsListString){
            minFuncComponents.add(Double.valueOf(elementString));
        }
        return minFuncComponents;
    }

    public boolean isSlackForm(){
        return areConstraintsRHSsPositive() && areVarsPositive() && areConstraintsOnlySpecificSign(Sign.LESS);
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
            constraints.get(j).makeRHSPositive();
            constraintMakeEquation(j);
        }
    }


    private void varSplitIntoPositive(int varsIndex){
        for (int j = 0; j < consNum; j++){
            double newComponent =  -get(j).getVarComponents().get(varsIndex);
            constraints.get(j).getVarComponents().add(varsIndex+1, newComponent);
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
                constraints.get(i).addSlackComponent();
                constraints.get(i).setSign(Sign.EQUAL);
            }
            else constraints.get(i).addSlackEmpty();
        }
        minFuncComponents.add(0.);
        signs.add(Sign.MORE);
        slackVarsNum++;
    }
    private void addSurplusVar(int consIndex){
        for(int i = 0; i < consNum; i++){
            if(i == consIndex){
                constraints.get(i).addSurplusComponent();
                constraints.get(i).setSign(Sign.EQUAL);
            }
            else constraints.get(i).addSurplusEmpty();
        }
        minFuncComponents.add(0.);
        signs.add(Sign.MORE);
        surplusVarsNum++;
    }

    private void constraintMakeEquation(int consIndex){
        if(constraints.get(consIndex).getSign() == Sign.LESS){
            addSlackVar(consIndex);
        }
        else if(constraints.get(consIndex).getSign() == Sign.MORE){
            addSurplusVar(consIndex);
        }
    }


    private boolean areVarsPositive(){
        boolean state = true;
        for(Sign sign: signs){
            if (sign != Sign.MORE){
                state = false;
               break;
            }
        }
        return state;
    }
    private boolean areConstraintsRHSsPositive(){
        boolean state = true;
        for(Constraint constraint: constraints){
            if (constraint.getRHS() < 0){
                state = false;
                break;
            }
        }
        return state;
    }
    private boolean areConstraintsOnlySpecificSign(Sign sign){
        boolean state = true;
        for(Constraint constraint: constraints){
            if (constraint.getSign() != sign){
                state = false;
                break;
            }
        }
        return state;
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

    private static List<String> getElementsSplit(String stringToSplit){
        List<String> elementsSplit = new ArrayList<>();
        int prevIndex = 0;
        int currIndex;

        for(currIndex = 0; currIndex < stringToSplit.length(); currIndex++){
            if(stringToSplit.charAt(currIndex) == ' '){
                String  newElement = stringToSplit.substring(prevIndex, currIndex);
                elementsSplit.add(newElement);
                prevIndex = currIndex+1;
            }
        }
        elementsSplit.add(stringToSplit.substring(prevIndex, currIndex));
        return elementsSplit;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(minFuncComponents.toString());
        builder.append("\n"+ signs.toString());

        for(Constraint constraint: constraints){
            builder.append("\n" + constraint);
        }
        return builder.toString();
    }

}
