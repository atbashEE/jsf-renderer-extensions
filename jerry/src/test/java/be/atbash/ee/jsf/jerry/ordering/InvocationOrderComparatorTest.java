/*
 * Copyright 2014-2022 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jsf.jerry.ordering;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class InvocationOrderComparatorTest {

    private static final String ID_X = "X";
    private static final String ID_Y = "Y";

    @Test
    public void testCompare_BothAnnotated() {
        List<Object> d = new ArrayList<>();
        d.add(new Order10000());
        d.add(new OrderDefault());
        d.add(new Order100());

        d.sort(new InvocationOrderComparator<>());
        assertThat(d.get(0)).isExactlyInstanceOf(Order100.class);
        assertThat(d.get(1)).isExactlyInstanceOf(OrderDefault.class);
        assertThat(d.get(2)).isExactlyInstanceOf(Order10000.class);
    }

    @Test
    public void testCompare_NoAnnotationAtTheEnd() {
        List<Object> d = new ArrayList<>();
        d.add(new NoOrder(ID_X));
        d.add(new Order10000());

        d.sort(new InvocationOrderComparator<>());
        assertThat(d.get(0)).isExactlyInstanceOf(Order10000.class);
        assertThat(d.get(1)).isExactlyInstanceOf(NoOrder.class);

    }

    @Test
    public void testCompare_NoAnnotationKeepOriginalOrder() {
        List<NoOrder> d = new ArrayList<>();
        d.add(new NoOrder(ID_X));
        d.add(new NoOrder(ID_Y));

        d.sort(new InvocationOrderComparator<NoOrder>());
        assertThat(d.get(0).getId()).isEqualTo(ID_X);
        assertThat(d.get(1).getId()).isEqualTo(ID_Y);

    }

    @InvocationOrder(value = 100)
    private static class Order100 {

    }

    @InvocationOrder()
    private static class OrderDefault {

    }

    @InvocationOrder(value = 10000)
    private static class Order10000 {

    }

    private static class NoOrder {
        private String id;

        public NoOrder(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}