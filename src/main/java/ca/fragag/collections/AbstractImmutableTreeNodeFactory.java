package ca.fragag.collections;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A factory of {@link AbstractImmutableTreeNode} objects.
 *
 * @param <E>
 *            the type of the value in the node and its child nodes
 * @param <N>
 *            the specific type of node that this factory produces
 *
 * @author Francis Gagné
 */
@Immutable
public abstract class AbstractImmutableTreeNodeFactory<E, N extends AbstractImmutableTreeNode<E, N>> {

    @Immutable
    private static class Removal<E, N extends AbstractImmutableTreeNode<E, N>> {

        final E element;
        @CheckForNull
        final N node;

        Removal(E element, @CheckForNull N node) {
            this.element = element;
            this.node = node;
        }

    }

    private static final int DELTA = 3;
    private static final int RATIO = 2;

    /**
     * Creates a node (of type {@code N}) with the specified left node, right node and element value.
     *
     * @param left
     *            the left node
     * @param value
     *            the element value
     * @param right
     *            the right node
     * @return a node with the specified left node, right node and element value.
     */
    @Nonnull
    protected abstract N createNode(@CheckForNull N left, E value, @CheckForNull N right);

    @Nonnull
    final N balanceLeft(@CheckForNull N left, E element, @CheckForNull N right) {
        N balanced;

        if (right == null) {
            if (left == null) {
                balanced = this.createLeafNode(element);
            } else {
                final N leftLeft = left.getLeft();
                final N leftRight = left.getRight();

                if (leftLeft == null) {
                    if (leftRight == null) {
                        balanced = this.createNode(left, element, null);
                    } else {
                        balanced = this.createNode(this.createLeafNode(left.getValue()), leftRight.getValue(),
                                this.createLeafNode(element));
                    }
                } else {
                    if (leftRight == null) {
                        balanced = this.createNode(leftLeft, left.getValue(), this.createLeafNode(element));
                    } else {
                        if (leftRight.getSize() < RATIO * leftLeft.getSize()) {
                            balanced = this.createNode(leftLeft, left.getValue(), this.createNode(leftRight, element, null));
                        } else {
                            balanced = this.createNode(this.createNode(leftLeft, left.getValue(), leftRight.getLeft()),
                                    leftRight.getValue(), this.createNode(leftRight.getRight(), element, null));
                        }
                    }
                }
            }
        } else {
            if (left == null) {
                balanced = this.createNode(null, element, right);
            } else {
                if (left.getSize() > DELTA * right.getSize()) {
                    final N leftLeft = left.getLeft();
                    final N leftRight = left.getRight();

                    // If left.getSize() is large enough to enter this branch,
                    // then necessarily, it has subnodes (assuming the tree is well-balanced...)
                    assert leftLeft != null;
                    assert leftRight != null;

                    if (leftRight.getSize() < RATIO * leftLeft.getSize()) {
                        balanced = this.createNode(leftLeft, left.getValue(), this.createNode(leftRight, element, right));
                    } else {
                        balanced = this.createNode(this.createNode(leftLeft, left.getValue(), leftRight.getLeft()),
                                leftRight.getValue(), this.createNode(leftRight.getRight(), element, right));
                    }
                } else {
                    balanced = this.createNode(left, element, right);
                }
            }
        }

        return balanced;
    }

    final N balanceRight(@CheckForNull N left, E element, @CheckForNull N right) {
        N balanced;

        if (left == null) {
            if (right == null) {
                balanced = this.createLeafNode(element);
            } else {
                final N rightLeft = right.getLeft();
                final N rightRight = right.getRight();

                if (rightLeft == null) {
                    if (rightRight == null) {
                        balanced = this.createNode(null, element, right);
                    } else {
                        balanced = this.createNode(this.createLeafNode(element), right.getValue(), rightRight);
                    }
                } else {
                    if (rightRight == null) {
                        balanced = this.createNode(this.createLeafNode(element), rightLeft.getValue(),
                                this.createLeafNode(right.getValue()));
                    } else {
                        if (rightLeft.getSize() < RATIO * rightRight.getSize()) {
                            balanced = this.createNode(this.createNode(null, element, rightLeft), right.getValue(), rightRight);
                        } else {
                            balanced = this.createNode(this.createNode(null, element, rightLeft.getLeft()), rightLeft.getValue(),
                                    this.createNode(rightLeft.getRight(), right.getValue(), rightRight));
                        }
                    }
                }
            }
        } else {
            if (right == null) {
                balanced = this.createNode(left, element, null);
            } else {
                if (right.getSize() > DELTA * left.getSize()) {
                    final N rightLeft = right.getLeft();
                    final N rightRight = right.getRight();

                    // If left.getSize() is large enough to enter this branch,
                    // then necessarily, it has subnodes (assuming the tree is well-balanced...)
                    assert rightLeft != null;
                    assert rightRight != null;

                    if (rightLeft.getSize() < RATIO * rightRight.getSize()) {
                        balanced = this.createNode(this.createNode(left, element, rightLeft), right.getValue(), rightRight);
                    } else {
                        balanced = this.createNode(this.createNode(left, element, rightLeft.getLeft()), rightLeft.getValue(),
                                this.createNode(rightLeft.getRight(), right.getValue(), rightRight));
                    }
                } else {
                    balanced = this.createNode(left, element, right);
                }
            }
        }

        return balanced;
    }

    final N glue(@CheckForNull N left, @CheckForNull N right) {
        if (left == null) {
            if (right == null) {
                return null;
            }

            return right;
        }

        if (right == null) {
            return left;
        }

        if (left.getSize() > right.getSize()) {
            final Removal<E, N> removal = this.removeLast(left);
            return this.balanceRight(removal.node, removal.element, right);
        }

        final Removal<E, N> removal = this.removeFirst(right);
        return this.balanceLeft(left, removal.element, removal.node);
    }

    @Nonnull
    private final N createLeafNode(E value) {
        return this.createNode(null, value, null);
    }

    @Nonnull
    private final Removal<E, N> removeFirst(@Nonnull N node) {
        final N left = node.getLeft();
        if (left == null) {
            return new Removal<>(node.getValue(), node.getRight());
        }

        Removal<E, N> removal = this.removeFirst(left);
        return new Removal<>(removal.element, this.createNode(removal.node, node.getValue(), node.getRight()));
    }

    @Nonnull
    private final Removal<E, N> removeLast(@Nonnull N node) {
        final N right = node.getRight();
        if (right == null) {
            return new Removal<>(node.getValue(), node.getLeft());
        }

        Removal<E, N> removal = this.removeLast(right);
        return new Removal<>(removal.element, this.createNode(node.getLeft(), node.getValue(), removal.node));
    }

}
