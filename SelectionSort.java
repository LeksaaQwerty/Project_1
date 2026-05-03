public class SelectionSort implements SortingStrategy {
    @Override
    public void sort(Car[] source) {
        for(int i = 0; i < source.length; i++){
            for(int j = i + 1; j < source.length; j++){
                if(source[i].compareTo(source[j]) > 0){
                    var temp = source[i];
                    source[i] = source[j];
                    source[j] = temp;
                }
            }
        }
    }
}
