package ca.fragag.collections;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link ImmutableTreeListFactory}.
 *
 * @author Francis Gagné
 */
public class ImmutableTreeListFactoryTest {

    private static final ImmutableTreeListFactory<Object> INSTANCE = ImmutableTreeListFactory.getInstance();

    /**
     * Asserts that {@link ImmutableTreeListFactory#create()} returns the same value as {@link ImmutableTreeList#getEmpty()}.
     */
    @Test
    public void create() {
        assertThat(INSTANCE.create(), is(sameInstance(ImmutableTreeList.getEmpty())));
    }

    /**
     * Asserts that {@link ImmutableTreeListFactory#createList(ImmutableTreeNode)} returns an {@link ImmutableTreeList}.
     */
    @Test
    public void createListImmutableTreeNode() {
        assertThat(INSTANCE.createList(null), is(instanceOf(ImmutableTreeList.class)));
    }

}
