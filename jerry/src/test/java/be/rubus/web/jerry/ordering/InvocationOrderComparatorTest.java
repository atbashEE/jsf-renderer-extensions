package be.rubus.web.jerry.ordering;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
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

        Collections.sort(d, new InvocationOrderComparator<Object>());
        assertThat(d.get(0)).isExactlyInstanceOf(Order100.class);
        assertThat(d.get(1)).isExactlyInstanceOf(OrderDefault.class);
        assertThat(d.get(2)).isExactlyInstanceOf(Order10000.class);
    }

    @Test
    public void testCompare_NoAnnotationAtTheEnd() {
        List<Object> d = new ArrayList<>();
        d.add(new NoOrder(ID_X));
        d.add(new Order10000());

        Collections.sort(d, new InvocationOrderComparator<Object>());
        assertThat(d.get(0)).isExactlyInstanceOf(Order10000.class);
        assertThat(d.get(1)).isExactlyInstanceOf(NoOrder.class);

    }

    @Test
    public void testCompare_NoAnnotationKeepOriginalOrder() {
        List<NoOrder> d = new ArrayList<>();
        d.add(new NoOrder(ID_X));
        d.add(new NoOrder(ID_Y));

        Collections.sort(d, new InvocationOrderComparator<NoOrder>());
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