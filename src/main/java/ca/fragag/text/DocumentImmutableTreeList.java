package ca.fragag.text;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import ca.fragag.collections.AbstractImmutableTreeList;
import ca.fragag.collections.AbstractImmutableTreeListFactory;
import ca.fragag.collections.AbstractImmutableTreeNode;
import ca.fragag.collections.AbstractImmutableTreeNodeFactory;

@Immutable
final class DocumentImmutableTreeList extends AbstractImmutableTreeList<char[], DocumentImmutableTreeList.Node> {

    @Immutable
    static final class Factory extends AbstractImmutableTreeListFactory<char[], Node, DocumentImmutableTreeList> {

        static final Factory INSTANCE = new Factory();

        private Factory() {
            super(Node.Factory.INSTANCE);
        }

        @Nonnull
        @Override
        public DocumentImmutableTreeList create() {
            return DocumentImmutableTreeList.EMPTY;
        }

        @Nonnull
        @Override
        protected DocumentImmutableTreeList createList(@CheckForNull Node root) {
            return new DocumentImmutableTreeList(root);
        }

    }

    @Immutable
    static final class Node extends AbstractImmutableTreeNode<char[], Node> {

        @Immutable
        static final class Factory extends AbstractImmutableTreeNodeFactory<char[], Node> {

            static final Factory INSTANCE = new Factory();

            private Factory() {
            }

            @Nonnull
            @Override
            protected Node createNode(@CheckForNull Node left, @Nonnull char[] value, @CheckForNull Node right) {
                return new Node(left, value, right);
            }

        }

        static char charAt(@Nonnull Node node, int index) {
            if (index < 0 || index >= node.textLength) {
                throw new IndexOutOfBoundsException();
            }

            for (;;) {
                final Node left = node.getLeft();
                if (left != null) {
                    if (index < left.textLength) {
                        node = left;
                        continue;
                    }

                    index -= left.textLength;
                }

                final char[] value = node.getValue();
                assert value != null;
                if (index < value.length) {
                    return value[index];
                }

                index -= value.length;
                node = node.getRight();
                assert node != null;
            }
        }

        private final transient int textLength;

        protected Node(@CheckForNull Node left, @Nonnull char[] value, @CheckForNull Node right) {
            super(left, value, right);
            this.textLength = (left == null ? 0 : left.textLength) + value.length + (right == null ? 0 : right.textLength);
        }

        int textLength() {
            return this.textLength;
        }

    }

    static final DocumentImmutableTreeList EMPTY = new DocumentImmutableTreeList(null);

    protected DocumentImmutableTreeList(@CheckForNull Node root) {
        super(root);
    }

    final char charAt(int index) {
        Node root = this.getRoot();
        if (root == null) {
            throw new IndexOutOfBoundsException();
        }

        return Node.charAt(root, index);
    }

    final int textLength() {
        Node root = this.getRoot();
        if (root == null) {
            return 0;
        }

        return root.textLength();
    }

}
