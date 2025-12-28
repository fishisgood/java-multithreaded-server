enum ACADEMICSTATUS {
	STUDENT,
	TEACHER,
	OTHER
}

public class Client {
    private String username;
    private String password;
    private ACADEMICSTATUS academicStatus;
    private int yearsAtRuppin;

    public Client(String username, String password, String status, int years) {
		setAcademicStatus(status);
		setPassword(password);
		setYearsAtRuppin(years);
		setUsername(username);
    }



	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ACADEMICSTATUS getAcademicStatus() {
		return academicStatus;
	}

	public int getYearsAtRuppin() {
		return yearsAtRuppin;
	}

	public void setPassword(String password) {
		if (isValidPassword(password)) {
			this.password = password;
		}
	}

	public void setAcademicStatus(String status) {
		if (isValidAcademicStatus(status)) {
			this.academicStatus = ACADEMICSTATUS.valueOf(status.toUpperCase());
		}
	}

	public void setYearsAtRuppin(int years) {
		if (isValidYears(years)) {
			this.yearsAtRuppin = years;
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}


	    // מתודת ולידציה לסיסמה (9+ תווים, אות גדולה, קטנה וספרה)
    public static boolean isValidPassword(String pass) {
        if (pass == null || pass.length() < 9) {
			System.out.println("Password must be at least 9 characters long.");
			return false;
		}
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
		if (hasUpper && hasLower && hasDigit) {
			return true;
		}else{
			System.out.println("Password must be at least 9 characters long and include uppercase, lowercase letters, and digits.");
			return false;
		}
    }

	public static boolean isValidAcademicStatus(String academicStatus) {
		if (academicStatus.equalsIgnoreCase("student") || 
			academicStatus.equalsIgnoreCase("teacher") || 
		    academicStatus.equalsIgnoreCase("other")) {
			return true;
		} else {
			System.out.println("Invalid academic status. Choose STUDENT, TEACHER, or OTHER.");
			return false;
		}
	}

	public static boolean isValidYears(int yearsAtRuppin) {
		if (yearsAtRuppin<0) {
			System.out.println("Years at Ruppin cannot be negative.");
			return false;
		}
		return true;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Client) {
            return this.username.equalsIgnoreCase(((Client)obj).username);
        }
        return false;
    }
    

}