package org.checkerframework.dataflow.cfg.node;

import java.util.Collection;
import java.util.LinkedList;

import org.checkerframework.dataflow.util.HashCodeUtils;

import org.checkerframework.javacutil.InternalUtils;

import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;

/**
 * A node for the bitwise or logical (single bit) and operation:
 *
 * <pre>
 *   <em>expression</em> &amp; <em>expression</em>
 * </pre>
 *
 * @author Stefan Heule
 * @author Charlie Garrett
 *
 */
public class BitwiseAndNode extends Node {

    protected Tree tree;
    protected Node left;
    protected Node right;

    public BitwiseAndNode(Tree tree, Node left, Node right) {
        super(InternalUtils.typeOf(tree));
        assert tree.getKind() == Kind.AND;
        this.tree = tree;
        this.left = left;
        this.right = right;
    }

    public Node getLeftOperand() {
        return left;
    }

    public Node getRightOperand() {
        return right;
    }

    @Override
    public Tree getTree() {
        return tree;
    }

    @Override
    public <R, P> R accept(NodeVisitor<R, P> visitor, P p) {
        return visitor.visitBitwiseAnd(this, p);
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand() + " & " + getRightOperand() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BitwiseAndNode)) {
            return false;
        }
        BitwiseAndNode other = (BitwiseAndNode) obj;
        return getLeftOperand().equals(other.getLeftOperand())
                && getRightOperand().equals(other.getRightOperand());
    }

    @Override
    public int hashCode() {
        return HashCodeUtils.hash(getLeftOperand(), getRightOperand());
    }

    @Override
    public Collection<Node> getOperands() {
        LinkedList<Node> list = new LinkedList<Node>();
        list.add(getLeftOperand());
        list.add(getRightOperand());
        return list;
    }
}
