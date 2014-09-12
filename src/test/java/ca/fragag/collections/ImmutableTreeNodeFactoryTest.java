package ca.fragag.collections;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test class for {@link ImmutableTreeNode.Factory}.
 *
 * @author Francis Gagné
 */
public class ImmutableTreeNodeFactoryTest {

    private static final ImmutableTreeNode.Factory<Object> INSTANCE = ImmutableTreeNode.Factory.getInstance();

    /**
     * Asserts that {@link ImmutableTreeNode.Factory#createNode(ImmutableTreeNode, Object, ImmutableTreeNode)} returns an
     * {@link ImmutableTreeNode}.
     */
    @Test
    public void createNode() {
        assertThat(INSTANCE.createNode(null, null, null), is(instanceOf(ImmutableTreeNode.class)));
    }

}
