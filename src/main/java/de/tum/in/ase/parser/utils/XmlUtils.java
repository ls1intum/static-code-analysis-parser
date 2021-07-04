package de.tum.in.ase.parser.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlUtils {
    public static Optional<Element> getFirstChild(Element parent, String name) {
        Iterator<Element> iterator = getChildElements(parent, name).iterator();
        if (iterator.hasNext())
            return Optional.of(iterator.next());
        else
            return Optional.empty();
    }

    /**
     * Gets all child elements of the given parent
     *
     * @param parent of the elements
     * @return a list of all children with any name in the empty namespace
     */
    public static Iterable<Element> getChildElements(Element parent) {
        return getChildElements(parent, null);
    }

    /**
     * Gets all child elements of the given parent with the given properties
     *
     * @param parent of the elements
     * @param name name of the tag of the elements
     * @return a list of all children with the name in the empty namespace
     */
    public static Iterable<Element> getChildElements(Element parent, String name) {
        return () -> new Iterator<Element>() {
            final NodeList children = parent.getElementsByTagName(name != null ? name : "*");
            int index = skipIndirectChildren();

            @Override
            public boolean hasNext() {
                return index < children.getLength();
            }

            @Override
            public Element next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                Element result = (Element) children.item(index++);
                skipIndirectChildren();

                return result;
            }

            private int skipIndirectChildren() {
                while (hasNext() && !children.item(index).getParentNode().isEqualNode(parent))
                    index++;

                return index;
            }
        };
    }
}
