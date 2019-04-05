/**
 * DoubleNode is a helper class that links an element to two other ones.
 *
 * @author Benjamin Warner
 */

public class DoubleNode<E> {
	private DoubleNode<E> next;
	private DoubleNode<E> previous;
	private E element;

	public DoubleNode() {
		next = null;
		previous = null;
		element = null;
	}

	public DoubleNode(E element) {
		next = null;
		previous = null;
		this.element = element;
	}

	public DoubleNode(E element, DoubleNode<E> next, DoubleNode<E> previous) {
		this.next = next;
		this.previous = previous;
		this.element = element;
	}

	public DoubleNode<E> getPrevious() {
		return previous;
	}

	public void setPrevious(DoubleNode<E> node) {
		previous = node;
	}

	public DoubleNode<E> getNext() {
		return next;
	}

	public void setNext(DoubleNode<E> node) {
		next = node;
	}

	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}
}
