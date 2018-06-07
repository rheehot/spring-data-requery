package com.coupang.springframework.data.requery.domain;

import io.requery.meta.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Sort option for queries that wraps Requery entity model {@link Attribute}s for sorting.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
public class RequerySort extends Sort {

    private static final long serialVersionUID = 1L;

    public RequerySort(Attribute<?, ?>... attributes) {
        this(DEFAULT_DIRECTION, attributes);
    }

    public RequerySort(RequerySort.Path<?, ?>... paths) {
        this(DEFAULT_DIRECTION, paths);
    }

    public RequerySort(Direction direction, Attribute<?, ?>... attributes) {
        this(direction, paths(attributes));
    }

    public RequerySort(Direction direction, RequerySort.Path<?, ?>... paths) {
        this(direction, Arrays.asList(paths));
    }

    public RequerySort(Direction direction, List<Path<?, ?>> paths) {
        this(Collections.emptyList(), direction, paths);
    }

    @SuppressWarnings("deprecation")
    public RequerySort(List<Order> orders, @Nullable Direction direction, List<Path<?, ?>> paths) {
        super(combine(orders, direction, paths));
    }

    @SuppressWarnings("deprecation")
    public RequerySort(List<Order> orders) {
        super(orders);
    }


    public RequerySort and(@Nullable Direction direction, Attribute<?, ?>... attributes) {
        Assert.notNull(attributes, "Attributes must not be null!");
        return and(direction, paths(attributes));
    }

    public RequerySort and(@Nullable Direction direction, Path<?, ?>... paths) {
        Assert.notNull(paths, "Paths must not be null!");

        List<Order> existing = this.stream().collect(Collectors.toList());
        return new RequerySort(existing, direction, Arrays.asList(paths));
    }

    public RequerySort andUnsafe(@Nullable Direction direction, String... properties) {
        Assert.notEmpty(properties, "Properties must not be empty!");

        List<Order> orders = this.stream().collect(Collectors.toList());

        for (String property : properties) {
            orders.add(new RequeryOrder(direction, property));
        }

        return new RequerySort(orders, direction, Collections.emptyList());
    }

    private static Path<?, ?>[] paths(Attribute<?, ?>[] attributes) {

        Assert.notNull(attributes, "attributes must not be null!");
        Assert.notEmpty(attributes, "attributes must not be empty");

        Path<?, ?>[] paths = new Path<?, ?>[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            paths[i] = path(attributes[i]);
        }
        return paths;
    }

    private static List<Order> combine(List<Order> orders, @Nullable Direction direction, @NotNull List<Path<?, ?>> paths) {
        List<Order> result = new ArrayList<>(orders);

        for (Path<?, ?> path : paths) {
            result.add(new Order(direction, path.toString()));
        }

        return result;
    }

    @NotNull
    public static <A extends Attribute<T, S>, T, S> Path<T, S> path(A attribute) {
        Assert.notNull(attribute, "attribute must not be null!");
        return new Path<>(Collections.singletonList(attribute));
    }

    public static RequerySort unsafe(String... properties) {
        return unsafe(Sort.DEFAULT_DIRECTION, properties);
    }

    public static RequerySort unsafe(@NotNull Direction direction, String... properties) {
        Assert.notEmpty(properties, "properties must not be empty!");
        Assert.noNullElements(properties, "properties must not contains null values!");

        return unsafe(direction, Arrays.asList(properties));
    }

    public static RequerySort unsafe(@NotNull Direction direction, List<String> properties) {
        Assert.notEmpty(properties, "properties must not be empty!");

        List<Order> orders = new ArrayList<>();

        for (String property : properties) {
            orders.add(new RequeryOrder(direction, property));
        }

        return new RequerySort(orders);
    }


    /**
     * Value object to abstract a collection of {@link Attribute}
     */
    public static class Path<T, S> {

        private final Collection<Attribute<?, ?>> attributes;

        private Path(List<? extends Attribute<?, ?>> attributes) {
            this.attributes = Collections.unmodifiableList(attributes);
        }

        public <A extends Attribute<S, U>, U> Path<S, U> dot(A attribute) {
            return new Path<>(add(attribute));
        }

        private List<Attribute<?, ?>> add(@NotNull Attribute<?, ?> attribute) {

            List<Attribute<?, ?>> newAttributes = new ArrayList<>(this.attributes.size() + 1);
            newAttributes.addAll(this.attributes);
            newAttributes.add(attribute);
            return newAttributes;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (Attribute<?, ?> attribute : this.attributes) {
                sb.append(attribute.getName()).append(".");
            }

            return sb.length() == 0 ? "" : sb.substring(0, sb.lastIndexOf("."));
        }
    }

    /**
     * Custom {@link Order} that keeps a flag to indicate unsafe property handling, i.e. the String provided is not
     * necessarily a property but can be an arbitrary expression piped into the query execution. We also keep an
     * additional {@code ignoreCase} flag around as the constructor of the superclass is private currently.
     */
    public static class RequeryOrder extends Order {

        private static final long serialVersionUID = 1L;

        private final boolean unsafe;
        private final boolean ignoreCase;

        private RequeryOrder(Direction direction, String property) {
            this(direction, property, NullHandling.NATIVE);
        }

        private RequeryOrder(Direction direction, String property, NullHandling nullHandlingHint) {
            this(direction, property, nullHandlingHint, false, true);
        }

        private RequeryOrder(Direction direction,
                             String property,
                             NullHandling nullHandlingHint,
                             boolean ignoreCase,
                             boolean unsafe) {
            super(direction, property, nullHandlingHint);

            this.ignoreCase = ignoreCase;
            this.unsafe = unsafe;
        }

        @Override
        public Order with(Direction direction) {
            return new RequeryOrder(direction, getProperty(), getNullHandling(), isIgnoreCase(), this.unsafe);
        }

        @Override
        public Order with(NullHandling nullHandling) {
            return new RequeryOrder(getDirection(), getProperty(), nullHandling, isIgnoreCase(), this.unsafe);
        }

        public Sort withUnsafe(String... properties) {
            Assert.notEmpty(properties, "Properties must not be empty!");
            Assert.noNullElements(properties, "Properties must not contains null values!");

            List<Order> orders = new ArrayList<>();
            for (String property : properties) {
                orders.add(new RequeryOrder(getDirection(), property, getNullHandling(), isIgnoreCase(), this.unsafe));
            }

            return Sort.by(orders);
        }

        public RequeryOrder ignoreCase() {
            return new RequeryOrder(getDirection(), getProperty(), getNullHandling(), true, this.unsafe);
        }

        public boolean isIgnoreCase() {
            return super.isIgnoreCase() || this.ignoreCase;
        }

        public boolean isUnsafe() {
            return unsafe;
        }
    }
}
