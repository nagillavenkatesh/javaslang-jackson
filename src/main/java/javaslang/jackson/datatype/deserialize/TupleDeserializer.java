/**
 * Copyright 2015 The Javaslang Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javaslang.jackson.datatype.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javaslang.Tuple;
import javaslang.Tuple0;
import javaslang.Tuple1;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.Tuple4;
import javaslang.Tuple5;
import javaslang.Tuple6;
import javaslang.Tuple7;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;

class TupleDeserializer extends ValueDeserializer<Tuple> {

    private static final long serialVersionUID = 1L;

    private final JavaType javaType;

    TupleDeserializer(JavaType valueType) {
        super(valueType, arity(valueType));
        this.javaType = valueType;
    }

    @Override
    public Tuple deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<Object> list = new ArrayList<>();
        int ptr = 0;

        for (JsonToken jsonToken = p.nextToken(); jsonToken != END_ARRAY; jsonToken = p.nextToken()) {
            if (ptr >= deserializersCount()) {
                throw mappingException(ctxt, javaType.getRawClass(), jsonToken);
            }
            JsonDeserializer<?> deserializer = deserializer(ptr++);
            Object value = (jsonToken != VALUE_NULL) ? deserializer.deserialize(p, ctxt) : deserializer.getNullValue(ctxt);
            list.add(value);
        }
        return create(list, ctxt);
    }

    private Tuple create(List<Object> list, DeserializationContext ctxt) throws JsonMappingException {
        final Tuple result;
        switch (list.size()) {
            case 1:
                result = Tuple.of(list.get(0));
                break;
            case 2:
                result = Tuple.of(list.get(0), list.get(1));
                break;
            case 3:
                result = Tuple.of(list.get(0), list.get(1), list.get(2));
                break;
            case 4:
                result = Tuple.of(list.get(0), list.get(1), list.get(2), list.get(3));
                break;
            case 5:
                result = Tuple.of(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
                break;
            case 6:
                result = Tuple.of(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5));
                break;
            case 7:
                result = Tuple.of(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
                break;
            case 8:
                result = Tuple.of(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
                break;
            default:
                result = Tuple.empty();
        }
        if (!javaType.getRawClass().isAssignableFrom(result.getClass())) {
            throw mappingException(ctxt, javaType.getRawClass(), null);
        }
        return result;
    }

    private static int arity(JavaType valueType) {
        Class<?> clz = valueType.getRawClass();
        if (clz == Tuple0.class) {
            return 0;
        } else if (clz == Tuple1.class) {
            return 1;
        } else if (clz == Tuple2.class) {
            return 2;
        } else if (clz == Tuple3.class) {
            return 3;
        } else if (clz == Tuple4.class) {
            return 4;
        } else if (clz == Tuple5.class) {
            return 5;
        } else if (clz == Tuple6.class) {
            return 6;
        } else if (clz == Tuple7.class) {
            return 7;
        } else {
            return 8;
        }
    }
}
