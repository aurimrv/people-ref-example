import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleCustomConverterTest {
    @ParameterizedTest
    @CsvFileSource(resources="/people.csv", numLinesToSkip = 1)
    void testCompare(
       @ConvertWith(PeopleConverter.class) People p1,
       @ConvertWith(PeopleConverter.class) People p2,
       int expected) {

        assertEquals(expected, p1.compareTo(p2));
    }
}