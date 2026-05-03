public class ArraySorter {
    private final SortingStrategy sortingStrategy;

    public ArraySorter(SortingStrategy sortingStrategy){
        this.sortingStrategy = sortingStrategy;
    }
    public void sort(Car[] source){
        sortingStrategy.sort(source);
    }
}
