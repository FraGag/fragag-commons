package ca.fragag.collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ca.fragag.NamedTestObject;

/**
 * Test class for {@link AbstractImmutableTreeListFactory} (via {@link ImmutableTreeListFactory} or a private implementation).
 *
 * @author Francis Gagné
 */
public class AbstractImmutableTreeListFactoryTest {

    private static class TestFactory extends AbstractImmutableTreeListFactory<Object, TestList.Node, TestList> {

        TestFactory() {
            super(new AbstractImmutableTreeNodeFactory<Object, TestList.Node>() {

                @Override
                protected TestList.Node createNode(TestList.Node left, Object value, TestList.Node right) {
                    return new TestList.Node(left, value, right);
                }

            });
        }

        @Override
        protected TestList createList(TestList.Node root) {
            return new TestList(root);
        }

    }

    private static class TestList extends AbstractImmutableTreeList<Object, TestList.Node> {

        protected static class Node extends AbstractImmutableTreeNode<Object, Node> {

            protected Node(Node left, Object value, Node right) {
                super(left, value, right);
            }

        }

        protected TestList(Node root) {
            super(root);
        }

    }

    private static final TestFactory FACTORY_0 = new TestFactory();
    private static final ImmutableTreeListFactory<Object> FACTORY_1 = ImmutableTreeListFactory.getInstance();
    private static final ImmutableTreeListFactory<String> FACTORY_2 = ImmutableTreeListFactory.getInstance();

    private static final Iterable<Integer> SAMPLE_ITERABLE = new Iterable<Integer>() {

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {

                private int i;

                @Override
                public boolean hasNext() {
                    return this.i < 3;
                }

                @Override
                public Integer next() {
                    return this.i++;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

            };
        }

    };
    private static final Collection<Integer> SAMPLE_COLLECTION_2 = Arrays.asList(new Integer[] { 3, 4 });
    private static final Collection<Integer> SAMPLE_COLLECTION_3 = Arrays.asList(new Integer[] { 5, 6, 7 });
    private static final Object OBJECT_0 = new NamedTestObject("object 0");
    private static final Object OBJECT_1 = new NamedTestObject("object 1");
    private static final Object OBJECT_2 = new NamedTestObject("object 2");

    private static ImmutableTreeList<String> createListFromString(String list) {
        try {
            try (Reader reader = new StringReader(list)) {
                return new ImmutableTreeList<>(createNodeFromReader(reader));
            }
        } catch (IOException e) {
            throw new AssertionError("StringReader shouldn't be throwing IOException...", e);
        }
    }

    private static ImmutableTreeNode<String> createNodeFromReader(Reader reader) throws IOException {
        int ch;
        ImmutableTreeNode<String> left = null;
        String element = "";
        ImmutableTreeNode<String> right = null;

        ch = reader.read();
        if (ch == '(') {
            left = createNodeFromReader(reader);
            ch = reader.read();
        }

        while (ch != -1 && ch != '(' && ch != ')') {
            element += (char) ch;
            ch = reader.read();
        }

        if (ch == '(') {
            right = createNodeFromReader(reader);
            while (ch != -1 && ch != ')') {
                ch = reader.read();
            }
        }

        return new ImmutableTreeNode<>(left, element, right);
    }

