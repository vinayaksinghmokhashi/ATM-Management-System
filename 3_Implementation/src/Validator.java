class WrongPasswordException extends Exception {

	
	private static final long serialVersionUID = 4244307756761267255L;

	public WrongPasswordException() {
		super();
	}

	public WrongPasswordException(String msg) {
		super(msg);
	}
}

class Validator {

	public boolean validatePassword(String password) throws WrongPasswordException {

		if (password.length() < 8 || password.contains(" "))
			throw new WrongPasswordException("Password should meet the standard specifications!");

		int countSmallLetters = 0;
		int countCapitalLetters = 0;
		int countNumbers = 0;
		int countSymbols = 0;
		
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);

			if (c <= '9' && c >= '0')
				countNumbers++;
			else if (c >= 'a' && c <= 'z')
				countSmallLetters++;
			else if (c >= 'A' && c <= 'Z')
				countCapitalLetters++;
			else  
				countSymbols++;
		}

		if (countSymbols < 1 || countNumbers < 1 || countCapitalLetters < 1 || countSmallLetters < 2
				|| countSmallLetters <= countCapitalLetters)
			throw new WrongPasswordException("Password should meet the standard specifications!");

		return true;

	}
}
