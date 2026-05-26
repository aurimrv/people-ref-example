# JUnit 5 Parameterized Tests with Custom Objects

Consider I want to test a method that takes a custom object as a parameter, for example, a method like:

```java
compareTo(People other)
```

you need to create `People` objects from the CSV values before running the test.

A common solution is:

1. Store the attributes of each `People` object in the CSV
2. Construct the objects inside the parameterized test

---

# Example

Suppose your class is:

```java
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
```

---

# CSV File

You can represent TWO `People` objects in one CSV row:

```csv
name1,age1,name2,age2,expected
John,20,Mary,25,-1
Mary,30,John,20,1
Ana,40,Ana,40,0
```

Meaning:

```java
person1.compareTo(person2) == expected
```

---

# JUnit 5 Parameterized Test

```java
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
```

---

# Better Alternative: Use `ArgumentsAccessor`

If your object has many fields, parameter lists become ugly.

JUnit 5 provides `ArgumentsAccessor`:

```java
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
```

---

# Even Better: Custom Converter

For larger projects, you can create a custom converter so JUnit automatically transforms CSV text into `People` objects.

Example CSV:

```csv
John:20,Mary:25,-1
```

Then create a converter:

```java
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class PeopleConverter extends SimpleArgumentConverter {

   @Override
   protected Object convert(Object source, Class<?> targetType) {

      String[] parts = source.toString().split(":");

      return new People(parts[0],
         Integer.parseInt(parts[1]));
   }
}
```

Test:

```java
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
```

---

# Recommendation

For most university assignments or medium projects:

- Small objects → construct inside the test
- Large objects → use `ArgumentsAccessor`
- Enterprise/clean architecture → custom converters

The first approach is usually the simplest and most readable.