    private static String dumpListToString(ImmutableTreeList<String> list) {
        final ImmutableTreeNode<String> root = list.getRoot();
        if (root == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        dumpNodeToStringBuilder(root, sb);
        return sb.toString();
    }

    private static void dumpNodeToStringBuilder(ImmutableTreeNode<String> node, StringBuilder sb) {
        final ImmutableTreeNode<String> left = node.getLeft();
        if (left != null) {
            sb.append("(");
            dumpNodeToStringBuilder(left, sb);
            sb.append(")");
        }

        sb.append(node.getValue());

        final ImmutableTreeNode<String> right = node.getRight();
        if (right != null) {
            sb.append("(");
            dumpNodeToStringBuilder(right, sb);
            sb.append(")");
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#AbstractImmutableTreeListFactory(AbstractImmutableTreeNodeFactory)}
     * throws a {@link NullPointerException} when the <code>nodeFactory</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void abstractImmutableTreeListFactory() {
        new AbstractImmutableTreeListFactory<Object, ImmutableTreeNode<Object>, ImmutableTreeList<Object>>(null) {
            @Override
            protected ImmutableTreeList<Object> createList(ImmutableTreeNode<Object> root) {
                return new ImmutableTreeList<>(root);
            }
        };
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#add(AbstractImmutableTreeList, int, Object)} creates a new
     * {@link AbstractImmutableTreeList} with one additional element at the specified position and re-balances the tree if
     * necessary.
     */
    @Test
    public void addAbstractImmutableTreeListIntObject() {
        // Test various balancing scenarios to get maximum code coverage.

        {
            final ImmutableTreeList<String> list = createListFromString("A");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("A(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A(B)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A)!(B)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B)C");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A((B)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A)!((B)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B((!)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("((A)B)C(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A(B))C");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A((B)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A)!((B)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B((!)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("(A(B))C(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B(C))D");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("((!)A)B((C)D)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A(!))B((C)D)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("((A)B(!))C(D)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("((A)B)C((!)D)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 4, "!")), is("((A)B(C))D(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A(B)");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A(B)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A)!(B)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A((B)C)");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A((B)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A(!))B(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A(B))!(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("(A(B))C(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A(B(C))");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A(B(C))"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A(!))B(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A(B))!(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("(A(B))C(!)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A((B)C(D))");

            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("(!)A((B)C(D))"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A(!))B(C(D))"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B((!)C(D))"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("(A(B))C((!)D)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 4, "!")), is("(A(B))C(D(!))"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B(C)");
            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("((!)A)B(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 1, "!")), is("(A(!))B(C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("(A)B((!)C)"));
            assertThat(dumpListToString(FACTORY_2.add(list, 3, "!")), is("(A)B(C(!))"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B(C))D(E)");
            assertThat(dumpListToString(FACTORY_2.add(list, 0, "!")), is("((!)A)B((C)D(E))"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B((C)D))E(F)");
            assertThat(dumpListToString(FACTORY_2.add(list, 4, "!")), is("((A)B(C))D((!)E(F))"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B((C)D(E))");
            assertThat(dumpListToString(FACTORY_2.add(list, 5, "!")), is("((A)B(C))D(E(!))"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B((C(D))E(F))");
            assertThat(dumpListToString(FACTORY_2.add(list, 2, "!")), is("((A)B(!))C((D)E(F))"));
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#add(AbstractImmutableTreeList, int, Object)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is greater than the list's size.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void addAbstractImmutableTreeListIntObjectIndexTooHigh() {
        FACTORY_0.add(new TestList(null), 1, OBJECT_0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#add(AbstractImmutableTreeList, int, Object)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void addAbstractImmutableTreeListIntObjectIndexTooLow() {
        FACTORY_0.add(new TestList(null), -1, OBJECT_0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#add(AbstractImmutableTreeList, Object)} creates a new
     * {@link AbstractImmutableTreeList} with one element when called on an empty list.
     */
    @Test
    public void addAbstractImmutableTreeListObjectEmptyList() {
        final ImmutableTreeList<Object> list = FACTORY_1.add(new ImmutableTreeList<>(null), OBJECT_0);
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(OBJECT_0));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#add(AbstractImmutableTreeList, Object)} creates a new
     * {@link AbstractImmutableTreeList} with one additional element at the end when called on a non-empty list.
     */
    @Test
    public void addAbstractImmutableTreeListObjectNonEmptyList() {
        final TestList list = FACTORY_0.add(new TestList(new TestList.Node(null, OBJECT_0, null)), OBJECT_1);
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(OBJECT_0));
        assertThat(list.get(1), is(OBJECT_1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#create()} creates an empty {@link AbstractImmutableTreeList}.
     */
    @Test
    public void create() {
        // Using FACTORY_0 to avoid calling ImmutableTreeList.create().
        assertThat(FACTORY_0.create().isEmpty(), is(true));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#create(Collection)} creates an {@link AbstractImmutableTreeList} that
     * contains the specified collection's elements when that collection contains 2 elements.
     */
    @Test
    public void createCollectionOf2() {
        final ImmutableTreeList<Object> list = FACTORY_1.create(SAMPLE_COLLECTION_2);
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is((Object) 3));
        assertThat(list.get(1), is((Object) 4));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#create(Collection)} creates an {@link AbstractImmutableTreeList} that
     * contains the specified collection's elements when that collection contains 3 elements.
     */
    @Test
    public void createCollectionOf3() {
        final ImmutableTreeList<Object> list = FACTORY_1.create(SAMPLE_COLLECTION_3);
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is((Object) 5));
        assertThat(list.get(1), is((Object) 6));
        assertThat(list.get(2), is((Object) 7));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#create(Iterable)} creates an {@link AbstractImmutableTreeList} that
     * contains the elements returned by the specified {@link Iterable}'s iterator.
     */
    @Test
    public void createIterable() {
        final ImmutableTreeList<Object> list = FACTORY_1.create(SAMPLE_ITERABLE);
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is((Object) 0));
        assertThat(list.get(1), is((Object) 1));
        assertThat(list.get(2), is((Object) 2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#create(Iterable)}, when the <code>iterable</code> argument is a
     * {@link Collection}, creates an {@link AbstractImmutableTreeList} that contains the collection's elements.
     */
    @Test
    public void createIterableWithCollection() {
        final Iterable<Integer> iterable = SAMPLE_COLLECTION_2;
        final ImmutableTreeList<Object> list = FACTORY_1.create(iterable); // test create(Iterable) with a Collection
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is((Object) 3));
        assertThat(list.get(1), is((Object) 4));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#remove(AbstractImmutableTreeList, int)} creates a new
     * {@link AbstractImmutableTreeList} with the specified element removed from the list and re-balances the list if necessary.
     */
    @Test
    public void remove() {
        {
            final ImmutableTreeList<String> list = createListFromString("A");
            assertThat(FACTORY_2.remove(list, 0).size(), is(0));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("B"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("A"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B)C");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("(B)C"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("(A)C"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("(A)B"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A(B))C");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("(B)C"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("(A)C"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("A(B)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A(B)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("B"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("A"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A((B)C)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("(B)C"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("A(C)"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("A(B)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("A(B(C))");
            assertThat(dumpListToString(FACTORY_2.remove(list, 0)), is("B(C)"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("A(C)"));
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("A(B)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("(A)B(C)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 1)), is("(A)C"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B)C(D)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("(A)B(D)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B)C((D)E)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 2)), is("((A)B)D(E)"));
        }

        {
            final ImmutableTreeList<String> list = createListFromString("((A)B(C))D((E)F)");
            assertThat(dumpListToString(FACTORY_2.remove(list, 3)), is("((A)B)C((E)F)"));
        }
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#remove(AbstractImmutableTreeList, int)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is greater than or equal to the list's size.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIndexTooHigh() {
        FACTORY_0.remove(new TestList(null), 0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#remove(AbstractImmutableTreeList, int)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIndexTooLow() {
        FACTORY_0.remove(new TestList(null), -1);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#set(AbstractImmutableTreeList, int, Object)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is greater than or equal to the list's size.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setIndexTooHigh() {
        FACTORY_0.set(new TestList(null), 0, OBJECT_0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#set(AbstractImmutableTreeList, int, Object)} throws an
     * {@link IndexOutOfBoundsException} when the <code>index</code> argument is negative.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setIndexTooLow() {
        FACTORY_0.set(new TestList(null), -1, OBJECT_0);
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#set(AbstractImmutableTreeList, int, Object)} creates a new
     * {@link AbstractImmutableTreeList} with an element on the left subtree replaced with the specified value.
     */
    @Test
    public void setInLeftSubtree() {
        final TestList sourceList = new TestList(new TestList.Node(new TestList.Node(null, OBJECT_0, null), OBJECT_1, null));
        final TestList list = FACTORY_0.set(sourceList, 0, OBJECT_2);
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(OBJECT_2));
        assertThat(list.get(1), is(OBJECT_1));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#set(AbstractImmutableTreeList, int, Object)} creates a new
     * {@link AbstractImmutableTreeList} with an element on the right subtree replaced with the specified value.
     */
    @Test
    public void setInRightSubtree() {
        final TestList sourceList = new TestList(new TestList.Node(null, OBJECT_0, new TestList.Node(null, OBJECT_1, null)));
        final TestList list = FACTORY_0.set(sourceList, 1, OBJECT_2);
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(OBJECT_0));
        assertThat(list.get(1), is(OBJECT_2));
    }

    /**
     * Asserts that {@link AbstractImmutableTreeListFactory#set(AbstractImmutableTreeList, int, Object)} creates a new
     * {@link AbstractImmutableTreeList} with the element on the root node replaced with the specified value.
     */
    @Test
    public void setRoot() {
        final TestList sourceList = new TestList(new TestList.Node(null, OBJECT_0, null));
        final TestList list = FACTORY_0.set(sourceList, 0, OBJECT_1);
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(OBJECT_1));
    }

}
