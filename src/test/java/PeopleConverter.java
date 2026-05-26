import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class PeopleConverter extends SimpleArgumentConverter {

   @Override
   protected Object convert(Object source, Class<?> targetType) {

      String[] parts = source.toString().split(":");

      return new People(parts[0],
         Integer.parseInt(parts[1]));
   }
}