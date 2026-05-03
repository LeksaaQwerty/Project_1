public class SelectionSort implements SortingStrategy {
    @Override
    public void sort(Car[] source) {
        for(int i = 0; i < source.length; i++){
            int pos = i;
            var min = source[i];
            for(int j = i + 1; j < source.length; j++){
                if(min.compareTo(source[j]) > 0){
                    pos = j;
                    min = source[j];
                }
            }
            source[pos] = source[i];
            source[i] = min;
        }
    }
}
