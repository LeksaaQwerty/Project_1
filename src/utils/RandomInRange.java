package utils;

public class RandomInRange {
  /* Включает в себя min и max */
  public static int randomInt(int min, int max) {
    if (max < min) {
      int temp = min;
      min = max;
      max = temp;
    }

    int res = (int) (Math.random() * (max - min + 1)) + min;

    return res;
  }
}
