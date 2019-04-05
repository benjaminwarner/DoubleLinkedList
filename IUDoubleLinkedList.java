import java.util.Iterator;
import java.util.ListIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * A ListIterator is implemented with working add, remove, and set methods.
 * 
 * @author Benjamin Warner
 * 
 * @param <T> store data of type T
 */

 public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
     private DoubleNode<T> head, tail;
     private int size;
     private int modCount;

     /**
      * Creates an empty list
      */
      public IUDoubleLinkedList() {
          this.head = null;
          this.tail = null;
          this.size = 0;
          this.modCount = 0;
      }

    @Override
	public void addToFront(T element) {
		if (isEmpty()) {
			head = tail = new DoubleNode<T>(element);
		} else {
			DoubleNode<T> currentHead = head;
			head = new DoubleNode<T>(element);
            head.setNext(currentHead);
            currentHead.setPrevious(head);
		}
		++size;
		++modCount;
	}

	@Override
	public void addToRear(T element) {
		if (isEmpty()) {
			head = tail = new DoubleNode<T>(element);
		} else {
			DoubleNode<T> currentTail = tail;
			tail = new DoubleNode<T>(element);
            currentTail.setNext(tail);
            tail.setPrevious(currentTail);
		}
		++size;
		++modCount;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void add(int index, T element) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException();

		if (index == 0)
			addToFront(element);
		else if (index == size)
			addToRear(element);
		else {
			DoubleNode<T> current = head;
			DoubleNode<T> previous = null;
			DoubleNode<T> newNode = new DoubleNode<T>(element);

			for (int i = 0; i < index; ++i) {
				previous = current;
				current = current.getNext();
			}

            newNode.setNext(current);
            current.setPrevious(newNode);
			if (previous != null) {
                previous.setNext(newNode);
                newNode.setPrevious(previous);
            }
			++size;
			++modCount;
		}
	}

	@Override
	public void addAfter(T element, T target) {
		DoubleNode<T> targetNode = null;
		DoubleNode<T> current = head;
		DoubleNode<T> newNode = new DoubleNode<T>(element);

		for (int i = 0; i < size; ++i) {
			if (current.getElement().equals(target)) {
				targetNode = current;
				break;
			} else {
				current = current.getNext();
			}
		}

		if (targetNode == null)
			throw new NoSuchElementException();
		else if (targetNode == tail) {
            targetNode.setNext(newNode);
            newNode.setPrevious(targetNode);
			tail = newNode;
		} else {
            targetNode.getNext().setPrevious(newNode);
            newNode.setNext(targetNode.getNext());
            newNode.setPrevious(targetNode);
			targetNode.setNext(newNode);
		}

		++size;
		++modCount;
	}

	@Override
	public T removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
        T element = head.getElement();
        if (size > 1)
            head.getNext().setPrevious(null);
		head = head.getNext();
		--size;
		++modCount;
		return element;
	}

	@Override
	public T removeLast() {
		if (isEmpty())
            throw new NoSuchElementException();
        T element = tail.getElement();
        if (size > 1)
            tail.getPrevious().setNext(null);
        tail = tail.getPrevious();
        --size;
        ++modCount;
        return element;
	}

	@Override
	public T remove(T element) {
		if (isEmpty())
			throw new NoSuchElementException();

		boolean found = false;
		DoubleNode<T> current = head;

		while (current != null && !found) {
			if (element.equals(current.getElement()))
				found = true;
			else {
				current = current.getNext();
			}
		}

		if (!found)
			throw new NoSuchElementException();

		if (size() == 1)
			head = tail = null;
		else if (current == head) {
            head.getNext().setPrevious(null);
            head = current.getNext();
        }
		else if (current == tail) {
            tail.getPrevious().setNext(null);
			tail = current.getPrevious();
		} else {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
        }

		--size;
		++modCount;
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		T element;
		if (index == 0) {
            element = head.getElement();
            if (size > 1)
                head.getNext().setPrevious(null);
            head = head.getNext();
        } else if (index == size() - 1) {
            element = tail.getElement();
            if (size > 1)
                tail.getPrevious().setNext(null);
            tail = tail.getPrevious();
		} else {
			DoubleNode<T> current = head;

			for (int i = 0; i < index; ++i) {
				current = current.getNext();
			}
            element = current.getElement();
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
		}

		--size;
		++modCount;
		return element;
	}

	@Override
	public void set(int index, T element) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		DoubleNode<T> current = head;
		for (int i = 0; i < index; ++i)
			current = current.getNext();
		current.setElement(element);
	}

	@Override
	public T get(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		DoubleNode<T> current = head;
		for (int i = 0; i < index; ++i) {
			current = current.getNext();
		}
		return current.getElement();
    }
    
    /**
     * Get the node at index
     * @param index index of the node to return
     */
    private DoubleNode<T> getNode(int index) {
        if (index >= size || index < 0)
            return null;
        DoubleNode<T> current = head;
        for (int i = 0; i < index; ++i) {
            current = current.getNext();
        }
        return current;
    }

	@Override
	public int indexOf(T element) {
		DoubleNode<T> current = head;
		for (int i = 0; i < size; ++i) {
			if (current.getElement().equals(element))
				return i;
			current = current.getNext();
		}
		return -1;
	}

	@Override
	public T first() {
		if (isEmpty())
			throw new NoSuchElementException();
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty())
			throw new NoSuchElementException();
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		DoubleNode<T> current = head;
		for (int i = 0; i < size; ++i) {
			if (current.getElement().equals(target))
				return true;
			current = current.getNext();
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	public String toString() {
		if (isEmpty())
			return "[]";
		String str = "[";
		DoubleNode<T> current = head;
		for (int i = 0; i < size - 1; ++i) {
			str += current.getElement().toString() + ", ";
			current = current.getNext();
		}
		str += current.getElement().toString() + "]";
		return str;
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new ImplementedListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new ImplementedListIterator(startingIndex);
    }
    
    /** ListIterator for IUDoubleLinkedList */
    private class ImplementedListIterator implements ListIterator<T> {
        private DoubleNode<T> nextNode;
        private DoubleNode<T> previousNode = null;
        private DoubleNode<T> lastAccessedNode = null;
        private int iterModCount;

        /** Creates a new iterator for the list */
        public ImplementedListIterator() {
            nextNode = head;
            iterModCount = modCount;
        }

        public ImplementedListIterator(int startingIndex) {
            if (startingIndex >= size || startingIndex < 0)
                throw new IndexOutOfBoundsException();
            nextNode = IUDoubleLinkedList.this.getNode(startingIndex);
            previousNode = IUDoubleLinkedList.this.getNode(startingIndex - 1);
            iterModCount = modCount;
        }

        @Override
        public void add(T e) {
            IUDoubleLinkedList.this.add(e);
            ++iterModCount;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount)
                throw new ConcurrentModificationException();
            return nextNode != null;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount)
                throw new ConcurrentModificationException();
            return previousNode != null;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            T element = nextNode.getElement();
            previousNode = nextNode;
            nextNode = nextNode.getNext();
            lastAccessedNode = previousNode;
            return element;
        }

        @Override
        public int nextIndex() {
            if (!hasNext())
                return IUDoubleLinkedList.this.size;
            return IUDoubleLinkedList.this.indexOf(nextNode.getElement());
        }

        @Override
        public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            T element = previousNode.getElement();
            nextNode = previousNode;
            previousNode = previousNode.getPrevious();
            lastAccessedNode = nextNode;
            return element;
        }

        @Override
        public int previousIndex() {
            if (!hasPrevious())
                return -1;
            return IUDoubleLinkedList.this.indexOf(nextNode.getPrevious().getElement());
        }

        @Override
        public void remove() {
            if (lastAccessedNode == null)
                throw new IllegalStateException();
            IUDoubleLinkedList.this.remove(lastAccessedNode.getElement());
            lastAccessedNode = null;
            ++iterModCount;
        }

        @Override
        public void set(T element) {
            if (lastAccessedNode == null)
                throw new IllegalStateException();
            lastAccessedNode.setElement(element);
        }
    }

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private DoubleNode<T> nextNode;
		private int iterModCount;
		private boolean canRemove;

		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount)
				throw new ConcurrentModificationException();
			return nextNode != null;
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			T element = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;
			return element;
		}

		@Override
		public void remove() {
			if (iterModCount != modCount)
				throw new ConcurrentModificationException();
			else if (!canRemove)
				throw new IllegalStateException();
			canRemove = false;
		}
	}
 }