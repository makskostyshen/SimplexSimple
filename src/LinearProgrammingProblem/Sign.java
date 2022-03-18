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
            throws SignException{
        Sign newSign;
        try{
            newSign = Sign.valueOf(signImpl);
        }
        catch(IllegalArgumentException iae){
            throw new WrongSignException("Cannot convert \"" + signImpl + "\" to Sign");
        }

        if(newSign.isNotProper(possibleSigns)){
            throw new InappropriateSignException("For sign \"" + newSign + "\"");
        }
        return newSign;
    }

    @Override
    public String toString() {
        return signImpl;
    }

}

class SignException extends SimplexProblemException{
    SignException(String msg){
        super(msg);
    }
    SignException(){}
}
class InappropriateSignException extends SignException{
    InappropriateSignException(String msg){
        super(msg);
    }
    InappropriateSignException(){}
}
class WrongSignException extends SignException{
    WrongSignException(String msg){
        super(msg);
    }
    WrongSignException(){}
}