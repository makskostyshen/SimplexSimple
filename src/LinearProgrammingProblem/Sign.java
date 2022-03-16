package LinearProgrammingProblem;

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
        Sign newSign = sign == Sign.LESS ? Sign.MORE : Sign.LESS;
        return newSign;
    }

    @Override
    public String toString() {
        return signImpl;
    }

}
