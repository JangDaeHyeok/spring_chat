package jdh.example.chat.util.etc;

public class ValidateUtil {
	// null이 아니거나 빈 값이 아닌 경우
	public static boolean checkNotEmpty(Object o) {
		if(o == null) return false;
		if(o instanceof String && o.toString().equals("")) return false;
		
		return true;
	}
}
