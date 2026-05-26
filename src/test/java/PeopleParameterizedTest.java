import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleParameterizedTest {
    @ParameterizedTest(name =
                          "{index} => compareTo({0},{1},{2},{3}) == {4}")
    @CsvFileSource(resources = "/peopleData.csv", numLinesToSkip = 1)
    void comparePeopleTest(
       String name1,
       int age1,
       String name2,
       int age2,
       int expected) {

        People p1 = new People(name1, age1);
        People p2 = new People(name2, age2);

        int result = p1.compareTo(p2);

        assertEquals(expected, result);
    }
}