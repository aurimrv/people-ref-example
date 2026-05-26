import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleArgumentsAccessorTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/peopleData.csv", numLinesToSkip = 1)
    void comparePeopleTest(ArgumentsAccessor args) {

        People p1 = new People(
           args.getString(0),
           args.getInteger(1));

        People p2 = new People(
           args.getString(2),
           args.getInteger(3));

        int expected = args.getInteger(4);

        assertEquals(expected, p1.compareTo(p2));
    }
}