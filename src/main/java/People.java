public class People implements Comparable<People> {

    private String name;
    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(People other) {
        return Integer.compare(this.age, other.age);
    }
}
