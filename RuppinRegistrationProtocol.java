import java.util.List;

public class RuppinRegistrationProtocol {
    private enum State { 
        START, INITIAL_DECISION, LOGIN_USER, LOGIN_PASS, UPDATE_DECISION,
        USER_CHANGE_Q, GET_NEW_USER, PASS_CHANGE_Q, GET_NEW_PASS,
        YEAR_CHANGE_Q, GET_NEW_YEARS, REG_FLOW_USER, REG_FLOW_PASS, 
        REG_FLOW_STATUS, REG_FLOW_YEARS, FINISHED 
    }
    
    private State state = State.START;
    private Client activeClient;
    private String tempUser, tempPass, tempStat;

    public String processInput(String theInput, List<Client> clientState) {
        switch (state) {
            case START:
                state = State.INITIAL_DECISION;
                return "Do you want to register? (yes/no)";

            case INITIAL_DECISION:
                if (theInput.equalsIgnoreCase("yes")) {
                    state = State.REG_FLOW_USER; return "Enter username:";
                } else if (theInput.equalsIgnoreCase("no")) {
                    state = State.LOGIN_USER; return "Username:";
                } else {
                    return "Invalid input. Please answer 'yes' or 'no': Do you want to register?";
                }

            case LOGIN_USER:
                activeClient = findClient(theInput, clientState);
                state = State.LOGIN_PASS; return "Password:";

            case LOGIN_PASS:
                if (activeClient != null && activeClient.getPassword().equals(theInput)) {
                    state = State.UPDATE_DECISION;
                    return "Welcome back, " + activeClient.getUsername() + ". Last time you defined yourself as " + 
                           activeClient.getAcademicStatus() + " for " + activeClient.getYearsAtRuppin() + 
                           " years. Do you want to update your information? (yes/no)";
                }
                state = State.START; return "Auth failed. Do you want to register? (yes/no)";

            case UPDATE_DECISION:
                if (theInput.equalsIgnoreCase("yes")) {
                    state = State.USER_CHANGE_Q; return "Do you want to change your username? (yes/no)";
                } else if (theInput.equalsIgnoreCase("no")) {
                    state = State.FINISHED; return "Bye.";
                } else {
                    return "Invalid input. Do you want to update your information? (yes/no)";
                }

            case USER_CHANGE_Q:
                if (theInput.equalsIgnoreCase("yes")) {
                    state = State.GET_NEW_USER; return "Enter new username:";
                } else if (theInput.equalsIgnoreCase("no")) {
                    state = State.PASS_CHANGE_Q; return "Do you want to change your password? (yes/no)";
                } else {
                    return "Invalid input. Do you want to change your username? (yes/no)";
                }

            case GET_NEW_USER:
                activeClient.setUsername(theInput);
                state = State.PASS_CHANGE_Q;
                return "Username updated successfully. Do you want to change your password? (yes/no)";

            case PASS_CHANGE_Q:
                if (theInput.equalsIgnoreCase("yes")) {
                    state = State.GET_NEW_PASS; return "Enter new password:";
                } else if (theInput.equalsIgnoreCase("no")) {
                    state = State.YEAR_CHANGE_Q; return "Do you want to update your years of study? (yes/no)";
                } else {
                    return "Invalid input. Do you want to change your password? (yes/no)";
                }

            case GET_NEW_PASS:
                if (Client.isValidPassword(theInput)) {
                    activeClient.setPassword(theInput);
                    state = State.YEAR_CHANGE_Q;
                    return "Password updated successfully. Do you want to update your years of study? (yes/no)";
                }
                return "Invalid password. Enter new password (9+ chars, Upper, Lower, Digit):";

            case YEAR_CHANGE_Q:
                if (theInput.equalsIgnoreCase("yes")) {
                    state = State.GET_NEW_YEARS; return "Enter number of years:";
                } else if (theInput.equalsIgnoreCase("no")) {
                    state = State.FINISHED; return "Thanks. Your information has been updated. Bye.";
                } else {
                    return "Invalid input. Do you want to update your years of study? (yes/no)";
                }

            case GET_NEW_YEARS:
                try {
                    int years = Integer.parseInt(theInput);
                    if (Client.isValidYears(years)) {
                        activeClient.setYearsAtRuppin(years);
                        state = State.FINISHED;
                        return "Years updated successfully. Thanks. Your information has been updated. Bye.";
                    }
                    return "Invalid number of years. Enter number of years:";
                } catch (NumberFormatException e) {
                    return "Please enter a valid number: How many years have you been at Ruppin?";
                }

            // registration
            case REG_FLOW_USER:
                if (findClient(theInput, clientState) != null) return "Username exists. Try again:";
                tempUser = theInput; state = State.REG_FLOW_PASS; return "Enter password:";
            
            case REG_FLOW_PASS:
                if (Client.isValidPassword(theInput)) {
                    tempPass = theInput; state = State.REG_FLOW_STATUS; return "Status (student/teacher/other):";
                }
                return "Password too weak. Try again:";

            case REG_FLOW_STATUS:
                if (Client.isValidAcademicStatus(theInput)) {
                    tempStat = theInput; state = State.REG_FLOW_YEARS; return "Number of years:";
                }
                return "Invalid status. (student/teacher/other):";

            case REG_FLOW_YEARS:
                try {
                    int years = Integer.parseInt(theInput);
                    if (Client.isValidYears(years)) {
                        clientState.add(new Client(tempUser, tempPass, tempStat, years));
                        state = State.FINISHED; return "Registration complete. Bye.";
                    }
                    return "Invalid number. Enter years:";
                } catch (NumberFormatException e) {
                    return "Invalid input. Enter number of years:";
                }

            case FINISHED: return "Bye.";
            default: return "Error.";
        }
    }

    private Client findClient(String user, List<Client> list) {
        for (Client c : list) if (c.getUsername().equalsIgnoreCase(user)) return c;
        return null;
    }
}