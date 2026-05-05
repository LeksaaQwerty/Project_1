package app;

import java.util.Scanner;

public class UserDataResponse {
	private static Scanner sc = new Scanner(System.in);

	public static int getIntAnswerFromUser(int min, int max, String message) {
		int answer = 0;
		System.out.println(message);
		System.out.println("от " + min + " до " + max);

		while (!sc.hasNextInt()) {
			System.out.println("попробуйте ещё раз >>>>");
			sc.nextLine();
		}
		answer = sc.nextInt();
		sc.nextLine();
		if (answer < min || answer > max) {
			System.out.println("попробуйте ещё раз >>>>");
			getIntAnswerFromUser(min, max, message);
		}
		return answer;
	}

	public static String getStringAnswerFromUser(String message) {

		int counter = 0;
		int numberOfAttempts = 10;
		String answer = null;
		while (counter < numberOfAttempts) {
			System.out.println(message);
			answer = sc.nextLine();
			if (!answer.isBlank()) {
				return answer.trim();
			}
			counter++;
		}
		System.out.println("Что-то пошло не так, попробуйте запустить заново");
		return null;
	}
}
