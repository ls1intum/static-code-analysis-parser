package de.tum.in.ase.parser.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        return getChildElements(parent, name, null);
    }

    /**
     * Gets all child elements of the given parent with the given properties
     *
     * @param parent of the elements
     * @param name name of the tag of the elements
     * @param nameSpace namespace of the elements
     * @return a list of all children with the name and namespace
     */
    public static Iterable<Element> getChildElements(Element parent, String name, String nameSpace) {
        return () -> new Iterator<Element>() {
            final NodeList children = parent.getElementsByTagNameNS(nameSpace, name != null ? name : "*");
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < children.getLength();
            }

            @Override
            public Element next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                return (Element) children.item(index++);
            }
        };
    }
}
