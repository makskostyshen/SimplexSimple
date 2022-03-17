package LinearProgrammingProblem;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum Sign {
    MORE(">="),
    LESS("<="),
    EQUAL("="),
    NON("_");

    String signImpl;


    Sign(String signImpl) {
        this.signImpl = signImpl;
    }

    public static Sign getOpposite(Sign sign){
        return sign == Sign.LESS ? Sign.MORE : Sign.LESS;
    }

    public boolean isNotProper(Sign... signs){
        Set<Sign> possibleSigns = EnumSet.copyOf(Arrays.asList(signs));
        return !possibleSigns.contains(this);
    }

    public static Sign getProperSign(String signImpl, Sign... possibleSigns)
            throws InappropriateSignException{

        Sign newSign = Sign.valueOf(signImpl);
        if(newSign.isNotProper(possibleSigns)){
            throw new InappropriateSignException();
        }
        return newSign;
    }

    @Override
    public String toString() {
        return signImpl;
    }

}

class InappropriateSignException extends IllegalArgumentException{}