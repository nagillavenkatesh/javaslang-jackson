package javaslang.jackson.datatype.tuples;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import javaslang.Tuple;
import javaslang.Tuple0;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.jackson.datatype.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TupleXTest extends BaseTest {

    @Test
    public void test0() throws IOException {
        Tuple0 tuple0 = Tuple0.instance();
        String json = mapper().writer().writeValueAsString(tuple0);
        Assert.assertEquals(mapper().readValue(json, Tuple0.class), tuple0);
        Assert.assertEquals(mapper().readValue(json, Tuple.class), tuple0);
    }

    @Test(expected = JsonMappingException.class)
    public void test9() throws IOException {
        String wrongJson = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
        mapper().readValue(wrongJson, Tuple.class);
    }

    @Test(expected = JsonMappingException.class)
    public void test10() throws IOException {
        String json = "[1, 2, 3]";
        mapper().readValue(json, Tuple2.class);
    }

    @Test(expected = JsonMappingException.class)
    public void test11() throws IOException {
        String json = "[1, 2]";
        mapper().readValue(json, Tuple3.class);
    }

    public static class Parameterized<T1, T2> {
        public Tuple2<T1, T2> value;
        public Parameterized() {}
        public Parameterized(Tuple2<T1, T2> value) {
            this.value = value;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWrappedParameterizedSome() throws IOException {
        String expected = "{\"value\":[1,2]}";
        Parameterized<Integer, Integer> object = new Parameterized<>(Tuple.of(1, 2));
        Assert.assertEquals(expected, mapper().writeValueAsString(object));
        Parameterized<Integer, Integer> restored = mapper().readValue(expected, new TypeReference<Parameterized<Integer, Integer>>() {});
        Assert.assertEquals(restored.value._1, (Integer) 1);
        Assert.assertEquals(restored.value._2, (Integer) 2);
    }
}
