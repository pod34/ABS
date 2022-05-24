
package mypackage;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mypackage package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AbsBalance_QNAME = new QName("", "abs-balance");
    private final static QName _AbsOwner_QNAME = new QName("", "abs-owner");
    private final static QName _AbsTotalYazTime_QNAME = new QName("", "abs-total-yaz-time");
    private final static QName _AbsIntristPerPayment_QNAME = new QName("", "abs-intrist-per-payment");
    private final static QName _AbsCategory_QNAME = new QName("", "abs-category");
    private final static QName _AbsPaysEveryYaz_QNAME = new QName("", "abs-pays-every-yaz");
    private final static QName _AbsCapital_QNAME = new QName("", "abs-capital");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mypackage
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AbsCustomers }
     * 
     */
    public AbsCustomers createAbsCustomers() {
        return new AbsCustomers();
    }

    /**
     * Create an instance of {@link AbsCustomer }
     * 
     */
    public AbsCustomer createAbsCustomer() {
        return new AbsCustomer();
    }

    /**
     * Create an instance of {@link AbsCategories }
     * 
     */
    public AbsCategories createAbsCategories() {
        return new AbsCategories();
    }

    /**
     * Create an instance of {@link AbsLoans }
     * 
     */
    public AbsLoans createAbsLoans() {
        return new AbsLoans();
    }

    /**
     * Create an instance of {@link AbsLoan }
     * 
     */
    public AbsLoan createAbsLoan() {
        return new AbsLoan();
    }

    /**
     * Create an instance of {@link AbsDescriptor }
     * 
     */
    public AbsDescriptor createAbsDescriptor() {
        return new AbsDescriptor();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-balance")
    public JAXBElement<Integer> createAbsBalance(Integer value) {
        return new JAXBElement<Integer>(_AbsBalance_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-owner")
    public JAXBElement<String> createAbsOwner(String value) {
        return new JAXBElement<String>(_AbsOwner_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-total-yaz-time")
    public JAXBElement<Integer> createAbsTotalYazTime(Integer value) {
        return new JAXBElement<Integer>(_AbsTotalYazTime_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-intrist-per-payment")
    public JAXBElement<Integer> createAbsIntristPerPayment(Integer value) {
        return new JAXBElement<Integer>(_AbsIntristPerPayment_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-category")
    public JAXBElement<String> createAbsCategory(String value) {
        return new JAXBElement<String>(_AbsCategory_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-pays-every-yaz")
    public JAXBElement<Integer> createAbsPaysEveryYaz(Integer value) {
        return new JAXBElement<Integer>(_AbsPaysEveryYaz_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "abs-capital")
    public JAXBElement<Integer> createAbsCapital(Integer value) {
        return new JAXBElement<Integer>(_AbsCapital_QNAME, Integer.class, null, value);
    }

}
