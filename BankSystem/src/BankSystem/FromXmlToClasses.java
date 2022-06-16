package BankSystem;

import BankActions.Loan;
import Costumers.Customer;
import SystemExceptions.InccorectInputType;
import mypackage.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class FromXmlToClasses implements Serializable {

    public static boolean fromXmlToObjects(List<Customer> customerInSystem, List<Loan> loanInSystem, String FileName,
                                        List<String> allCategoriesInSystem) {
        boolean greatSuccess = true;
        try {
            File file = new File(FileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
            allCategoriesInSystem.addAll(descriptor.getAbsCategories().getAbsCategory());
            fromObjectToLoans(loanInSystem, descriptor.getAbsLoans().getAbsLoan(), descriptor.getAbsCategories().getAbsCategory(), descriptor.getAbsCustomers().getAbsCustomer().stream().map(AbsCustomer::getName).collect(Collectors.toList()));
            fromObjectToCustomersList(customerInSystem, descriptor.getAbsCustomers().getAbsCustomer(), loanInSystem);

        } catch (JAXBException | InccorectInputType e) {
            System.out.println(e);
            greatSuccess = false;
        }
        return greatSuccess;
    }

    private static void fromObjectToCustomersList(List<Customer> customerInSystem, List<AbsCustomer> absCustomers, List<Loan> loansInSystems) throws InccorectInputType {
        List<Loan> loansAfterNameFilter;
        List<String> loanNamesAsBorrower;
        if(fromListToMapCheck(absCustomers.stream().map(AbsCustomer::getName).collect(Collectors.toList()))){
        for (AbsCustomer curCustomer: absCustomers) {
            loansAfterNameFilter = loansInSystems.stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).collect(Collectors.toList());
            loanNamesAsBorrower = loansNames(loansAfterNameFilter);
            customerInSystem.add(new Customer(curCustomer.getName(), curCustomer.getAbsBalance(), loanNamesAsBorrower));
        }
        }
        else
            throw new InccorectInputType(InccorectInputType.getCustomerDuplication());
    }

    private static void fromObjectToLoans(List<Loan> loansInSystem, List<AbsLoan> absLoans, List<String> categories, List<String> allCustomersNames) throws InccorectInputType {
        for (AbsLoan curLoan: absLoans) {
            if (fromListToMapCheck(absLoans.stream().map(AbsLoan::getId).collect(Collectors.toList()))) {
                if (categories.contains(curLoan.getAbsCategory())) {
                    if (allCustomersNames.contains(curLoan.getAbsOwner())) {
                        if ((curLoan.getAbsTotalYazTime()) % (curLoan.getAbsPaysEveryYaz()) == 0) {
                            loansInSystem.add(new Loan(curLoan.getId(), curLoan.getAbsOwner(), curLoan.getAbsCategory(),
                                    curLoan.getAbsCapital(), curLoan.getAbsTotalYazTime(),
                                    curLoan.getAbsPaysEveryYaz(), curLoan.getAbsIntristPerPayment()));
                        } else
                            throw new InccorectInputType(InccorectInputType.getNotDivided());
                    } else
                        throw new InccorectInputType("The name of the loan owner for loan: " + curLoan.getId() + "is not a customer in the bank.");
                } else
                    throw new InccorectInputType("The category for loan: " + curLoan.getId() + "does not exist in the system");
            }
            else
                throw new InccorectInputType(InccorectInputType.getLoanNameDuplication());
        }
    }

    private static List<String> loansNames(List<Loan> loansAfterFilter){
        List<String> listOfLoansNames = new ArrayList<>();
        for (Loan curLoan: loansAfterFilter) {
            listOfLoansNames.add(curLoan.getNameOfLoan());
        }
        return listOfLoansNames;
    }

    private static Boolean fromListToMapCheck(List<String> allNames){
        List<String> listAfterChangToUpperCase;
        listAfterChangToUpperCase = allNames.stream().map(String::toUpperCase).collect(Collectors.toList());
        Set<String> customerMap = new HashSet<>(listAfterChangToUpperCase);
        if(customerMap.size() == allNames.size())
            return true;
        return false;
    }
}
